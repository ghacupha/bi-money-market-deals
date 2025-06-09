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

import io.github.bi.domain.FiscalYear;
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
public class FiscalYearRepositoryWithBagRelationshipsImpl implements FiscalYearRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String FISCALYEARS_PARAMETER = "fiscalYears";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<FiscalYear> fetchBagRelationships(Optional<FiscalYear> fiscalYear) {
        return fiscalYear.map(this::fetchPlaceholders);
    }

    @Override
    public Page<FiscalYear> fetchBagRelationships(Page<FiscalYear> fiscalYears) {
        return new PageImpl<>(fetchBagRelationships(fiscalYears.getContent()), fiscalYears.getPageable(), fiscalYears.getTotalElements());
    }

    @Override
    public List<FiscalYear> fetchBagRelationships(List<FiscalYear> fiscalYears) {
        return Optional.of(fiscalYears).map(this::fetchPlaceholders).orElse(Collections.emptyList());
    }

    FiscalYear fetchPlaceholders(FiscalYear result) {
        return entityManager
            .createQuery(
                "select fiscalYear from FiscalYear fiscalYear left join fetch fiscalYear.placeholders where fiscalYear.id = :id",
                FiscalYear.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<FiscalYear> fetchPlaceholders(List<FiscalYear> fiscalYears) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, fiscalYears.size()).forEach(index -> order.put(fiscalYears.get(index).getId(), index));
        List<FiscalYear> result = entityManager
            .createQuery(
                "select fiscalYear from FiscalYear fiscalYear left join fetch fiscalYear.placeholders where fiscalYear in :fiscalYears",
                FiscalYear.class
            )
            .setParameter(FISCALYEARS_PARAMETER, fiscalYears)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
