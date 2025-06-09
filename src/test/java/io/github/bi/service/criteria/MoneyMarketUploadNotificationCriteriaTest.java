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

class MoneyMarketUploadNotificationCriteriaTest {

    @Test
    void newMoneyMarketUploadNotificationCriteriaHasAllFiltersNullTest() {
        var moneyMarketUploadNotificationCriteria = new MoneyMarketUploadNotificationCriteria();
        assertThat(moneyMarketUploadNotificationCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void moneyMarketUploadNotificationCriteriaFluentMethodsCreatesFiltersTest() {
        var moneyMarketUploadNotificationCriteria = new MoneyMarketUploadNotificationCriteria();

        setAllFilters(moneyMarketUploadNotificationCriteria);

        assertThat(moneyMarketUploadNotificationCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void moneyMarketUploadNotificationCriteriaCopyCreatesNullFilterTest() {
        var moneyMarketUploadNotificationCriteria = new MoneyMarketUploadNotificationCriteria();
        var copy = moneyMarketUploadNotificationCriteria.copy();

        assertThat(moneyMarketUploadNotificationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(moneyMarketUploadNotificationCriteria)
        );
    }

    @Test
    void moneyMarketUploadNotificationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var moneyMarketUploadNotificationCriteria = new MoneyMarketUploadNotificationCriteria();
        setAllFilters(moneyMarketUploadNotificationCriteria);

        var copy = moneyMarketUploadNotificationCriteria.copy();

        assertThat(moneyMarketUploadNotificationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(moneyMarketUploadNotificationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var moneyMarketUploadNotificationCriteria = new MoneyMarketUploadNotificationCriteria();

        assertThat(moneyMarketUploadNotificationCriteria).hasToString("MoneyMarketUploadNotificationCriteria{}");
    }

    private static void setAllFilters(MoneyMarketUploadNotificationCriteria moneyMarketUploadNotificationCriteria) {
        moneyMarketUploadNotificationCriteria.id();
        moneyMarketUploadNotificationCriteria.rowNumber();
        moneyMarketUploadNotificationCriteria.referenceNumber();
        moneyMarketUploadNotificationCriteria.moneyMarketListId();
        moneyMarketUploadNotificationCriteria.reportBatchId();
        moneyMarketUploadNotificationCriteria.placeholderId();
        moneyMarketUploadNotificationCriteria.distinct();
    }

    private static Condition<MoneyMarketUploadNotificationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getRowNumber()) &&
                condition.apply(criteria.getReferenceNumber()) &&
                condition.apply(criteria.getMoneyMarketListId()) &&
                condition.apply(criteria.getReportBatchId()) &&
                condition.apply(criteria.getPlaceholderId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MoneyMarketUploadNotificationCriteria> copyFiltersAre(
        MoneyMarketUploadNotificationCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getRowNumber(), copy.getRowNumber()) &&
                condition.apply(criteria.getReferenceNumber(), copy.getReferenceNumber()) &&
                condition.apply(criteria.getMoneyMarketListId(), copy.getMoneyMarketListId()) &&
                condition.apply(criteria.getReportBatchId(), copy.getReportBatchId()) &&
                condition.apply(criteria.getPlaceholderId(), copy.getPlaceholderId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
