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
 * Criteria class for the {@link io.github.bi.domain.Dealer} entity. This class is used
 * in {@link io.github.bi.web.rest.DealerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /dealers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DealerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter dealerName;

    private StringFilter taxNumber;

    private StringFilter identificationDocumentNumber;

    private StringFilter organizationName;

    private StringFilter department;

    private StringFilter position;

    private StringFilter postalAddress;

    private StringFilter physicalAddress;

    private StringFilter accountName;

    private StringFilter accountNumber;

    private StringFilter bankersName;

    private StringFilter bankersBranch;

    private StringFilter bankersSwiftCode;

    private StringFilter fileUploadToken;

    private StringFilter compilationToken;

    private StringFilter otherNames;

    private LongFilter dealerGroupId;

    private LongFilter placeholderId;

    private LongFilter applicationUserId;

    private Boolean distinct;

    public DealerCriteria() {}

    public DealerCriteria(DealerCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.dealerName = other.optionalDealerName().map(StringFilter::copy).orElse(null);
        this.taxNumber = other.optionalTaxNumber().map(StringFilter::copy).orElse(null);
        this.identificationDocumentNumber = other.optionalIdentificationDocumentNumber().map(StringFilter::copy).orElse(null);
        this.organizationName = other.optionalOrganizationName().map(StringFilter::copy).orElse(null);
        this.department = other.optionalDepartment().map(StringFilter::copy).orElse(null);
        this.position = other.optionalPosition().map(StringFilter::copy).orElse(null);
        this.postalAddress = other.optionalPostalAddress().map(StringFilter::copy).orElse(null);
        this.physicalAddress = other.optionalPhysicalAddress().map(StringFilter::copy).orElse(null);
        this.accountName = other.optionalAccountName().map(StringFilter::copy).orElse(null);
        this.accountNumber = other.optionalAccountNumber().map(StringFilter::copy).orElse(null);
        this.bankersName = other.optionalBankersName().map(StringFilter::copy).orElse(null);
        this.bankersBranch = other.optionalBankersBranch().map(StringFilter::copy).orElse(null);
        this.bankersSwiftCode = other.optionalBankersSwiftCode().map(StringFilter::copy).orElse(null);
        this.fileUploadToken = other.optionalFileUploadToken().map(StringFilter::copy).orElse(null);
        this.compilationToken = other.optionalCompilationToken().map(StringFilter::copy).orElse(null);
        this.otherNames = other.optionalOtherNames().map(StringFilter::copy).orElse(null);
        this.dealerGroupId = other.optionalDealerGroupId().map(LongFilter::copy).orElse(null);
        this.placeholderId = other.optionalPlaceholderId().map(LongFilter::copy).orElse(null);
        this.applicationUserId = other.optionalApplicationUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DealerCriteria copy() {
        return new DealerCriteria(this);
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

    public StringFilter getDealerName() {
        return dealerName;
    }

    public Optional<StringFilter> optionalDealerName() {
        return Optional.ofNullable(dealerName);
    }

    public StringFilter dealerName() {
        if (dealerName == null) {
            setDealerName(new StringFilter());
        }
        return dealerName;
    }

    public void setDealerName(StringFilter dealerName) {
        this.dealerName = dealerName;
    }

    public StringFilter getTaxNumber() {
        return taxNumber;
    }

    public Optional<StringFilter> optionalTaxNumber() {
        return Optional.ofNullable(taxNumber);
    }

    public StringFilter taxNumber() {
        if (taxNumber == null) {
            setTaxNumber(new StringFilter());
        }
        return taxNumber;
    }

    public void setTaxNumber(StringFilter taxNumber) {
        this.taxNumber = taxNumber;
    }

    public StringFilter getIdentificationDocumentNumber() {
        return identificationDocumentNumber;
    }

    public Optional<StringFilter> optionalIdentificationDocumentNumber() {
        return Optional.ofNullable(identificationDocumentNumber);
    }

    public StringFilter identificationDocumentNumber() {
        if (identificationDocumentNumber == null) {
            setIdentificationDocumentNumber(new StringFilter());
        }
        return identificationDocumentNumber;
    }

    public void setIdentificationDocumentNumber(StringFilter identificationDocumentNumber) {
        this.identificationDocumentNumber = identificationDocumentNumber;
    }

    public StringFilter getOrganizationName() {
        return organizationName;
    }

    public Optional<StringFilter> optionalOrganizationName() {
        return Optional.ofNullable(organizationName);
    }

    public StringFilter organizationName() {
        if (organizationName == null) {
            setOrganizationName(new StringFilter());
        }
        return organizationName;
    }

    public void setOrganizationName(StringFilter organizationName) {
        this.organizationName = organizationName;
    }

    public StringFilter getDepartment() {
        return department;
    }

    public Optional<StringFilter> optionalDepartment() {
        return Optional.ofNullable(department);
    }

    public StringFilter department() {
        if (department == null) {
            setDepartment(new StringFilter());
        }
        return department;
    }

    public void setDepartment(StringFilter department) {
        this.department = department;
    }

    public StringFilter getPosition() {
        return position;
    }

    public Optional<StringFilter> optionalPosition() {
        return Optional.ofNullable(position);
    }

    public StringFilter position() {
        if (position == null) {
            setPosition(new StringFilter());
        }
        return position;
    }

    public void setPosition(StringFilter position) {
        this.position = position;
    }

    public StringFilter getPostalAddress() {
        return postalAddress;
    }

    public Optional<StringFilter> optionalPostalAddress() {
        return Optional.ofNullable(postalAddress);
    }

    public StringFilter postalAddress() {
        if (postalAddress == null) {
            setPostalAddress(new StringFilter());
        }
        return postalAddress;
    }

    public void setPostalAddress(StringFilter postalAddress) {
        this.postalAddress = postalAddress;
    }

    public StringFilter getPhysicalAddress() {
        return physicalAddress;
    }

    public Optional<StringFilter> optionalPhysicalAddress() {
        return Optional.ofNullable(physicalAddress);
    }

    public StringFilter physicalAddress() {
        if (physicalAddress == null) {
            setPhysicalAddress(new StringFilter());
        }
        return physicalAddress;
    }

    public void setPhysicalAddress(StringFilter physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public StringFilter getAccountName() {
        return accountName;
    }

    public Optional<StringFilter> optionalAccountName() {
        return Optional.ofNullable(accountName);
    }

    public StringFilter accountName() {
        if (accountName == null) {
            setAccountName(new StringFilter());
        }
        return accountName;
    }

    public void setAccountName(StringFilter accountName) {
        this.accountName = accountName;
    }

    public StringFilter getAccountNumber() {
        return accountNumber;
    }

    public Optional<StringFilter> optionalAccountNumber() {
        return Optional.ofNullable(accountNumber);
    }

    public StringFilter accountNumber() {
        if (accountNumber == null) {
            setAccountNumber(new StringFilter());
        }
        return accountNumber;
    }

    public void setAccountNumber(StringFilter accountNumber) {
        this.accountNumber = accountNumber;
    }

    public StringFilter getBankersName() {
        return bankersName;
    }

    public Optional<StringFilter> optionalBankersName() {
        return Optional.ofNullable(bankersName);
    }

    public StringFilter bankersName() {
        if (bankersName == null) {
            setBankersName(new StringFilter());
        }
        return bankersName;
    }

    public void setBankersName(StringFilter bankersName) {
        this.bankersName = bankersName;
    }

    public StringFilter getBankersBranch() {
        return bankersBranch;
    }

    public Optional<StringFilter> optionalBankersBranch() {
        return Optional.ofNullable(bankersBranch);
    }

    public StringFilter bankersBranch() {
        if (bankersBranch == null) {
            setBankersBranch(new StringFilter());
        }
        return bankersBranch;
    }

    public void setBankersBranch(StringFilter bankersBranch) {
        this.bankersBranch = bankersBranch;
    }

    public StringFilter getBankersSwiftCode() {
        return bankersSwiftCode;
    }

    public Optional<StringFilter> optionalBankersSwiftCode() {
        return Optional.ofNullable(bankersSwiftCode);
    }

    public StringFilter bankersSwiftCode() {
        if (bankersSwiftCode == null) {
            setBankersSwiftCode(new StringFilter());
        }
        return bankersSwiftCode;
    }

    public void setBankersSwiftCode(StringFilter bankersSwiftCode) {
        this.bankersSwiftCode = bankersSwiftCode;
    }

    public StringFilter getFileUploadToken() {
        return fileUploadToken;
    }

    public Optional<StringFilter> optionalFileUploadToken() {
        return Optional.ofNullable(fileUploadToken);
    }

    public StringFilter fileUploadToken() {
        if (fileUploadToken == null) {
            setFileUploadToken(new StringFilter());
        }
        return fileUploadToken;
    }

    public void setFileUploadToken(StringFilter fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public StringFilter getCompilationToken() {
        return compilationToken;
    }

    public Optional<StringFilter> optionalCompilationToken() {
        return Optional.ofNullable(compilationToken);
    }

    public StringFilter compilationToken() {
        if (compilationToken == null) {
            setCompilationToken(new StringFilter());
        }
        return compilationToken;
    }

    public void setCompilationToken(StringFilter compilationToken) {
        this.compilationToken = compilationToken;
    }

    public StringFilter getOtherNames() {
        return otherNames;
    }

    public Optional<StringFilter> optionalOtherNames() {
        return Optional.ofNullable(otherNames);
    }

    public StringFilter otherNames() {
        if (otherNames == null) {
            setOtherNames(new StringFilter());
        }
        return otherNames;
    }

    public void setOtherNames(StringFilter otherNames) {
        this.otherNames = otherNames;
    }

    public LongFilter getDealerGroupId() {
        return dealerGroupId;
    }

    public Optional<LongFilter> optionalDealerGroupId() {
        return Optional.ofNullable(dealerGroupId);
    }

    public LongFilter dealerGroupId() {
        if (dealerGroupId == null) {
            setDealerGroupId(new LongFilter());
        }
        return dealerGroupId;
    }

    public void setDealerGroupId(LongFilter dealerGroupId) {
        this.dealerGroupId = dealerGroupId;
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
        final DealerCriteria that = (DealerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dealerName, that.dealerName) &&
            Objects.equals(taxNumber, that.taxNumber) &&
            Objects.equals(identificationDocumentNumber, that.identificationDocumentNumber) &&
            Objects.equals(organizationName, that.organizationName) &&
            Objects.equals(department, that.department) &&
            Objects.equals(position, that.position) &&
            Objects.equals(postalAddress, that.postalAddress) &&
            Objects.equals(physicalAddress, that.physicalAddress) &&
            Objects.equals(accountName, that.accountName) &&
            Objects.equals(accountNumber, that.accountNumber) &&
            Objects.equals(bankersName, that.bankersName) &&
            Objects.equals(bankersBranch, that.bankersBranch) &&
            Objects.equals(bankersSwiftCode, that.bankersSwiftCode) &&
            Objects.equals(fileUploadToken, that.fileUploadToken) &&
            Objects.equals(compilationToken, that.compilationToken) &&
            Objects.equals(otherNames, that.otherNames) &&
            Objects.equals(dealerGroupId, that.dealerGroupId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(applicationUserId, that.applicationUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            dealerName,
            taxNumber,
            identificationDocumentNumber,
            organizationName,
            department,
            position,
            postalAddress,
            physicalAddress,
            accountName,
            accountNumber,
            bankersName,
            bankersBranch,
            bankersSwiftCode,
            fileUploadToken,
            compilationToken,
            otherNames,
            dealerGroupId,
            placeholderId,
            applicationUserId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DealerCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDealerName().map(f -> "dealerName=" + f + ", ").orElse("") +
            optionalTaxNumber().map(f -> "taxNumber=" + f + ", ").orElse("") +
            optionalIdentificationDocumentNumber().map(f -> "identificationDocumentNumber=" + f + ", ").orElse("") +
            optionalOrganizationName().map(f -> "organizationName=" + f + ", ").orElse("") +
            optionalDepartment().map(f -> "department=" + f + ", ").orElse("") +
            optionalPosition().map(f -> "position=" + f + ", ").orElse("") +
            optionalPostalAddress().map(f -> "postalAddress=" + f + ", ").orElse("") +
            optionalPhysicalAddress().map(f -> "physicalAddress=" + f + ", ").orElse("") +
            optionalAccountName().map(f -> "accountName=" + f + ", ").orElse("") +
            optionalAccountNumber().map(f -> "accountNumber=" + f + ", ").orElse("") +
            optionalBankersName().map(f -> "bankersName=" + f + ", ").orElse("") +
            optionalBankersBranch().map(f -> "bankersBranch=" + f + ", ").orElse("") +
            optionalBankersSwiftCode().map(f -> "bankersSwiftCode=" + f + ", ").orElse("") +
            optionalFileUploadToken().map(f -> "fileUploadToken=" + f + ", ").orElse("") +
            optionalCompilationToken().map(f -> "compilationToken=" + f + ", ").orElse("") +
            optionalOtherNames().map(f -> "otherNames=" + f + ", ").orElse("") +
            optionalDealerGroupId().map(f -> "dealerGroupId=" + f + ", ").orElse("") +
            optionalPlaceholderId().map(f -> "placeholderId=" + f + ", ").orElse("") +
            optionalApplicationUserId().map(f -> "applicationUserId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
