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

import io.github.bi.domain.FiscalQuarter;
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
public class FiscalQuarterRepositoryWithBagRelationshipsImpl implements FiscalQuarterRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String FISCALQUARTERS_PARAMETER = "fiscalQuarters";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<FiscalQuarter> fetchBagRelationships(Optional<FiscalQuarter> fiscalQuarter) {
        return fiscalQuarter.map(this::fetchPlaceholders);
    }

    @Override
    public Page<FiscalQuarter> fetchBagRelationships(Page<FiscalQuarter> fiscalQuarters) {
        return new PageImpl<>(
            fetchBagRelationships(fiscalQuarters.getContent()),
            fiscalQuarters.getPageable(),
            fiscalQuarters.getTotalElements()
        );
    }

    @Override
    public List<FiscalQuarter> fetchBagRelationships(List<FiscalQuarter> fiscalQuarters) {
        return Optional.of(fiscalQuarters).map(this::fetchPlaceholders).orElse(Collections.emptyList());
    }

    FiscalQuarter fetchPlaceholders(FiscalQuarter result) {
        return entityManager
            .createQuery(
                "select fiscalQuarter from FiscalQuarter fiscalQuarter left join fetch fiscalQuarter.placeholders where fiscalQuarter.id = :id",
                FiscalQuarter.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<FiscalQuarter> fetchPlaceholders(List<FiscalQuarter> fiscalQuarters) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, fiscalQuarters.size()).forEach(index -> order.put(fiscalQuarters.get(index).getId(), index));
        List<FiscalQuarter> result = entityManager
            .createQuery(
                "select fiscalQuarter from FiscalQuarter fiscalQuarter left join fetch fiscalQuarter.placeholders where fiscalQuarter in :fiscalQuarters",
                FiscalQuarter.class
            )
            .setParameter(FISCALQUARTERS_PARAMETER, fiscalQuarters)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
