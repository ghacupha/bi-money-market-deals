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

class MoneyMarketListCriteriaTest {

    @Test
    void newMoneyMarketListCriteriaHasAllFiltersNullTest() {
        var moneyMarketListCriteria = new MoneyMarketListCriteria();
        assertThat(moneyMarketListCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void moneyMarketListCriteriaFluentMethodsCreatesFiltersTest() {
        var moneyMarketListCriteria = new MoneyMarketListCriteria();

        setAllFilters(moneyMarketListCriteria);

        assertThat(moneyMarketListCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void moneyMarketListCriteriaCopyCreatesNullFilterTest() {
        var moneyMarketListCriteria = new MoneyMarketListCriteria();
        var copy = moneyMarketListCriteria.copy();

        assertThat(moneyMarketListCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(moneyMarketListCriteria)
        );
    }

    @Test
    void moneyMarketListCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var moneyMarketListCriteria = new MoneyMarketListCriteria();
        setAllFilters(moneyMarketListCriteria);

        var copy = moneyMarketListCriteria.copy();

        assertThat(moneyMarketListCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(moneyMarketListCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var moneyMarketListCriteria = new MoneyMarketListCriteria();

        assertThat(moneyMarketListCriteria).hasToString("MoneyMarketListCriteria{}");
    }

    private static void setAllFilters(MoneyMarketListCriteria moneyMarketListCriteria) {
        moneyMarketListCriteria.id();
        moneyMarketListCriteria.reportDate();
        moneyMarketListCriteria.uploadTimeStamp();
        moneyMarketListCriteria.status();
        moneyMarketListCriteria.description();
        moneyMarketListCriteria.active();
        moneyMarketListCriteria.placeholderId();
        moneyMarketListCriteria.moneyMarketDealId();
        moneyMarketListCriteria.distinct();
    }

    private static Condition<MoneyMarketListCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getReportDate()) &&
                condition.apply(criteria.getUploadTimeStamp()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getPlaceholderId()) &&
                condition.apply(criteria.getMoneyMarketDealId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MoneyMarketListCriteria> copyFiltersAre(
        MoneyMarketListCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getReportDate(), copy.getReportDate()) &&
                condition.apply(criteria.getUploadTimeStamp(), copy.getUploadTimeStamp()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getPlaceholderId(), copy.getPlaceholderId()) &&
                condition.apply(criteria.getMoneyMarketDealId(), copy.getMoneyMarketDealId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
