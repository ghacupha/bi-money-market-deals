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
 * Criteria class for the {@link io.github.bi.domain.ApplicationUser} entity. This class is used
 * in {@link io.github.bi.web.rest.ApplicationUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /application-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApplicationUserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter designation;

    private StringFilter applicationIdentity;

    private LongFilter organizationId;

    private LongFilter departmentId;

    private LongFilter securityClearanceId;

    private LongFilter dealerIdentityId;

    private LongFilter placeholderId;

    private LongFilter reportBatchId;

    private LongFilter moneyMarketListId;

    private Boolean distinct;

    public ApplicationUserCriteria() {}

    public ApplicationUserCriteria(ApplicationUserCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.designation = other.optionalDesignation().map(UUIDFilter::copy).orElse(null);
        this.applicationIdentity = other.optionalApplicationIdentity().map(StringFilter::copy).orElse(null);
        this.organizationId = other.optionalOrganizationId().map(LongFilter::copy).orElse(null);
        this.departmentId = other.optionalDepartmentId().map(LongFilter::copy).orElse(null);
        this.securityClearanceId = other.optionalSecurityClearanceId().map(LongFilter::copy).orElse(null);
        this.dealerIdentityId = other.optionalDealerIdentityId().map(LongFilter::copy).orElse(null);
        this.placeholderId = other.optionalPlaceholderId().map(LongFilter::copy).orElse(null);
        this.reportBatchId = other.optionalReportBatchId().map(LongFilter::copy).orElse(null);
        this.moneyMarketListId = other.optionalMoneyMarketListId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ApplicationUserCriteria copy() {
        return new ApplicationUserCriteria(this);
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

    public UUIDFilter getDesignation() {
        return designation;
    }

    public Optional<UUIDFilter> optionalDesignation() {
        return Optional.ofNullable(designation);
    }

    public UUIDFilter designation() {
        if (designation == null) {
            setDesignation(new UUIDFilter());
        }
        return designation;
    }

    public void setDesignation(UUIDFilter designation) {
        this.designation = designation;
    }

    public StringFilter getApplicationIdentity() {
        return applicationIdentity;
    }

    public Optional<StringFilter> optionalApplicationIdentity() {
        return Optional.ofNullable(applicationIdentity);
    }

    public StringFilter applicationIdentity() {
        if (applicationIdentity == null) {
            setApplicationIdentity(new StringFilter());
        }
        return applicationIdentity;
    }

    public void setApplicationIdentity(StringFilter applicationIdentity) {
        this.applicationIdentity = applicationIdentity;
    }

    public LongFilter getOrganizationId() {
        return organizationId;
    }

    public Optional<LongFilter> optionalOrganizationId() {
        return Optional.ofNullable(organizationId);
    }

    public LongFilter organizationId() {
        if (organizationId == null) {
            setOrganizationId(new LongFilter());
        }
        return organizationId;
    }

    public void setOrganizationId(LongFilter organizationId) {
        this.organizationId = organizationId;
    }

    public LongFilter getDepartmentId() {
        return departmentId;
    }

    public Optional<LongFilter> optionalDepartmentId() {
        return Optional.ofNullable(departmentId);
    }

    public LongFilter departmentId() {
        if (departmentId == null) {
            setDepartmentId(new LongFilter());
        }
        return departmentId;
    }

    public void setDepartmentId(LongFilter departmentId) {
        this.departmentId = departmentId;
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

    public LongFilter getDealerIdentityId() {
        return dealerIdentityId;
    }

    public Optional<LongFilter> optionalDealerIdentityId() {
        return Optional.ofNullable(dealerIdentityId);
    }

    public LongFilter dealerIdentityId() {
        if (dealerIdentityId == null) {
            setDealerIdentityId(new LongFilter());
        }
        return dealerIdentityId;
    }

    public void setDealerIdentityId(LongFilter dealerIdentityId) {
        this.dealerIdentityId = dealerIdentityId;
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
        final ApplicationUserCriteria that = (ApplicationUserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(designation, that.designation) &&
            Objects.equals(applicationIdentity, that.applicationIdentity) &&
            Objects.equals(organizationId, that.organizationId) &&
            Objects.equals(departmentId, that.departmentId) &&
            Objects.equals(securityClearanceId, that.securityClearanceId) &&
            Objects.equals(dealerIdentityId, that.dealerIdentityId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(reportBatchId, that.reportBatchId) &&
            Objects.equals(moneyMarketListId, that.moneyMarketListId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            designation,
            applicationIdentity,
            organizationId,
            departmentId,
            securityClearanceId,
            dealerIdentityId,
            placeholderId,
            reportBatchId,
            moneyMarketListId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUserCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDesignation().map(f -> "designation=" + f + ", ").orElse("") +
            optionalApplicationIdentity().map(f -> "applicationIdentity=" + f + ", ").orElse("") +
            optionalOrganizationId().map(f -> "organizationId=" + f + ", ").orElse("") +
            optionalDepartmentId().map(f -> "departmentId=" + f + ", ").orElse("") +
            optionalSecurityClearanceId().map(f -> "securityClearanceId=" + f + ", ").orElse("") +
            optionalDealerIdentityId().map(f -> "dealerIdentityId=" + f + ", ").orElse("") +
            optionalPlaceholderId().map(f -> "placeholderId=" + f + ", ").orElse("") +
            optionalReportBatchId().map(f -> "reportBatchId=" + f + ", ").orElse("") +
            optionalMoneyMarketListId().map(f -> "moneyMarketListId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
