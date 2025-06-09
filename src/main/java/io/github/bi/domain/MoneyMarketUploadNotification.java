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
 * A MoneyMarketUploadNotification.
 */
@Entity
@Table(name = "money_market_upload_notification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "moneymarketuploadnotification")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MoneyMarketUploadNotification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "error_message")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String errorMessage;

    @Column(name = "row_number")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer rowNumber;

    @NotNull
    @Column(name = "reference_number", nullable = false, unique = true)
    private UUID referenceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "placeholders", "uploadedBy", "reportBatch", "moneyMarketDeals", "moneyMarketUploadNotifications" },
        allowSetters = true
    )
    private MoneyMarketList moneyMarketList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "uploadedBy", "placeholders", "moneyMarketList", "moneyMarketUploadNotifications" },
        allowSetters = true
    )
    private ReportBatch reportBatch;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_money_market_upload_notification__placeholder",
        joinColumns = @JoinColumn(name = "money_market_upload_notification_id"),
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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MoneyMarketUploadNotification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public MoneyMarketUploadNotification errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getRowNumber() {
        return this.rowNumber;
    }

    public MoneyMarketUploadNotification rowNumber(Integer rowNumber) {
        this.setRowNumber(rowNumber);
        return this;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public UUID getReferenceNumber() {
        return this.referenceNumber;
    }

    public MoneyMarketUploadNotification referenceNumber(UUID referenceNumber) {
        this.setReferenceNumber(referenceNumber);
        return this;
    }

    public void setReferenceNumber(UUID referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public MoneyMarketList getMoneyMarketList() {
        return this.moneyMarketList;
    }

    public void setMoneyMarketList(MoneyMarketList moneyMarketList) {
        this.moneyMarketList = moneyMarketList;
    }

    public MoneyMarketUploadNotification moneyMarketList(MoneyMarketList moneyMarketList) {
        this.setMoneyMarketList(moneyMarketList);
        return this;
    }

    public ReportBatch getReportBatch() {
        return this.reportBatch;
    }

    public void setReportBatch(ReportBatch reportBatch) {
        this.reportBatch = reportBatch;
    }

    public MoneyMarketUploadNotification reportBatch(ReportBatch reportBatch) {
        this.setReportBatch(reportBatch);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public MoneyMarketUploadNotification placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public MoneyMarketUploadNotification addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public MoneyMarketUploadNotification removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MoneyMarketUploadNotification)) {
            return false;
        }
        return getId() != null && getId().equals(((MoneyMarketUploadNotification) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoneyMarketUploadNotification{" +
            "id=" + getId() +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", rowNumber=" + getRowNumber() +
            ", referenceNumber='" + getReferenceNumber() + "'" +
            "}";
    }
}
