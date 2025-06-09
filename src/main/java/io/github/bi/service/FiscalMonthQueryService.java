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
import io.github.bi.domain.FiscalMonth;
import io.github.bi.repository.FiscalMonthRepository;
import io.github.bi.repository.search.FiscalMonthSearchRepository;
import io.github.bi.service.criteria.FiscalMonthCriteria;
import io.github.bi.service.dto.FiscalMonthDTO;
import io.github.bi.service.mapper.FiscalMonthMapper;
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
 * Service for executing complex queries for {@link FiscalMonth} entities in the database.
 * The main input is a {@link FiscalMonthCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FiscalMonthDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FiscalMonthQueryService extends QueryService<FiscalMonth> {

    private static final Logger LOG = LoggerFactory.getLogger(FiscalMonthQueryService.class);

    private final FiscalMonthRepository fiscalMonthRepository;

    private final FiscalMonthMapper fiscalMonthMapper;

    private final FiscalMonthSearchRepository fiscalMonthSearchRepository;

    public FiscalMonthQueryService(
        FiscalMonthRepository fiscalMonthRepository,
        FiscalMonthMapper fiscalMonthMapper,
        FiscalMonthSearchRepository fiscalMonthSearchRepository
    ) {
        this.fiscalMonthRepository = fiscalMonthRepository;
        this.fiscalMonthMapper = fiscalMonthMapper;
        this.fiscalMonthSearchRepository = fiscalMonthSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link FiscalMonthDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FiscalMonthDTO> findByCriteria(FiscalMonthCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FiscalMonth> specification = createSpecification(criteria);
        return fiscalMonthRepository
            .fetchBagRelationships(fiscalMonthRepository.findAll(specification, page))
            .map(fiscalMonthMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FiscalMonthCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<FiscalMonth> specification = createSpecification(criteria);
        return fiscalMonthRepository.count(specification);
    }

    /**
     * Function to convert {@link FiscalMonthCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FiscalMonth> createSpecification(FiscalMonthCriteria criteria) {
        Specification<FiscalMonth> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), FiscalMonth_.id),
                buildRangeSpecification(criteria.getMonthNumber(), FiscalMonth_.monthNumber),
                buildRangeSpecification(criteria.getStartDate(), FiscalMonth_.startDate),
                buildRangeSpecification(criteria.getEndDate(), FiscalMonth_.endDate),
                buildStringSpecification(criteria.getFiscalMonthCode(), FiscalMonth_.fiscalMonthCode),
                buildSpecification(criteria.getFiscalYearId(), root -> root.join(FiscalMonth_.fiscalYear, JoinType.LEFT).get(FiscalYear_.id)
                ),
                buildSpecification(criteria.getPlaceholderId(), root ->
                    root.join(FiscalMonth_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                ),
                buildSpecification(criteria.getFiscalQuarterId(), root ->
                    root.join(FiscalMonth_.fiscalQuarter, JoinType.LEFT).get(FiscalQuarter_.id)
                )
            );
        }
        return specification;
    }
}
