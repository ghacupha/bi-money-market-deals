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
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link io.github.bi.domain.FiscalQuarter} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FiscalQuarterDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer quarterNumber;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private String fiscalQuarterCode;

    @NotNull
    private FiscalYearDTO fiscalYear;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuarterNumber() {
        return quarterNumber;
    }

    public void setQuarterNumber(Integer quarterNumber) {
        this.quarterNumber = quarterNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getFiscalQuarterCode() {
        return fiscalQuarterCode;
    }

    public void setFiscalQuarterCode(String fiscalQuarterCode) {
        this.fiscalQuarterCode = fiscalQuarterCode;
    }

    public FiscalYearDTO getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(FiscalYearDTO fiscalYear) {
        this.fiscalYear = fiscalYear;
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
        if (!(o instanceof FiscalQuarterDTO)) {
            return false;
        }

        FiscalQuarterDTO fiscalQuarterDTO = (FiscalQuarterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fiscalQuarterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FiscalQuarterDTO{" +
            "id=" + getId() +
            ", quarterNumber=" + getQuarterNumber() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", fiscalQuarterCode='" + getFiscalQuarterCode() + "'" +
            ", fiscalYear=" + getFiscalYear() +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
