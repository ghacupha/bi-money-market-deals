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

import io.github.bi.service.dto.MoneyMarketDealDailySummaryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.bi.domain.MoneyMarketDealDailySummary}.
 */
public interface MoneyMarketDealDailySummaryService {
    /**
     * Save a moneyMarketDealDailySummary.
     *
     * @param moneyMarketDealDailySummaryDTO the entity to save.
     * @return the persisted entity.
     */
    MoneyMarketDealDailySummaryDTO save(MoneyMarketDealDailySummaryDTO moneyMarketDealDailySummaryDTO);

    /**
     * Updates a moneyMarketDealDailySummary.
     *
     * @param moneyMarketDealDailySummaryDTO the entity to update.
     * @return the persisted entity.
     */
    MoneyMarketDealDailySummaryDTO update(MoneyMarketDealDailySummaryDTO moneyMarketDealDailySummaryDTO);

    /**
     * Partially updates a moneyMarketDealDailySummary.
     *
     * @param moneyMarketDealDailySummaryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MoneyMarketDealDailySummaryDTO> partialUpdate(MoneyMarketDealDailySummaryDTO moneyMarketDealDailySummaryDTO);

    /**
     * Get all the moneyMarketDealDailySummaries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MoneyMarketDealDailySummaryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" moneyMarketDealDailySummary.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MoneyMarketDealDailySummaryDTO> findOne(Long id);

    /**
     * Delete the "id" moneyMarketDealDailySummary.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the moneyMarketDealDailySummary corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MoneyMarketDealDailySummaryDTO> search(String query, Pageable pageable);
}
