package io.github.bi.web.rest;

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

import io.github.bi.service.MoneyMarketDealDailySummaryService;
import io.github.bi.service.dto.MoneyMarketDealDailySummaryDTO;
import io.github.bi.web.rest.errors.ElasticsearchExceptionMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link io.github.bi.domain.MoneyMarketDealDailySummary}.
 */
@RestController
@RequestMapping("/api/money-market-deal-daily-summaries")
public class MoneyMarketDealDailySummaryResource {

    private static final Logger LOG = LoggerFactory.getLogger(MoneyMarketDealDailySummaryResource.class);

    private final MoneyMarketDealDailySummaryService moneyMarketDealDailySummaryService;

    public MoneyMarketDealDailySummaryResource(MoneyMarketDealDailySummaryService moneyMarketDealDailySummaryService) {
        this.moneyMarketDealDailySummaryService = moneyMarketDealDailySummaryService;
    }

    /**
     * {@code GET  /money-market-deal-daily-summaries} : get all the moneyMarketDealDailySummaries.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of moneyMarketDealDailySummaries in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MoneyMarketDealDailySummaryDTO>> getAllMoneyMarketDealDailySummaries(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of MoneyMarketDealDailySummaries");
        Page<MoneyMarketDealDailySummaryDTO> page = moneyMarketDealDailySummaryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /money-market-deal-daily-summaries/:id} : get the "id" moneyMarketDealDailySummary.
     *
     * @param id the id of the moneyMarketDealDailySummaryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the moneyMarketDealDailySummaryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MoneyMarketDealDailySummaryDTO> getMoneyMarketDealDailySummary(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MoneyMarketDealDailySummary : {}", id);
        Optional<MoneyMarketDealDailySummaryDTO> moneyMarketDealDailySummaryDTO = moneyMarketDealDailySummaryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(moneyMarketDealDailySummaryDTO);
    }

    /**
     * {@code SEARCH  /money-market-deal-daily-summaries/_search?query=:query} : search for the moneyMarketDealDailySummary corresponding
     * to the query.
     *
     * @param query the query of the moneyMarketDealDailySummary search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<MoneyMarketDealDailySummaryDTO>> searchMoneyMarketDealDailySummaries(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of MoneyMarketDealDailySummaries for query {}", query);
        try {
            Page<MoneyMarketDealDailySummaryDTO> page = moneyMarketDealDailySummaryService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
