package io.github.bi.domain;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MoneyMarketDeal.
 */
@Entity
@Table(name = "money_market_deal")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "moneymarketdeal")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MoneyMarketDeal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "deal_number", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String dealNumber;

    @Column(name = "trading_book")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String tradingBook;

    @Column(name = "counter_party_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String counterPartyName;

    @NotNull
    @Column(name = "final_interest_accrual_date", nullable = false)
    private LocalDate finalInterestAccrualDate;

    @Column(name = "counter_party_side_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String counterPartySideType;

    @Column(name = "date_of_collection_statement")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String dateOfCollectionStatement;

    @Column(name = "currency_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String currencyCode;

    @Column(name = "principal_amount", precision = 21, scale = 2)
    private BigDecimal principalAmount;

    @Column(name = "interest_rate", precision = 21, scale = 2)
    private BigDecimal interestRate;

    @Column(name = "interest_accrued_amount", precision = 21, scale = 2)
    private BigDecimal interestAccruedAmount;

    @Column(name = "total_interest_at_maturity", precision = 21, scale = 2)
    private BigDecimal totalInterestAtMaturity;

    @Column(name = "counterparty_nationality")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String counterpartyNationality;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "treasury_ledger")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String treasuryLedger;

    @Column(name = "deal_subtype")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String dealSubtype;

    @Column(name = "shilling_equivalent_principal", precision = 21, scale = 2)
    private BigDecimal shillingEquivalentPrincipal;

    @Column(name = "shilling_equivalent_interest_accrued", precision = 21, scale = 2)
    private BigDecimal shillingEquivalentInterestAccrued;

    @Column(name = "shilling_equivalent_pv_full", precision = 21, scale = 2)
    private BigDecimal shillingEquivalentPVFull;

    @Column(name = "counterparty_domicile")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String counterpartyDomicile;

    @NotNull
    @Column(name = "settlement_date", nullable = false)
    private LocalDate settlementDate;

    @Column(name = "transaction_collateral")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String transactionCollateral;

    @Column(name = "institution_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String institutionType;

    @NotNull
    @Column(name = "maturity_date", nullable = false)
    private LocalDate maturityDate;

    @Column(name = "institution_report_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String institutionReportName;

    @Column(name = "transaction_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String transactionType;

    @NotNull
    @Column(name = "report_date", nullable = false)
    private LocalDate reportDate;

    @NotNull
    @Column(name = "active", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean active;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders", "moneyMarketDeals" }, allowSetters = true)
    private MoneyMarketList moneyMarketList;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MoneyMarketDeal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDealNumber() {
        return this.dealNumber;
    }

    public MoneyMarketDeal dealNumber(String dealNumber) {
        this.setDealNumber(dealNumber);
        return this;
    }

    public void setDealNumber(String dealNumber) {
        this.dealNumber = dealNumber;
    }

    public String getTradingBook() {
        return this.tradingBook;
    }

    public MoneyMarketDeal tradingBook(String tradingBook) {
        this.setTradingBook(tradingBook);
        return this;
    }

    public void setTradingBook(String tradingBook) {
        this.tradingBook = tradingBook;
    }

    public String getCounterPartyName() {
        return this.counterPartyName;
    }

    public MoneyMarketDeal counterPartyName(String counterPartyName) {
        this.setCounterPartyName(counterPartyName);
        return this;
    }

    public void setCounterPartyName(String counterPartyName) {
        this.counterPartyName = counterPartyName;
    }

    public LocalDate getFinalInterestAccrualDate() {
        return this.finalInterestAccrualDate;
    }

    public MoneyMarketDeal finalInterestAccrualDate(LocalDate finalInterestAccrualDate) {
        this.setFinalInterestAccrualDate(finalInterestAccrualDate);
        return this;
    }

    public void setFinalInterestAccrualDate(LocalDate finalInterestAccrualDate) {
        this.finalInterestAccrualDate = finalInterestAccrualDate;
    }

    public String getCounterPartySideType() {
        return this.counterPartySideType;
    }

    public MoneyMarketDeal counterPartySideType(String counterPartySideType) {
        this.setCounterPartySideType(counterPartySideType);
        return this;
    }

    public void setCounterPartySideType(String counterPartySideType) {
        this.counterPartySideType = counterPartySideType;
    }

    public String getDateOfCollectionStatement() {
        return this.dateOfCollectionStatement;
    }

    public MoneyMarketDeal dateOfCollectionStatement(String dateOfCollectionStatement) {
        this.setDateOfCollectionStatement(dateOfCollectionStatement);
        return this;
    }

    public void setDateOfCollectionStatement(String dateOfCollectionStatement) {
        this.dateOfCollectionStatement = dateOfCollectionStatement;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public MoneyMarketDeal currencyCode(String currencyCode) {
        this.setCurrencyCode(currencyCode);
        return this;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getPrincipalAmount() {
        return this.principalAmount;
    }

    public MoneyMarketDeal principalAmount(BigDecimal principalAmount) {
        this.setPrincipalAmount(principalAmount);
        return this;
    }

    public void setPrincipalAmount(BigDecimal principalAmount) {
        this.principalAmount = principalAmount;
    }

    public BigDecimal getInterestRate() {
        return this.interestRate;
    }

    public MoneyMarketDeal interestRate(BigDecimal interestRate) {
        this.setInterestRate(interestRate);
        return this;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getInterestAccruedAmount() {
        return this.interestAccruedAmount;
    }

    public MoneyMarketDeal interestAccruedAmount(BigDecimal interestAccruedAmount) {
        this.setInterestAccruedAmount(interestAccruedAmount);
        return this;
    }

    public void setInterestAccruedAmount(BigDecimal interestAccruedAmount) {
        this.interestAccruedAmount = interestAccruedAmount;
    }

    public BigDecimal getTotalInterestAtMaturity() {
        return this.totalInterestAtMaturity;
    }

    public MoneyMarketDeal totalInterestAtMaturity(BigDecimal totalInterestAtMaturity) {
        this.setTotalInterestAtMaturity(totalInterestAtMaturity);
        return this;
    }

    public void setTotalInterestAtMaturity(BigDecimal totalInterestAtMaturity) {
        this.totalInterestAtMaturity = totalInterestAtMaturity;
    }

    public String getCounterpartyNationality() {
        return this.counterpartyNationality;
    }

    public MoneyMarketDeal counterpartyNationality(String counterpartyNationality) {
        this.setCounterpartyNationality(counterpartyNationality);
        return this;
    }

    public void setCounterpartyNationality(String counterpartyNationality) {
        this.counterpartyNationality = counterpartyNationality;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public MoneyMarketDeal endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getTreasuryLedger() {
        return this.treasuryLedger;
    }

    public MoneyMarketDeal treasuryLedger(String treasuryLedger) {
        this.setTreasuryLedger(treasuryLedger);
        return this;
    }

    public void setTreasuryLedger(String treasuryLedger) {
        this.treasuryLedger = treasuryLedger;
    }

    public String getDealSubtype() {
        return this.dealSubtype;
    }

    public MoneyMarketDeal dealSubtype(String dealSubtype) {
        this.setDealSubtype(dealSubtype);
        return this;
    }

    public void setDealSubtype(String dealSubtype) {
        this.dealSubtype = dealSubtype;
    }

    public BigDecimal getShillingEquivalentPrincipal() {
        return this.shillingEquivalentPrincipal;
    }

    public MoneyMarketDeal shillingEquivalentPrincipal(BigDecimal shillingEquivalentPrincipal) {
        this.setShillingEquivalentPrincipal(shillingEquivalentPrincipal);
        return this;
    }

    public void setShillingEquivalentPrincipal(BigDecimal shillingEquivalentPrincipal) {
        this.shillingEquivalentPrincipal = shillingEquivalentPrincipal;
    }

    public BigDecimal getShillingEquivalentInterestAccrued() {
        return this.shillingEquivalentInterestAccrued;
    }

    public MoneyMarketDeal shillingEquivalentInterestAccrued(BigDecimal shillingEquivalentInterestAccrued) {
        this.setShillingEquivalentInterestAccrued(shillingEquivalentInterestAccrued);
        return this;
    }

    public void setShillingEquivalentInterestAccrued(BigDecimal shillingEquivalentInterestAccrued) {
        this.shillingEquivalentInterestAccrued = shillingEquivalentInterestAccrued;
    }

    public BigDecimal getShillingEquivalentPVFull() {
        return this.shillingEquivalentPVFull;
    }

    public MoneyMarketDeal shillingEquivalentPVFull(BigDecimal shillingEquivalentPVFull) {
        this.setShillingEquivalentPVFull(shillingEquivalentPVFull);
        return this;
    }

    public void setShillingEquivalentPVFull(BigDecimal shillingEquivalentPVFull) {
        this.shillingEquivalentPVFull = shillingEquivalentPVFull;
    }

    public String getCounterpartyDomicile() {
        return this.counterpartyDomicile;
    }

    public MoneyMarketDeal counterpartyDomicile(String counterpartyDomicile) {
        this.setCounterpartyDomicile(counterpartyDomicile);
        return this;
    }

    public void setCounterpartyDomicile(String counterpartyDomicile) {
        this.counterpartyDomicile = counterpartyDomicile;
    }

    public LocalDate getSettlementDate() {
        return this.settlementDate;
    }

    public MoneyMarketDeal settlementDate(LocalDate settlementDate) {
        this.setSettlementDate(settlementDate);
        return this;
    }

    public void setSettlementDate(LocalDate settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getTransactionCollateral() {
        return this.transactionCollateral;
    }

    public MoneyMarketDeal transactionCollateral(String transactionCollateral) {
        this.setTransactionCollateral(transactionCollateral);
        return this;
    }

    public void setTransactionCollateral(String transactionCollateral) {
        this.transactionCollateral = transactionCollateral;
    }

    public String getInstitutionType() {
        return this.institutionType;
    }

    public MoneyMarketDeal institutionType(String institutionType) {
        this.setInstitutionType(institutionType);
        return this;
    }

    public void setInstitutionType(String institutionType) {
        this.institutionType = institutionType;
    }

    public LocalDate getMaturityDate() {
        return this.maturityDate;
    }

    public MoneyMarketDeal maturityDate(LocalDate maturityDate) {
        this.setMaturityDate(maturityDate);
        return this;
    }

    public void setMaturityDate(LocalDate maturityDate) {
        this.maturityDate = maturityDate;
    }

    public String getInstitutionReportName() {
        return this.institutionReportName;
    }

    public MoneyMarketDeal institutionReportName(String institutionReportName) {
        this.setInstitutionReportName(institutionReportName);
        return this;
    }

    public void setInstitutionReportName(String institutionReportName) {
        this.institutionReportName = institutionReportName;
    }

    public String getTransactionType() {
        return this.transactionType;
    }

    public MoneyMarketDeal transactionType(String transactionType) {
        this.setTransactionType(transactionType);
        return this;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDate getReportDate() {
        return this.reportDate;
    }

    public MoneyMarketDeal reportDate(LocalDate reportDate) {
        this.setReportDate(reportDate);
        return this;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public Boolean getActive() {
        return this.active;
    }

    public MoneyMarketDeal active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public MoneyMarketList getMoneyMarketList() {
        return this.moneyMarketList;
    }

    public void setMoneyMarketList(MoneyMarketList moneyMarketList) {
        this.moneyMarketList = moneyMarketList;
    }

    public MoneyMarketDeal moneyMarketList(MoneyMarketList moneyMarketList) {
        this.setMoneyMarketList(moneyMarketList);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MoneyMarketDeal)) {
            return false;
        }
        return getId() != null && getId().equals(((MoneyMarketDeal) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoneyMarketDeal{" +
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
            "}";
    }
}
