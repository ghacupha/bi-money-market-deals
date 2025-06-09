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
import io.github.bi.domain.Placeholder;
import io.github.bi.repository.PlaceholderRepository;
import io.github.bi.repository.search.PlaceholderSearchRepository;
import io.github.bi.service.criteria.PlaceholderCriteria;
import io.github.bi.service.dto.PlaceholderDTO;
import io.github.bi.service.mapper.PlaceholderMapper;
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
 * Service for executing complex queries for {@link Placeholder} entities in the database.
 * The main input is a {@link PlaceholderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PlaceholderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlaceholderQueryService extends QueryService<Placeholder> {

    private static final Logger LOG = LoggerFactory.getLogger(PlaceholderQueryService.class);

    private final PlaceholderRepository placeholderRepository;

    private final PlaceholderMapper placeholderMapper;

    private final PlaceholderSearchRepository placeholderSearchRepository;

    public PlaceholderQueryService(
        PlaceholderRepository placeholderRepository,
        PlaceholderMapper placeholderMapper,
        PlaceholderSearchRepository placeholderSearchRepository
    ) {
        this.placeholderRepository = placeholderRepository;
        this.placeholderMapper = placeholderMapper;
        this.placeholderSearchRepository = placeholderSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link PlaceholderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PlaceholderDTO> findByCriteria(PlaceholderCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Placeholder> specification = createSpecification(criteria);
        return placeholderRepository.findAll(specification, page).map(placeholderMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PlaceholderCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Placeholder> specification = createSpecification(criteria);
        return placeholderRepository.count(specification);
    }

    /**
     * Function to convert {@link PlaceholderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Placeholder> createSpecification(PlaceholderCriteria criteria) {
        Specification<Placeholder> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Placeholder_.id),
                buildStringSpecification(criteria.getDescription(), Placeholder_.description),
                buildStringSpecification(criteria.getToken(), Placeholder_.token),
                buildSpecification(criteria.getContainingPlaceholderId(), root ->
                    root.join(Placeholder_.containingPlaceholder, JoinType.LEFT).get(Placeholder_.id)
                ),
                buildSpecification(criteria.getDealerId(), root -> root.join(Placeholder_.dealers, JoinType.LEFT).get(Dealer_.id)),
                buildSpecification(criteria.getPlaceholderId(), root ->
                    root.join(Placeholder_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                ),
                buildSpecification(criteria.getSecurityClearanceId(), root ->
                    root.join(Placeholder_.securityClearances, JoinType.LEFT).get(SecurityClearance_.id)
                ),
                buildSpecification(criteria.getApplicationUserId(), root ->
                    root.join(Placeholder_.applicationUsers, JoinType.LEFT).get(ApplicationUser_.id)
                ),
                buildSpecification(criteria.getFiscalYearId(), root ->
                    root.join(Placeholder_.fiscalYears, JoinType.LEFT).get(FiscalYear_.id)
                ),
                buildSpecification(criteria.getFiscalQuarterId(), root ->
                    root.join(Placeholder_.fiscalQuarters, JoinType.LEFT).get(FiscalQuarter_.id)
                ),
                buildSpecification(criteria.getFiscalMonthId(), root ->
                    root.join(Placeholder_.fiscalMonths, JoinType.LEFT).get(FiscalMonth_.id)
                ),
                buildSpecification(criteria.getReportBatchId(), root ->
                    root.join(Placeholder_.reportBatches, JoinType.LEFT).get(ReportBatch_.id)
                ),
                buildSpecification(criteria.getMoneyMarketListId(), root ->
                    root.join(Placeholder_.moneyMarketLists, JoinType.LEFT).get(MoneyMarketList_.id)
                ),
                buildSpecification(criteria.getMoneyMarketUploadNotificationId(), root ->
                    root.join(Placeholder_.moneyMarketUploadNotifications, JoinType.LEFT).get(MoneyMarketUploadNotification_.id)
                )
            );
        }
        return specification;
    }
}
