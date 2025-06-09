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
import io.github.bi.domain.MoneyMarketList;
import io.github.bi.repository.MoneyMarketListRepository;
import io.github.bi.repository.search.MoneyMarketListSearchRepository;
import io.github.bi.service.criteria.MoneyMarketListCriteria;
import io.github.bi.service.dto.MoneyMarketListDTO;
import io.github.bi.service.mapper.MoneyMarketListMapper;
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
 * Service for executing complex queries for {@link MoneyMarketList} entities in the database.
 * The main input is a {@link MoneyMarketListCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MoneyMarketListDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MoneyMarketListQueryService extends QueryService<MoneyMarketList> {

    private static final Logger LOG = LoggerFactory.getLogger(MoneyMarketListQueryService.class);

    private final MoneyMarketListRepository moneyMarketListRepository;

    private final MoneyMarketListMapper moneyMarketListMapper;

    private final MoneyMarketListSearchRepository moneyMarketListSearchRepository;

    public MoneyMarketListQueryService(
        MoneyMarketListRepository moneyMarketListRepository,
        MoneyMarketListMapper moneyMarketListMapper,
        MoneyMarketListSearchRepository moneyMarketListSearchRepository
    ) {
        this.moneyMarketListRepository = moneyMarketListRepository;
        this.moneyMarketListMapper = moneyMarketListMapper;
        this.moneyMarketListSearchRepository = moneyMarketListSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link MoneyMarketListDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MoneyMarketListDTO> findByCriteria(MoneyMarketListCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MoneyMarketList> specification = createSpecification(criteria);
        return moneyMarketListRepository
            .fetchBagRelationships(moneyMarketListRepository.findAll(specification, page))
            .map(moneyMarketListMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MoneyMarketListCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MoneyMarketList> specification = createSpecification(criteria);
        return moneyMarketListRepository.count(specification);
    }

    /**
     * Function to convert {@link MoneyMarketListCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MoneyMarketList> createSpecification(MoneyMarketListCriteria criteria) {
        Specification<MoneyMarketList> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), MoneyMarketList_.id),
                buildRangeSpecification(criteria.getReportDate(), MoneyMarketList_.reportDate),
                buildRangeSpecification(criteria.getUploadTimeStamp(), MoneyMarketList_.uploadTimeStamp),
                buildSpecification(criteria.getStatus(), MoneyMarketList_.status),
                buildStringSpecification(criteria.getDescription(), MoneyMarketList_.description),
                buildSpecification(criteria.getActive(), MoneyMarketList_.active),
                buildSpecification(criteria.getPlaceholderId(), root ->
                    root.join(MoneyMarketList_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                ),
                buildSpecification(criteria.getUploadedById(), root ->
                    root.join(MoneyMarketList_.uploadedBy, JoinType.LEFT).get(ApplicationUser_.id)
                ),
                buildSpecification(criteria.getReportBatchId(), root ->
                    root.join(MoneyMarketList_.reportBatch, JoinType.LEFT).get(ReportBatch_.id)
                ),
                buildSpecification(criteria.getMoneyMarketDealId(), root ->
                    root.join(MoneyMarketList_.moneyMarketDeals, JoinType.LEFT).get(MoneyMarketDeal_.id)
                ),
                buildSpecification(criteria.getMoneyMarketUploadNotificationId(), root ->
                    root.join(MoneyMarketList_.moneyMarketUploadNotifications, JoinType.LEFT).get(MoneyMarketUploadNotification_.id)
                )
            );
        }
        return specification;
    }
}
