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

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link io.github.bi.domain.MoneyMarketDealDailySummary} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MoneyMarketDealDailySummaryDTO implements Serializable {

    private Long id;

    private LocalDate reportDate;

    private String ledger;

    private Integer numberOfDeals;

    private BigDecimal totalPrincipal;

    private BigDecimal interestAccrued;

    private BigDecimal totalPVFull;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getLedger() {
        return ledger;
    }

    public void setLedger(String ledger) {
        this.ledger = ledger;
    }

    public Integer getNumberOfDeals() {
        return numberOfDeals;
    }

    public void setNumberOfDeals(Integer numberOfDeals) {
        this.numberOfDeals = numberOfDeals;
    }

    public BigDecimal getTotalPrincipal() {
        return totalPrincipal;
    }

    public void setTotalPrincipal(BigDecimal totalPrincipal) {
        this.totalPrincipal = totalPrincipal;
    }

    public BigDecimal getInterestAccrued() {
        return interestAccrued;
    }

    public void setInterestAccrued(BigDecimal interestAccrued) {
        this.interestAccrued = interestAccrued;
    }

    public BigDecimal getTotalPVFull() {
        return totalPVFull;
    }

    public void setTotalPVFull(BigDecimal totalPVFull) {
        this.totalPVFull = totalPVFull;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MoneyMarketDealDailySummaryDTO)) {
            return false;
        }

        MoneyMarketDealDailySummaryDTO moneyMarketDealDailySummaryDTO = (MoneyMarketDealDailySummaryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, moneyMarketDealDailySummaryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoneyMarketDealDailySummaryDTO{" +
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
