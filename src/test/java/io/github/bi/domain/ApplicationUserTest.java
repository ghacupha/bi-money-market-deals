package io.github.bi.domain;

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

import static io.github.bi.domain.ApplicationUserTestSamples.*;
import static io.github.bi.domain.DealerTestSamples.*;
import static io.github.bi.domain.MoneyMarketListTestSamples.*;
import static io.github.bi.domain.PlaceholderTestSamples.*;
import static io.github.bi.domain.ReportBatchTestSamples.*;
import static io.github.bi.domain.SecurityClearanceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.bi.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ApplicationUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationUser.class);
        ApplicationUser applicationUser1 = getApplicationUserSample1();
        ApplicationUser applicationUser2 = new ApplicationUser();
        assertThat(applicationUser1).isNotEqualTo(applicationUser2);

        applicationUser2.setId(applicationUser1.getId());
        assertThat(applicationUser1).isEqualTo(applicationUser2);

        applicationUser2 = getApplicationUserSample2();
        assertThat(applicationUser1).isNotEqualTo(applicationUser2);
    }

    @Test
    void organizationTest() {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        Dealer dealerBack = getDealerRandomSampleGenerator();

        applicationUser.setOrganization(dealerBack);
        assertThat(applicationUser.getOrganization()).isEqualTo(dealerBack);

        applicationUser.organization(null);
        assertThat(applicationUser.getOrganization()).isNull();
    }

    @Test
    void departmentTest() {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        Dealer dealerBack = getDealerRandomSampleGenerator();

        applicationUser.setDepartment(dealerBack);
        assertThat(applicationUser.getDepartment()).isEqualTo(dealerBack);

        applicationUser.department(null);
        assertThat(applicationUser.getDepartment()).isNull();
    }

    @Test
    void securityClearanceTest() {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        SecurityClearance securityClearanceBack = getSecurityClearanceRandomSampleGenerator();

        applicationUser.setSecurityClearance(securityClearanceBack);
        assertThat(applicationUser.getSecurityClearance()).isEqualTo(securityClearanceBack);

        applicationUser.securityClearance(null);
        assertThat(applicationUser.getSecurityClearance()).isNull();
    }

    @Test
    void dealerIdentityTest() {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        Dealer dealerBack = getDealerRandomSampleGenerator();

        applicationUser.setDealerIdentity(dealerBack);
        assertThat(applicationUser.getDealerIdentity()).isEqualTo(dealerBack);

        applicationUser.dealerIdentity(null);
        assertThat(applicationUser.getDealerIdentity()).isNull();
    }

    @Test
    void placeholderTest() {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        Placeholder placeholderBack = getPlaceholderRandomSampleGenerator();

        applicationUser.addPlaceholder(placeholderBack);
        assertThat(applicationUser.getPlaceholders()).containsOnly(placeholderBack);

        applicationUser.removePlaceholder(placeholderBack);
        assertThat(applicationUser.getPlaceholders()).doesNotContain(placeholderBack);

        applicationUser.placeholders(new HashSet<>(Set.of(placeholderBack)));
        assertThat(applicationUser.getPlaceholders()).containsOnly(placeholderBack);

        applicationUser.setPlaceholders(new HashSet<>());
        assertThat(applicationUser.getPlaceholders()).doesNotContain(placeholderBack);
    }

    @Test
    void reportBatchTest() {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        ReportBatch reportBatchBack = getReportBatchRandomSampleGenerator();

        applicationUser.addReportBatch(reportBatchBack);
        assertThat(applicationUser.getReportBatches()).containsOnly(reportBatchBack);
        assertThat(reportBatchBack.getUploadedBy()).isEqualTo(applicationUser);

        applicationUser.removeReportBatch(reportBatchBack);
        assertThat(applicationUser.getReportBatches()).doesNotContain(reportBatchBack);
        assertThat(reportBatchBack.getUploadedBy()).isNull();

        applicationUser.reportBatches(new HashSet<>(Set.of(reportBatchBack)));
        assertThat(applicationUser.getReportBatches()).containsOnly(reportBatchBack);
        assertThat(reportBatchBack.getUploadedBy()).isEqualTo(applicationUser);

        applicationUser.setReportBatches(new HashSet<>());
        assertThat(applicationUser.getReportBatches()).doesNotContain(reportBatchBack);
        assertThat(reportBatchBack.getUploadedBy()).isNull();
    }

    @Test
    void moneyMarketListTest() {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        MoneyMarketList moneyMarketListBack = getMoneyMarketListRandomSampleGenerator();

        applicationUser.addMoneyMarketList(moneyMarketListBack);
        assertThat(applicationUser.getMoneyMarketLists()).containsOnly(moneyMarketListBack);
        assertThat(moneyMarketListBack.getUploadedBy()).isEqualTo(applicationUser);

        applicationUser.removeMoneyMarketList(moneyMarketListBack);
        assertThat(applicationUser.getMoneyMarketLists()).doesNotContain(moneyMarketListBack);
        assertThat(moneyMarketListBack.getUploadedBy()).isNull();

        applicationUser.moneyMarketLists(new HashSet<>(Set.of(moneyMarketListBack)));
        assertThat(applicationUser.getMoneyMarketLists()).containsOnly(moneyMarketListBack);
        assertThat(moneyMarketListBack.getUploadedBy()).isEqualTo(applicationUser);

        applicationUser.setMoneyMarketLists(new HashSet<>());
        assertThat(applicationUser.getMoneyMarketLists()).doesNotContain(moneyMarketListBack);
        assertThat(moneyMarketListBack.getUploadedBy()).isNull();
    }
}
