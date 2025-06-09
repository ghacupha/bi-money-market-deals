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

import io.github.bi.domain.enumeration.FiscalYearStatusType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link io.github.bi.domain.FiscalYear} entity. This class is used
 * in {@link io.github.bi.web.rest.FiscalYearResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fiscal-years?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FiscalYearCriteria implements Serializable, Criteria {

    /**
     * Class for filtering FiscalYearStatusType
     */
    public static class FiscalYearStatusTypeFilter extends Filter<FiscalYearStatusType> {

        public FiscalYearStatusTypeFilter() {}

        public FiscalYearStatusTypeFilter(FiscalYearStatusTypeFilter filter) {
            super(filter);
        }

        @Override
        public FiscalYearStatusTypeFilter copy() {
            return new FiscalYearStatusTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fiscalYearCode;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private FiscalYearStatusTypeFilter fiscalYearStatus;

    private LongFilter placeholderId;

    private LongFilter createdById;

    private LongFilter lastUpdatedById;

    private Boolean distinct;

    public FiscalYearCriteria() {}

    public FiscalYearCriteria(FiscalYearCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fiscalYearCode = other.optionalFiscalYearCode().map(StringFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(LocalDateFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(LocalDateFilter::copy).orElse(null);
        this.fiscalYearStatus = other.optionalFiscalYearStatus().map(FiscalYearStatusTypeFilter::copy).orElse(null);
        this.placeholderId = other.optionalPlaceholderId().map(LongFilter::copy).orElse(null);
        this.createdById = other.optionalCreatedById().map(LongFilter::copy).orElse(null);
        this.lastUpdatedById = other.optionalLastUpdatedById().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FiscalYearCriteria copy() {
        return new FiscalYearCriteria(this);
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

    public StringFilter getFiscalYearCode() {
        return fiscalYearCode;
    }

    public Optional<StringFilter> optionalFiscalYearCode() {
        return Optional.ofNullable(fiscalYearCode);
    }

    public StringFilter fiscalYearCode() {
        if (fiscalYearCode == null) {
            setFiscalYearCode(new StringFilter());
        }
        return fiscalYearCode;
    }

    public void setFiscalYearCode(StringFilter fiscalYearCode) {
        this.fiscalYearCode = fiscalYearCode;
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

    public FiscalYearStatusTypeFilter getFiscalYearStatus() {
        return fiscalYearStatus;
    }

    public Optional<FiscalYearStatusTypeFilter> optionalFiscalYearStatus() {
        return Optional.ofNullable(fiscalYearStatus);
    }

    public FiscalYearStatusTypeFilter fiscalYearStatus() {
        if (fiscalYearStatus == null) {
            setFiscalYearStatus(new FiscalYearStatusTypeFilter());
        }
        return fiscalYearStatus;
    }

    public void setFiscalYearStatus(FiscalYearStatusTypeFilter fiscalYearStatus) {
        this.fiscalYearStatus = fiscalYearStatus;
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

    public LongFilter getCreatedById() {
        return createdById;
    }

    public Optional<LongFilter> optionalCreatedById() {
        return Optional.ofNullable(createdById);
    }

    public LongFilter createdById() {
        if (createdById == null) {
            setCreatedById(new LongFilter());
        }
        return createdById;
    }

    public void setCreatedById(LongFilter createdById) {
        this.createdById = createdById;
    }

    public LongFilter getLastUpdatedById() {
        return lastUpdatedById;
    }

    public Optional<LongFilter> optionalLastUpdatedById() {
        return Optional.ofNullable(lastUpdatedById);
    }

    public LongFilter lastUpdatedById() {
        if (lastUpdatedById == null) {
            setLastUpdatedById(new LongFilter());
        }
        return lastUpdatedById;
    }

    public void setLastUpdatedById(LongFilter lastUpdatedById) {
        this.lastUpdatedById = lastUpdatedById;
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
        final FiscalYearCriteria that = (FiscalYearCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fiscalYearCode, that.fiscalYearCode) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(fiscalYearStatus, that.fiscalYearStatus) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(createdById, that.createdById) &&
            Objects.equals(lastUpdatedById, that.lastUpdatedById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            fiscalYearCode,
            startDate,
            endDate,
            fiscalYearStatus,
            placeholderId,
            createdById,
            lastUpdatedById,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FiscalYearCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFiscalYearCode().map(f -> "fiscalYearCode=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalFiscalYearStatus().map(f -> "fiscalYearStatus=" + f + ", ").orElse("") +
            optionalPlaceholderId().map(f -> "placeholderId=" + f + ", ").orElse("") +
            optionalCreatedById().map(f -> "createdById=" + f + ", ").orElse("") +
            optionalLastUpdatedById().map(f -> "lastUpdatedById=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
