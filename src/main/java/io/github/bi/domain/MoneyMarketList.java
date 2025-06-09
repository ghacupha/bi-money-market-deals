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
import io.github.bi.domain.enumeration.reportBatchStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MoneyMarketList.
 */
@Entity
@Table(name = "money_market_list")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "moneymarketlist")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MoneyMarketList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "report_date", nullable = false)
    private LocalDate reportDate;

    @NotNull
    @Column(name = "upload_time_stamp", nullable = false)
    private ZonedDateTime uploadTimeStamp;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private reportBatchStatus status;

    @Column(name = "description", unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @NotNull
    @Column(name = "active", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean active;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_money_market_list__placeholder",
        joinColumns = @JoinColumn(name = "money_market_list_id"),
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

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "organization", "department", "securityClearance", "dealerIdentity", "placeholders", "reportBatches", "moneyMarketLists",
        },
        allowSetters = true
    )
    private ApplicationUser uploadedBy;

    @JsonIgnoreProperties(
        value = { "uploadedBy", "placeholders", "moneyMarketList", "moneyMarketUploadNotifications" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @MapsId
    @JoinColumn(name = "id")
    private ReportBatch reportBatch;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "moneyMarketList")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "moneyMarketList" }, allowSetters = true)
    private Set<MoneyMarketDeal> moneyMarketDeals = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "moneyMarketList")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "moneyMarketList", "reportBatch", "placeholders" }, allowSetters = true)
    private Set<MoneyMarketUploadNotification> moneyMarketUploadNotifications = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MoneyMarketList id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReportDate() {
        return this.reportDate;
    }

    public MoneyMarketList reportDate(LocalDate reportDate) {
        this.setReportDate(reportDate);
        return this;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public ZonedDateTime getUploadTimeStamp() {
        return this.uploadTimeStamp;
    }

    public MoneyMarketList uploadTimeStamp(ZonedDateTime uploadTimeStamp) {
        this.setUploadTimeStamp(uploadTimeStamp);
        return this;
    }

    public void setUploadTimeStamp(ZonedDateTime uploadTimeStamp) {
        this.uploadTimeStamp = uploadTimeStamp;
    }

    public reportBatchStatus getStatus() {
        return this.status;
    }

    public MoneyMarketList status(reportBatchStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(reportBatchStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return this.description;
    }

    public MoneyMarketList description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return this.active;
    }

    public MoneyMarketList active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public MoneyMarketList placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public MoneyMarketList addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public MoneyMarketList removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public ApplicationUser getUploadedBy() {
        return this.uploadedBy;
    }

    public void setUploadedBy(ApplicationUser applicationUser) {
        this.uploadedBy = applicationUser;
    }

    public MoneyMarketList uploadedBy(ApplicationUser applicationUser) {
        this.setUploadedBy(applicationUser);
        return this;
    }

    public ReportBatch getReportBatch() {
        return this.reportBatch;
    }

    public void setReportBatch(ReportBatch reportBatch) {
        this.reportBatch = reportBatch;
    }

    public MoneyMarketList reportBatch(ReportBatch reportBatch) {
        this.setReportBatch(reportBatch);
        return this;
    }

    public Set<MoneyMarketDeal> getMoneyMarketDeals() {
        return this.moneyMarketDeals;
    }

    public void setMoneyMarketDeals(Set<MoneyMarketDeal> moneyMarketDeals) {
        if (this.moneyMarketDeals != null) {
            this.moneyMarketDeals.forEach(i -> i.setMoneyMarketList(null));
        }
        if (moneyMarketDeals != null) {
            moneyMarketDeals.forEach(i -> i.setMoneyMarketList(this));
        }
        this.moneyMarketDeals = moneyMarketDeals;
    }

    public MoneyMarketList moneyMarketDeals(Set<MoneyMarketDeal> moneyMarketDeals) {
        this.setMoneyMarketDeals(moneyMarketDeals);
        return this;
    }

    public MoneyMarketList addMoneyMarketDeal(MoneyMarketDeal moneyMarketDeal) {
        this.moneyMarketDeals.add(moneyMarketDeal);
        moneyMarketDeal.setMoneyMarketList(this);
        return this;
    }

    public MoneyMarketList removeMoneyMarketDeal(MoneyMarketDeal moneyMarketDeal) {
        this.moneyMarketDeals.remove(moneyMarketDeal);
        moneyMarketDeal.setMoneyMarketList(null);
        return this;
    }

    public Set<MoneyMarketUploadNotification> getMoneyMarketUploadNotifications() {
        return this.moneyMarketUploadNotifications;
    }

    public void setMoneyMarketUploadNotifications(Set<MoneyMarketUploadNotification> moneyMarketUploadNotifications) {
        if (this.moneyMarketUploadNotifications != null) {
            this.moneyMarketUploadNotifications.forEach(i -> i.setMoneyMarketList(null));
        }
        if (moneyMarketUploadNotifications != null) {
            moneyMarketUploadNotifications.forEach(i -> i.setMoneyMarketList(this));
        }
        this.moneyMarketUploadNotifications = moneyMarketUploadNotifications;
    }

    public MoneyMarketList moneyMarketUploadNotifications(Set<MoneyMarketUploadNotification> moneyMarketUploadNotifications) {
        this.setMoneyMarketUploadNotifications(moneyMarketUploadNotifications);
        return this;
    }

    public MoneyMarketList addMoneyMarketUploadNotification(MoneyMarketUploadNotification moneyMarketUploadNotification) {
        this.moneyMarketUploadNotifications.add(moneyMarketUploadNotification);
        moneyMarketUploadNotification.setMoneyMarketList(this);
        return this;
    }

    public MoneyMarketList removeMoneyMarketUploadNotification(MoneyMarketUploadNotification moneyMarketUploadNotification) {
        this.moneyMarketUploadNotifications.remove(moneyMarketUploadNotification);
        moneyMarketUploadNotification.setMoneyMarketList(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MoneyMarketList)) {
            return false;
        }
        return getId() != null && getId().equals(((MoneyMarketList) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoneyMarketList{" +
            "id=" + getId() +
            ", reportDate='" + getReportDate() + "'" +
            ", uploadTimeStamp='" + getUploadTimeStamp() + "'" +
            ", status='" + getStatus() + "'" +
            ", description='" + getDescription() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
