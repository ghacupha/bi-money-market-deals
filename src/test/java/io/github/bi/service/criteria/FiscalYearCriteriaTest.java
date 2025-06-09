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

class FiscalYearCriteriaTest {

    @Test
    void newFiscalYearCriteriaHasAllFiltersNullTest() {
        var fiscalYearCriteria = new FiscalYearCriteria();
        assertThat(fiscalYearCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void fiscalYearCriteriaFluentMethodsCreatesFiltersTest() {
        var fiscalYearCriteria = new FiscalYearCriteria();

        setAllFilters(fiscalYearCriteria);

        assertThat(fiscalYearCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void fiscalYearCriteriaCopyCreatesNullFilterTest() {
        var fiscalYearCriteria = new FiscalYearCriteria();
        var copy = fiscalYearCriteria.copy();

        assertThat(fiscalYearCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(fiscalYearCriteria)
        );
    }

    @Test
    void fiscalYearCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var fiscalYearCriteria = new FiscalYearCriteria();
        setAllFilters(fiscalYearCriteria);

        var copy = fiscalYearCriteria.copy();

        assertThat(fiscalYearCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(fiscalYearCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var fiscalYearCriteria = new FiscalYearCriteria();

        assertThat(fiscalYearCriteria).hasToString("FiscalYearCriteria{}");
    }

    private static void setAllFilters(FiscalYearCriteria fiscalYearCriteria) {
        fiscalYearCriteria.id();
        fiscalYearCriteria.fiscalYearCode();
        fiscalYearCriteria.startDate();
        fiscalYearCriteria.endDate();
        fiscalYearCriteria.fiscalYearStatus();
        fiscalYearCriteria.placeholderId();
        fiscalYearCriteria.createdById();
        fiscalYearCriteria.lastUpdatedById();
        fiscalYearCriteria.distinct();
    }

    private static Condition<FiscalYearCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFiscalYearCode()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getFiscalYearStatus()) &&
                condition.apply(criteria.getPlaceholderId()) &&
                condition.apply(criteria.getCreatedById()) &&
                condition.apply(criteria.getLastUpdatedById()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FiscalYearCriteria> copyFiltersAre(FiscalYearCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFiscalYearCode(), copy.getFiscalYearCode()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getFiscalYearStatus(), copy.getFiscalYearStatus()) &&
                condition.apply(criteria.getPlaceholderId(), copy.getPlaceholderId()) &&
                condition.apply(criteria.getCreatedById(), copy.getCreatedById()) &&
                condition.apply(criteria.getLastUpdatedById(), copy.getLastUpdatedById()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
