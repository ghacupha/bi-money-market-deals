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

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link io.github.bi.domain.MoneyMarketDeal} entity. This class is used
 * in {@link io.github.bi.web.rest.MoneyMarketDealResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /money-market-deals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MoneyMarketDealCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter dealNumber;

    private StringFilter tradingBook;

    private StringFilter counterPartyName;

    private LocalDateFilter finalInterestAccrualDate;

    private StringFilter counterPartySideType;

    private StringFilter dateOfCollectionStatement;

    private StringFilter currencyCode;

    private BigDecimalFilter principalAmount;

    private BigDecimalFilter interestRate;

    private BigDecimalFilter interestAccruedAmount;

    private BigDecimalFilter totalInterestAtMaturity;

    private StringFilter counterpartyNationality;

    private LocalDateFilter endDate;

    private StringFilter treasuryLedger;

    private StringFilter dealSubtype;

    private BigDecimalFilter shillingEquivalentPrincipal;

    private BigDecimalFilter shillingEquivalentInterestAccrued;

    private BigDecimalFilter shillingEquivalentPVFull;

    private StringFilter counterpartyDomicile;

    private LocalDateFilter settlementDate;

    private StringFilter transactionCollateral;

    private StringFilter institutionType;

    private LocalDateFilter maturityDate;

    private StringFilter institutionReportName;

    private StringFilter transactionType;

    private LocalDateFilter reportDate;

    private BooleanFilter active;

    private LongFilter moneyMarketListId;

    private Boolean distinct;

    public MoneyMarketDealCriteria() {}

    public MoneyMarketDealCriteria(MoneyMarketDealCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.dealNumber = other.optionalDealNumber().map(StringFilter::copy).orElse(null);
        this.tradingBook = other.optionalTradingBook().map(StringFilter::copy).orElse(null);
        this.counterPartyName = other.optionalCounterPartyName().map(StringFilter::copy).orElse(null);
        this.finalInterestAccrualDate = other.optionalFinalInterestAccrualDate().map(LocalDateFilter::copy).orElse(null);
        this.counterPartySideType = other.optionalCounterPartySideType().map(StringFilter::copy).orElse(null);
        this.dateOfCollectionStatement = other.optionalDateOfCollectionStatement().map(StringFilter::copy).orElse(null);
        this.currencyCode = other.optionalCurrencyCode().map(StringFilter::copy).orElse(null);
        this.principalAmount = other.optionalPrincipalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.interestRate = other.optionalInterestRate().map(BigDecimalFilter::copy).orElse(null);
        this.interestAccruedAmount = other.optionalInterestAccruedAmount().map(BigDecimalFilter::copy).orElse(null);
        this.totalInterestAtMaturity = other.optionalTotalInterestAtMaturity().map(BigDecimalFilter::copy).orElse(null);
        this.counterpartyNationality = other.optionalCounterpartyNationality().map(StringFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(LocalDateFilter::copy).orElse(null);
        this.treasuryLedger = other.optionalTreasuryLedger().map(StringFilter::copy).orElse(null);
        this.dealSubtype = other.optionalDealSubtype().map(StringFilter::copy).orElse(null);
        this.shillingEquivalentPrincipal = other.optionalShillingEquivalentPrincipal().map(BigDecimalFilter::copy).orElse(null);
        this.shillingEquivalentInterestAccrued = other.optionalShillingEquivalentInterestAccrued().map(BigDecimalFilter::copy).orElse(null);
        this.shillingEquivalentPVFull = other.optionalShillingEquivalentPVFull().map(BigDecimalFilter::copy).orElse(null);
        this.counterpartyDomicile = other.optionalCounterpartyDomicile().map(StringFilter::copy).orElse(null);
        this.settlementDate = other.optionalSettlementDate().map(LocalDateFilter::copy).orElse(null);
        this.transactionCollateral = other.optionalTransactionCollateral().map(StringFilter::copy).orElse(null);
        this.institutionType = other.optionalInstitutionType().map(StringFilter::copy).orElse(null);
        this.maturityDate = other.optionalMaturityDate().map(LocalDateFilter::copy).orElse(null);
        this.institutionReportName = other.optionalInstitutionReportName().map(StringFilter::copy).orElse(null);
        this.transactionType = other.optionalTransactionType().map(StringFilter::copy).orElse(null);
        this.reportDate = other.optionalReportDate().map(LocalDateFilter::copy).orElse(null);
        this.active = other.optionalActive().map(BooleanFilter::copy).orElse(null);
        this.moneyMarketListId = other.optionalMoneyMarketListId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MoneyMarketDealCriteria copy() {
        return new MoneyMarketDealCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDealNumber() {
        return dealNumber;
    }

    public Optional<StringFilter> optionalDealNumber() {
        return Optional.ofNullable(dealNumber);
    }

    public StringFilter dealNumber() {
        if (dealNumber == null) {
            setDealNumber(new StringFilter());
        }
        return dealNumber;
    }

    public void setDealNumber(StringFilter dealNumber) {
        this.dealNumber = dealNumber;
    }

    public StringFilter getTradingBook() {
        return tradingBook;
    }

    public Optional<StringFilter> optionalTradingBook() {
        return Optional.ofNullable(tradingBook);
    }

    public StringFilter tradingBook() {
        if (tradingBook == null) {
            setTradingBook(new StringFilter());
        }
        return tradingBook;
    }

    public void setTradingBook(StringFilter tradingBook) {
        this.tradingBook = tradingBook;
    }

    public StringFilter getCounterPartyName() {
        return counterPartyName;
    }

    public Optional<StringFilter> optionalCounterPartyName() {
        return Optional.ofNullable(counterPartyName);
    }

    public StringFilter counterPartyName() {
        if (counterPartyName == null) {
            setCounterPartyName(new StringFilter());
        }
        return counterPartyName;
    }

    public void setCounterPartyName(StringFilter counterPartyName) {
        this.counterPartyName = counterPartyName;
    }

    public LocalDateFilter getFinalInterestAccrualDate() {
        return finalInterestAccrualDate;
    }

    public Optional<LocalDateFilter> optionalFinalInterestAccrualDate() {
        return Optional.ofNullable(finalInterestAccrualDate);
    }

    public LocalDateFilter finalInterestAccrualDate() {
        if (finalInterestAccrualDate == null) {
            setFinalInterestAccrualDate(new LocalDateFilter());
        }
        return finalInterestAccrualDate;
    }

    public void setFinalInterestAccrualDate(LocalDateFilter finalInterestAccrualDate) {
        this.finalInterestAccrualDate = finalInterestAccrualDate;
    }

    public StringFilter getCounterPartySideType() {
        return counterPartySideType;
    }

    public Optional<StringFilter> optionalCounterPartySideType() {
        return Optional.ofNullable(counterPartySideType);
    }

    public StringFilter counterPartySideType() {
        if (counterPartySideType == null) {
            setCounterPartySideType(new StringFilter());
        }
        return counterPartySideType;
    }

    public void setCounterPartySideType(StringFilter counterPartySideType) {
        this.counterPartySideType = counterPartySideType;
    }

    public StringFilter getDateOfCollectionStatement() {
        return dateOfCollectionStatement;
    }

    public Optional<StringFilter> optionalDateOfCollectionStatement() {
        return Optional.ofNullable(dateOfCollectionStatement);
    }

    public StringFilter dateOfCollectionStatement() {
        if (dateOfCollectionStatement == null) {
            setDateOfCollectionStatement(new StringFilter());
        }
        return dateOfCollectionStatement;
    }

    public void setDateOfCollectionStatement(StringFilter dateOfCollectionStatement) {
        this.dateOfCollectionStatement = dateOfCollectionStatement;
    }

    public StringFilter getCurrencyCode() {
        return currencyCode;
    }

    public Optional<StringFilter> optionalCurrencyCode() {
        return Optional.ofNullable(currencyCode);
    }

    public StringFilter currencyCode() {
        if (currencyCode == null) {
            setCurrencyCode(new StringFilter());
        }
        return currencyCode;
    }

    public void setCurrencyCode(StringFilter currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimalFilter getPrincipalAmount() {
        return principalAmount;
    }

    public Optional<BigDecimalFilter> optionalPrincipalAmount() {
        return Optional.ofNullable(principalAmount);
    }

    public BigDecimalFilter principalAmount() {
        if (principalAmount == null) {
            setPrincipalAmount(new BigDecimalFilter());
        }
        return principalAmount;
    }

    public void setPrincipalAmount(BigDecimalFilter principalAmount) {
        this.principalAmount = principalAmount;
    }

    public BigDecimalFilter getInterestRate() {
        return interestRate;
    }

    public Optional<BigDecimalFilter> optionalInterestRate() {
        return Optional.ofNullable(interestRate);
    }

    public BigDecimalFilter interestRate() {
        if (interestRate == null) {
            setInterestRate(new BigDecimalFilter());
        }
        return interestRate;
    }

    public void setInterestRate(BigDecimalFilter interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimalFilter getInterestAccruedAmount() {
        return interestAccruedAmount;
    }

    public Optional<BigDecimalFilter> optionalInterestAccruedAmount() {
        return Optional.ofNullable(interestAccruedAmount);
    }

    public BigDecimalFilter interestAccruedAmount() {
        if (interestAccruedAmount == null) {
            setInterestAccruedAmount(new BigDecimalFilter());
        }
        return interestAccruedAmount;
    }

    public void setInterestAccruedAmount(BigDecimalFilter interestAccruedAmount) {
        this.interestAccruedAmount = interestAccruedAmount;
    }

    public BigDecimalFilter getTotalInterestAtMaturity() {
        return totalInterestAtMaturity;
    }

    public Optional<BigDecimalFilter> optionalTotalInterestAtMaturity() {
        return Optional.ofNullable(totalInterestAtMaturity);
    }

    public BigDecimalFilter totalInterestAtMaturity() {
        if (totalInterestAtMaturity == null) {
            setTotalInterestAtMaturity(new BigDecimalFilter());
        }
        return totalInterestAtMaturity;
    }

    public void setTotalInterestAtMaturity(BigDecimalFilter totalInterestAtMaturity) {
        this.totalInterestAtMaturity = totalInterestAtMaturity;
    }

    public StringFilter getCounterpartyNationality() {
        return counterpartyNationality;
    }

    public Optional<StringFilter> optionalCounterpartyNationality() {
        return Optional.ofNullable(counterpartyNationality);
    }

    public StringFilter counterpartyNationality() {
        if (counterpartyNationality == null) {
            setCounterpartyNationality(new StringFilter());
        }
        return counterpartyNationality;
    }

    public void setCounterpartyNationality(StringFilter counterpartyNationality) {
        this.counterpartyNationality = counterpartyNationality;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public Optional<LocalDateFilter> optionalEndDate() {
        return Optional.ofNullable(endDate);
    }

    public LocalDateFilter endDate() {
        if (endDate == null) {
            setEndDate(new LocalDateFilter());
        }
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public StringFilter getTreasuryLedger() {
        return treasuryLedger;
    }

    public Optional<StringFilter> optionalTreasuryLedger() {
        return Optional.ofNullable(treasuryLedger);
    }

    public StringFilter treasuryLedger() {
        if (treasuryLedger == null) {
            setTreasuryLedger(new StringFilter());
        }
        return treasuryLedger;
    }

    public void setTreasuryLedger(StringFilter treasuryLedger) {
        this.treasuryLedger = treasuryLedger;
    }

    public StringFilter getDealSubtype() {
        return dealSubtype;
    }

    public Optional<StringFilter> optionalDealSubtype() {
        return Optional.ofNullable(dealSubtype);
    }

    public StringFilter dealSubtype() {
        if (dealSubtype == null) {
            setDealSubtype(new StringFilter());
        }
        return dealSubtype;
    }

    public void setDealSubtype(StringFilter dealSubtype) {
        this.dealSubtype = dealSubtype;
    }

    public BigDecimalFilter getShillingEquivalentPrincipal() {
        return shillingEquivalentPrincipal;
    }

    public Optional<BigDecimalFilter> optionalShillingEquivalentPrincipal() {
        return Optional.ofNullable(shillingEquivalentPrincipal);
    }

    public BigDecimalFilter shillingEquivalentPrincipal() {
        if (shillingEquivalentPrincipal == null) {
            setShillingEquivalentPrincipal(new BigDecimalFilter());
        }
        return shillingEquivalentPrincipal;
    }

    public void setShillingEquivalentPrincipal(BigDecimalFilter shillingEquivalentPrincipal) {
        this.shillingEquivalentPrincipal = shillingEquivalentPrincipal;
    }

    public BigDecimalFilter getShillingEquivalentInterestAccrued() {
        return shillingEquivalentInterestAccrued;
    }

    public Optional<BigDecimalFilter> optionalShillingEquivalentInterestAccrued() {
        return Optional.ofNullable(shillingEquivalentInterestAccrued);
    }

    public BigDecimalFilter shillingEquivalentInterestAccrued() {
        if (shillingEquivalentInterestAccrued == null) {
            setShillingEquivalentInterestAccrued(new BigDecimalFilter());
        }
        return shillingEquivalentInterestAccrued;
    }

    public void setShillingEquivalentInterestAccrued(BigDecimalFilter shillingEquivalentInterestAccrued) {
        this.shillingEquivalentInterestAccrued = shillingEquivalentInterestAccrued;
    }

    public BigDecimalFilter getShillingEquivalentPVFull() {
        return shillingEquivalentPVFull;
    }

    public Optional<BigDecimalFilter> optionalShillingEquivalentPVFull() {
        return Optional.ofNullable(shillingEquivalentPVFull);
    }

    public BigDecimalFilter shillingEquivalentPVFull() {
        if (shillingEquivalentPVFull == null) {
            setShillingEquivalentPVFull(new BigDecimalFilter());
        }
        return shillingEquivalentPVFull;
    }

    public void setShillingEquivalentPVFull(BigDecimalFilter shillingEquivalentPVFull) {
        this.shillingEquivalentPVFull = shillingEquivalentPVFull;
    }

    public StringFilter getCounterpartyDomicile() {
        return counterpartyDomicile;
    }

    public Optional<StringFilter> optionalCounterpartyDomicile() {
        return Optional.ofNullable(counterpartyDomicile);
    }

    public StringFilter counterpartyDomicile() {
        if (counterpartyDomicile == null) {
            setCounterpartyDomicile(new StringFilter());
        }
        return counterpartyDomicile;
    }

    public void setCounterpartyDomicile(StringFilter counterpartyDomicile) {
        this.counterpartyDomicile = counterpartyDomicile;
    }

    public LocalDateFilter getSettlementDate() {
        return settlementDate;
    }

    public Optional<LocalDateFilter> optionalSettlementDate() {
        return Optional.ofNullable(settlementDate);
    }

    public LocalDateFilter settlementDate() {
        if (settlementDate == null) {
            setSettlementDate(new LocalDateFilter());
        }
        return settlementDate;
    }

    public void setSettlementDate(LocalDateFilter settlementDate) {
        this.settlementDate = settlementDate;
    }

    public StringFilter getTransactionCollateral() {
        return transactionCollateral;
    }

    public Optional<StringFilter> optionalTransactionCollateral() {
        return Optional.ofNullable(transactionCollateral);
    }

    public StringFilter transactionCollateral() {
        if (transactionCollateral == null) {
            setTransactionCollateral(new StringFilter());
        }
        return transactionCollateral;
    }

    public void setTransactionCollateral(StringFilter transactionCollateral) {
        this.transactionCollateral = transactionCollateral;
    }

    public StringFilter getInstitutionType() {
        return institutionType;
    }

    public Optional<StringFilter> optionalInstitutionType() {
        return Optional.ofNullable(institutionType);
    }

    public StringFilter institutionType() {
        if (institutionType == null) {
            setInstitutionType(new StringFilter());
        }
        return institutionType;
    }

    public void setInstitutionType(StringFilter institutionType) {
        this.institutionType = institutionType;
    }

    public LocalDateFilter getMaturityDate() {
        return maturityDate;
    }

    public Optional<LocalDateFilter> optionalMaturityDate() {
        return Optional.ofNullable(maturityDate);
    }

    public LocalDateFilter maturityDate() {
        if (maturityDate == null) {
            setMaturityDate(new LocalDateFilter());
        }
        return maturityDate;
    }

    public void setMaturityDate(LocalDateFilter maturityDate) {
        this.maturityDate = maturityDate;
    }

    public StringFilter getInstitutionReportName() {
        return institutionReportName;
    }

    public Optional<StringFilter> optionalInstitutionReportName() {
        return Optional.ofNullable(institutionReportName);
    }

    public StringFilter institutionReportName() {
        if (institutionReportName == null) {
            setInstitutionReportName(new StringFilter());
        }
        return institutionReportName;
    }

    public void setInstitutionReportName(StringFilter institutionReportName) {
        this.institutionReportName = institutionReportName;
    }

    public StringFilter getTransactionType() {
        return transactionType;
    }

    public Optional<StringFilter> optionalTransactionType() {
        return Optional.ofNullable(transactionType);
    }

    public StringFilter transactionType() {
        if (transactionType == null) {
            setTransactionType(new StringFilter());
        }
        return transactionType;
    }

    public void setTransactionType(StringFilter transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDateFilter getReportDate() {
        return reportDate;
    }

    public Optional<LocalDateFilter> optionalReportDate() {
        return Optional.ofNullable(reportDate);
    }

    public LocalDateFilter reportDate() {
        if (reportDate == null) {
            setReportDate(new LocalDateFilter());
        }
        return reportDate;
    }

    public void setReportDate(LocalDateFilter reportDate) {
        this.reportDate = reportDate;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public Optional<BooleanFilter> optionalActive() {
        return Optional.ofNullable(active);
    }

    public BooleanFilter active() {
        if (active == null) {
            setActive(new BooleanFilter());
        }
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public LongFilter getMoneyMarketListId() {
        return moneyMarketListId;
    }

    public Optional<LongFilter> optionalMoneyMarketListId() {
        return Optional.ofNullable(moneyMarketListId);
    }

    public LongFilter moneyMarketListId() {
        if (moneyMarketListId == null) {
            setMoneyMarketListId(new LongFilter());
        }
        return moneyMarketListId;
    }

    public void setMoneyMarketListId(LongFilter moneyMarketListId) {
        this.moneyMarketListId = moneyMarketListId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MoneyMarketDealCriteria that = (MoneyMarketDealCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dealNumber, that.dealNumber) &&
            Objects.equals(tradingBook, that.tradingBook) &&
            Objects.equals(counterPartyName, that.counterPartyName) &&
            Objects.equals(finalInterestAccrualDate, that.finalInterestAccrualDate) &&
            Objects.equals(counterPartySideType, that.counterPartySideType) &&
            Objects.equals(dateOfCollectionStatement, that.dateOfCollectionStatement) &&
            Objects.equals(currencyCode, that.currencyCode) &&
            Objects.equals(principalAmount, that.principalAmount) &&
            Objects.equals(interestRate, that.interestRate) &&
            Objects.equals(interestAccruedAmount, that.interestAccruedAmount) &&
            Objects.equals(totalInterestAtMaturity, that.totalInterestAtMaturity) &&
            Objects.equals(counterpartyNationality, that.counterpartyNationality) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(treasuryLedger, that.treasuryLedger) &&
            Objects.equals(dealSubtype, that.dealSubtype) &&
            Objects.equals(shillingEquivalentPrincipal, that.shillingEquivalentPrincipal) &&
            Objects.equals(shillingEquivalentInterestAccrued, that.shillingEquivalentInterestAccrued) &&
            Objects.equals(shillingEquivalentPVFull, that.shillingEquivalentPVFull) &&
            Objects.equals(counterpartyDomicile, that.counterpartyDomicile) &&
            Objects.equals(settlementDate, that.settlementDate) &&
            Objects.equals(transactionCollateral, that.transactionCollateral) &&
            Objects.equals(institutionType, that.institutionType) &&
            Objects.equals(maturityDate, that.maturityDate) &&
            Objects.equals(institutionReportName, that.institutionReportName) &&
            Objects.equals(transactionType, that.transactionType) &&
            Objects.equals(reportDate, that.reportDate) &&
            Objects.equals(active, that.active) &&
            Objects.equals(moneyMarketListId, that.moneyMarketListId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            dealNumber,
            tradingBook,
            counterPartyName,
            finalInterestAccrualDate,
            counterPartySideType,
            dateOfCollectionStatement,
            currencyCode,
            principalAmount,
            interestRate,
            interestAccruedAmount,
            totalInterestAtMaturity,
            counterpartyNationality,
            endDate,
            treasuryLedger,
            dealSubtype,
            shillingEquivalentPrincipal,
            shillingEquivalentInterestAccrued,
            shillingEquivalentPVFull,
            counterpartyDomicile,
            settlementDate,
            transactionCollateral,
            institutionType,
            maturityDate,
            institutionReportName,
            transactionType,
            reportDate,
            active,
            moneyMarketListId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoneyMarketDealCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDealNumber().map(f -> "dealNumber=" + f + ", ").orElse("") +
            optionalTradingBook().map(f -> "tradingBook=" + f + ", ").orElse("") +
            optionalCounterPartyName().map(f -> "counterPartyName=" + f + ", ").orElse("") +
            optionalFinalInterestAccrualDate().map(f -> "finalInterestAccrualDate=" + f + ", ").orElse("") +
            optionalCounterPartySideType().map(f -> "counterPartySideType=" + f + ", ").orElse("") +
            optionalDateOfCollectionStatement().map(f -> "dateOfCollectionStatement=" + f + ", ").orElse("") +
            optionalCurrencyCode().map(f -> "currencyCode=" + f + ", ").orElse("") +
            optionalPrincipalAmount().map(f -> "principalAmount=" + f + ", ").orElse("") +
            optionalInterestRate().map(f -> "interestRate=" + f + ", ").orElse("") +
            optionalInterestAccruedAmount().map(f -> "interestAccruedAmount=" + f + ", ").orElse("") +
            optionalTotalInterestAtMaturity().map(f -> "totalInterestAtMaturity=" + f + ", ").orElse("") +
            optionalCounterpartyNationality().map(f -> "counterpartyNationality=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalTreasuryLedger().map(f -> "treasuryLedger=" + f + ", ").orElse("") +
            optionalDealSubtype().map(f -> "dealSubtype=" + f + ", ").orElse("") +
            optionalShillingEquivalentPrincipal().map(f -> "shillingEquivalentPrincipal=" + f + ", ").orElse("") +
            optionalShillingEquivalentInterestAccrued().map(f -> "shillingEquivalentInterestAccrued=" + f + ", ").orElse("") +
            optionalShillingEquivalentPVFull().map(f -> "shillingEquivalentPVFull=" + f + ", ").orElse("") +
            optionalCounterpartyDomicile().map(f -> "counterpartyDomicile=" + f + ", ").orElse("") +
            optionalSettlementDate().map(f -> "settlementDate=" + f + ", ").orElse("") +
            optionalTransactionCollateral().map(f -> "transactionCollateral=" + f + ", ").orElse("") +
            optionalInstitutionType().map(f -> "institutionType=" + f + ", ").orElse("") +
            optionalMaturityDate().map(f -> "maturityDate=" + f + ", ").orElse("") +
            optionalInstitutionReportName().map(f -> "institutionReportName=" + f + ", ").orElse("") +
            optionalTransactionType().map(f -> "transactionType=" + f + ", ").orElse("") +
            optionalReportDate().map(f -> "reportDate=" + f + ", ").orElse("") +
            optionalActive().map(f -> "active=" + f + ", ").orElse("") +
            optionalMoneyMarketListId().map(f -> "moneyMarketListId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
