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
import io.github.bi.domain.ApplicationUser;
import io.github.bi.repository.ApplicationUserRepository;
import io.github.bi.repository.search.ApplicationUserSearchRepository;
import io.github.bi.service.criteria.ApplicationUserCriteria;
import io.github.bi.service.dto.ApplicationUserDTO;
import io.github.bi.service.mapper.ApplicationUserMapper;
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
 * Service for executing complex queries for {@link ApplicationUser} entities in the database.
 * The main input is a {@link ApplicationUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ApplicationUserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ApplicationUserQueryService extends QueryService<ApplicationUser> {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationUserQueryService.class);

    private final ApplicationUserRepository applicationUserRepository;

    private final ApplicationUserMapper applicationUserMapper;

    private final ApplicationUserSearchRepository applicationUserSearchRepository;

    public ApplicationUserQueryService(
        ApplicationUserRepository applicationUserRepository,
        ApplicationUserMapper applicationUserMapper,
        ApplicationUserSearchRepository applicationUserSearchRepository
    ) {
        this.applicationUserRepository = applicationUserRepository;
        this.applicationUserMapper = applicationUserMapper;
        this.applicationUserSearchRepository = applicationUserSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link ApplicationUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ApplicationUserDTO> findByCriteria(ApplicationUserCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ApplicationUser> specification = createSpecification(criteria);
        return applicationUserRepository
            .fetchBagRelationships(applicationUserRepository.findAll(specification, page))
            .map(applicationUserMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ApplicationUserCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ApplicationUser> specification = createSpecification(criteria);
        return applicationUserRepository.count(specification);
    }

    /**
     * Function to convert {@link ApplicationUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ApplicationUser> createSpecification(ApplicationUserCriteria criteria) {
        Specification<ApplicationUser> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ApplicationUser_.id),
                buildSpecification(criteria.getDesignation(), ApplicationUser_.designation),
                buildStringSpecification(criteria.getApplicationIdentity(), ApplicationUser_.applicationIdentity),
                buildSpecification(criteria.getOrganizationId(), root ->
                    root.join(ApplicationUser_.organization, JoinType.LEFT).get(Dealer_.id)
                ),
                buildSpecification(criteria.getDepartmentId(), root -> root.join(ApplicationUser_.department, JoinType.LEFT).get(Dealer_.id)
                ),
                buildSpecification(criteria.getSecurityClearanceId(), root ->
                    root.join(ApplicationUser_.securityClearance, JoinType.LEFT).get(SecurityClearance_.id)
                ),
                buildSpecification(criteria.getDealerIdentityId(), root ->
                    root.join(ApplicationUser_.dealerIdentity, JoinType.LEFT).get(Dealer_.id)
                ),
                buildSpecification(criteria.getPlaceholderId(), root ->
                    root.join(ApplicationUser_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                ),
                buildSpecification(criteria.getReportBatchId(), root ->
                    root.join(ApplicationUser_.reportBatches, JoinType.LEFT).get(ReportBatch_.id)
                ),
                buildSpecification(criteria.getMoneyMarketListId(), root ->
                    root.join(ApplicationUser_.moneyMarketLists, JoinType.LEFT).get(MoneyMarketList_.id)
                )
            );
        }
        return specification;
    }
}
