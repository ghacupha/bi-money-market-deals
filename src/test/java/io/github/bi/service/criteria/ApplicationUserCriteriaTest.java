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

class ApplicationUserCriteriaTest {

    @Test
    void newApplicationUserCriteriaHasAllFiltersNullTest() {
        var applicationUserCriteria = new ApplicationUserCriteria();
        assertThat(applicationUserCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void applicationUserCriteriaFluentMethodsCreatesFiltersTest() {
        var applicationUserCriteria = new ApplicationUserCriteria();

        setAllFilters(applicationUserCriteria);

        assertThat(applicationUserCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void applicationUserCriteriaCopyCreatesNullFilterTest() {
        var applicationUserCriteria = new ApplicationUserCriteria();
        var copy = applicationUserCriteria.copy();

        assertThat(applicationUserCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(applicationUserCriteria)
        );
    }

    @Test
    void applicationUserCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var applicationUserCriteria = new ApplicationUserCriteria();
        setAllFilters(applicationUserCriteria);

        var copy = applicationUserCriteria.copy();

        assertThat(applicationUserCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(applicationUserCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var applicationUserCriteria = new ApplicationUserCriteria();

        assertThat(applicationUserCriteria).hasToString("ApplicationUserCriteria{}");
    }

    private static void setAllFilters(ApplicationUserCriteria applicationUserCriteria) {
        applicationUserCriteria.id();
        applicationUserCriteria.designation();
        applicationUserCriteria.applicationIdentity();
        applicationUserCriteria.organizationId();
        applicationUserCriteria.departmentId();
        applicationUserCriteria.securityClearanceId();
        applicationUserCriteria.dealerIdentityId();
        applicationUserCriteria.placeholderId();
        applicationUserCriteria.reportBatchId();
        applicationUserCriteria.moneyMarketListId();
        applicationUserCriteria.distinct();
    }

    private static Condition<ApplicationUserCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDesignation()) &&
                condition.apply(criteria.getApplicationIdentity()) &&
                condition.apply(criteria.getOrganizationId()) &&
                condition.apply(criteria.getDepartmentId()) &&
                condition.apply(criteria.getSecurityClearanceId()) &&
                condition.apply(criteria.getDealerIdentityId()) &&
                condition.apply(criteria.getPlaceholderId()) &&
                condition.apply(criteria.getReportBatchId()) &&
                condition.apply(criteria.getMoneyMarketListId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ApplicationUserCriteria> copyFiltersAre(
        ApplicationUserCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDesignation(), copy.getDesignation()) &&
                condition.apply(criteria.getApplicationIdentity(), copy.getApplicationIdentity()) &&
                condition.apply(criteria.getOrganizationId(), copy.getOrganizationId()) &&
                condition.apply(criteria.getDepartmentId(), copy.getDepartmentId()) &&
                condition.apply(criteria.getSecurityClearanceId(), copy.getSecurityClearanceId()) &&
                condition.apply(criteria.getDealerIdentityId(), copy.getDealerIdentityId()) &&
                condition.apply(criteria.getPlaceholderId(), copy.getPlaceholderId()) &&
                condition.apply(criteria.getReportBatchId(), copy.getReportBatchId()) &&
                condition.apply(criteria.getMoneyMarketListId(), copy.getMoneyMarketListId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
