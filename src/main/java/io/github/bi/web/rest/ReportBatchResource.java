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

import io.github.bi.repository.ReportBatchRepository;
import io.github.bi.service.ReportBatchQueryService;
import io.github.bi.service.ReportBatchService;
import io.github.bi.service.criteria.ReportBatchCriteria;
import io.github.bi.service.dto.ReportBatchDTO;
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
 * REST controller for managing {@link io.github.bi.domain.ReportBatch}.
 */
@RestController
@RequestMapping("/api/report-batches")
public class ReportBatchResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReportBatchResource.class);

    private static final String ENTITY_NAME = "moneyMarketBiReportBatch";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportBatchService reportBatchService;

    private final ReportBatchRepository reportBatchRepository;

    private final ReportBatchQueryService reportBatchQueryService;

    public ReportBatchResource(
        ReportBatchService reportBatchService,
        ReportBatchRepository reportBatchRepository,
        ReportBatchQueryService reportBatchQueryService
    ) {
        this.reportBatchService = reportBatchService;
        this.reportBatchRepository = reportBatchRepository;
        this.reportBatchQueryService = reportBatchQueryService;
    }

    /**
     * {@code POST  /report-batches} : Create a new reportBatch.
     *
     * @param reportBatchDTO the reportBatchDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportBatchDTO, or with status {@code 400 (Bad Request)} if the reportBatch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReportBatchDTO> createReportBatch(@Valid @RequestBody ReportBatchDTO reportBatchDTO) throws URISyntaxException {
        LOG.debug("REST request to save ReportBatch : {}", reportBatchDTO);
        if (reportBatchDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportBatch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reportBatchDTO = reportBatchService.save(reportBatchDTO);
        return ResponseEntity.created(new URI("/api/report-batches/" + reportBatchDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, reportBatchDTO.getId().toString()))
            .body(reportBatchDTO);
    }

    /**
     * {@code PUT  /report-batches/:id} : Updates an existing reportBatch.
     *
     * @param id the id of the reportBatchDTO to save.
     * @param reportBatchDTO the reportBatchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportBatchDTO,
     * or with status {@code 400 (Bad Request)} if the reportBatchDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportBatchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReportBatchDTO> updateReportBatch(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReportBatchDTO reportBatchDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReportBatch : {}, {}", id, reportBatchDTO);
        if (reportBatchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportBatchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportBatchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reportBatchDTO = reportBatchService.update(reportBatchDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, reportBatchDTO.getId().toString()))
            .body(reportBatchDTO);
    }

    /**
     * {@code PATCH  /report-batches/:id} : Partial updates given fields of an existing reportBatch, field will ignore if it is null
     *
     * @param id the id of the reportBatchDTO to save.
     * @param reportBatchDTO the reportBatchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportBatchDTO,
     * or with status {@code 400 (Bad Request)} if the reportBatchDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportBatchDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportBatchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportBatchDTO> partialUpdateReportBatch(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReportBatchDTO reportBatchDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReportBatch partially : {}, {}", id, reportBatchDTO);
        if (reportBatchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportBatchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportBatchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportBatchDTO> result = reportBatchService.partialUpdate(reportBatchDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, reportBatchDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /report-batches} : get all the reportBatches.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportBatches in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReportBatchDTO>> getAllReportBatches(
        ReportBatchCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ReportBatches by criteria: {}", criteria);

        Page<ReportBatchDTO> page = reportBatchQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /report-batches/count} : count all the reportBatches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReportBatches(ReportBatchCriteria criteria) {
        LOG.debug("REST request to count ReportBatches by criteria: {}", criteria);
        return ResponseEntity.ok().body(reportBatchQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /report-batches/:id} : get the "id" reportBatch.
     *
     * @param id the id of the reportBatchDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportBatchDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReportBatchDTO> getReportBatch(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReportBatch : {}", id);
        Optional<ReportBatchDTO> reportBatchDTO = reportBatchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportBatchDTO);
    }

    /**
     * {@code DELETE  /report-batches/:id} : delete the "id" reportBatch.
     *
     * @param id the id of the reportBatchDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReportBatch(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReportBatch : {}", id);
        reportBatchService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /report-batches/_search?query=:query} : search for the reportBatch corresponding
     * to the query.
     *
     * @param query the query of the reportBatch search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<ReportBatchDTO>> searchReportBatches(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of ReportBatches for query {}", query);
        try {
            Page<ReportBatchDTO> page = reportBatchService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
