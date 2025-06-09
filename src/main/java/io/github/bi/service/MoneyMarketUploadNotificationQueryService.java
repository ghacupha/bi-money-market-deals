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
import io.github.bi.domain.MoneyMarketUploadNotification;
import io.github.bi.repository.MoneyMarketUploadNotificationRepository;
import io.github.bi.repository.search.MoneyMarketUploadNotificationSearchRepository;
import io.github.bi.service.criteria.MoneyMarketUploadNotificationCriteria;
import io.github.bi.service.dto.MoneyMarketUploadNotificationDTO;
import io.github.bi.service.mapper.MoneyMarketUploadNotificationMapper;
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
 * Service for executing complex queries for {@link MoneyMarketUploadNotification} entities in the database.
 * The main input is a {@link MoneyMarketUploadNotificationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MoneyMarketUploadNotificationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MoneyMarketUploadNotificationQueryService extends QueryService<MoneyMarketUploadNotification> {

    private static final Logger LOG = LoggerFactory.getLogger(MoneyMarketUploadNotificationQueryService.class);

    private final MoneyMarketUploadNotificationRepository moneyMarketUploadNotificationRepository;

    private final MoneyMarketUploadNotificationMapper moneyMarketUploadNotificationMapper;

    private final MoneyMarketUploadNotificationSearchRepository moneyMarketUploadNotificationSearchRepository;

    public MoneyMarketUploadNotificationQueryService(
        MoneyMarketUploadNotificationRepository moneyMarketUploadNotificationRepository,
        MoneyMarketUploadNotificationMapper moneyMarketUploadNotificationMapper,
        MoneyMarketUploadNotificationSearchRepository moneyMarketUploadNotificationSearchRepository
    ) {
        this.moneyMarketUploadNotificationRepository = moneyMarketUploadNotificationRepository;
        this.moneyMarketUploadNotificationMapper = moneyMarketUploadNotificationMapper;
        this.moneyMarketUploadNotificationSearchRepository = moneyMarketUploadNotificationSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link MoneyMarketUploadNotificationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MoneyMarketUploadNotificationDTO> findByCriteria(MoneyMarketUploadNotificationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MoneyMarketUploadNotification> specification = createSpecification(criteria);
        return moneyMarketUploadNotificationRepository
            .fetchBagRelationships(moneyMarketUploadNotificationRepository.findAll(specification, page))
            .map(moneyMarketUploadNotificationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MoneyMarketUploadNotificationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MoneyMarketUploadNotification> specification = createSpecification(criteria);
        return moneyMarketUploadNotificationRepository.count(specification);
    }

    /**
     * Function to convert {@link MoneyMarketUploadNotificationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MoneyMarketUploadNotification> createSpecification(MoneyMarketUploadNotificationCriteria criteria) {
        Specification<MoneyMarketUploadNotification> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), MoneyMarketUploadNotification_.id),
                buildRangeSpecification(criteria.getRowNumber(), MoneyMarketUploadNotification_.rowNumber),
                buildSpecification(criteria.getReferenceNumber(), MoneyMarketUploadNotification_.referenceNumber),
                buildSpecification(criteria.getMoneyMarketListId(), root ->
                    root.join(MoneyMarketUploadNotification_.moneyMarketList, JoinType.LEFT).get(MoneyMarketList_.id)
                ),
                buildSpecification(criteria.getReportBatchId(), root ->
                    root.join(MoneyMarketUploadNotification_.reportBatch, JoinType.LEFT).get(ReportBatch_.id)
                ),
                buildSpecification(criteria.getPlaceholderId(), root ->
                    root.join(MoneyMarketUploadNotification_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                )
            );
        }
        return specification;
    }
}
