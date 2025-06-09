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
 * Criteria class for the {@link io.github.bi.domain.FiscalMonth} entity. This class is used
 * in {@link io.github.bi.web.rest.FiscalMonthResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fiscal-months?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FiscalMonthCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter monthNumber;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private StringFilter fiscalMonthCode;

    private LongFilter fiscalYearId;

    private LongFilter placeholderId;

    private LongFilter fiscalQuarterId;

    private Boolean distinct;

    public FiscalMonthCriteria() {}

    public FiscalMonthCriteria(FiscalMonthCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.monthNumber = other.optionalMonthNumber().map(IntegerFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(LocalDateFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(LocalDateFilter::copy).orElse(null);
        this.fiscalMonthCode = other.optionalFiscalMonthCode().map(StringFilter::copy).orElse(null);
        this.fiscalYearId = other.optionalFiscalYearId().map(LongFilter::copy).orElse(null);
        this.placeholderId = other.optionalPlaceholderId().map(LongFilter::copy).orElse(null);
        this.fiscalQuarterId = other.optionalFiscalQuarterId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FiscalMonthCriteria copy() {
        return new FiscalMonthCriteria(this);
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

    public IntegerFilter getMonthNumber() {
        return monthNumber;
    }

    public Optional<IntegerFilter> optionalMonthNumber() {
        return Optional.ofNullable(monthNumber);
    }

    public IntegerFilter monthNumber() {
        if (monthNumber == null) {
            setMonthNumber(new IntegerFilter());
        }
        return monthNumber;
    }

    public void setMonthNumber(IntegerFilter monthNumber) {
        this.monthNumber = monthNumber;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public Optional<LocalDateFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            setStartDate(new LocalDateFilter());
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
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

    public StringFilter getFiscalMonthCode() {
        return fiscalMonthCode;
    }

    public Optional<StringFilter> optionalFiscalMonthCode() {
        return Optional.ofNullable(fiscalMonthCode);
    }

    public StringFilter fiscalMonthCode() {
        if (fiscalMonthCode == null) {
            setFiscalMonthCode(new StringFilter());
        }
        return fiscalMonthCode;
    }

    public void setFiscalMonthCode(StringFilter fiscalMonthCode) {
        this.fiscalMonthCode = fiscalMonthCode;
    }

    public LongFilter getFiscalYearId() {
        return fiscalYearId;
    }

    public Optional<LongFilter> optionalFiscalYearId() {
        return Optional.ofNullable(fiscalYearId);
    }

    public LongFilter fiscalYearId() {
        if (fiscalYearId == null) {
            setFiscalYearId(new LongFilter());
        }
        return fiscalYearId;
    }

    public void setFiscalYearId(LongFilter fiscalYearId) {
        this.fiscalYearId = fiscalYearId;
    }

    public LongFilter getPlaceholderId() {
        return placeholderId;
    }

    public Optional<LongFilter> optionalPlaceholderId() {
        return Optional.ofNullable(placeholderId);
    }

    public LongFilter placeholderId() {
        if (placeholderId == null) {
            setPlaceholderId(new LongFilter());
        }
        return placeholderId;
    }

    public void setPlaceholderId(LongFilter placeholderId) {
        this.placeholderId = placeholderId;
    }

    public LongFilter getFiscalQuarterId() {
        return fiscalQuarterId;
    }

    public Optional<LongFilter> optionalFiscalQuarterId() {
        return Optional.ofNullable(fiscalQuarterId);
    }

    public LongFilter fiscalQuarterId() {
        if (fiscalQuarterId == null) {
            setFiscalQuarterId(new LongFilter());
        }
        return fiscalQuarterId;
    }

    public void setFiscalQuarterId(LongFilter fiscalQuarterId) {
        this.fiscalQuarterId = fiscalQuarterId;
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
        final FiscalMonthCriteria that = (FiscalMonthCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(monthNumber, that.monthNumber) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(fiscalMonthCode, that.fiscalMonthCode) &&
            Objects.equals(fiscalYearId, that.fiscalYearId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(fiscalQuarterId, that.fiscalQuarterId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, monthNumber, startDate, endDate, fiscalMonthCode, fiscalYearId, placeholderId, fiscalQuarterId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FiscalMonthCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalMonthNumber().map(f -> "monthNumber=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalFiscalMonthCode().map(f -> "fiscalMonthCode=" + f + ", ").orElse("") +
            optionalFiscalYearId().map(f -> "fiscalYearId=" + f + ", ").orElse("") +
            optionalPlaceholderId().map(f -> "placeholderId=" + f + ", ").orElse("") +
            optionalFiscalQuarterId().map(f -> "fiscalQuarterId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
