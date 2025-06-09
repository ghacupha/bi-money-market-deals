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
 * Criteria class for the {@link io.github.bi.domain.SecurityClearance} entity. This class is used
 * in {@link io.github.bi.web.rest.SecurityClearanceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /security-clearances?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SecurityClearanceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter clearanceLevel;

    private IntegerFilter level;

    private LongFilter placeholderId;

    private Boolean distinct;

    public SecurityClearanceCriteria() {}

    public SecurityClearanceCriteria(SecurityClearanceCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.clearanceLevel = other.optionalClearanceLevel().map(StringFilter::copy).orElse(null);
        this.level = other.optionalLevel().map(IntegerFilter::copy).orElse(null);
        this.placeholderId = other.optionalPlaceholderId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SecurityClearanceCriteria copy() {
        return new SecurityClearanceCriteria(this);
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

    public StringFilter getClearanceLevel() {
        return clearanceLevel;
    }

    public Optional<StringFilter> optionalClearanceLevel() {
        return Optional.ofNullable(clearanceLevel);
    }

    public StringFilter clearanceLevel() {
        if (clearanceLevel == null) {
            setClearanceLevel(new StringFilter());
        }
        return clearanceLevel;
    }

    public void setClearanceLevel(StringFilter clearanceLevel) {
        this.clearanceLevel = clearanceLevel;
    }

    public IntegerFilter getLevel() {
        return level;
    }

    public Optional<IntegerFilter> optionalLevel() {
        return Optional.ofNullable(level);
    }

    public IntegerFilter level() {
        if (level == null) {
            setLevel(new IntegerFilter());
        }
        return level;
    }

    public void setLevel(IntegerFilter level) {
        this.level = level;
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
        final SecurityClearanceCriteria that = (SecurityClearanceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(clearanceLevel, that.clearanceLevel) &&
            Objects.equals(level, that.level) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clearanceLevel, level, placeholderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SecurityClearanceCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalClearanceLevel().map(f -> "clearanceLevel=" + f + ", ").orElse("") +
            optionalLevel().map(f -> "level=" + f + ", ").orElse("") +
            optionalPlaceholderId().map(f -> "placeholderId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
