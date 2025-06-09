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
import io.github.bi.domain.MoneyMarketDeal;
import io.github.bi.repository.MoneyMarketDealRepository;
import io.github.bi.repository.search.MoneyMarketDealSearchRepository;
import io.github.bi.service.criteria.MoneyMarketDealCriteria;
import io.github.bi.service.dto.MoneyMarketDealDTO;
import io.github.bi.service.mapper.MoneyMarketDealMapper;
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
 * Service for executing complex queries for {@link MoneyMarketDeal} entities in the database.
 * The main input is a {@link MoneyMarketDealCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MoneyMarketDealDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MoneyMarketDealQueryService extends QueryService<MoneyMarketDeal> {

    private static final Logger LOG = LoggerFactory.getLogger(MoneyMarketDealQueryService.class);

    private final MoneyMarketDealRepository moneyMarketDealRepository;

    private final MoneyMarketDealMapper moneyMarketDealMapper;

    private final MoneyMarketDealSearchRepository moneyMarketDealSearchRepository;

    public MoneyMarketDealQueryService(
        MoneyMarketDealRepository moneyMarketDealRepository,
        MoneyMarketDealMapper moneyMarketDealMapper,
        MoneyMarketDealSearchRepository moneyMarketDealSearchRepository
    ) {
        this.moneyMarketDealRepository = moneyMarketDealRepository;
        this.moneyMarketDealMapper = moneyMarketDealMapper;
        this.moneyMarketDealSearchRepository = moneyMarketDealSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link MoneyMarketDealDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MoneyMarketDealDTO> findByCriteria(MoneyMarketDealCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MoneyMarketDeal> specification = createSpecification(criteria);
        return moneyMarketDealRepository.findAll(specification, page).map(moneyMarketDealMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MoneyMarketDealCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MoneyMarketDeal> specification = createSpecification(criteria);
        return moneyMarketDealRepository.count(specification);
    }

    /**
     * Function to convert {@link MoneyMarketDealCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MoneyMarketDeal> createSpecification(MoneyMarketDealCriteria criteria) {
        Specification<MoneyMarketDeal> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), MoneyMarketDeal_.id),
                buildStringSpecification(criteria.getDealNumber(), MoneyMarketDeal_.dealNumber),
                buildStringSpecification(criteria.getTradingBook(), MoneyMarketDeal_.tradingBook),
                buildStringSpecification(criteria.getCounterPartyName(), MoneyMarketDeal_.counterPartyName),
                buildRangeSpecification(criteria.getFinalInterestAccrualDate(), MoneyMarketDeal_.finalInterestAccrualDate),
                buildStringSpecification(criteria.getCounterPartySideType(), MoneyMarketDeal_.counterPartySideType),
                buildStringSpecification(criteria.getDateOfCollectionStatement(), MoneyMarketDeal_.dateOfCollectionStatement),
                buildStringSpecification(criteria.getCurrencyCode(), MoneyMarketDeal_.currencyCode),
                buildRangeSpecification(criteria.getPrincipalAmount(), MoneyMarketDeal_.principalAmount),
                buildRangeSpecification(criteria.getInterestRate(), MoneyMarketDeal_.interestRate),
                buildRangeSpecification(criteria.getInterestAccruedAmount(), MoneyMarketDeal_.interestAccruedAmount),
                buildRangeSpecification(criteria.getTotalInterestAtMaturity(), MoneyMarketDeal_.totalInterestAtMaturity),
                buildStringSpecification(criteria.getCounterpartyNationality(), MoneyMarketDeal_.counterpartyNationality),
                buildRangeSpecification(criteria.getEndDate(), MoneyMarketDeal_.endDate),
                buildStringSpecification(criteria.getTreasuryLedger(), MoneyMarketDeal_.treasuryLedger),
                buildStringSpecification(criteria.getDealSubtype(), MoneyMarketDeal_.dealSubtype),
                buildRangeSpecification(criteria.getShillingEquivalentPrincipal(), MoneyMarketDeal_.shillingEquivalentPrincipal),
                buildRangeSpecification(
                    criteria.getShillingEquivalentInterestAccrued(),
                    MoneyMarketDeal_.shillingEquivalentInterestAccrued
                ),
                buildRangeSpecification(criteria.getShillingEquivalentPVFull(), MoneyMarketDeal_.shillingEquivalentPVFull),
                buildStringSpecification(criteria.getCounterpartyDomicile(), MoneyMarketDeal_.counterpartyDomicile),
                buildRangeSpecification(criteria.getSettlementDate(), MoneyMarketDeal_.settlementDate),
                buildStringSpecification(criteria.getTransactionCollateral(), MoneyMarketDeal_.transactionCollateral),
                buildStringSpecification(criteria.getInstitutionType(), MoneyMarketDeal_.institutionType),
                buildRangeSpecification(criteria.getMaturityDate(), MoneyMarketDeal_.maturityDate),
                buildStringSpecification(criteria.getInstitutionReportName(), MoneyMarketDeal_.institutionReportName),
                buildStringSpecification(criteria.getTransactionType(), MoneyMarketDeal_.transactionType),
                buildRangeSpecification(criteria.getReportDate(), MoneyMarketDeal_.reportDate),
                buildSpecification(criteria.getActive(), MoneyMarketDeal_.active),
                buildSpecification(criteria.getMoneyMarketListId(), root ->
                    root.join(MoneyMarketDeal_.moneyMarketList, JoinType.LEFT).get(MoneyMarketList_.id)
                )
            );
        }
        return specification;
    }
}
