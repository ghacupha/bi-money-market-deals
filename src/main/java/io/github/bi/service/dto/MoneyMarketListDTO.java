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

import io.github.bi.domain.enumeration.reportBatchStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link io.github.bi.domain.MoneyMarketList} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MoneyMarketListDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate reportDate;

    @NotNull
    private ZonedDateTime uploadTimeStamp;

    @NotNull
    private reportBatchStatus status;

    private String description;

    @NotNull
    private Boolean active;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

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

    public ZonedDateTime getUploadTimeStamp() {
        return uploadTimeStamp;
    }

    public void setUploadTimeStamp(ZonedDateTime uploadTimeStamp) {
        this.uploadTimeStamp = uploadTimeStamp;
    }

    public reportBatchStatus getStatus() {
        return status;
    }

    public void setStatus(reportBatchStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
        if (!(o instanceof MoneyMarketListDTO)) {
            return false;
        }

        MoneyMarketListDTO moneyMarketListDTO = (MoneyMarketListDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, moneyMarketListDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoneyMarketListDTO{" +
            "id=" + getId() +
            ", reportDate='" + getReportDate() + "'" +
            ", uploadTimeStamp='" + getUploadTimeStamp() + "'" +
            ", status='" + getStatus() + "'" +
            ", description='" + getDescription() + "'" +
            ", active='" + getActive() + "'" +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
