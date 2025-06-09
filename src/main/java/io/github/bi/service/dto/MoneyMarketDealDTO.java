package io.github.bi.service.dto;

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

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link io.github.bi.domain.MoneyMarketDeal} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MoneyMarketDealDTO implements Serializable {

    private Long id;

    @NotNull
    private String dealNumber;

    private String tradingBook;

    private String counterPartyName;

    @NotNull
    private LocalDate finalInterestAccrualDate;

    private String counterPartySideType;

    private String dateOfCollectionStatement;

    private String currencyCode;

    private BigDecimal principalAmount;

    private BigDecimal interestRate;

    private BigDecimal interestAccruedAmount;

    private BigDecimal totalInterestAtMaturity;

    private String counterpartyNationality;

    @NotNull
    private LocalDate endDate;

    private String treasuryLedger;

    private String dealSubtype;

    private BigDecimal shillingEquivalentPrincipal;

    private BigDecimal shillingEquivalentInterestAccrued;

    private BigDecimal shillingEquivalentPVFull;

    private String counterpartyDomicile;

    @NotNull
    private LocalDate settlementDate;

    private String transactionCollateral;

    private String institutionType;

    @NotNull
    private LocalDate maturityDate;

    private String institutionReportName;

    private String transactionType;

    @NotNull
    private LocalDate reportDate;

    @NotNull
    private Boolean active;

    @NotNull
    private MoneyMarketListDTO moneyMarketList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDealNumber() {
        return dealNumber;
    }

    public void setDealNumber(String dealNumber) {
        this.dealNumber = dealNumber;
    }

    public String getTradingBook() {
        return tradingBook;
    }

    public void setTradingBook(String tradingBook) {
        this.tradingBook = tradingBook;
    }

    public String getCounterPartyName() {
        return counterPartyName;
    }

    public void setCounterPartyName(String counterPartyName) {
        this.counterPartyName = counterPartyName;
    }

    public LocalDate getFinalInterestAccrualDate() {
        return finalInterestAccrualDate;
    }

    public void setFinalInterestAccrualDate(LocalDate finalInterestAccrualDate) {
        this.finalInterestAccrualDate = finalInterestAccrualDate;
    }

    public String getCounterPartySideType() {
        return counterPartySideType;
    }

    public void setCounterPartySideType(String counterPartySideType) {
        this.counterPartySideType = counterPartySideType;
    }

    public String getDateOfCollectionStatement() {
        return dateOfCollectionStatement;
    }

    public void setDateOfCollectionStatement(String dateOfCollectionStatement) {
        this.dateOfCollectionStatement = dateOfCollectionStatement;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(BigDecimal principalAmount) {
        this.principalAmount = principalAmount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getInterestAccruedAmount() {
        return interestAccruedAmount;
    }

    public void setInterestAccruedAmount(BigDecimal interestAccruedAmount) {
        this.interestAccruedAmount = interestAccruedAmount;
    }

    public BigDecimal getTotalInterestAtMaturity() {
        return totalInterestAtMaturity;
    }

    public void setTotalInterestAtMaturity(BigDecimal totalInterestAtMaturity) {
        this.totalInterestAtMaturity = totalInterestAtMaturity;
    }

    public String getCounterpartyNationality() {
        return counterpartyNationality;
    }

    public void setCounterpartyNationality(String counterpartyNationality) {
        this.counterpartyNationality = counterpartyNationality;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getTreasuryLedger() {
        return treasuryLedger;
    }

    public void setTreasuryLedger(String treasuryLedger) {
        this.treasuryLedger = treasuryLedger;
    }

    public String getDealSubtype() {
        return dealSubtype;
    }

    public void setDealSubtype(String dealSubtype) {
        this.dealSubtype = dealSubtype;
    }

    public BigDecimal getShillingEquivalentPrincipal() {
        return shillingEquivalentPrincipal;
    }

    public void setShillingEquivalentPrincipal(BigDecimal shillingEquivalentPrincipal) {
        this.shillingEquivalentPrincipal = shillingEquivalentPrincipal;
    }

    public BigDecimal getShillingEquivalentInterestAccrued() {
        return shillingEquivalentInterestAccrued;
    }

    public void setShillingEquivalentInterestAccrued(BigDecimal shillingEquivalentInterestAccrued) {
        this.shillingEquivalentInterestAccrued = shillingEquivalentInterestAccrued;
    }

    public BigDecimal getShillingEquivalentPVFull() {
        return shillingEquivalentPVFull;
    }

    public void setShillingEquivalentPVFull(BigDecimal shillingEquivalentPVFull) {
        this.shillingEquivalentPVFull = shillingEquivalentPVFull;
    }

    public String getCounterpartyDomicile() {
        return counterpartyDomicile;
    }

    public void setCounterpartyDomicile(String counterpartyDomicile) {
        this.counterpartyDomicile = counterpartyDomicile;
    }

    public LocalDate getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(LocalDate settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getTransactionCollateral() {
        return transactionCollateral;
    }

    public void setTransactionCollateral(String transactionCollateral) {
        this.transactionCollateral = transactionCollateral;
    }

    public String getInstitutionType() {
        return institutionType;
    }

    public void setInstitutionType(String institutionType) {
        this.institutionType = institutionType;
    }

    public LocalDate getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(LocalDate maturityDate) {
        this.maturityDate = maturityDate;
    }

    public String getInstitutionReportName() {
        return institutionReportName;
    }

    public void setInstitutionReportName(String institutionReportName) {
        this.institutionReportName = institutionReportName;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public MoneyMarketListDTO getMoneyMarketList() {
        return moneyMarketList;
    }

    public void setMoneyMarketList(MoneyMarketListDTO moneyMarketList) {
        this.moneyMarketList = moneyMarketList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MoneyMarketDealDTO)) {
            return false;
        }

        MoneyMarketDealDTO moneyMarketDealDTO = (MoneyMarketDealDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, moneyMarketDealDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoneyMarketDealDTO{" +
            "id=" + getId() +
            ", dealNumber='" + getDealNumber() + "'" +
            ", tradingBook='" + getTradingBook() + "'" +
            ", counterPartyName='" + getCounterPartyName() + "'" +
            ", finalInterestAccrualDate='" + getFinalInterestAccrualDate() + "'" +
            ", counterPartySideType='" + getCounterPartySideType() + "'" +
            ", dateOfCollectionStatement='" + getDateOfCollectionStatement() + "'" +
            ", currencyCode='" + getCurrencyCode() + "'" +
            ", principalAmount=" + getPrincipalAmount() +
            ", interestRate=" + getInterestRate() +
            ", interestAccruedAmount=" + getInterestAccruedAmount() +
            ", totalInterestAtMaturity=" + getTotalInterestAtMaturity() +
            ", counterpartyNationality='" + getCounterpartyNationality() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", treasuryLedger='" + getTreasuryLedger() + "'" +
            ", dealSubtype='" + getDealSubtype() + "'" +
            ", shillingEquivalentPrincipal=" + getShillingEquivalentPrincipal() +
            ", shillingEquivalentInterestAccrued=" + getShillingEquivalentInterestAccrued() +
            ", shillingEquivalentPVFull=" + getShillingEquivalentPVFull() +
            ", counterpartyDomicile='" + getCounterpartyDomicile() + "'" +
            ", settlementDate='" + getSettlementDate() + "'" +
            ", transactionCollateral='" + getTransactionCollateral() + "'" +
            ", institutionType='" + getInstitutionType() + "'" +
            ", maturityDate='" + getMaturityDate() + "'" +
            ", institutionReportName='" + getInstitutionReportName() + "'" +
            ", transactionType='" + getTransactionType() + "'" +
            ", reportDate='" + getReportDate() + "'" +
            ", active='" + getActive() + "'" +
            ", moneyMarketList=" + getMoneyMarketList() +
            "}";
    }
}
