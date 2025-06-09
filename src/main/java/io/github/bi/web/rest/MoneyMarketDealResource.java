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

import io.github.bi.repository.MoneyMarketDealRepository;
import io.github.bi.service.MoneyMarketDealQueryService;
import io.github.bi.service.MoneyMarketDealService;
import io.github.bi.service.criteria.MoneyMarketDealCriteria;
import io.github.bi.service.dto.MoneyMarketDealDTO;
import io.github.bi.web.rest.errors.BadRequestAlertException;
import io.github.bi.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link io.github.bi.domain.MoneyMarketDeal}.
 */
@RestController
@RequestMapping("/api/money-market-deals")
public class MoneyMarketDealResource {

    private static final Logger LOG = LoggerFactory.getLogger(MoneyMarketDealResource.class);

    private static final String ENTITY_NAME = "moneyMarketBiMoneyMarketDeal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MoneyMarketDealService moneyMarketDealService;

    private final MoneyMarketDealRepository moneyMarketDealRepository;

    private final MoneyMarketDealQueryService moneyMarketDealQueryService;

    public MoneyMarketDealResource(
        MoneyMarketDealService moneyMarketDealService,
        MoneyMarketDealRepository moneyMarketDealRepository,
        MoneyMarketDealQueryService moneyMarketDealQueryService
    ) {
        this.moneyMarketDealService = moneyMarketDealService;
        this.moneyMarketDealRepository = moneyMarketDealRepository;
        this.moneyMarketDealQueryService = moneyMarketDealQueryService;
    }

    /**
     * {@code POST  /money-market-deals} : Create a new moneyMarketDeal.
     *
     * @param moneyMarketDealDTO the moneyMarketDealDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new moneyMarketDealDTO, or with status {@code 400 (Bad Request)} if the moneyMarketDeal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MoneyMarketDealDTO> createMoneyMarketDeal(@Valid @RequestBody MoneyMarketDealDTO moneyMarketDealDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save MoneyMarketDeal : {}", moneyMarketDealDTO);
        if (moneyMarketDealDTO.getId() != null) {
            throw new BadRequestAlertException("A new moneyMarketDeal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        moneyMarketDealDTO = moneyMarketDealService.save(moneyMarketDealDTO);
        return ResponseEntity.created(new URI("/api/money-market-deals/" + moneyMarketDealDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, moneyMarketDealDTO.getId().toString()))
            .body(moneyMarketDealDTO);
    }

    /**
     * {@code PUT  /money-market-deals/:id} : Updates an existing moneyMarketDeal.
     *
     * @param id the id of the moneyMarketDealDTO to save.
     * @param moneyMarketDealDTO the moneyMarketDealDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moneyMarketDealDTO,
     * or with status {@code 400 (Bad Request)} if the moneyMarketDealDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the moneyMarketDealDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MoneyMarketDealDTO> updateMoneyMarketDeal(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MoneyMarketDealDTO moneyMarketDealDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MoneyMarketDeal : {}, {}", id, moneyMarketDealDTO);
        if (moneyMarketDealDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moneyMarketDealDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moneyMarketDealRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        moneyMarketDealDTO = moneyMarketDealService.update(moneyMarketDealDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, moneyMarketDealDTO.getId().toString()))
            .body(moneyMarketDealDTO);
    }

    /**
     * {@code PATCH  /money-market-deals/:id} : Partial updates given fields of an existing moneyMarketDeal, field will ignore if it is null
     *
     * @param id the id of the moneyMarketDealDTO to save.
     * @param moneyMarketDealDTO the moneyMarketDealDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moneyMarketDealDTO,
     * or with status {@code 400 (Bad Request)} if the moneyMarketDealDTO is not valid,
     * or with status {@code 404 (Not Found)} if the moneyMarketDealDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the moneyMarketDealDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MoneyMarketDealDTO> partialUpdateMoneyMarketDeal(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MoneyMarketDealDTO moneyMarketDealDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MoneyMarketDeal partially : {}, {}", id, moneyMarketDealDTO);
        if (moneyMarketDealDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moneyMarketDealDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moneyMarketDealRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MoneyMarketDealDTO> result = moneyMarketDealService.partialUpdate(moneyMarketDealDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, moneyMarketDealDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /money-market-deals} : get all the moneyMarketDeals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of moneyMarketDeals in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MoneyMarketDealDTO>> getAllMoneyMarketDeals(
        MoneyMarketDealCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MoneyMarketDeals by criteria: {}", criteria);

        Page<MoneyMarketDealDTO> page = moneyMarketDealQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /money-market-deals/count} : count all the moneyMarketDeals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMoneyMarketDeals(MoneyMarketDealCriteria criteria) {
        LOG.debug("REST request to count MoneyMarketDeals by criteria: {}", criteria);
        return ResponseEntity.ok().body(moneyMarketDealQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /money-market-deals/:id} : get the "id" moneyMarketDeal.
     *
     * @param id the id of the moneyMarketDealDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the moneyMarketDealDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MoneyMarketDealDTO> getMoneyMarketDeal(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MoneyMarketDeal : {}", id);
        Optional<MoneyMarketDealDTO> moneyMarketDealDTO = moneyMarketDealService.findOne(id);
        return ResponseUtil.wrapOrNotFound(moneyMarketDealDTO);
    }

    /**
     * {@code DELETE  /money-market-deals/:id} : delete the "id" moneyMarketDeal.
     *
     * @param id the id of the moneyMarketDealDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMoneyMarketDeal(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MoneyMarketDeal : {}", id);
        moneyMarketDealService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /money-market-deals/_search?query=:query} : search for the moneyMarketDeal corresponding
     * to the query.
     *
     * @param query the query of the moneyMarketDeal search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<MoneyMarketDealDTO>> searchMoneyMarketDeals(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of MoneyMarketDeals for query {}", query);
        try {
            Page<MoneyMarketDealDTO> page = moneyMarketDealService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
