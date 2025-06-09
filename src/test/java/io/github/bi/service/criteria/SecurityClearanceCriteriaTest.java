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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SecurityClearanceCriteriaTest {

    @Test
    void newSecurityClearanceCriteriaHasAllFiltersNullTest() {
        var securityClearanceCriteria = new SecurityClearanceCriteria();
        assertThat(securityClearanceCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void securityClearanceCriteriaFluentMethodsCreatesFiltersTest() {
        var securityClearanceCriteria = new SecurityClearanceCriteria();

        setAllFilters(securityClearanceCriteria);

        assertThat(securityClearanceCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void securityClearanceCriteriaCopyCreatesNullFilterTest() {
        var securityClearanceCriteria = new SecurityClearanceCriteria();
        var copy = securityClearanceCriteria.copy();

        assertThat(securityClearanceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(securityClearanceCriteria)
        );
    }

    @Test
    void securityClearanceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var securityClearanceCriteria = new SecurityClearanceCriteria();
        setAllFilters(securityClearanceCriteria);

        var copy = securityClearanceCriteria.copy();

        assertThat(securityClearanceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(securityClearanceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var securityClearanceCriteria = new SecurityClearanceCriteria();

        assertThat(securityClearanceCriteria).hasToString("SecurityClearanceCriteria{}");
    }

    private static void setAllFilters(SecurityClearanceCriteria securityClearanceCriteria) {
        securityClearanceCriteria.id();
        securityClearanceCriteria.clearanceLevel();
        securityClearanceCriteria.level();
        securityClearanceCriteria.placeholderId();
        securityClearanceCriteria.distinct();
    }

    private static Condition<SecurityClearanceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getClearanceLevel()) &&
                condition.apply(criteria.getLevel()) &&
                condition.apply(criteria.getPlaceholderId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SecurityClearanceCriteria> copyFiltersAre(
        SecurityClearanceCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getClearanceLevel(), copy.getClearanceLevel()) &&
                condition.apply(criteria.getLevel(), copy.getLevel()) &&
                condition.apply(criteria.getPlaceholderId(), copy.getPlaceholderId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
