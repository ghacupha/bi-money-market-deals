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
import io.github.bi.domain.ReportBatch;
import io.github.bi.repository.ReportBatchRepository;
import io.github.bi.repository.search.ReportBatchSearchRepository;
import io.github.bi.service.criteria.ReportBatchCriteria;
import io.github.bi.service.dto.ReportBatchDTO;
import io.github.bi.service.mapper.ReportBatchMapper;
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
 * Service for executing complex queries for {@link ReportBatch} entities in the database.
 * The main input is a {@link ReportBatchCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ReportBatchDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReportBatchQueryService extends QueryService<ReportBatch> {

    private static final Logger LOG = LoggerFactory.getLogger(ReportBatchQueryService.class);

    private final ReportBatchRepository reportBatchRepository;

    private final ReportBatchMapper reportBatchMapper;

    private final ReportBatchSearchRepository reportBatchSearchRepository;

    public ReportBatchQueryService(
        ReportBatchRepository reportBatchRepository,
        ReportBatchMapper reportBatchMapper,
        ReportBatchSearchRepository reportBatchSearchRepository
    ) {
        this.reportBatchRepository = reportBatchRepository;
        this.reportBatchMapper = reportBatchMapper;
        this.reportBatchSearchRepository = reportBatchSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link ReportBatchDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReportBatchDTO> findByCriteria(ReportBatchCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReportBatch> specification = createSpecification(criteria);
        return reportBatchRepository
            .fetchBagRelationships(reportBatchRepository.findAll(specification, page))
            .map(reportBatchMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReportBatchCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ReportBatch> specification = createSpecification(criteria);
        return reportBatchRepository.count(specification);
    }

    /**
     * Function to convert {@link ReportBatchCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReportBatch> createSpecification(ReportBatchCriteria criteria) {
        Specification<ReportBatch> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ReportBatch_.id),
                buildRangeSpecification(criteria.getReportDate(), ReportBatch_.reportDate),
                buildRangeSpecification(criteria.getUploadTimeStamp(), ReportBatch_.uploadTimeStamp),
                buildSpecification(criteria.getStatus(), ReportBatch_.status),
                buildSpecification(criteria.getActive(), ReportBatch_.active),
                buildStringSpecification(criteria.getDescription(), ReportBatch_.description),
                buildSpecification(criteria.getFileIdentifier(), ReportBatch_.fileIdentifier),
                buildSpecification(criteria.getProcessFlag(), ReportBatch_.processFlag),
                buildSpecification(criteria.getUploadedById(), root ->
                    root.join(ReportBatch_.uploadedBy, JoinType.LEFT).get(ApplicationUser_.id)
                ),
                buildSpecification(criteria.getPlaceholderId(), root ->
                    root.join(ReportBatch_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                ),
                buildSpecification(criteria.getMoneyMarketListId(), root ->
                    root.join(ReportBatch_.moneyMarketList, JoinType.LEFT).get(MoneyMarketList_.id)
                ),
                buildSpecification(criteria.getMoneyMarketUploadNotificationId(), root ->
                    root.join(ReportBatch_.moneyMarketUploadNotifications, JoinType.LEFT).get(MoneyMarketUploadNotification_.id)
                )
            );
        }
        return specification;
    }
}
