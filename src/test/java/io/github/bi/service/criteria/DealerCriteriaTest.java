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

class DealerCriteriaTest {

    @Test
    void newDealerCriteriaHasAllFiltersNullTest() {
        var dealerCriteria = new DealerCriteria();
        assertThat(dealerCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void dealerCriteriaFluentMethodsCreatesFiltersTest() {
        var dealerCriteria = new DealerCriteria();

        setAllFilters(dealerCriteria);

        assertThat(dealerCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void dealerCriteriaCopyCreatesNullFilterTest() {
        var dealerCriteria = new DealerCriteria();
        var copy = dealerCriteria.copy();

        assertThat(dealerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(dealerCriteria)
        );
    }

    @Test
    void dealerCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var dealerCriteria = new DealerCriteria();
        setAllFilters(dealerCriteria);

        var copy = dealerCriteria.copy();

        assertThat(dealerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(dealerCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var dealerCriteria = new DealerCriteria();

        assertThat(dealerCriteria).hasToString("DealerCriteria{}");
    }

    private static void setAllFilters(DealerCriteria dealerCriteria) {
        dealerCriteria.id();
        dealerCriteria.dealerName();
        dealerCriteria.taxNumber();
        dealerCriteria.identificationDocumentNumber();
        dealerCriteria.organizationName();
        dealerCriteria.department();
        dealerCriteria.position();
        dealerCriteria.postalAddress();
        dealerCriteria.physicalAddress();
        dealerCriteria.accountName();
        dealerCriteria.accountNumber();
        dealerCriteria.bankersName();
        dealerCriteria.bankersBranch();
        dealerCriteria.bankersSwiftCode();
        dealerCriteria.fileUploadToken();
        dealerCriteria.compilationToken();
        dealerCriteria.otherNames();
        dealerCriteria.dealerGroupId();
        dealerCriteria.placeholderId();
        dealerCriteria.applicationUserId();
        dealerCriteria.distinct();
    }

    private static Condition<DealerCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDealerName()) &&
                condition.apply(criteria.getTaxNumber()) &&
                condition.apply(criteria.getIdentificationDocumentNumber()) &&
                condition.apply(criteria.getOrganizationName()) &&
                condition.apply(criteria.getDepartment()) &&
                condition.apply(criteria.getPosition()) &&
                condition.apply(criteria.getPostalAddress()) &&
                condition.apply(criteria.getPhysicalAddress()) &&
                condition.apply(criteria.getAccountName()) &&
                condition.apply(criteria.getAccountNumber()) &&
                condition.apply(criteria.getBankersName()) &&
                condition.apply(criteria.getBankersBranch()) &&
                condition.apply(criteria.getBankersSwiftCode()) &&
                condition.apply(criteria.getFileUploadToken()) &&
                condition.apply(criteria.getCompilationToken()) &&
                condition.apply(criteria.getOtherNames()) &&
                condition.apply(criteria.getDealerGroupId()) &&
                condition.apply(criteria.getPlaceholderId()) &&
                condition.apply(criteria.getApplicationUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DealerCriteria> copyFiltersAre(DealerCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDealerName(), copy.getDealerName()) &&
                condition.apply(criteria.getTaxNumber(), copy.getTaxNumber()) &&
                condition.apply(criteria.getIdentificationDocumentNumber(), copy.getIdentificationDocumentNumber()) &&
                condition.apply(criteria.getOrganizationName(), copy.getOrganizationName()) &&
                condition.apply(criteria.getDepartment(), copy.getDepartment()) &&
                condition.apply(criteria.getPosition(), copy.getPosition()) &&
                condition.apply(criteria.getPostalAddress(), copy.getPostalAddress()) &&
                condition.apply(criteria.getPhysicalAddress(), copy.getPhysicalAddress()) &&
                condition.apply(criteria.getAccountName(), copy.getAccountName()) &&
                condition.apply(criteria.getAccountNumber(), copy.getAccountNumber()) &&
                condition.apply(criteria.getBankersName(), copy.getBankersName()) &&
                condition.apply(criteria.getBankersBranch(), copy.getBankersBranch()) &&
                condition.apply(criteria.getBankersSwiftCode(), copy.getBankersSwiftCode()) &&
                condition.apply(criteria.getFileUploadToken(), copy.getFileUploadToken()) &&
                condition.apply(criteria.getCompilationToken(), copy.getCompilationToken()) &&
                condition.apply(criteria.getOtherNames(), copy.getOtherNames()) &&
                condition.apply(criteria.getDealerGroupId(), copy.getDealerGroupId()) &&
                condition.apply(criteria.getPlaceholderId(), copy.getPlaceholderId()) &&
                condition.apply(criteria.getApplicationUserId(), copy.getApplicationUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
