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
import io.github.bi.domain.enumeration.FileProcessFlag;
import io.github.bi.domain.enumeration.reportBatchStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReportBatch.
 */
@Entity
@Table(name = "report_batch")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "reportbatch")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportBatch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
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

    @NotNull
    @Column(name = "active", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean active;

    @NotNull
    @Column(name = "description", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @NotNull
    @Column(name = "file_identifier", nullable = false, unique = true)
    private UUID fileIdentifier;

    @Enumerated(EnumType.STRING)
    @Column(name = "process_flag")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private FileProcessFlag processFlag;

    @Lob
    @Column(name = "csv_file_attachment", nullable = false)
    private byte[] csvFileAttachment;

    @NotNull
    @Column(name = "csv_file_attachment_content_type", nullable = false)
    private String csvFileAttachmentContentType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "organization", "department", "securityClearance", "dealerIdentity", "placeholders", "reportBatches", "moneyMarketLists",
        },
        allowSetters = true
    )
    private ApplicationUser uploadedBy;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_report_batch__placeholder",
        joinColumns = @JoinColumn(name = "report_batch_id"),
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

    @JsonIgnoreProperties(
        value = { "placeholders", "uploadedBy", "reportBatch", "moneyMarketDeals", "moneyMarketUploadNotifications" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "reportBatch")
    @org.springframework.data.annotation.Transient
    private MoneyMarketList moneyMarketList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reportBatch")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "moneyMarketList", "reportBatch", "placeholders" }, allowSetters = true)
    private Set<MoneyMarketUploadNotification> moneyMarketUploadNotifications = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReportBatch id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReportDate() {
        return this.reportDate;
    }

    public ReportBatch reportDate(LocalDate reportDate) {
        this.setReportDate(reportDate);
        return this;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public ZonedDateTime getUploadTimeStamp() {
        return this.uploadTimeStamp;
    }

    public ReportBatch uploadTimeStamp(ZonedDateTime uploadTimeStamp) {
        this.setUploadTimeStamp(uploadTimeStamp);
        return this;
    }

    public void setUploadTimeStamp(ZonedDateTime uploadTimeStamp) {
        this.uploadTimeStamp = uploadTimeStamp;
    }

    public reportBatchStatus getStatus() {
        return this.status;
    }

    public ReportBatch status(reportBatchStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(reportBatchStatus status) {
        this.status = status;
    }

    public Boolean getActive() {
        return this.active;
    }

    public ReportBatch active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return this.description;
    }

    public ReportBatch description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getFileIdentifier() {
        return this.fileIdentifier;
    }

    public ReportBatch fileIdentifier(UUID fileIdentifier) {
        this.setFileIdentifier(fileIdentifier);
        return this;
    }

    public void setFileIdentifier(UUID fileIdentifier) {
        this.fileIdentifier = fileIdentifier;
    }

    public FileProcessFlag getProcessFlag() {
        return this.processFlag;
    }

    public ReportBatch processFlag(FileProcessFlag processFlag) {
        this.setProcessFlag(processFlag);
        return this;
    }

    public void setProcessFlag(FileProcessFlag processFlag) {
        this.processFlag = processFlag;
    }

    public byte[] getCsvFileAttachment() {
        return this.csvFileAttachment;
    }

    public ReportBatch csvFileAttachment(byte[] csvFileAttachment) {
        this.setCsvFileAttachment(csvFileAttachment);
        return this;
    }

    public void setCsvFileAttachment(byte[] csvFileAttachment) {
        this.csvFileAttachment = csvFileAttachment;
    }

    public String getCsvFileAttachmentContentType() {
        return this.csvFileAttachmentContentType;
    }

    public ReportBatch csvFileAttachmentContentType(String csvFileAttachmentContentType) {
        this.csvFileAttachmentContentType = csvFileAttachmentContentType;
        return this;
    }

    public void setCsvFileAttachmentContentType(String csvFileAttachmentContentType) {
        this.csvFileAttachmentContentType = csvFileAttachmentContentType;
    }

    public ApplicationUser getUploadedBy() {
        return this.uploadedBy;
    }

    public void setUploadedBy(ApplicationUser applicationUser) {
        this.uploadedBy = applicationUser;
    }

    public ReportBatch uploadedBy(ApplicationUser applicationUser) {
        this.setUploadedBy(applicationUser);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public ReportBatch placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public ReportBatch addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public ReportBatch removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public MoneyMarketList getMoneyMarketList() {
        return this.moneyMarketList;
    }

    public void setMoneyMarketList(MoneyMarketList moneyMarketList) {
        if (this.moneyMarketList != null) {
            this.moneyMarketList.setReportBatch(null);
        }
        if (moneyMarketList != null) {
            moneyMarketList.setReportBatch(this);
        }
        this.moneyMarketList = moneyMarketList;
    }

    public ReportBatch moneyMarketList(MoneyMarketList moneyMarketList) {
        this.setMoneyMarketList(moneyMarketList);
        return this;
    }

    public Set<MoneyMarketUploadNotification> getMoneyMarketUploadNotifications() {
        return this.moneyMarketUploadNotifications;
    }

    public void setMoneyMarketUploadNotifications(Set<MoneyMarketUploadNotification> moneyMarketUploadNotifications) {
        if (this.moneyMarketUploadNotifications != null) {
            this.moneyMarketUploadNotifications.forEach(i -> i.setReportBatch(null));
        }
        if (moneyMarketUploadNotifications != null) {
            moneyMarketUploadNotifications.forEach(i -> i.setReportBatch(this));
        }
        this.moneyMarketUploadNotifications = moneyMarketUploadNotifications;
    }

    public ReportBatch moneyMarketUploadNotifications(Set<MoneyMarketUploadNotification> moneyMarketUploadNotifications) {
        this.setMoneyMarketUploadNotifications(moneyMarketUploadNotifications);
        return this;
    }

    public ReportBatch addMoneyMarketUploadNotification(MoneyMarketUploadNotification moneyMarketUploadNotification) {
        this.moneyMarketUploadNotifications.add(moneyMarketUploadNotification);
        moneyMarketUploadNotification.setReportBatch(this);
        return this;
    }

    public ReportBatch removeMoneyMarketUploadNotification(MoneyMarketUploadNotification moneyMarketUploadNotification) {
        this.moneyMarketUploadNotifications.remove(moneyMarketUploadNotification);
        moneyMarketUploadNotification.setReportBatch(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportBatch)) {
            return false;
        }
        return getId() != null && getId().equals(((ReportBatch) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportBatch{" +
            "id=" + getId() +
            ", reportDate='" + getReportDate() + "'" +
            ", uploadTimeStamp='" + getUploadTimeStamp() + "'" +
            ", status='" + getStatus() + "'" +
            ", active='" + getActive() + "'" +
            ", description='" + getDescription() + "'" +
            ", fileIdentifier='" + getFileIdentifier() + "'" +
            ", processFlag='" + getProcessFlag() + "'" +
            ", csvFileAttachment='" + getCsvFileAttachment() + "'" +
            ", csvFileAttachmentContentType='" + getCsvFileAttachmentContentType() + "'" +
            "}";
    }
}
