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
import io.github.bi.domain.SecurityClearance;
import io.github.bi.repository.SecurityClearanceRepository;
import io.github.bi.repository.search.SecurityClearanceSearchRepository;
import io.github.bi.service.criteria.SecurityClearanceCriteria;
import io.github.bi.service.dto.SecurityClearanceDTO;
import io.github.bi.service.mapper.SecurityClearanceMapper;
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
 * Service for executing complex queries for {@link SecurityClearance} entities in the database.
 * The main input is a {@link SecurityClearanceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SecurityClearanceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SecurityClearanceQueryService extends QueryService<SecurityClearance> {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityClearanceQueryService.class);

    private final SecurityClearanceRepository securityClearanceRepository;

    private final SecurityClearanceMapper securityClearanceMapper;

    private final SecurityClearanceSearchRepository securityClearanceSearchRepository;

    public SecurityClearanceQueryService(
        SecurityClearanceRepository securityClearanceRepository,
        SecurityClearanceMapper securityClearanceMapper,
        SecurityClearanceSearchRepository securityClearanceSearchRepository
    ) {
        this.securityClearanceRepository = securityClearanceRepository;
        this.securityClearanceMapper = securityClearanceMapper;
        this.securityClearanceSearchRepository = securityClearanceSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link SecurityClearanceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SecurityClearanceDTO> findByCriteria(SecurityClearanceCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SecurityClearance> specification = createSpecification(criteria);
        return securityClearanceRepository
            .fetchBagRelationships(securityClearanceRepository.findAll(specification, page))
            .map(securityClearanceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SecurityClearanceCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SecurityClearance> specification = createSpecification(criteria);
        return securityClearanceRepository.count(specification);
    }

    /**
     * Function to convert {@link SecurityClearanceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SecurityClearance> createSpecification(SecurityClearanceCriteria criteria) {
        Specification<SecurityClearance> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), SecurityClearance_.id),
                buildStringSpecification(criteria.getClearanceLevel(), SecurityClearance_.clearanceLevel),
                buildRangeSpecification(criteria.getLevel(), SecurityClearance_.level),
                buildSpecification(criteria.getPlaceholderId(), root ->
                    root.join(SecurityClearance_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                )
            );
        }
        return specification;
    }
}
