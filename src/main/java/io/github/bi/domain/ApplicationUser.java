package io.github.bi.domain;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ApplicationUser.
 */
@Entity
@Table(name = "application_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "applicationuser")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApplicationUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "designation", nullable = false, unique = true)
    private UUID designation;

    @NotNull
    @Column(name = "application_identity", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String applicationIdentity;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "dealerGroup", "placeholders", "applicationUser" }, allowSetters = true)
    private Dealer organization;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "dealerGroup", "placeholders", "applicationUser" }, allowSetters = true)
    private Dealer department;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private SecurityClearance securityClearance;

    @JsonIgnoreProperties(value = { "dealerGroup", "placeholders", "applicationUser" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Dealer dealerIdentity;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_application_user__placeholder",
        joinColumns = @JoinColumn(name = "application_user_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "containingPlaceholder",
            "dealers",
            "placeholders",
            "securityClearances",
            "applicationUsers",
            "fiscalYears",
            "fiscalQuarters",
            "fiscalMonths",
            "reportBatches",
            "moneyMarketLists",
            "moneyMarketUploadNotifications",
        },
        allowSetters = true
    )
    private Set<Placeholder> placeholders = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "uploadedBy")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = { "uploadedBy", "placeholders", "moneyMarketList", "moneyMarketUploadNotifications" },
        allowSetters = true
    )
    private Set<ReportBatch> reportBatches = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "uploadedBy")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = { "placeholders", "uploadedBy", "reportBatch", "moneyMarketDeals", "moneyMarketUploadNotifications" },
        allowSetters = true
    )
    private Set<MoneyMarketList> moneyMarketLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApplicationUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getDesignation() {
        return this.designation;
    }

    public ApplicationUser designation(UUID designation) {
        this.setDesignation(designation);
        return this;
    }

    public void setDesignation(UUID designation) {
        this.designation = designation;
    }

    public String getApplicationIdentity() {
        return this.applicationIdentity;
    }

    public ApplicationUser applicationIdentity(String applicationIdentity) {
        this.setApplicationIdentity(applicationIdentity);
        return this;
    }

    public void setApplicationIdentity(String applicationIdentity) {
        this.applicationIdentity = applicationIdentity;
    }

    public Dealer getOrganization() {
        return this.organization;
    }

    public void setOrganization(Dealer dealer) {
        this.organization = dealer;
    }

    public ApplicationUser organization(Dealer dealer) {
        this.setOrganization(dealer);
        return this;
    }

    public Dealer getDepartment() {
        return this.department;
    }

    public void setDepartment(Dealer dealer) {
        this.department = dealer;
    }

    public ApplicationUser department(Dealer dealer) {
        this.setDepartment(dealer);
        return this;
    }

    public SecurityClearance getSecurityClearance() {
        return this.securityClearance;
    }

    public void setSecurityClearance(SecurityClearance securityClearance) {
        this.securityClearance = securityClearance;
    }

    public ApplicationUser securityClearance(SecurityClearance securityClearance) {
        this.setSecurityClearance(securityClearance);
        return this;
    }

    public Dealer getDealerIdentity() {
        return this.dealerIdentity;
    }

    public void setDealerIdentity(Dealer dealer) {
        this.dealerIdentity = dealer;
    }

    public ApplicationUser dealerIdentity(Dealer dealer) {
        this.setDealerIdentity(dealer);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public ApplicationUser placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public ApplicationUser addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public ApplicationUser removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public Set<ReportBatch> getReportBatches() {
        return this.reportBatches;
    }

    public void setReportBatches(Set<ReportBatch> reportBatches) {
        if (this.reportBatches != null) {
            this.reportBatches.forEach(i -> i.setUploadedBy(null));
        }
        if (reportBatches != null) {
            reportBatches.forEach(i -> i.setUploadedBy(this));
        }
        this.reportBatches = reportBatches;
    }

    public ApplicationUser reportBatches(Set<ReportBatch> reportBatches) {
        this.setReportBatches(reportBatches);
        return this;
    }

    public ApplicationUser addReportBatch(ReportBatch reportBatch) {
        this.reportBatches.add(reportBatch);
        reportBatch.setUploadedBy(this);
        return this;
    }

    public ApplicationUser removeReportBatch(ReportBatch reportBatch) {
        this.reportBatches.remove(reportBatch);
        reportBatch.setUploadedBy(null);
        return this;
    }

    public Set<MoneyMarketList> getMoneyMarketLists() {
        return this.moneyMarketLists;
    }

    public void setMoneyMarketLists(Set<MoneyMarketList> moneyMarketLists) {
        if (this.moneyMarketLists != null) {
            this.moneyMarketLists.forEach(i -> i.setUploadedBy(null));
        }
        if (moneyMarketLists != null) {
            moneyMarketLists.forEach(i -> i.setUploadedBy(this));
        }
        this.moneyMarketLists = moneyMarketLists;
    }

    public ApplicationUser moneyMarketLists(Set<MoneyMarketList> moneyMarketLists) {
        this.setMoneyMarketLists(moneyMarketLists);
        return this;
    }

    public ApplicationUser addMoneyMarketList(MoneyMarketList moneyMarketList) {
        this.moneyMarketLists.add(moneyMarketList);
        moneyMarketList.setUploadedBy(this);
        return this;
    }

    public ApplicationUser removeMoneyMarketList(MoneyMarketList moneyMarketList) {
        this.moneyMarketLists.remove(moneyMarketList);
        moneyMarketList.setUploadedBy(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationUser)) {
            return false;
        }
        return getId() != null && getId().equals(((ApplicationUser) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUser{" +
            "id=" + getId() +
            ", designation='" + getDesignation() + "'" +
            ", applicationIdentity='" + getApplicationIdentity() + "'" +
            "}";
    }
}
