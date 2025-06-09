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
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Placeholder.
 */
@Entity
@Table(name = "placeholder")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "placeholder")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Placeholder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @Column(name = "token", unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "containingPlaceholder", "placeholders", "fiscalYears", "fiscalQuarters", "fiscalMonths", "moneyMarketLists" },
        allowSetters = true
    )
    private Placeholder containingPlaceholder;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "containingPlaceholder")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = { "containingPlaceholder", "placeholders", "fiscalYears", "fiscalQuarters", "fiscalMonths", "moneyMarketLists" },
        allowSetters = true
    )
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "placeholders")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private Set<FiscalYear> fiscalYears = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "placeholders")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "fiscalYear", "placeholders" }, allowSetters = true)
    private Set<FiscalQuarter> fiscalQuarters = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "placeholders")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "fiscalYear", "placeholders", "fiscalQuarter" }, allowSetters = true)
    private Set<FiscalMonth> fiscalMonths = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "placeholders")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "placeholders", "moneyMarketDeals" }, allowSetters = true)
    private Set<MoneyMarketList> moneyMarketLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Placeholder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Placeholder description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getToken() {
        return this.token;
    }

    public Placeholder token(String token) {
        this.setToken(token);
        return this;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Placeholder getContainingPlaceholder() {
        return this.containingPlaceholder;
    }

    public void setContainingPlaceholder(Placeholder placeholder) {
        this.containingPlaceholder = placeholder;
    }

    public Placeholder containingPlaceholder(Placeholder placeholder) {
        this.setContainingPlaceholder(placeholder);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        if (this.placeholders != null) {
            this.placeholders.forEach(i -> i.setContainingPlaceholder(null));
        }
        if (placeholders != null) {
            placeholders.forEach(i -> i.setContainingPlaceholder(this));
        }
        this.placeholders = placeholders;
    }

    public Placeholder placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public Placeholder addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        placeholder.setContainingPlaceholder(this);
        return this;
    }

    public Placeholder removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        placeholder.setContainingPlaceholder(null);
        return this;
    }

    public Set<FiscalYear> getFiscalYears() {
        return this.fiscalYears;
    }

    public void setFiscalYears(Set<FiscalYear> fiscalYears) {
        if (this.fiscalYears != null) {
            this.fiscalYears.forEach(i -> i.removePlaceholder(this));
        }
        if (fiscalYears != null) {
            fiscalYears.forEach(i -> i.addPlaceholder(this));
        }
        this.fiscalYears = fiscalYears;
    }

    public Placeholder fiscalYears(Set<FiscalYear> fiscalYears) {
        this.setFiscalYears(fiscalYears);
        return this;
    }

    public Placeholder addFiscalYear(FiscalYear fiscalYear) {
        this.fiscalYears.add(fiscalYear);
        fiscalYear.getPlaceholders().add(this);
        return this;
    }

    public Placeholder removeFiscalYear(FiscalYear fiscalYear) {
        this.fiscalYears.remove(fiscalYear);
        fiscalYear.getPlaceholders().remove(this);
        return this;
    }

    public Set<FiscalQuarter> getFiscalQuarters() {
        return this.fiscalQuarters;
    }

    public void setFiscalQuarters(Set<FiscalQuarter> fiscalQuarters) {
        if (this.fiscalQuarters != null) {
            this.fiscalQuarters.forEach(i -> i.removePlaceholder(this));
        }
        if (fiscalQuarters != null) {
            fiscalQuarters.forEach(i -> i.addPlaceholder(this));
        }
        this.fiscalQuarters = fiscalQuarters;
    }

    public Placeholder fiscalQuarters(Set<FiscalQuarter> fiscalQuarters) {
        this.setFiscalQuarters(fiscalQuarters);
        return this;
    }

    public Placeholder addFiscalQuarter(FiscalQuarter fiscalQuarter) {
        this.fiscalQuarters.add(fiscalQuarter);
        fiscalQuarter.getPlaceholders().add(this);
        return this;
    }

    public Placeholder removeFiscalQuarter(FiscalQuarter fiscalQuarter) {
        this.fiscalQuarters.remove(fiscalQuarter);
        fiscalQuarter.getPlaceholders().remove(this);
        return this;
    }

    public Set<FiscalMonth> getFiscalMonths() {
        return this.fiscalMonths;
    }

    public void setFiscalMonths(Set<FiscalMonth> fiscalMonths) {
        if (this.fiscalMonths != null) {
            this.fiscalMonths.forEach(i -> i.removePlaceholder(this));
        }
        if (fiscalMonths != null) {
            fiscalMonths.forEach(i -> i.addPlaceholder(this));
        }
        this.fiscalMonths = fiscalMonths;
    }

    public Placeholder fiscalMonths(Set<FiscalMonth> fiscalMonths) {
        this.setFiscalMonths(fiscalMonths);
        return this;
    }

    public Placeholder addFiscalMonth(FiscalMonth fiscalMonth) {
        this.fiscalMonths.add(fiscalMonth);
        fiscalMonth.getPlaceholders().add(this);
        return this;
    }

    public Placeholder removeFiscalMonth(FiscalMonth fiscalMonth) {
        this.fiscalMonths.remove(fiscalMonth);
        fiscalMonth.getPlaceholders().remove(this);
        return this;
    }

    public Set<MoneyMarketList> getMoneyMarketLists() {
        return this.moneyMarketLists;
    }

    public void setMoneyMarketLists(Set<MoneyMarketList> moneyMarketLists) {
        if (this.moneyMarketLists != null) {
            this.moneyMarketLists.forEach(i -> i.removePlaceholder(this));
        }
        if (moneyMarketLists != null) {
            moneyMarketLists.forEach(i -> i.addPlaceholder(this));
        }
        this.moneyMarketLists = moneyMarketLists;
    }

    public Placeholder moneyMarketLists(Set<MoneyMarketList> moneyMarketLists) {
        this.setMoneyMarketLists(moneyMarketLists);
        return this;
    }

    public Placeholder addMoneyMarketList(MoneyMarketList moneyMarketList) {
        this.moneyMarketLists.add(moneyMarketList);
        moneyMarketList.getPlaceholders().add(this);
        return this;
    }

    public Placeholder removeMoneyMarketList(MoneyMarketList moneyMarketList) {
        this.moneyMarketLists.remove(moneyMarketList);
        moneyMarketList.getPlaceholders().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Placeholder)) {
            return false;
        }
        return getId() != null && getId().equals(((Placeholder) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Placeholder{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", token='" + getToken() + "'" +
            "}";
    }
}
