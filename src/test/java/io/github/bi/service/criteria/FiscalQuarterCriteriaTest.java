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

class FiscalQuarterCriteriaTest {

    @Test
    void newFiscalQuarterCriteriaHasAllFiltersNullTest() {
        var fiscalQuarterCriteria = new FiscalQuarterCriteria();
        assertThat(fiscalQuarterCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void fiscalQuarterCriteriaFluentMethodsCreatesFiltersTest() {
        var fiscalQuarterCriteria = new FiscalQuarterCriteria();

        setAllFilters(fiscalQuarterCriteria);

        assertThat(fiscalQuarterCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void fiscalQuarterCriteriaCopyCreatesNullFilterTest() {
        var fiscalQuarterCriteria = new FiscalQuarterCriteria();
        var copy = fiscalQuarterCriteria.copy();

        assertThat(fiscalQuarterCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(fiscalQuarterCriteria)
        );
    }

    @Test
    void fiscalQuarterCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var fiscalQuarterCriteria = new FiscalQuarterCriteria();
        setAllFilters(fiscalQuarterCriteria);

        var copy = fiscalQuarterCriteria.copy();

        assertThat(fiscalQuarterCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(fiscalQuarterCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var fiscalQuarterCriteria = new FiscalQuarterCriteria();

        assertThat(fiscalQuarterCriteria).hasToString("FiscalQuarterCriteria{}");
    }

    private static void setAllFilters(FiscalQuarterCriteria fiscalQuarterCriteria) {
        fiscalQuarterCriteria.id();
        fiscalQuarterCriteria.quarterNumber();
        fiscalQuarterCriteria.startDate();
        fiscalQuarterCriteria.endDate();
        fiscalQuarterCriteria.fiscalQuarterCode();
        fiscalQuarterCriteria.fiscalYearId();
        fiscalQuarterCriteria.placeholderId();
        fiscalQuarterCriteria.distinct();
    }

    private static Condition<FiscalQuarterCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getQuarterNumber()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getFiscalQuarterCode()) &&
                condition.apply(criteria.getFiscalYearId()) &&
                condition.apply(criteria.getPlaceholderId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FiscalQuarterCriteria> copyFiltersAre(
        FiscalQuarterCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getQuarterNumber(), copy.getQuarterNumber()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getFiscalQuarterCode(), copy.getFiscalQuarterCode()) &&
                condition.apply(criteria.getFiscalYearId(), copy.getFiscalYearId()) &&
                condition.apply(criteria.getPlaceholderId(), copy.getPlaceholderId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
