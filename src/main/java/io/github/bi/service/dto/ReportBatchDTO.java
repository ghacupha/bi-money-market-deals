package io.github.bi.service.dto;

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
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link io.github.bi.domain.ReportBatch} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportBatchDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate reportDate;

    @NotNull
    private ZonedDateTime uploadTimeStamp;

    @NotNull
    private reportBatchStatus status;

    @NotNull
    private Boolean active;

    @NotNull
    private String description;

    @NotNull
    private UUID fileIdentifier;

    private FileProcessFlag processFlag;

    @Lob
    private byte[] csvFileAttachment;

    private String csvFileAttachmentContentType;

    @NotNull
    private ApplicationUserDTO uploadedBy;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public ZonedDateTime getUploadTimeStamp() {
        return uploadTimeStamp;
    }

    public void setUploadTimeStamp(ZonedDateTime uploadTimeStamp) {
        this.uploadTimeStamp = uploadTimeStamp;
    }

    public reportBatchStatus getStatus() {
        return status;
    }

    public void setStatus(reportBatchStatus status) {
        this.status = status;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getFileIdentifier() {
        return fileIdentifier;
    }

    public void setFileIdentifier(UUID fileIdentifier) {
        this.fileIdentifier = fileIdentifier;
    }

    public FileProcessFlag getProcessFlag() {
        return processFlag;
    }

    public void setProcessFlag(FileProcessFlag processFlag) {
        this.processFlag = processFlag;
    }

    public byte[] getCsvFileAttachment() {
        return csvFileAttachment;
    }

    public void setCsvFileAttachment(byte[] csvFileAttachment) {
        this.csvFileAttachment = csvFileAttachment;
    }

    public String getCsvFileAttachmentContentType() {
        return csvFileAttachmentContentType;
    }

    public void setCsvFileAttachmentContentType(String csvFileAttachmentContentType) {
        this.csvFileAttachmentContentType = csvFileAttachmentContentType;
    }

    public ApplicationUserDTO getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(ApplicationUserDTO uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportBatchDTO)) {
            return false;
        }

        ReportBatchDTO reportBatchDTO = (ReportBatchDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportBatchDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportBatchDTO{" +
            "id=" + getId() +
            ", reportDate='" + getReportDate() + "'" +
            ", uploadTimeStamp='" + getUploadTimeStamp() + "'" +
            ", status='" + getStatus() + "'" +
            ", active='" + getActive() + "'" +
            ", description='" + getDescription() + "'" +
            ", fileIdentifier='" + getFileIdentifier() + "'" +
            ", processFlag='" + getProcessFlag() + "'" +
            ", csvFileAttachment='" + getCsvFileAttachment() + "'" +
            ", uploadedBy=" + getUploadedBy() +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
