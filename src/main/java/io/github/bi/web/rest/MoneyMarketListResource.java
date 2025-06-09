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

import io.github.bi.repository.MoneyMarketListRepository;
import io.github.bi.service.MoneyMarketListQueryService;
import io.github.bi.service.MoneyMarketListService;
import io.github.bi.service.criteria.MoneyMarketListCriteria;
import io.github.bi.service.dto.MoneyMarketListDTO;
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
 * REST controller for managing {@link io.github.bi.domain.MoneyMarketList}.
 */
@RestController
@RequestMapping("/api/money-market-lists")
public class MoneyMarketListResource {

    private static final Logger LOG = LoggerFactory.getLogger(MoneyMarketListResource.class);

    private static final String ENTITY_NAME = "moneyMarketBiMoneyMarketList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MoneyMarketListService moneyMarketListService;

    private final MoneyMarketListRepository moneyMarketListRepository;

    private final MoneyMarketListQueryService moneyMarketListQueryService;

    public MoneyMarketListResource(
        MoneyMarketListService moneyMarketListService,
        MoneyMarketListRepository moneyMarketListRepository,
        MoneyMarketListQueryService moneyMarketListQueryService
    ) {
        this.moneyMarketListService = moneyMarketListService;
        this.moneyMarketListRepository = moneyMarketListRepository;
        this.moneyMarketListQueryService = moneyMarketListQueryService;
    }

    /**
     * {@code POST  /money-market-lists} : Create a new moneyMarketList.
     *
     * @param moneyMarketListDTO the moneyMarketListDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new moneyMarketListDTO, or with status {@code 400 (Bad Request)} if the moneyMarketList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MoneyMarketListDTO> createMoneyMarketList(@Valid @RequestBody MoneyMarketListDTO moneyMarketListDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save MoneyMarketList : {}", moneyMarketListDTO);
        if (moneyMarketListDTO.getId() != null) {
            throw new BadRequestAlertException("A new moneyMarketList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (Objects.isNull(moneyMarketListDTO.getReportBatch())) {
            throw new BadRequestAlertException("Invalid association value provided", ENTITY_NAME, "null");
        }
        moneyMarketListDTO = moneyMarketListService.save(moneyMarketListDTO);
        return ResponseEntity.created(new URI("/api/money-market-lists/" + moneyMarketListDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, moneyMarketListDTO.getId().toString()))
            .body(moneyMarketListDTO);
    }

    /**
     * {@code PUT  /money-market-lists/:id} : Updates an existing moneyMarketList.
     *
     * @param id the id of the moneyMarketListDTO to save.
     * @param moneyMarketListDTO the moneyMarketListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moneyMarketListDTO,
     * or with status {@code 400 (Bad Request)} if the moneyMarketListDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the moneyMarketListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MoneyMarketListDTO> updateMoneyMarketList(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MoneyMarketListDTO moneyMarketListDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MoneyMarketList : {}, {}", id, moneyMarketListDTO);
        if (moneyMarketListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moneyMarketListDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moneyMarketListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        moneyMarketListDTO = moneyMarketListService.update(moneyMarketListDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, moneyMarketListDTO.getId().toString()))
            .body(moneyMarketListDTO);
    }

    /**
     * {@code PATCH  /money-market-lists/:id} : Partial updates given fields of an existing moneyMarketList, field will ignore if it is null
     *
     * @param id the id of the moneyMarketListDTO to save.
     * @param moneyMarketListDTO the moneyMarketListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moneyMarketListDTO,
     * or with status {@code 400 (Bad Request)} if the moneyMarketListDTO is not valid,
     * or with status {@code 404 (Not Found)} if the moneyMarketListDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the moneyMarketListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MoneyMarketListDTO> partialUpdateMoneyMarketList(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MoneyMarketListDTO moneyMarketListDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MoneyMarketList partially : {}, {}", id, moneyMarketListDTO);
        if (moneyMarketListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moneyMarketListDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moneyMarketListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MoneyMarketListDTO> result = moneyMarketListService.partialUpdate(moneyMarketListDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, moneyMarketListDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /money-market-lists} : get all the moneyMarketLists.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of moneyMarketLists in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MoneyMarketListDTO>> getAllMoneyMarketLists(
        MoneyMarketListCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MoneyMarketLists by criteria: {}", criteria);

        Page<MoneyMarketListDTO> page = moneyMarketListQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /money-market-lists/count} : count all the moneyMarketLists.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMoneyMarketLists(MoneyMarketListCriteria criteria) {
        LOG.debug("REST request to count MoneyMarketLists by criteria: {}", criteria);
        return ResponseEntity.ok().body(moneyMarketListQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /money-market-lists/:id} : get the "id" moneyMarketList.
     *
     * @param id the id of the moneyMarketListDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the moneyMarketListDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MoneyMarketListDTO> getMoneyMarketList(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MoneyMarketList : {}", id);
        Optional<MoneyMarketListDTO> moneyMarketListDTO = moneyMarketListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(moneyMarketListDTO);
    }

    /**
     * {@code DELETE  /money-market-lists/:id} : delete the "id" moneyMarketList.
     *
     * @param id the id of the moneyMarketListDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMoneyMarketList(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MoneyMarketList : {}", id);
        moneyMarketListService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /money-market-lists/_search?query=:query} : search for the moneyMarketList corresponding
     * to the query.
     *
     * @param query the query of the moneyMarketList search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<MoneyMarketListDTO>> searchMoneyMarketLists(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of MoneyMarketLists for query {}", query);
        try {
            Page<MoneyMarketListDTO> page = moneyMarketListService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
