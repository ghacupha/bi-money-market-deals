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

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MoneyMarketDealDailySummary.
 */
@Entity
@Table(name = "money_market_deal_daily_summary")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "moneymarketdealdailysummary")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MoneyMarketDealDailySummary implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "report_date")
    private LocalDate reportDate;

    @Column(name = "ledger")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String ledger;

    @Column(name = "number_of_deals")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer numberOfDeals;

    @Column(name = "total_principal", precision = 21, scale = 2)
    private BigDecimal totalPrincipal;

    @Column(name = "interest_accrued", precision = 21, scale = 2)
    private BigDecimal interestAccrued;

    @Column(name = "total_pv_full", precision = 21, scale = 2)
    private BigDecimal totalPVFull;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MoneyMarketDealDailySummary id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReportDate() {
        return this.reportDate;
    }

    public MoneyMarketDealDailySummary reportDate(LocalDate reportDate) {
        this.setReportDate(reportDate);
        return this;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getLedger() {
        return this.ledger;
    }

    public MoneyMarketDealDailySummary ledger(String ledger) {
        this.setLedger(ledger);
        return this;
    }

    public void setLedger(String ledger) {
        this.ledger = ledger;
    }

    public Integer getNumberOfDeals() {
        return this.numberOfDeals;
    }

    public MoneyMarketDealDailySummary numberOfDeals(Integer numberOfDeals) {
        this.setNumberOfDeals(numberOfDeals);
        return this;
    }

    public void setNumberOfDeals(Integer numberOfDeals) {
        this.numberOfDeals = numberOfDeals;
    }

    public BigDecimal getTotalPrincipal() {
        return this.totalPrincipal;
    }

    public MoneyMarketDealDailySummary totalPrincipal(BigDecimal totalPrincipal) {
        this.setTotalPrincipal(totalPrincipal);
        return this;
    }

    public void setTotalPrincipal(BigDecimal totalPrincipal) {
        this.totalPrincipal = totalPrincipal;
    }

    public BigDecimal getInterestAccrued() {
        return this.interestAccrued;
    }

    public MoneyMarketDealDailySummary interestAccrued(BigDecimal interestAccrued) {
        this.setInterestAccrued(interestAccrued);
        return this;
    }

    public void setInterestAccrued(BigDecimal interestAccrued) {
        this.interestAccrued = interestAccrued;
    }

    public BigDecimal getTotalPVFull() {
        return this.totalPVFull;
    }

    public MoneyMarketDealDailySummary totalPVFull(BigDecimal totalPVFull) {
        this.setTotalPVFull(totalPVFull);
        return this;
    }

    public void setTotalPVFull(BigDecimal totalPVFull) {
        this.totalPVFull = totalPVFull;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MoneyMarketDealDailySummary)) {
            return false;
        }
        return getId() != null && getId().equals(((MoneyMarketDealDailySummary) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoneyMarketDealDailySummary{" +
            "id=" + getId() +
            ", reportDate='" + getReportDate() + "'" +
            ", ledger='" + getLedger() + "'" +
            ", numberOfDeals=" + getNumberOfDeals() +
            ", totalPrincipal=" + getTotalPrincipal() +
            ", interestAccrued=" + getInterestAccrued() +
            ", totalPVFull=" + getTotalPVFull() +
            "}";
    }
}
