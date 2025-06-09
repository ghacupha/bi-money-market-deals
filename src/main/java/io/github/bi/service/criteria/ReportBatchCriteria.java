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

import io.github.bi.domain.enumeration.FileProcessFlag;
import io.github.bi.domain.enumeration.reportBatchStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link io.github.bi.domain.ReportBatch} entity. This class is used
 * in {@link io.github.bi.web.rest.ReportBatchResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /report-batches?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportBatchCriteria implements Serializable, Criteria {

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

    /**
     * Class for filtering FileProcessFlag
     */
    public static class FileProcessFlagFilter extends Filter<FileProcessFlag> {

        public FileProcessFlagFilter() {}

        public FileProcessFlagFilter(FileProcessFlagFilter filter) {
            super(filter);
        }

        @Override
        public FileProcessFlagFilter copy() {
            return new FileProcessFlagFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter reportDate;

    private ZonedDateTimeFilter uploadTimeStamp;

    private reportBatchStatusFilter status;

    private BooleanFilter active;

    private StringFilter description;

    private UUIDFilter fileIdentifier;

    private FileProcessFlagFilter processFlag;

    private LongFilter uploadedById;

    private LongFilter placeholderId;

    private LongFilter moneyMarketListId;

    private LongFilter moneyMarketUploadNotificationId;

    private Boolean distinct;

    public ReportBatchCriteria() {}

    public ReportBatchCriteria(ReportBatchCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.reportDate = other.optionalReportDate().map(LocalDateFilter::copy).orElse(null);
        this.uploadTimeStamp = other.optionalUploadTimeStamp().map(ZonedDateTimeFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(reportBatchStatusFilter::copy).orElse(null);
        this.active = other.optionalActive().map(BooleanFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.fileIdentifier = other.optionalFileIdentifier().map(UUIDFilter::copy).orElse(null);
        this.processFlag = other.optionalProcessFlag().map(FileProcessFlagFilter::copy).orElse(null);
        this.uploadedById = other.optionalUploadedById().map(LongFilter::copy).orElse(null);
        this.placeholderId = other.optionalPlaceholderId().map(LongFilter::copy).orElse(null);
        this.moneyMarketListId = other.optionalMoneyMarketListId().map(LongFilter::copy).orElse(null);
        this.moneyMarketUploadNotificationId = other.optionalMoneyMarketUploadNotificationId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ReportBatchCriteria copy() {
        return new ReportBatchCriteria(this);
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

    public UUIDFilter getFileIdentifier() {
        return fileIdentifier;
    }

    public Optional<UUIDFilter> optionalFileIdentifier() {
        return Optional.ofNullable(fileIdentifier);
    }

    public UUIDFilter fileIdentifier() {
        if (fileIdentifier == null) {
            setFileIdentifier(new UUIDFilter());
        }
        return fileIdentifier;
    }

    public void setFileIdentifier(UUIDFilter fileIdentifier) {
        this.fileIdentifier = fileIdentifier;
    }

    public FileProcessFlagFilter getProcessFlag() {
        return processFlag;
    }

    public Optional<FileProcessFlagFilter> optionalProcessFlag() {
        return Optional.ofNullable(processFlag);
    }

    public FileProcessFlagFilter processFlag() {
        if (processFlag == null) {
            setProcessFlag(new FileProcessFlagFilter());
        }
        return processFlag;
    }

    public void setProcessFlag(FileProcessFlagFilter processFlag) {
        this.processFlag = processFlag;
    }

    public LongFilter getUploadedById() {
        return uploadedById;
    }

    public Optional<LongFilter> optionalUploadedById() {
        return Optional.ofNullable(uploadedById);
    }

    public LongFilter uploadedById() {
        if (uploadedById == null) {
            setUploadedById(new LongFilter());
        }
        return uploadedById;
    }

    public void setUploadedById(LongFilter uploadedById) {
        this.uploadedById = uploadedById;
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

    public LongFilter getMoneyMarketUploadNotificationId() {
        return moneyMarketUploadNotificationId;
    }

    public Optional<LongFilter> optionalMoneyMarketUploadNotificationId() {
        return Optional.ofNullable(moneyMarketUploadNotificationId);
    }

    public LongFilter moneyMarketUploadNotificationId() {
        if (moneyMarketUploadNotificationId == null) {
            setMoneyMarketUploadNotificationId(new LongFilter());
        }
        return moneyMarketUploadNotificationId;
    }

    public void setMoneyMarketUploadNotificationId(LongFilter moneyMarketUploadNotificationId) {
        this.moneyMarketUploadNotificationId = moneyMarketUploadNotificationId;
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
        final ReportBatchCriteria that = (ReportBatchCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reportDate, that.reportDate) &&
            Objects.equals(uploadTimeStamp, that.uploadTimeStamp) &&
            Objects.equals(status, that.status) &&
            Objects.equals(active, that.active) &&
            Objects.equals(description, that.description) &&
            Objects.equals(fileIdentifier, that.fileIdentifier) &&
            Objects.equals(processFlag, that.processFlag) &&
            Objects.equals(uploadedById, that.uploadedById) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(moneyMarketListId, that.moneyMarketListId) &&
            Objects.equals(moneyMarketUploadNotificationId, that.moneyMarketUploadNotificationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            reportDate,
            uploadTimeStamp,
            status,
            active,
            description,
            fileIdentifier,
            processFlag,
            uploadedById,
            placeholderId,
            moneyMarketListId,
            moneyMarketUploadNotificationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportBatchCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalReportDate().map(f -> "reportDate=" + f + ", ").orElse("") +
            optionalUploadTimeStamp().map(f -> "uploadTimeStamp=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalActive().map(f -> "active=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalFileIdentifier().map(f -> "fileIdentifier=" + f + ", ").orElse("") +
            optionalProcessFlag().map(f -> "processFlag=" + f + ", ").orElse("") +
            optionalUploadedById().map(f -> "uploadedById=" + f + ", ").orElse("") +
            optionalPlaceholderId().map(f -> "placeholderId=" + f + ", ").orElse("") +
            optionalMoneyMarketListId().map(f -> "moneyMarketListId=" + f + ", ").orElse("") +
            optionalMoneyMarketUploadNotificationId().map(f -> "moneyMarketUploadNotificationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
