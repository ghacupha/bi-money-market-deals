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
 * Criteria class for the {@link io.github.bi.domain.MoneyMarketUploadNotification} entity. This class is used
 * in {@link io.github.bi.web.rest.MoneyMarketUploadNotificationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /money-market-upload-notifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MoneyMarketUploadNotificationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter rowNumber;

    private UUIDFilter referenceNumber;

    private LongFilter moneyMarketListId;

    private LongFilter reportBatchId;

    private LongFilter placeholderId;

    private Boolean distinct;

    public MoneyMarketUploadNotificationCriteria() {}

    public MoneyMarketUploadNotificationCriteria(MoneyMarketUploadNotificationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.rowNumber = other.optionalRowNumber().map(IntegerFilter::copy).orElse(null);
        this.referenceNumber = other.optionalReferenceNumber().map(UUIDFilter::copy).orElse(null);
        this.moneyMarketListId = other.optionalMoneyMarketListId().map(LongFilter::copy).orElse(null);
        this.reportBatchId = other.optionalReportBatchId().map(LongFilter::copy).orElse(null);
        this.placeholderId = other.optionalPlaceholderId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MoneyMarketUploadNotificationCriteria copy() {
        return new MoneyMarketUploadNotificationCriteria(this);
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

    public IntegerFilter getRowNumber() {
        return rowNumber;
    }

    public Optional<IntegerFilter> optionalRowNumber() {
        return Optional.ofNullable(rowNumber);
    }

    public IntegerFilter rowNumber() {
        if (rowNumber == null) {
            setRowNumber(new IntegerFilter());
        }
        return rowNumber;
    }

    public void setRowNumber(IntegerFilter rowNumber) {
        this.rowNumber = rowNumber;
    }

    public UUIDFilter getReferenceNumber() {
        return referenceNumber;
    }

    public Optional<UUIDFilter> optionalReferenceNumber() {
        return Optional.ofNullable(referenceNumber);
    }

    public UUIDFilter referenceNumber() {
        if (referenceNumber == null) {
            setReferenceNumber(new UUIDFilter());
        }
        return referenceNumber;
    }

    public void setReferenceNumber(UUIDFilter referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public LongFilter getMoneyMarketListId() {
        return moneyMarketListId;
    }

    public Optional<LongFilter> optionalMoneyMarketListId() {
        return Optional.ofNullable(moneyMarketListId);
    }

    public LongFilter moneyMarketListId() {
        if (moneyMarketListId == null) {
            setMoneyMarketListId(new LongFilter());
        }
        return moneyMarketListId;
    }

    public void setMoneyMarketListId(LongFilter moneyMarketListId) {
        this.moneyMarketListId = moneyMarketListId;
    }

    public LongFilter getReportBatchId() {
        return reportBatchId;
    }

    public Optional<LongFilter> optionalReportBatchId() {
        return Optional.ofNullable(reportBatchId);
    }

    public LongFilter reportBatchId() {
        if (reportBatchId == null) {
            setReportBatchId(new LongFilter());
        }
        return reportBatchId;
    }

    public void setReportBatchId(LongFilter reportBatchId) {
        this.reportBatchId = reportBatchId;
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
        final MoneyMarketUploadNotificationCriteria that = (MoneyMarketUploadNotificationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(rowNumber, that.rowNumber) &&
            Objects.equals(referenceNumber, that.referenceNumber) &&
            Objects.equals(moneyMarketListId, that.moneyMarketListId) &&
            Objects.equals(reportBatchId, that.reportBatchId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rowNumber, referenceNumber, moneyMarketListId, reportBatchId, placeholderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoneyMarketUploadNotificationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalRowNumber().map(f -> "rowNumber=" + f + ", ").orElse("") +
            optionalReferenceNumber().map(f -> "referenceNumber=" + f + ", ").orElse("") +
            optionalMoneyMarketListId().map(f -> "moneyMarketListId=" + f + ", ").orElse("") +
            optionalReportBatchId().map(f -> "reportBatchId=" + f + ", ").orElse("") +
            optionalPlaceholderId().map(f -> "placeholderId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
