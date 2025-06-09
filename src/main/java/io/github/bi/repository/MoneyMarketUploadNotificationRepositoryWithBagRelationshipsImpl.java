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

import io.github.bi.domain.MoneyMarketUploadNotification;
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
public class MoneyMarketUploadNotificationRepositoryWithBagRelationshipsImpl
    implements MoneyMarketUploadNotificationRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String MONEYMARKETUPLOADNOTIFICATIONS_PARAMETER = "moneyMarketUploadNotifications";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<MoneyMarketUploadNotification> fetchBagRelationships(
        Optional<MoneyMarketUploadNotification> moneyMarketUploadNotification
    ) {
        return moneyMarketUploadNotification.map(this::fetchPlaceholders);
    }

    @Override
    public Page<MoneyMarketUploadNotification> fetchBagRelationships(Page<MoneyMarketUploadNotification> moneyMarketUploadNotifications) {
        return new PageImpl<>(
            fetchBagRelationships(moneyMarketUploadNotifications.getContent()),
            moneyMarketUploadNotifications.getPageable(),
            moneyMarketUploadNotifications.getTotalElements()
        );
    }

    @Override
    public List<MoneyMarketUploadNotification> fetchBagRelationships(List<MoneyMarketUploadNotification> moneyMarketUploadNotifications) {
        return Optional.of(moneyMarketUploadNotifications).map(this::fetchPlaceholders).orElse(Collections.emptyList());
    }

    MoneyMarketUploadNotification fetchPlaceholders(MoneyMarketUploadNotification result) {
        return entityManager
            .createQuery(
                "select moneyMarketUploadNotification from MoneyMarketUploadNotification moneyMarketUploadNotification left join fetch moneyMarketUploadNotification.placeholders where moneyMarketUploadNotification.id = :id",
                MoneyMarketUploadNotification.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<MoneyMarketUploadNotification> fetchPlaceholders(List<MoneyMarketUploadNotification> moneyMarketUploadNotifications) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, moneyMarketUploadNotifications.size()).forEach(index ->
            order.put(moneyMarketUploadNotifications.get(index).getId(), index)
        );
        List<MoneyMarketUploadNotification> result = entityManager
            .createQuery(
                "select moneyMarketUploadNotification from MoneyMarketUploadNotification moneyMarketUploadNotification left join fetch moneyMarketUploadNotification.placeholders where moneyMarketUploadNotification in :moneyMarketUploadNotifications",
                MoneyMarketUploadNotification.class
            )
            .setParameter(MONEYMARKETUPLOADNOTIFICATIONS_PARAMETER, moneyMarketUploadNotifications)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
