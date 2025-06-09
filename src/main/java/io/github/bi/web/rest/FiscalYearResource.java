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

import io.github.bi.repository.FiscalYearRepository;
import io.github.bi.service.FiscalYearQueryService;
import io.github.bi.service.FiscalYearService;
import io.github.bi.service.criteria.FiscalYearCriteria;
import io.github.bi.service.dto.FiscalYearDTO;
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
 * REST controller for managing {@link io.github.bi.domain.FiscalYear}.
 */
@RestController
@RequestMapping("/api/fiscal-years")
public class FiscalYearResource {

    private static final Logger LOG = LoggerFactory.getLogger(FiscalYearResource.class);

    private static final String ENTITY_NAME = "maintenanceFiscalYear";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FiscalYearService fiscalYearService;

    private final FiscalYearRepository fiscalYearRepository;

    private final FiscalYearQueryService fiscalYearQueryService;

    public FiscalYearResource(
        FiscalYearService fiscalYearService,
        FiscalYearRepository fiscalYearRepository,
        FiscalYearQueryService fiscalYearQueryService
    ) {
        this.fiscalYearService = fiscalYearService;
        this.fiscalYearRepository = fiscalYearRepository;
        this.fiscalYearQueryService = fiscalYearQueryService;
    }

    /**
     * {@code POST  /fiscal-years} : Create a new fiscalYear.
     *
     * @param fiscalYearDTO the fiscalYearDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fiscalYearDTO, or with status {@code 400 (Bad Request)} if the fiscalYear has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FiscalYearDTO> createFiscalYear(@Valid @RequestBody FiscalYearDTO fiscalYearDTO) throws URISyntaxException {
        LOG.debug("REST request to save FiscalYear : {}", fiscalYearDTO);
        if (fiscalYearDTO.getId() != null) {
            throw new BadRequestAlertException("A new fiscalYear cannot already have an ID", ENTITY_NAME, "idexists");
        }
        fiscalYearDTO = fiscalYearService.save(fiscalYearDTO);
        return ResponseEntity.created(new URI("/api/fiscal-years/" + fiscalYearDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, fiscalYearDTO.getId().toString()))
            .body(fiscalYearDTO);
    }

    /**
     * {@code PUT  /fiscal-years/:id} : Updates an existing fiscalYear.
     *
     * @param id the id of the fiscalYearDTO to save.
     * @param fiscalYearDTO the fiscalYearDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fiscalYearDTO,
     * or with status {@code 400 (Bad Request)} if the fiscalYearDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fiscalYearDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FiscalYearDTO> updateFiscalYear(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FiscalYearDTO fiscalYearDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update FiscalYear : {}, {}", id, fiscalYearDTO);
        if (fiscalYearDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fiscalYearDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fiscalYearRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        fiscalYearDTO = fiscalYearService.update(fiscalYearDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fiscalYearDTO.getId().toString()))
            .body(fiscalYearDTO);
    }

    /**
     * {@code PATCH  /fiscal-years/:id} : Partial updates given fields of an existing fiscalYear, field will ignore if it is null
     *
     * @param id the id of the fiscalYearDTO to save.
     * @param fiscalYearDTO the fiscalYearDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fiscalYearDTO,
     * or with status {@code 400 (Bad Request)} if the fiscalYearDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fiscalYearDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fiscalYearDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FiscalYearDTO> partialUpdateFiscalYear(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FiscalYearDTO fiscalYearDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FiscalYear partially : {}, {}", id, fiscalYearDTO);
        if (fiscalYearDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fiscalYearDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fiscalYearRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FiscalYearDTO> result = fiscalYearService.partialUpdate(fiscalYearDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fiscalYearDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fiscal-years} : get all the fiscalYears.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fiscalYears in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FiscalYearDTO>> getAllFiscalYears(
        FiscalYearCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get FiscalYears by criteria: {}", criteria);

        Page<FiscalYearDTO> page = fiscalYearQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fiscal-years/count} : count all the fiscalYears.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFiscalYears(FiscalYearCriteria criteria) {
        LOG.debug("REST request to count FiscalYears by criteria: {}", criteria);
        return ResponseEntity.ok().body(fiscalYearQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fiscal-years/:id} : get the "id" fiscalYear.
     *
     * @param id the id of the fiscalYearDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fiscalYearDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FiscalYearDTO> getFiscalYear(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FiscalYear : {}", id);
        Optional<FiscalYearDTO> fiscalYearDTO = fiscalYearService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fiscalYearDTO);
    }

    /**
     * {@code DELETE  /fiscal-years/:id} : delete the "id" fiscalYear.
     *
     * @param id the id of the fiscalYearDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFiscalYear(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FiscalYear : {}", id);
        fiscalYearService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /fiscal-years/_search?query=:query} : search for the fiscalYear corresponding
     * to the query.
     *
     * @param query the query of the fiscalYear search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<FiscalYearDTO>> searchFiscalYears(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of FiscalYears for query {}", query);
        try {
            Page<FiscalYearDTO> page = fiscalYearService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
