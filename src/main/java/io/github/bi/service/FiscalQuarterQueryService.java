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
import io.github.bi.domain.FiscalQuarter;
import io.github.bi.repository.FiscalQuarterRepository;
import io.github.bi.repository.search.FiscalQuarterSearchRepository;
import io.github.bi.service.criteria.FiscalQuarterCriteria;
import io.github.bi.service.dto.FiscalQuarterDTO;
import io.github.bi.service.mapper.FiscalQuarterMapper;
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
 * Service for executing complex queries for {@link FiscalQuarter} entities in the database.
 * The main input is a {@link FiscalQuarterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FiscalQuarterDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FiscalQuarterQueryService extends QueryService<FiscalQuarter> {

    private static final Logger LOG = LoggerFactory.getLogger(FiscalQuarterQueryService.class);

    private final FiscalQuarterRepository fiscalQuarterRepository;

    private final FiscalQuarterMapper fiscalQuarterMapper;

    private final FiscalQuarterSearchRepository fiscalQuarterSearchRepository;

    public FiscalQuarterQueryService(
        FiscalQuarterRepository fiscalQuarterRepository,
        FiscalQuarterMapper fiscalQuarterMapper,
        FiscalQuarterSearchRepository fiscalQuarterSearchRepository
    ) {
        this.fiscalQuarterRepository = fiscalQuarterRepository;
        this.fiscalQuarterMapper = fiscalQuarterMapper;
        this.fiscalQuarterSearchRepository = fiscalQuarterSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link FiscalQuarterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FiscalQuarterDTO> findByCriteria(FiscalQuarterCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FiscalQuarter> specification = createSpecification(criteria);
        return fiscalQuarterRepository
            .fetchBagRelationships(fiscalQuarterRepository.findAll(specification, page))
            .map(fiscalQuarterMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FiscalQuarterCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<FiscalQuarter> specification = createSpecification(criteria);
        return fiscalQuarterRepository.count(specification);
    }

    /**
     * Function to convert {@link FiscalQuarterCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FiscalQuarter> createSpecification(FiscalQuarterCriteria criteria) {
        Specification<FiscalQuarter> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), FiscalQuarter_.id),
                buildRangeSpecification(criteria.getQuarterNumber(), FiscalQuarter_.quarterNumber),
                buildRangeSpecification(criteria.getStartDate(), FiscalQuarter_.startDate),
                buildRangeSpecification(criteria.getEndDate(), FiscalQuarter_.endDate),
                buildStringSpecification(criteria.getFiscalQuarterCode(), FiscalQuarter_.fiscalQuarterCode),
                buildSpecification(criteria.getFiscalYearId(), root ->
                    root.join(FiscalQuarter_.fiscalYear, JoinType.LEFT).get(FiscalYear_.id)
                ),
                buildSpecification(criteria.getPlaceholderId(), root ->
                    root.join(FiscalQuarter_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                )
            );
        }
        return specification;
    }
}
