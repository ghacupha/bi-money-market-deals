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

import io.github.bi.domain.ReportBatch;
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
public class ReportBatchRepositoryWithBagRelationshipsImpl implements ReportBatchRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String REPORTBATCHES_PARAMETER = "reportBatches";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<ReportBatch> fetchBagRelationships(Optional<ReportBatch> reportBatch) {
        return reportBatch.map(this::fetchPlaceholders);
    }

    @Override
    public Page<ReportBatch> fetchBagRelationships(Page<ReportBatch> reportBatches) {
        return new PageImpl<>(
            fetchBagRelationships(reportBatches.getContent()),
            reportBatches.getPageable(),
            reportBatches.getTotalElements()
        );
    }

    @Override
    public List<ReportBatch> fetchBagRelationships(List<ReportBatch> reportBatches) {
        return Optional.of(reportBatches).map(this::fetchPlaceholders).orElse(Collections.emptyList());
    }

    ReportBatch fetchPlaceholders(ReportBatch result) {
        return entityManager
            .createQuery(
                "select reportBatch from ReportBatch reportBatch left join fetch reportBatch.placeholders where reportBatch.id = :id",
                ReportBatch.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<ReportBatch> fetchPlaceholders(List<ReportBatch> reportBatches) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, reportBatches.size()).forEach(index -> order.put(reportBatches.get(index).getId(), index));
        List<ReportBatch> result = entityManager
            .createQuery(
                "select reportBatch from ReportBatch reportBatch left join fetch reportBatch.placeholders where reportBatch in :reportBatches",
                ReportBatch.class
            )
            .setParameter(REPORTBATCHES_PARAMETER, reportBatches)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
