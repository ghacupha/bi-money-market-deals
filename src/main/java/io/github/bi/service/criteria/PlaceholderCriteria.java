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
 * Criteria class for the {@link io.github.bi.domain.Placeholder} entity. This class is used
 * in {@link io.github.bi.web.rest.PlaceholderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /placeholders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlaceholderCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private StringFilter token;

    private LongFilter containingPlaceholderId;

    private LongFilter dealerId;

    private LongFilter placeholderId;

    private LongFilter securityClearanceId;

    private LongFilter applicationUserId;

    private LongFilter fiscalYearId;

    private LongFilter fiscalQuarterId;

    private LongFilter fiscalMonthId;

    private LongFilter reportBatchId;

    private LongFilter moneyMarketListId;

    private LongFilter moneyMarketUploadNotificationId;

    private Boolean distinct;

    public PlaceholderCriteria() {}

    public PlaceholderCriteria(PlaceholderCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.token = other.optionalToken().map(StringFilter::copy).orElse(null);
        this.containingPlaceholderId = other.optionalContainingPlaceholderId().map(LongFilter::copy).orElse(null);
        this.dealerId = other.optionalDealerId().map(LongFilter::copy).orElse(null);
        this.placeholderId = other.optionalPlaceholderId().map(LongFilter::copy).orElse(null);
        this.securityClearanceId = other.optionalSecurityClearanceId().map(LongFilter::copy).orElse(null);
        this.applicationUserId = other.optionalApplicationUserId().map(LongFilter::copy).orElse(null);
        this.fiscalYearId = other.optionalFiscalYearId().map(LongFilter::copy).orElse(null);
        this.fiscalQuarterId = other.optionalFiscalQuarterId().map(LongFilter::copy).orElse(null);
        this.fiscalMonthId = other.optionalFiscalMonthId().map(LongFilter::copy).orElse(null);
        this.reportBatchId = other.optionalReportBatchId().map(LongFilter::copy).orElse(null);
        this.moneyMarketListId = other.optionalMoneyMarketListId().map(LongFilter::copy).orElse(null);
        this.moneyMarketUploadNotificationId = other.optionalMoneyMarketUploadNotificationId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PlaceholderCriteria copy() {
        return new PlaceholderCriteria(this);
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

    public StringFilter getToken() {
        return token;
    }

    public Optional<StringFilter> optionalToken() {
        return Optional.ofNullable(token);
    }

    public StringFilter token() {
        if (token == null) {
            setToken(new StringFilter());
        }
        return token;
    }

    public void setToken(StringFilter token) {
        this.token = token;
    }

    public LongFilter getContainingPlaceholderId() {
        return containingPlaceholderId;
    }

    public Optional<LongFilter> optionalContainingPlaceholderId() {
        return Optional.ofNullable(containingPlaceholderId);
    }

    public LongFilter containingPlaceholderId() {
        if (containingPlaceholderId == null) {
            setContainingPlaceholderId(new LongFilter());
        }
        return containingPlaceholderId;
    }

    public void setContainingPlaceholderId(LongFilter containingPlaceholderId) {
        this.containingPlaceholderId = containingPlaceholderId;
    }

    public LongFilter getDealerId() {
        return dealerId;
    }

    public Optional<LongFilter> optionalDealerId() {
        return Optional.ofNullable(dealerId);
    }

    public LongFilter dealerId() {
        if (dealerId == null) {
            setDealerId(new LongFilter());
        }
        return dealerId;
    }

    public void setDealerId(LongFilter dealerId) {
        this.dealerId = dealerId;
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

    public LongFilter getSecurityClearanceId() {
        return securityClearanceId;
    }

    public Optional<LongFilter> optionalSecurityClearanceId() {
        return Optional.ofNullable(securityClearanceId);
    }

    public LongFilter securityClearanceId() {
        if (securityClearanceId == null) {
            setSecurityClearanceId(new LongFilter());
        }
        return securityClearanceId;
    }

    public void setSecurityClearanceId(LongFilter securityClearanceId) {
        this.securityClearanceId = securityClearanceId;
    }

    public LongFilter getApplicationUserId() {
        return applicationUserId;
    }

    public Optional<LongFilter> optionalApplicationUserId() {
        return Optional.ofNullable(applicationUserId);
    }

    public LongFilter applicationUserId() {
        if (applicationUserId == null) {
            setApplicationUserId(new LongFilter());
        }
        return applicationUserId;
    }

    public void setApplicationUserId(LongFilter applicationUserId) {
        this.applicationUserId = applicationUserId;
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

    public LongFilter getFiscalMonthId() {
        return fiscalMonthId;
    }

    public Optional<LongFilter> optionalFiscalMonthId() {
        return Optional.ofNullable(fiscalMonthId);
    }

    public LongFilter fiscalMonthId() {
        if (fiscalMonthId == null) {
            setFiscalMonthId(new LongFilter());
        }
        return fiscalMonthId;
    }

    public void setFiscalMonthId(LongFilter fiscalMonthId) {
        this.fiscalMonthId = fiscalMonthId;
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
        final PlaceholderCriteria that = (PlaceholderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(token, that.token) &&
            Objects.equals(containingPlaceholderId, that.containingPlaceholderId) &&
            Objects.equals(dealerId, that.dealerId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(securityClearanceId, that.securityClearanceId) &&
            Objects.equals(applicationUserId, that.applicationUserId) &&
            Objects.equals(fiscalYearId, that.fiscalYearId) &&
            Objects.equals(fiscalQuarterId, that.fiscalQuarterId) &&
            Objects.equals(fiscalMonthId, that.fiscalMonthId) &&
            Objects.equals(reportBatchId, that.reportBatchId) &&
            Objects.equals(moneyMarketListId, that.moneyMarketListId) &&
            Objects.equals(moneyMarketUploadNotificationId, that.moneyMarketUploadNotificationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            description,
            token,
            containingPlaceholderId,
            dealerId,
            placeholderId,
            securityClearanceId,
            applicationUserId,
            fiscalYearId,
            fiscalQuarterId,
            fiscalMonthId,
            reportBatchId,
            moneyMarketListId,
            moneyMarketUploadNotificationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlaceholderCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalToken().map(f -> "token=" + f + ", ").orElse("") +
            optionalContainingPlaceholderId().map(f -> "containingPlaceholderId=" + f + ", ").orElse("") +
            optionalDealerId().map(f -> "dealerId=" + f + ", ").orElse("") +
            optionalPlaceholderId().map(f -> "placeholderId=" + f + ", ").orElse("") +
            optionalSecurityClearanceId().map(f -> "securityClearanceId=" + f + ", ").orElse("") +
            optionalApplicationUserId().map(f -> "applicationUserId=" + f + ", ").orElse("") +
            optionalFiscalYearId().map(f -> "fiscalYearId=" + f + ", ").orElse("") +
            optionalFiscalQuarterId().map(f -> "fiscalQuarterId=" + f + ", ").orElse("") +
            optionalFiscalMonthId().map(f -> "fiscalMonthId=" + f + ", ").orElse("") +
            optionalReportBatchId().map(f -> "reportBatchId=" + f + ", ").orElse("") +
            optionalMoneyMarketListId().map(f -> "moneyMarketListId=" + f + ", ").orElse("") +
            optionalMoneyMarketUploadNotificationId().map(f -> "moneyMarketUploadNotificationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
