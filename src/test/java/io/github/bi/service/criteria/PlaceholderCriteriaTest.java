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

class PlaceholderCriteriaTest {

    @Test
    void newPlaceholderCriteriaHasAllFiltersNullTest() {
        var placeholderCriteria = new PlaceholderCriteria();
        assertThat(placeholderCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void placeholderCriteriaFluentMethodsCreatesFiltersTest() {
        var placeholderCriteria = new PlaceholderCriteria();

        setAllFilters(placeholderCriteria);

        assertThat(placeholderCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void placeholderCriteriaCopyCreatesNullFilterTest() {
        var placeholderCriteria = new PlaceholderCriteria();
        var copy = placeholderCriteria.copy();

        assertThat(placeholderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(placeholderCriteria)
        );
    }

    @Test
    void placeholderCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var placeholderCriteria = new PlaceholderCriteria();
        setAllFilters(placeholderCriteria);

        var copy = placeholderCriteria.copy();

        assertThat(placeholderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(placeholderCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var placeholderCriteria = new PlaceholderCriteria();

        assertThat(placeholderCriteria).hasToString("PlaceholderCriteria{}");
    }

    private static void setAllFilters(PlaceholderCriteria placeholderCriteria) {
        placeholderCriteria.id();
        placeholderCriteria.description();
        placeholderCriteria.token();
        placeholderCriteria.containingPlaceholderId();
        placeholderCriteria.dealerId();
        placeholderCriteria.placeholderId();
        placeholderCriteria.securityClearanceId();
        placeholderCriteria.applicationUserId();
        placeholderCriteria.fiscalYearId();
        placeholderCriteria.fiscalQuarterId();
        placeholderCriteria.fiscalMonthId();
        placeholderCriteria.reportBatchId();
        placeholderCriteria.moneyMarketListId();
        placeholderCriteria.moneyMarketUploadNotificationId();
        placeholderCriteria.distinct();
    }

    private static Condition<PlaceholderCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getToken()) &&
                condition.apply(criteria.getContainingPlaceholderId()) &&
                condition.apply(criteria.getDealerId()) &&
                condition.apply(criteria.getPlaceholderId()) &&
                condition.apply(criteria.getSecurityClearanceId()) &&
                condition.apply(criteria.getApplicationUserId()) &&
                condition.apply(criteria.getFiscalYearId()) &&
                condition.apply(criteria.getFiscalQuarterId()) &&
                condition.apply(criteria.getFiscalMonthId()) &&
                condition.apply(criteria.getReportBatchId()) &&
                condition.apply(criteria.getMoneyMarketListId()) &&
                condition.apply(criteria.getMoneyMarketUploadNotificationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PlaceholderCriteria> copyFiltersAre(PlaceholderCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getToken(), copy.getToken()) &&
                condition.apply(criteria.getContainingPlaceholderId(), copy.getContainingPlaceholderId()) &&
                condition.apply(criteria.getDealerId(), copy.getDealerId()) &&
                condition.apply(criteria.getPlaceholderId(), copy.getPlaceholderId()) &&
                condition.apply(criteria.getSecurityClearanceId(), copy.getSecurityClearanceId()) &&
                condition.apply(criteria.getApplicationUserId(), copy.getApplicationUserId()) &&
                condition.apply(criteria.getFiscalYearId(), copy.getFiscalYearId()) &&
                condition.apply(criteria.getFiscalQuarterId(), copy.getFiscalQuarterId()) &&
                condition.apply(criteria.getFiscalMonthId(), copy.getFiscalMonthId()) &&
                condition.apply(criteria.getReportBatchId(), copy.getReportBatchId()) &&
                condition.apply(criteria.getMoneyMarketListId(), copy.getMoneyMarketListId()) &&
                condition.apply(criteria.getMoneyMarketUploadNotificationId(), copy.getMoneyMarketUploadNotificationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
