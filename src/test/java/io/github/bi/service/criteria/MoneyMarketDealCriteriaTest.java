package io.github.bi.service.criteria;

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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MoneyMarketDealCriteriaTest {

    @Test
    void newMoneyMarketDealCriteriaHasAllFiltersNullTest() {
        var moneyMarketDealCriteria = new MoneyMarketDealCriteria();
        assertThat(moneyMarketDealCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void moneyMarketDealCriteriaFluentMethodsCreatesFiltersTest() {
        var moneyMarketDealCriteria = new MoneyMarketDealCriteria();

        setAllFilters(moneyMarketDealCriteria);

        assertThat(moneyMarketDealCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void moneyMarketDealCriteriaCopyCreatesNullFilterTest() {
        var moneyMarketDealCriteria = new MoneyMarketDealCriteria();
        var copy = moneyMarketDealCriteria.copy();

        assertThat(moneyMarketDealCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(moneyMarketDealCriteria)
        );
    }

    @Test
    void moneyMarketDealCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var moneyMarketDealCriteria = new MoneyMarketDealCriteria();
        setAllFilters(moneyMarketDealCriteria);

        var copy = moneyMarketDealCriteria.copy();

        assertThat(moneyMarketDealCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(moneyMarketDealCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var moneyMarketDealCriteria = new MoneyMarketDealCriteria();

        assertThat(moneyMarketDealCriteria).hasToString("MoneyMarketDealCriteria{}");
    }

    private static void setAllFilters(MoneyMarketDealCriteria moneyMarketDealCriteria) {
        moneyMarketDealCriteria.id();
        moneyMarketDealCriteria.dealNumber();
        moneyMarketDealCriteria.tradingBook();
        moneyMarketDealCriteria.counterPartyName();
        moneyMarketDealCriteria.finalInterestAccrualDate();
        moneyMarketDealCriteria.counterPartySideType();
        moneyMarketDealCriteria.dateOfCollectionStatement();
        moneyMarketDealCriteria.currencyCode();
        moneyMarketDealCriteria.principalAmount();
        moneyMarketDealCriteria.interestRate();
        moneyMarketDealCriteria.interestAccruedAmount();
        moneyMarketDealCriteria.totalInterestAtMaturity();
        moneyMarketDealCriteria.counterpartyNationality();
        moneyMarketDealCriteria.endDate();
        moneyMarketDealCriteria.treasuryLedger();
        moneyMarketDealCriteria.dealSubtype();
        moneyMarketDealCriteria.shillingEquivalentPrincipal();
        moneyMarketDealCriteria.shillingEquivalentInterestAccrued();
        moneyMarketDealCriteria.shillingEquivalentPVFull();
        moneyMarketDealCriteria.counterpartyDomicile();
        moneyMarketDealCriteria.settlementDate();
        moneyMarketDealCriteria.transactionCollateral();
        moneyMarketDealCriteria.institutionType();
        moneyMarketDealCriteria.maturityDate();
        moneyMarketDealCriteria.institutionReportName();
        moneyMarketDealCriteria.transactionType();
        moneyMarketDealCriteria.reportDate();
        moneyMarketDealCriteria.active();
        moneyMarketDealCriteria.moneyMarketListId();
        moneyMarketDealCriteria.distinct();
    }

    private static Condition<MoneyMarketDealCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDealNumber()) &&
                condition.apply(criteria.getTradingBook()) &&
                condition.apply(criteria.getCounterPartyName()) &&
                condition.apply(criteria.getFinalInterestAccrualDate()) &&
                condition.apply(criteria.getCounterPartySideType()) &&
                condition.apply(criteria.getDateOfCollectionStatement()) &&
                condition.apply(criteria.getCurrencyCode()) &&
                condition.apply(criteria.getPrincipalAmount()) &&
                condition.apply(criteria.getInterestRate()) &&
                condition.apply(criteria.getInterestAccruedAmount()) &&
                condition.apply(criteria.getTotalInterestAtMaturity()) &&
                condition.apply(criteria.getCounterpartyNationality()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getTreasuryLedger()) &&
                condition.apply(criteria.getDealSubtype()) &&
                condition.apply(criteria.getShillingEquivalentPrincipal()) &&
                condition.apply(criteria.getShillingEquivalentInterestAccrued()) &&
                condition.apply(criteria.getShillingEquivalentPVFull()) &&
                condition.apply(criteria.getCounterpartyDomicile()) &&
                condition.apply(criteria.getSettlementDate()) &&
                condition.apply(criteria.getTransactionCollateral()) &&
                condition.apply(criteria.getInstitutionType()) &&
                condition.apply(criteria.getMaturityDate()) &&
                condition.apply(criteria.getInstitutionReportName()) &&
                condition.apply(criteria.getTransactionType()) &&
                condition.apply(criteria.getReportDate()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getMoneyMarketListId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MoneyMarketDealCriteria> copyFiltersAre(
        MoneyMarketDealCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDealNumber(), copy.getDealNumber()) &&
                condition.apply(criteria.getTradingBook(), copy.getTradingBook()) &&
                condition.apply(criteria.getCounterPartyName(), copy.getCounterPartyName()) &&
                condition.apply(criteria.getFinalInterestAccrualDate(), copy.getFinalInterestAccrualDate()) &&
                condition.apply(criteria.getCounterPartySideType(), copy.getCounterPartySideType()) &&
                condition.apply(criteria.getDateOfCollectionStatement(), copy.getDateOfCollectionStatement()) &&
                condition.apply(criteria.getCurrencyCode(), copy.getCurrencyCode()) &&
                condition.apply(criteria.getPrincipalAmount(), copy.getPrincipalAmount()) &&
                condition.apply(criteria.getInterestRate(), copy.getInterestRate()) &&
                condition.apply(criteria.getInterestAccruedAmount(), copy.getInterestAccruedAmount()) &&
                condition.apply(criteria.getTotalInterestAtMaturity(), copy.getTotalInterestAtMaturity()) &&
                condition.apply(criteria.getCounterpartyNationality(), copy.getCounterpartyNationality()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getTreasuryLedger(), copy.getTreasuryLedger()) &&
                condition.apply(criteria.getDealSubtype(), copy.getDealSubtype()) &&
                condition.apply(criteria.getShillingEquivalentPrincipal(), copy.getShillingEquivalentPrincipal()) &&
                condition.apply(criteria.getShillingEquivalentInterestAccrued(), copy.getShillingEquivalentInterestAccrued()) &&
                condition.apply(criteria.getShillingEquivalentPVFull(), copy.getShillingEquivalentPVFull()) &&
                condition.apply(criteria.getCounterpartyDomicile(), copy.getCounterpartyDomicile()) &&
                condition.apply(criteria.getSettlementDate(), copy.getSettlementDate()) &&
                condition.apply(criteria.getTransactionCollateral(), copy.getTransactionCollateral()) &&
                condition.apply(criteria.getInstitutionType(), copy.getInstitutionType()) &&
                condition.apply(criteria.getMaturityDate(), copy.getMaturityDate()) &&
                condition.apply(criteria.getInstitutionReportName(), copy.getInstitutionReportName()) &&
                condition.apply(criteria.getTransactionType(), copy.getTransactionType()) &&
                condition.apply(criteria.getReportDate(), copy.getReportDate()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getMoneyMarketListId(), copy.getMoneyMarketListId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
