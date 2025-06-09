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
import io.github.bi.domain.enumeration.FiscalYearStatusType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FiscalYear.
 */
@Entity
@Table(name = "fiscal_year")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "fiscalyear")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FiscalYear implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fiscal_year_code", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String fiscalYearCode;

    @NotNull
    @Column(name = "start_date", nullable = false, unique = true)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false, unique = true)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "fiscal_year_status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private FiscalYearStatusType fiscalYearStatus;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_fiscal_year__placeholder",
        joinColumns = @JoinColumn(name = "fiscal_year_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "containingPlaceholder", "placeholders", "fiscalYears", "fiscalQuarters", "fiscalMonths", "moneyMarketLists" },
        allowSetters = true
    )
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FiscalYear id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFiscalYearCode() {
        return this.fiscalYearCode;
    }

    public FiscalYear fiscalYearCode(String fiscalYearCode) {
        this.setFiscalYearCode(fiscalYearCode);
        return this;
    }

    public void setFiscalYearCode(String fiscalYearCode) {
        this.fiscalYearCode = fiscalYearCode;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public FiscalYear startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public FiscalYear endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public FiscalYearStatusType getFiscalYearStatus() {
        return this.fiscalYearStatus;
    }

    public FiscalYear fiscalYearStatus(FiscalYearStatusType fiscalYearStatus) {
        this.setFiscalYearStatus(fiscalYearStatus);
        return this;
    }

    public void setFiscalYearStatus(FiscalYearStatusType fiscalYearStatus) {
        this.fiscalYearStatus = fiscalYearStatus;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public FiscalYear placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public FiscalYear addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public FiscalYear removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FiscalYear)) {
            return false;
        }
        return getId() != null && getId().equals(((FiscalYear) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FiscalYear{" +
            "id=" + getId() +
            ", fiscalYearCode='" + getFiscalYearCode() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", fiscalYearStatus='" + getFiscalYearStatus() + "'" +
            "}";
    }
}
