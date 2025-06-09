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

import io.github.bi.repository.FiscalMonthRepository;
import io.github.bi.service.FiscalMonthQueryService;
import io.github.bi.service.FiscalMonthService;
import io.github.bi.service.criteria.FiscalMonthCriteria;
import io.github.bi.service.dto.FiscalMonthDTO;
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
 * REST controller for managing {@link io.github.bi.domain.FiscalMonth}.
 */
@RestController
@RequestMapping("/api/fiscal-months")
public class FiscalMonthResource {

    private static final Logger LOG = LoggerFactory.getLogger(FiscalMonthResource.class);

    private static final String ENTITY_NAME = "maintenanceFiscalMonth";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FiscalMonthService fiscalMonthService;

    private final FiscalMonthRepository fiscalMonthRepository;

    private final FiscalMonthQueryService fiscalMonthQueryService;

    public FiscalMonthResource(
        FiscalMonthService fiscalMonthService,
        FiscalMonthRepository fiscalMonthRepository,
        FiscalMonthQueryService fiscalMonthQueryService
    ) {
        this.fiscalMonthService = fiscalMonthService;
        this.fiscalMonthRepository = fiscalMonthRepository;
        this.fiscalMonthQueryService = fiscalMonthQueryService;
    }

    /**
     * {@code POST  /fiscal-months} : Create a new fiscalMonth.
     *
     * @param fiscalMonthDTO the fiscalMonthDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fiscalMonthDTO, or with status {@code 400 (Bad Request)} if the fiscalMonth has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FiscalMonthDTO> createFiscalMonth(@Valid @RequestBody FiscalMonthDTO fiscalMonthDTO) throws URISyntaxException {
        LOG.debug("REST request to save FiscalMonth : {}", fiscalMonthDTO);
        if (fiscalMonthDTO.getId() != null) {
            throw new BadRequestAlertException("A new fiscalMonth cannot already have an ID", ENTITY_NAME, "idexists");
        }
        fiscalMonthDTO = fiscalMonthService.save(fiscalMonthDTO);
        return ResponseEntity.created(new URI("/api/fiscal-months/" + fiscalMonthDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, fiscalMonthDTO.getId().toString()))
            .body(fiscalMonthDTO);
    }

    /**
     * {@code PUT  /fiscal-months/:id} : Updates an existing fiscalMonth.
     *
     * @param id the id of the fiscalMonthDTO to save.
     * @param fiscalMonthDTO the fiscalMonthDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fiscalMonthDTO,
     * or with status {@code 400 (Bad Request)} if the fiscalMonthDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fiscalMonthDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FiscalMonthDTO> updateFiscalMonth(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FiscalMonthDTO fiscalMonthDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update FiscalMonth : {}, {}", id, fiscalMonthDTO);
        if (fiscalMonthDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fiscalMonthDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fiscalMonthRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        fiscalMonthDTO = fiscalMonthService.update(fiscalMonthDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fiscalMonthDTO.getId().toString()))
            .body(fiscalMonthDTO);
    }

    /**
     * {@code PATCH  /fiscal-months/:id} : Partial updates given fields of an existing fiscalMonth, field will ignore if it is null
     *
     * @param id the id of the fiscalMonthDTO to save.
     * @param fiscalMonthDTO the fiscalMonthDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fiscalMonthDTO,
     * or with status {@code 400 (Bad Request)} if the fiscalMonthDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fiscalMonthDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fiscalMonthDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FiscalMonthDTO> partialUpdateFiscalMonth(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FiscalMonthDTO fiscalMonthDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FiscalMonth partially : {}, {}", id, fiscalMonthDTO);
        if (fiscalMonthDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fiscalMonthDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fiscalMonthRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FiscalMonthDTO> result = fiscalMonthService.partialUpdate(fiscalMonthDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fiscalMonthDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fiscal-months} : get all the fiscalMonths.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fiscalMonths in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FiscalMonthDTO>> getAllFiscalMonths(
        FiscalMonthCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get FiscalMonths by criteria: {}", criteria);

        Page<FiscalMonthDTO> page = fiscalMonthQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fiscal-months/count} : count all the fiscalMonths.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFiscalMonths(FiscalMonthCriteria criteria) {
        LOG.debug("REST request to count FiscalMonths by criteria: {}", criteria);
        return ResponseEntity.ok().body(fiscalMonthQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fiscal-months/:id} : get the "id" fiscalMonth.
     *
     * @param id the id of the fiscalMonthDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fiscalMonthDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FiscalMonthDTO> getFiscalMonth(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FiscalMonth : {}", id);
        Optional<FiscalMonthDTO> fiscalMonthDTO = fiscalMonthService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fiscalMonthDTO);
    }

    /**
     * {@code DELETE  /fiscal-months/:id} : delete the "id" fiscalMonth.
     *
     * @param id the id of the fiscalMonthDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFiscalMonth(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FiscalMonth : {}", id);
        fiscalMonthService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /fiscal-months/_search?query=:query} : search for the fiscalMonth corresponding
     * to the query.
     *
     * @param query the query of the fiscalMonth search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<FiscalMonthDTO>> searchFiscalMonths(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of FiscalMonths for query {}", query);
        try {
            Page<FiscalMonthDTO> page = fiscalMonthService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
