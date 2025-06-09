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

class FiscalMonthCriteriaTest {

    @Test
    void newFiscalMonthCriteriaHasAllFiltersNullTest() {
        var fiscalMonthCriteria = new FiscalMonthCriteria();
        assertThat(fiscalMonthCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void fiscalMonthCriteriaFluentMethodsCreatesFiltersTest() {
        var fiscalMonthCriteria = new FiscalMonthCriteria();

        setAllFilters(fiscalMonthCriteria);

        assertThat(fiscalMonthCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void fiscalMonthCriteriaCopyCreatesNullFilterTest() {
        var fiscalMonthCriteria = new FiscalMonthCriteria();
        var copy = fiscalMonthCriteria.copy();

        assertThat(fiscalMonthCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(fiscalMonthCriteria)
        );
    }

    @Test
    void fiscalMonthCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var fiscalMonthCriteria = new FiscalMonthCriteria();
        setAllFilters(fiscalMonthCriteria);

        var copy = fiscalMonthCriteria.copy();

        assertThat(fiscalMonthCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(fiscalMonthCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var fiscalMonthCriteria = new FiscalMonthCriteria();

        assertThat(fiscalMonthCriteria).hasToString("FiscalMonthCriteria{}");
    }

    private static void setAllFilters(FiscalMonthCriteria fiscalMonthCriteria) {
        fiscalMonthCriteria.id();
        fiscalMonthCriteria.monthNumber();
        fiscalMonthCriteria.startDate();
        fiscalMonthCriteria.endDate();
        fiscalMonthCriteria.fiscalMonthCode();
        fiscalMonthCriteria.fiscalYearId();
        fiscalMonthCriteria.placeholderId();
        fiscalMonthCriteria.fiscalQuarterId();
        fiscalMonthCriteria.distinct();
    }

    private static Condition<FiscalMonthCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getMonthNumber()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getFiscalMonthCode()) &&
                condition.apply(criteria.getFiscalYearId()) &&
                condition.apply(criteria.getPlaceholderId()) &&
                condition.apply(criteria.getFiscalQuarterId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FiscalMonthCriteria> copyFiltersAre(FiscalMonthCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getMonthNumber(), copy.getMonthNumber()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getFiscalMonthCode(), copy.getFiscalMonthCode()) &&
                condition.apply(criteria.getFiscalYearId(), copy.getFiscalYearId()) &&
                condition.apply(criteria.getPlaceholderId(), copy.getPlaceholderId()) &&
                condition.apply(criteria.getFiscalQuarterId(), copy.getFiscalQuarterId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
