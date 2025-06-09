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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link io.github.bi.domain.Placeholder} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlaceholderDTO implements Serializable {

    private Long id;

    @NotNull
    private String description;

    private String token;

    private PlaceholderDTO containingPlaceholder;

    private Set<DealerDTO> dealers = new HashSet<>();

    private Set<SecurityClearanceDTO> securityClearances = new HashSet<>();

    private Set<ApplicationUserDTO> applicationUsers = new HashSet<>();

    private Set<FiscalYearDTO> fiscalYears = new HashSet<>();

    private Set<FiscalQuarterDTO> fiscalQuarters = new HashSet<>();

    private Set<FiscalMonthDTO> fiscalMonths = new HashSet<>();

    private Set<ReportBatchDTO> reportBatches = new HashSet<>();

    private Set<MoneyMarketListDTO> moneyMarketLists = new HashSet<>();

    private Set<MoneyMarketUploadNotificationDTO> moneyMarketUploadNotifications = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public PlaceholderDTO getContainingPlaceholder() {
        return containingPlaceholder;
    }

    public void setContainingPlaceholder(PlaceholderDTO containingPlaceholder) {
        this.containingPlaceholder = containingPlaceholder;
    }

    public Set<DealerDTO> getDealers() {
        return dealers;
    }

    public void setDealers(Set<DealerDTO> dealers) {
        this.dealers = dealers;
    }

    public Set<SecurityClearanceDTO> getSecurityClearances() {
        return securityClearances;
    }

    public void setSecurityClearances(Set<SecurityClearanceDTO> securityClearances) {
        this.securityClearances = securityClearances;
    }

    public Set<ApplicationUserDTO> getApplicationUsers() {
        return applicationUsers;
    }

    public void setApplicationUsers(Set<ApplicationUserDTO> applicationUsers) {
        this.applicationUsers = applicationUsers;
    }

    public Set<FiscalYearDTO> getFiscalYears() {
        return fiscalYears;
    }

    public void setFiscalYears(Set<FiscalYearDTO> fiscalYears) {
        this.fiscalYears = fiscalYears;
    }

    public Set<FiscalQuarterDTO> getFiscalQuarters() {
        return fiscalQuarters;
    }

    public void setFiscalQuarters(Set<FiscalQuarterDTO> fiscalQuarters) {
        this.fiscalQuarters = fiscalQuarters;
    }

    public Set<FiscalMonthDTO> getFiscalMonths() {
        return fiscalMonths;
    }

    public void setFiscalMonths(Set<FiscalMonthDTO> fiscalMonths) {
        this.fiscalMonths = fiscalMonths;
    }

    public Set<ReportBatchDTO> getReportBatches() {
        return reportBatches;
    }

    public void setReportBatches(Set<ReportBatchDTO> reportBatches) {
        this.reportBatches = reportBatches;
    }

    public Set<MoneyMarketListDTO> getMoneyMarketLists() {
        return moneyMarketLists;
    }

    public void setMoneyMarketLists(Set<MoneyMarketListDTO> moneyMarketLists) {
        this.moneyMarketLists = moneyMarketLists;
    }

    public Set<MoneyMarketUploadNotificationDTO> getMoneyMarketUploadNotifications() {
        return moneyMarketUploadNotifications;
    }

    public void setMoneyMarketUploadNotifications(Set<MoneyMarketUploadNotificationDTO> moneyMarketUploadNotifications) {
        this.moneyMarketUploadNotifications = moneyMarketUploadNotifications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlaceholderDTO)) {
            return false;
        }

        PlaceholderDTO placeholderDTO = (PlaceholderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, placeholderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlaceholderDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", token='" + getToken() + "'" +
            ", containingPlaceholder=" + getContainingPlaceholder() +
            ", dealers=" + getDealers() +
            ", securityClearances=" + getSecurityClearances() +
            ", applicationUsers=" + getApplicationUsers() +
            ", fiscalYears=" + getFiscalYears() +
            ", fiscalQuarters=" + getFiscalQuarters() +
            ", fiscalMonths=" + getFiscalMonths() +
            ", reportBatches=" + getReportBatches() +
            ", moneyMarketLists=" + getMoneyMarketLists() +
            ", moneyMarketUploadNotifications=" + getMoneyMarketUploadNotifications() +
            "}";
    }
}
