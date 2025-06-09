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

import io.github.bi.domain.*; // for static metamodels
import io.github.bi.domain.FiscalYear;
import io.github.bi.repository.FiscalYearRepository;
import io.github.bi.repository.search.FiscalYearSearchRepository;
import io.github.bi.service.criteria.FiscalYearCriteria;
import io.github.bi.service.dto.FiscalYearDTO;
import io.github.bi.service.mapper.FiscalYearMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link FiscalYear} entities in the database.
 * The main input is a {@link FiscalYearCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FiscalYearDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FiscalYearQueryService extends QueryService<FiscalYear> {

    private static final Logger LOG = LoggerFactory.getLogger(FiscalYearQueryService.class);

    private final FiscalYearRepository fiscalYearRepository;

    private final FiscalYearMapper fiscalYearMapper;

    private final FiscalYearSearchRepository fiscalYearSearchRepository;

    public FiscalYearQueryService(
        FiscalYearRepository fiscalYearRepository,
        FiscalYearMapper fiscalYearMapper,
        FiscalYearSearchRepository fiscalYearSearchRepository
    ) {
        this.fiscalYearRepository = fiscalYearRepository;
        this.fiscalYearMapper = fiscalYearMapper;
        this.fiscalYearSearchRepository = fiscalYearSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link FiscalYearDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FiscalYearDTO> findByCriteria(FiscalYearCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FiscalYear> specification = createSpecification(criteria);
        return fiscalYearRepository.fetchBagRelationships(fiscalYearRepository.findAll(specification, page)).map(fiscalYearMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FiscalYearCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<FiscalYear> specification = createSpecification(criteria);
        return fiscalYearRepository.count(specification);
    }

    /**
     * Function to convert {@link FiscalYearCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FiscalYear> createSpecification(FiscalYearCriteria criteria) {
        Specification<FiscalYear> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), FiscalYear_.id),
                buildStringSpecification(criteria.getFiscalYearCode(), FiscalYear_.fiscalYearCode),
                buildRangeSpecification(criteria.getStartDate(), FiscalYear_.startDate),
                buildRangeSpecification(criteria.getEndDate(), FiscalYear_.endDate),
                buildSpecification(criteria.getFiscalYearStatus(), FiscalYear_.fiscalYearStatus),
                buildSpecification(criteria.getPlaceholderId(), root ->
                    root.join(FiscalYear_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                )
            );
        }
        return specification;
    }
}
