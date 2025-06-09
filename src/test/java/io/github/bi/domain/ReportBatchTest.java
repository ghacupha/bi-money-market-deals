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
import static io.github.bi.domain.MoneyMarketListTestSamples.*;
import static io.github.bi.domain.MoneyMarketUploadNotificationTestSamples.*;
import static io.github.bi.domain.PlaceholderTestSamples.*;
import static io.github.bi.domain.ReportBatchTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.bi.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ReportBatchTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportBatch.class);
        ReportBatch reportBatch1 = getReportBatchSample1();
        ReportBatch reportBatch2 = new ReportBatch();
        assertThat(reportBatch1).isNotEqualTo(reportBatch2);

        reportBatch2.setId(reportBatch1.getId());
        assertThat(reportBatch1).isEqualTo(reportBatch2);

        reportBatch2 = getReportBatchSample2();
        assertThat(reportBatch1).isNotEqualTo(reportBatch2);
    }

    @Test
    void uploadedByTest() {
        ReportBatch reportBatch = getReportBatchRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        reportBatch.setUploadedBy(applicationUserBack);
        assertThat(reportBatch.getUploadedBy()).isEqualTo(applicationUserBack);

        reportBatch.uploadedBy(null);
        assertThat(reportBatch.getUploadedBy()).isNull();
    }

    @Test
    void placeholderTest() {
        ReportBatch reportBatch = getReportBatchRandomSampleGenerator();
        Placeholder placeholderBack = getPlaceholderRandomSampleGenerator();

        reportBatch.addPlaceholder(placeholderBack);
        assertThat(reportBatch.getPlaceholders()).containsOnly(placeholderBack);

        reportBatch.removePlaceholder(placeholderBack);
        assertThat(reportBatch.getPlaceholders()).doesNotContain(placeholderBack);

        reportBatch.placeholders(new HashSet<>(Set.of(placeholderBack)));
        assertThat(reportBatch.getPlaceholders()).containsOnly(placeholderBack);

        reportBatch.setPlaceholders(new HashSet<>());
        assertThat(reportBatch.getPlaceholders()).doesNotContain(placeholderBack);
    }

    @Test
    void moneyMarketListTest() {
        ReportBatch reportBatch = getReportBatchRandomSampleGenerator();
        MoneyMarketList moneyMarketListBack = getMoneyMarketListRandomSampleGenerator();

        reportBatch.setMoneyMarketList(moneyMarketListBack);
        assertThat(reportBatch.getMoneyMarketList()).isEqualTo(moneyMarketListBack);
        assertThat(moneyMarketListBack.getReportBatch()).isEqualTo(reportBatch);

        reportBatch.moneyMarketList(null);
        assertThat(reportBatch.getMoneyMarketList()).isNull();
        assertThat(moneyMarketListBack.getReportBatch()).isNull();
    }

    @Test
    void moneyMarketUploadNotificationTest() {
        ReportBatch reportBatch = getReportBatchRandomSampleGenerator();
        MoneyMarketUploadNotification moneyMarketUploadNotificationBack = getMoneyMarketUploadNotificationRandomSampleGenerator();

        reportBatch.addMoneyMarketUploadNotification(moneyMarketUploadNotificationBack);
        assertThat(reportBatch.getMoneyMarketUploadNotifications()).containsOnly(moneyMarketUploadNotificationBack);
        assertThat(moneyMarketUploadNotificationBack.getReportBatch()).isEqualTo(reportBatch);

        reportBatch.removeMoneyMarketUploadNotification(moneyMarketUploadNotificationBack);
        assertThat(reportBatch.getMoneyMarketUploadNotifications()).doesNotContain(moneyMarketUploadNotificationBack);
        assertThat(moneyMarketUploadNotificationBack.getReportBatch()).isNull();

        reportBatch.moneyMarketUploadNotifications(new HashSet<>(Set.of(moneyMarketUploadNotificationBack)));
        assertThat(reportBatch.getMoneyMarketUploadNotifications()).containsOnly(moneyMarketUploadNotificationBack);
        assertThat(moneyMarketUploadNotificationBack.getReportBatch()).isEqualTo(reportBatch);

        reportBatch.setMoneyMarketUploadNotifications(new HashSet<>());
        assertThat(reportBatch.getMoneyMarketUploadNotifications()).doesNotContain(moneyMarketUploadNotificationBack);
        assertThat(moneyMarketUploadNotificationBack.getReportBatch()).isNull();
    }
}
