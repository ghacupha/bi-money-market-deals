package io.github.bi.service;

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

import io.github.bi.service.dto.ReportBatchDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.bi.domain.ReportBatch}.
 */
public interface ReportBatchService {
    /**
     * Save a reportBatch.
     *
     * @param reportBatchDTO the entity to save.
     * @return the persisted entity.
     */
    ReportBatchDTO save(ReportBatchDTO reportBatchDTO);

    /**
     * Updates a reportBatch.
     *
     * @param reportBatchDTO the entity to update.
     * @return the persisted entity.
     */
    ReportBatchDTO update(ReportBatchDTO reportBatchDTO);

    /**
     * Partially updates a reportBatch.
     *
     * @param reportBatchDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReportBatchDTO> partialUpdate(ReportBatchDTO reportBatchDTO);

    /**
     * Get all the ReportBatchDTO where MoneyMarketList is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<ReportBatchDTO> findAllWhereMoneyMarketListIsNull();

    /**
     * Get all the reportBatches with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReportBatchDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" reportBatch.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReportBatchDTO> findOne(Long id);

    /**
     * Delete the "id" reportBatch.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the reportBatch corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReportBatchDTO> search(String query, Pageable pageable);
}
