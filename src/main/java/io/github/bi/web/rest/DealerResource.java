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

import io.github.bi.repository.DealerRepository;
import io.github.bi.service.DealerQueryService;
import io.github.bi.service.DealerService;
import io.github.bi.service.criteria.DealerCriteria;
import io.github.bi.service.dto.DealerDTO;
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
 * REST controller for managing {@link io.github.bi.domain.Dealer}.
 */
@RestController
@RequestMapping("/api/dealers")
public class DealerResource {

    private static final Logger LOG = LoggerFactory.getLogger(DealerResource.class);

    private static final String ENTITY_NAME = "moneyMarketBiDealer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DealerService dealerService;

    private final DealerRepository dealerRepository;

    private final DealerQueryService dealerQueryService;

    public DealerResource(DealerService dealerService, DealerRepository dealerRepository, DealerQueryService dealerQueryService) {
        this.dealerService = dealerService;
        this.dealerRepository = dealerRepository;
        this.dealerQueryService = dealerQueryService;
    }

    /**
     * {@code POST  /dealers} : Create a new dealer.
     *
     * @param dealerDTO the dealerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dealerDTO, or with status {@code 400 (Bad Request)} if the dealer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DealerDTO> createDealer(@Valid @RequestBody DealerDTO dealerDTO) throws URISyntaxException {
        LOG.debug("REST request to save Dealer : {}", dealerDTO);
        if (dealerDTO.getId() != null) {
            throw new BadRequestAlertException("A new dealer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        dealerDTO = dealerService.save(dealerDTO);
        return ResponseEntity.created(new URI("/api/dealers/" + dealerDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, dealerDTO.getId().toString()))
            .body(dealerDTO);
    }

    /**
     * {@code PUT  /dealers/:id} : Updates an existing dealer.
     *
     * @param id the id of the dealerDTO to save.
     * @param dealerDTO the dealerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dealerDTO,
     * or with status {@code 400 (Bad Request)} if the dealerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dealerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DealerDTO> updateDealer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DealerDTO dealerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Dealer : {}, {}", id, dealerDTO);
        if (dealerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dealerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dealerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        dealerDTO = dealerService.update(dealerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dealerDTO.getId().toString()))
            .body(dealerDTO);
    }

    /**
     * {@code PATCH  /dealers/:id} : Partial updates given fields of an existing dealer, field will ignore if it is null
     *
     * @param id the id of the dealerDTO to save.
     * @param dealerDTO the dealerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dealerDTO,
     * or with status {@code 400 (Bad Request)} if the dealerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dealerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dealerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DealerDTO> partialUpdateDealer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DealerDTO dealerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Dealer partially : {}, {}", id, dealerDTO);
        if (dealerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dealerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dealerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DealerDTO> result = dealerService.partialUpdate(dealerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dealerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /dealers} : get all the dealers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dealers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DealerDTO>> getAllDealers(
        DealerCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Dealers by criteria: {}", criteria);

        Page<DealerDTO> page = dealerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dealers/count} : count all the dealers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDealers(DealerCriteria criteria) {
        LOG.debug("REST request to count Dealers by criteria: {}", criteria);
        return ResponseEntity.ok().body(dealerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /dealers/:id} : get the "id" dealer.
     *
     * @param id the id of the dealerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dealerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DealerDTO> getDealer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Dealer : {}", id);
        Optional<DealerDTO> dealerDTO = dealerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dealerDTO);
    }

    /**
     * {@code DELETE  /dealers/:id} : delete the "id" dealer.
     *
     * @param id the id of the dealerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDealer(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Dealer : {}", id);
        dealerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /dealers/_search?query=:query} : search for the dealer corresponding
     * to the query.
     *
     * @param query the query of the dealer search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<DealerDTO>> searchDealers(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of Dealers for query {}", query);
        try {
            Page<DealerDTO> page = dealerService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
