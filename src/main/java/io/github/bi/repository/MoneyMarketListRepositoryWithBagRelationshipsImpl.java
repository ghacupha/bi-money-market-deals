package io.github.bi.repository;

/*-
 * Money Market Bi - BI Microservice for Money Market Bi deals is part of the Granular Bi System
 * Copyright © 2025 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import io.github.bi.domain.MoneyMarketList;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class MoneyMarketListRepositoryWithBagRelationshipsImpl implements MoneyMarketListRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String MONEYMARKETLISTS_PARAMETER = "moneyMarketLists";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<MoneyMarketList> fetchBagRelationships(Optional<MoneyMarketList> moneyMarketList) {
        return moneyMarketList.map(this::fetchPlaceholders);
    }

    @Override
    public Page<MoneyMarketList> fetchBagRelationships(Page<MoneyMarketList> moneyMarketLists) {
        return new PageImpl<>(
            fetchBagRelationships(moneyMarketLists.getContent()),
            moneyMarketLists.getPageable(),
            moneyMarketLists.getTotalElements()
        );
    }

    @Override
    public List<MoneyMarketList> fetchBagRelationships(List<MoneyMarketList> moneyMarketLists) {
        return Optional.of(moneyMarketLists).map(this::fetchPlaceholders).orElse(Collections.emptyList());
    }

    MoneyMarketList fetchPlaceholders(MoneyMarketList result) {
        return entityManager
            .createQuery(
                "select moneyMarketList from MoneyMarketList moneyMarketList left join fetch moneyMarketList.placeholders where moneyMarketList.id = :id",
                MoneyMarketList.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<MoneyMarketList> fetchPlaceholders(List<MoneyMarketList> moneyMarketLists) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, moneyMarketLists.size()).forEach(index -> order.put(moneyMarketLists.get(index).getId(), index));
        List<MoneyMarketList> result = entityManager
            .createQuery(
                "select moneyMarketList from MoneyMarketList moneyMarketList left join fetch moneyMarketList.placeholders where moneyMarketList in :moneyMarketLists",
                MoneyMarketList.class
            )
            .setParameter(MONEYMARKETLISTS_PARAMETER, moneyMarketLists)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
