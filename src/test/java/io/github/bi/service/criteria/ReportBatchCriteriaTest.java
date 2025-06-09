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

class ReportBatchCriteriaTest {

    @Test
    void newReportBatchCriteriaHasAllFiltersNullTest() {
        var reportBatchCriteria = new ReportBatchCriteria();
        assertThat(reportBatchCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void reportBatchCriteriaFluentMethodsCreatesFiltersTest() {
        var reportBatchCriteria = new ReportBatchCriteria();

        setAllFilters(reportBatchCriteria);

        assertThat(reportBatchCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void reportBatchCriteriaCopyCreatesNullFilterTest() {
        var reportBatchCriteria = new ReportBatchCriteria();
        var copy = reportBatchCriteria.copy();

        assertThat(reportBatchCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(reportBatchCriteria)
        );
    }

    @Test
    void reportBatchCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var reportBatchCriteria = new ReportBatchCriteria();
        setAllFilters(reportBatchCriteria);

        var copy = reportBatchCriteria.copy();

        assertThat(reportBatchCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(reportBatchCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var reportBatchCriteria = new ReportBatchCriteria();

        assertThat(reportBatchCriteria).hasToString("ReportBatchCriteria{}");
    }

    private static void setAllFilters(ReportBatchCriteria reportBatchCriteria) {
        reportBatchCriteria.id();
        reportBatchCriteria.reportDate();
        reportBatchCriteria.uploadTimeStamp();
        reportBatchCriteria.status();
        reportBatchCriteria.active();
        reportBatchCriteria.description();
        reportBatchCriteria.fileIdentifier();
        reportBatchCriteria.processFlag();
        reportBatchCriteria.uploadedById();
        reportBatchCriteria.placeholderId();
        reportBatchCriteria.moneyMarketListId();
        reportBatchCriteria.moneyMarketUploadNotificationId();
        reportBatchCriteria.distinct();
    }

    private static Condition<ReportBatchCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getReportDate()) &&
                condition.apply(criteria.getUploadTimeStamp()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getFileIdentifier()) &&
                condition.apply(criteria.getProcessFlag()) &&
                condition.apply(criteria.getUploadedById()) &&
                condition.apply(criteria.getPlaceholderId()) &&
                condition.apply(criteria.getMoneyMarketListId()) &&
                condition.apply(criteria.getMoneyMarketUploadNotificationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ReportBatchCriteria> copyFiltersAre(ReportBatchCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getReportDate(), copy.getReportDate()) &&
                condition.apply(criteria.getUploadTimeStamp(), copy.getUploadTimeStamp()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getFileIdentifier(), copy.getFileIdentifier()) &&
                condition.apply(criteria.getProcessFlag(), copy.getProcessFlag()) &&
                condition.apply(criteria.getUploadedById(), copy.getUploadedById()) &&
                condition.apply(criteria.getPlaceholderId(), copy.getPlaceholderId()) &&
                condition.apply(criteria.getMoneyMarketListId(), copy.getMoneyMarketListId()) &&
                condition.apply(criteria.getMoneyMarketUploadNotificationId(), copy.getMoneyMarketUploadNotificationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
