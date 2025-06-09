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

import io.github.bi.domain.FiscalMonth;
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
public class FiscalMonthRepositoryWithBagRelationshipsImpl implements FiscalMonthRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String FISCALMONTHS_PARAMETER = "fiscalMonths";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<FiscalMonth> fetchBagRelationships(Optional<FiscalMonth> fiscalMonth) {
        return fiscalMonth.map(this::fetchPlaceholders);
    }

    @Override
    public Page<FiscalMonth> fetchBagRelationships(Page<FiscalMonth> fiscalMonths) {
        return new PageImpl<>(
            fetchBagRelationships(fiscalMonths.getContent()),
            fiscalMonths.getPageable(),
            fiscalMonths.getTotalElements()
        );
    }

    @Override
    public List<FiscalMonth> fetchBagRelationships(List<FiscalMonth> fiscalMonths) {
        return Optional.of(fiscalMonths).map(this::fetchPlaceholders).orElse(Collections.emptyList());
    }

    FiscalMonth fetchPlaceholders(FiscalMonth result) {
        return entityManager
            .createQuery(
                "select fiscalMonth from FiscalMonth fiscalMonth left join fetch fiscalMonth.placeholders where fiscalMonth.id = :id",
                FiscalMonth.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<FiscalMonth> fetchPlaceholders(List<FiscalMonth> fiscalMonths) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, fiscalMonths.size()).forEach(index -> order.put(fiscalMonths.get(index).getId(), index));
        List<FiscalMonth> result = entityManager
            .createQuery(
                "select fiscalMonth from FiscalMonth fiscalMonth left join fetch fiscalMonth.placeholders where fiscalMonth in :fiscalMonths",
                FiscalMonth.class
            )
            .setParameter(FISCALMONTHS_PARAMETER, fiscalMonths)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
