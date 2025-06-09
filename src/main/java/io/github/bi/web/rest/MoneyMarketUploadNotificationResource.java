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

import io.github.bi.repository.MoneyMarketUploadNotificationRepository;
import io.github.bi.service.MoneyMarketUploadNotificationQueryService;
import io.github.bi.service.MoneyMarketUploadNotificationService;
import io.github.bi.service.criteria.MoneyMarketUploadNotificationCriteria;
import io.github.bi.service.dto.MoneyMarketUploadNotificationDTO;
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
 * REST controller for managing {@link io.github.bi.domain.MoneyMarketUploadNotification}.
 */
@RestController
@RequestMapping("/api/money-market-upload-notifications")
public class MoneyMarketUploadNotificationResource {

    private static final Logger LOG = LoggerFactory.getLogger(MoneyMarketUploadNotificationResource.class);

    private static final String ENTITY_NAME = "moneyMarketBiMoneyMarketUploadNotification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MoneyMarketUploadNotificationService moneyMarketUploadNotificationService;

    private final MoneyMarketUploadNotificationRepository moneyMarketUploadNotificationRepository;

    private final MoneyMarketUploadNotificationQueryService moneyMarketUploadNotificationQueryService;

    public MoneyMarketUploadNotificationResource(
        MoneyMarketUploadNotificationService moneyMarketUploadNotificationService,
        MoneyMarketUploadNotificationRepository moneyMarketUploadNotificationRepository,
        MoneyMarketUploadNotificationQueryService moneyMarketUploadNotificationQueryService
    ) {
        this.moneyMarketUploadNotificationService = moneyMarketUploadNotificationService;
        this.moneyMarketUploadNotificationRepository = moneyMarketUploadNotificationRepository;
        this.moneyMarketUploadNotificationQueryService = moneyMarketUploadNotificationQueryService;
    }

    /**
     * {@code POST  /money-market-upload-notifications} : Create a new moneyMarketUploadNotification.
     *
     * @param moneyMarketUploadNotificationDTO the moneyMarketUploadNotificationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new moneyMarketUploadNotificationDTO, or with status {@code 400 (Bad Request)} if the moneyMarketUploadNotification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MoneyMarketUploadNotificationDTO> createMoneyMarketUploadNotification(
        @Valid @RequestBody MoneyMarketUploadNotificationDTO moneyMarketUploadNotificationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save MoneyMarketUploadNotification : {}", moneyMarketUploadNotificationDTO);
        if (moneyMarketUploadNotificationDTO.getId() != null) {
            throw new BadRequestAlertException("A new moneyMarketUploadNotification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        moneyMarketUploadNotificationDTO = moneyMarketUploadNotificationService.save(moneyMarketUploadNotificationDTO);
        return ResponseEntity.created(new URI("/api/money-market-upload-notifications/" + moneyMarketUploadNotificationDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    applicationName,
                    false,
                    ENTITY_NAME,
                    moneyMarketUploadNotificationDTO.getId().toString()
                )
            )
            .body(moneyMarketUploadNotificationDTO);
    }

    /**
     * {@code PUT  /money-market-upload-notifications/:id} : Updates an existing moneyMarketUploadNotification.
     *
     * @param id the id of the moneyMarketUploadNotificationDTO to save.
     * @param moneyMarketUploadNotificationDTO the moneyMarketUploadNotificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moneyMarketUploadNotificationDTO,
     * or with status {@code 400 (Bad Request)} if the moneyMarketUploadNotificationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the moneyMarketUploadNotificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MoneyMarketUploadNotificationDTO> updateMoneyMarketUploadNotification(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MoneyMarketUploadNotificationDTO moneyMarketUploadNotificationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MoneyMarketUploadNotification : {}, {}", id, moneyMarketUploadNotificationDTO);
        if (moneyMarketUploadNotificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moneyMarketUploadNotificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moneyMarketUploadNotificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        moneyMarketUploadNotificationDTO = moneyMarketUploadNotificationService.update(moneyMarketUploadNotificationDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, moneyMarketUploadNotificationDTO.getId().toString())
            )
            .body(moneyMarketUploadNotificationDTO);
    }

    /**
     * {@code PATCH  /money-market-upload-notifications/:id} : Partial updates given fields of an existing moneyMarketUploadNotification, field will ignore if it is null
     *
     * @param id the id of the moneyMarketUploadNotificationDTO to save.
     * @param moneyMarketUploadNotificationDTO the moneyMarketUploadNotificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moneyMarketUploadNotificationDTO,
     * or with status {@code 400 (Bad Request)} if the moneyMarketUploadNotificationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the moneyMarketUploadNotificationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the moneyMarketUploadNotificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MoneyMarketUploadNotificationDTO> partialUpdateMoneyMarketUploadNotification(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MoneyMarketUploadNotificationDTO moneyMarketUploadNotificationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MoneyMarketUploadNotification partially : {}, {}", id, moneyMarketUploadNotificationDTO);
        if (moneyMarketUploadNotificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moneyMarketUploadNotificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moneyMarketUploadNotificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MoneyMarketUploadNotificationDTO> result = moneyMarketUploadNotificationService.partialUpdate(
            moneyMarketUploadNotificationDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, moneyMarketUploadNotificationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /money-market-upload-notifications} : get all the moneyMarketUploadNotifications.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of moneyMarketUploadNotifications in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MoneyMarketUploadNotificationDTO>> getAllMoneyMarketUploadNotifications(
        MoneyMarketUploadNotificationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MoneyMarketUploadNotifications by criteria: {}", criteria);

        Page<MoneyMarketUploadNotificationDTO> page = moneyMarketUploadNotificationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /money-market-upload-notifications/count} : count all the moneyMarketUploadNotifications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMoneyMarketUploadNotifications(MoneyMarketUploadNotificationCriteria criteria) {
        LOG.debug("REST request to count MoneyMarketUploadNotifications by criteria: {}", criteria);
        return ResponseEntity.ok().body(moneyMarketUploadNotificationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /money-market-upload-notifications/:id} : get the "id" moneyMarketUploadNotification.
     *
     * @param id the id of the moneyMarketUploadNotificationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the moneyMarketUploadNotificationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MoneyMarketUploadNotificationDTO> getMoneyMarketUploadNotification(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MoneyMarketUploadNotification : {}", id);
        Optional<MoneyMarketUploadNotificationDTO> moneyMarketUploadNotificationDTO = moneyMarketUploadNotificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(moneyMarketUploadNotificationDTO);
    }

    /**
     * {@code DELETE  /money-market-upload-notifications/:id} : delete the "id" moneyMarketUploadNotification.
     *
     * @param id the id of the moneyMarketUploadNotificationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMoneyMarketUploadNotification(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MoneyMarketUploadNotification : {}", id);
        moneyMarketUploadNotificationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /money-market-upload-notifications/_search?query=:query} : search for the moneyMarketUploadNotification corresponding
     * to the query.
     *
     * @param query the query of the moneyMarketUploadNotification search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<MoneyMarketUploadNotificationDTO>> searchMoneyMarketUploadNotifications(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of MoneyMarketUploadNotifications for query {}", query);
        try {
            Page<MoneyMarketUploadNotificationDTO> page = moneyMarketUploadNotificationService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
