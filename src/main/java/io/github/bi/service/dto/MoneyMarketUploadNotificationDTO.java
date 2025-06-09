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

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link io.github.bi.domain.MoneyMarketUploadNotification} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MoneyMarketUploadNotificationDTO implements Serializable {

    private Long id;

    @Lob
    private String errorMessage;

    private Integer rowNumber;

    @NotNull
    private UUID referenceNumber;

    private MoneyMarketListDTO moneyMarketList;

    private ReportBatchDTO reportBatch;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public UUID getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(UUID referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public MoneyMarketListDTO getMoneyMarketList() {
        return moneyMarketList;
    }

    public void setMoneyMarketList(MoneyMarketListDTO moneyMarketList) {
        this.moneyMarketList = moneyMarketList;
    }

    public ReportBatchDTO getReportBatch() {
        return reportBatch;
    }

    public void setReportBatch(ReportBatchDTO reportBatch) {
        this.reportBatch = reportBatch;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MoneyMarketUploadNotificationDTO)) {
            return false;
        }

        MoneyMarketUploadNotificationDTO moneyMarketUploadNotificationDTO = (MoneyMarketUploadNotificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, moneyMarketUploadNotificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoneyMarketUploadNotificationDTO{" +
            "id=" + getId() +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", rowNumber=" + getRowNumber() +
            ", referenceNumber='" + getReferenceNumber() + "'" +
            ", moneyMarketList=" + getMoneyMarketList() +
            ", reportBatch=" + getReportBatch() +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
