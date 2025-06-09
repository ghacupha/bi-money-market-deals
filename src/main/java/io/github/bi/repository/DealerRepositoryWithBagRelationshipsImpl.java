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

import io.github.bi.domain.Dealer;
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
public class DealerRepositoryWithBagRelationshipsImpl implements DealerRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String DEALERS_PARAMETER = "dealers";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Dealer> fetchBagRelationships(Optional<Dealer> dealer) {
        return dealer.map(this::fetchPlaceholders);
    }

    @Override
    public Page<Dealer> fetchBagRelationships(Page<Dealer> dealers) {
        return new PageImpl<>(fetchBagRelationships(dealers.getContent()), dealers.getPageable(), dealers.getTotalElements());
    }

    @Override
    public List<Dealer> fetchBagRelationships(List<Dealer> dealers) {
        return Optional.of(dealers).map(this::fetchPlaceholders).orElse(Collections.emptyList());
    }

    Dealer fetchPlaceholders(Dealer result) {
        return entityManager
            .createQuery("select dealer from Dealer dealer left join fetch dealer.placeholders where dealer.id = :id", Dealer.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Dealer> fetchPlaceholders(List<Dealer> dealers) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, dealers.size()).forEach(index -> order.put(dealers.get(index).getId(), index));
        List<Dealer> result = entityManager
            .createQuery("select dealer from Dealer dealer left join fetch dealer.placeholders where dealer in :dealers", Dealer.class)
            .setParameter(DEALERS_PARAMETER, dealers)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
