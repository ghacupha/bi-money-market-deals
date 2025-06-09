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

import io.github.bi.domain.enumeration.reportBatchStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link io.github.bi.domain.MoneyMarketList} entity. This class is used
 * in {@link io.github.bi.web.rest.MoneyMarketListResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /money-market-lists?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MoneyMarketListCriteria implements Serializable, Criteria {

    /**
     * Class for filtering reportBatchStatus
     */
    public static class reportBatchStatusFilter extends Filter<reportBatchStatus> {

        public reportBatchStatusFilter() {}

        public reportBatchStatusFilter(reportBatchStatusFilter filter) {
            super(filter);
        }

        @Override
        public reportBatchStatusFilter copy() {
            return new reportBatchStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter reportDate;

    private ZonedDateTimeFilter uploadTimeStamp;

    private reportBatchStatusFilter status;

    private StringFilter description;

    private BooleanFilter active;

    private LongFilter placeholderId;

    private LongFilter moneyMarketDealId;

    private Boolean distinct;

    public MoneyMarketListCriteria() {}

    public MoneyMarketListCriteria(MoneyMarketListCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.reportDate = other.optionalReportDate().map(LocalDateFilter::copy).orElse(null);
        this.uploadTimeStamp = other.optionalUploadTimeStamp().map(ZonedDateTimeFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(reportBatchStatusFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.active = other.optionalActive().map(BooleanFilter::copy).orElse(null);
        this.placeholderId = other.optionalPlaceholderId().map(LongFilter::copy).orElse(null);
        this.moneyMarketDealId = other.optionalMoneyMarketDealId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MoneyMarketListCriteria copy() {
        return new MoneyMarketListCriteria(this);
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

    public LocalDateFilter getReportDate() {
        return reportDate;
    }

    public Optional<LocalDateFilter> optionalReportDate() {
        return Optional.ofNullable(reportDate);
    }

    public LocalDateFilter reportDate() {
        if (reportDate == null) {
            setReportDate(new LocalDateFilter());
        }
        return reportDate;
    }

    public void setReportDate(LocalDateFilter reportDate) {
        this.reportDate = reportDate;
    }

    public ZonedDateTimeFilter getUploadTimeStamp() {
        return uploadTimeStamp;
    }

    public Optional<ZonedDateTimeFilter> optionalUploadTimeStamp() {
        return Optional.ofNullable(uploadTimeStamp);
    }

    public ZonedDateTimeFilter uploadTimeStamp() {
        if (uploadTimeStamp == null) {
            setUploadTimeStamp(new ZonedDateTimeFilter());
        }
        return uploadTimeStamp;
    }

    public void setUploadTimeStamp(ZonedDateTimeFilter uploadTimeStamp) {
        this.uploadTimeStamp = uploadTimeStamp;
    }

    public reportBatchStatusFilter getStatus() {
        return status;
    }

    public Optional<reportBatchStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public reportBatchStatusFilter status() {
        if (status == null) {
            setStatus(new reportBatchStatusFilter());
        }
        return status;
    }

    public void setStatus(reportBatchStatusFilter status) {
        this.status = status;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public Optional<BooleanFilter> optionalActive() {
        return Optional.ofNullable(active);
    }

    public BooleanFilter active() {
        if (active == null) {
            setActive(new BooleanFilter());
        }
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
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

    public LongFilter getMoneyMarketDealId() {
        return moneyMarketDealId;
    }

    public Optional<LongFilter> optionalMoneyMarketDealId() {
        return Optional.ofNullable(moneyMarketDealId);
    }

    public LongFilter moneyMarketDealId() {
        if (moneyMarketDealId == null) {
            setMoneyMarketDealId(new LongFilter());
        }
        return moneyMarketDealId;
    }

    public void setMoneyMarketDealId(LongFilter moneyMarketDealId) {
        this.moneyMarketDealId = moneyMarketDealId;
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
        final MoneyMarketListCriteria that = (MoneyMarketListCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reportDate, that.reportDate) &&
            Objects.equals(uploadTimeStamp, that.uploadTimeStamp) &&
            Objects.equals(status, that.status) &&
            Objects.equals(description, that.description) &&
            Objects.equals(active, that.active) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(moneyMarketDealId, that.moneyMarketDealId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reportDate, uploadTimeStamp, status, description, active, placeholderId, moneyMarketDealId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoneyMarketListCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalReportDate().map(f -> "reportDate=" + f + ", ").orElse("") +
            optionalUploadTimeStamp().map(f -> "uploadTimeStamp=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalActive().map(f -> "active=" + f + ", ").orElse("") +
            optionalPlaceholderId().map(f -> "placeholderId=" + f + ", ").orElse("") +
            optionalMoneyMarketDealId().map(f -> "moneyMarketDealId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
