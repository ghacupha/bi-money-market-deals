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
 * Criteria class for the {@link io.github.bi.domain.FiscalQuarter} entity. This class is used
 * in {@link io.github.bi.web.rest.FiscalQuarterResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fiscal-quarters?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FiscalQuarterCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter quarterNumber;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private StringFilter fiscalQuarterCode;

    private LongFilter fiscalYearId;

    private LongFilter placeholderId;

    private Boolean distinct;

    public FiscalQuarterCriteria() {}

    public FiscalQuarterCriteria(FiscalQuarterCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.quarterNumber = other.optionalQuarterNumber().map(IntegerFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(LocalDateFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(LocalDateFilter::copy).orElse(null);
        this.fiscalQuarterCode = other.optionalFiscalQuarterCode().map(StringFilter::copy).orElse(null);
        this.fiscalYearId = other.optionalFiscalYearId().map(LongFilter::copy).orElse(null);
        this.placeholderId = other.optionalPlaceholderId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FiscalQuarterCriteria copy() {
        return new FiscalQuarterCriteria(this);
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

    public IntegerFilter getQuarterNumber() {
        return quarterNumber;
    }

    public Optional<IntegerFilter> optionalQuarterNumber() {
        return Optional.ofNullable(quarterNumber);
    }

    public IntegerFilter quarterNumber() {
        if (quarterNumber == null) {
            setQuarterNumber(new IntegerFilter());
        }
        return quarterNumber;
    }

    public void setQuarterNumber(IntegerFilter quarterNumber) {
        this.quarterNumber = quarterNumber;
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

    public StringFilter getFiscalQuarterCode() {
        return fiscalQuarterCode;
    }

    public Optional<StringFilter> optionalFiscalQuarterCode() {
        return Optional.ofNullable(fiscalQuarterCode);
    }

    public StringFilter fiscalQuarterCode() {
        if (fiscalQuarterCode == null) {
            setFiscalQuarterCode(new StringFilter());
        }
        return fiscalQuarterCode;
    }

    public void setFiscalQuarterCode(StringFilter fiscalQuarterCode) {
        this.fiscalQuarterCode = fiscalQuarterCode;
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
        final FiscalQuarterCriteria that = (FiscalQuarterCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(quarterNumber, that.quarterNumber) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(fiscalQuarterCode, that.fiscalQuarterCode) &&
            Objects.equals(fiscalYearId, that.fiscalYearId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quarterNumber, startDate, endDate, fiscalQuarterCode, fiscalYearId, placeholderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FiscalQuarterCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalQuarterNumber().map(f -> "quarterNumber=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalFiscalQuarterCode().map(f -> "fiscalQuarterCode=" + f + ", ").orElse("") +
            optionalFiscalYearId().map(f -> "fiscalYearId=" + f + ", ").orElse("") +
            optionalPlaceholderId().map(f -> "placeholderId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
