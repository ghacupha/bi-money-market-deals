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

import static io.github.bi.domain.MoneyMarketListTestSamples.*;
import static io.github.bi.domain.MoneyMarketUploadNotificationTestSamples.*;
import static io.github.bi.domain.PlaceholderTestSamples.*;
import static io.github.bi.domain.ReportBatchTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.bi.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MoneyMarketUploadNotificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MoneyMarketUploadNotification.class);
        MoneyMarketUploadNotification moneyMarketUploadNotification1 = getMoneyMarketUploadNotificationSample1();
        MoneyMarketUploadNotification moneyMarketUploadNotification2 = new MoneyMarketUploadNotification();
        assertThat(moneyMarketUploadNotification1).isNotEqualTo(moneyMarketUploadNotification2);

        moneyMarketUploadNotification2.setId(moneyMarketUploadNotification1.getId());
        assertThat(moneyMarketUploadNotification1).isEqualTo(moneyMarketUploadNotification2);

        moneyMarketUploadNotification2 = getMoneyMarketUploadNotificationSample2();
        assertThat(moneyMarketUploadNotification1).isNotEqualTo(moneyMarketUploadNotification2);
    }

    @Test
    void moneyMarketListTest() {
        MoneyMarketUploadNotification moneyMarketUploadNotification = getMoneyMarketUploadNotificationRandomSampleGenerator();
        MoneyMarketList moneyMarketListBack = getMoneyMarketListRandomSampleGenerator();

        moneyMarketUploadNotification.setMoneyMarketList(moneyMarketListBack);
        assertThat(moneyMarketUploadNotification.getMoneyMarketList()).isEqualTo(moneyMarketListBack);

        moneyMarketUploadNotification.moneyMarketList(null);
        assertThat(moneyMarketUploadNotification.getMoneyMarketList()).isNull();
    }

    @Test
    void reportBatchTest() {
        MoneyMarketUploadNotification moneyMarketUploadNotification = getMoneyMarketUploadNotificationRandomSampleGenerator();
        ReportBatch reportBatchBack = getReportBatchRandomSampleGenerator();

        moneyMarketUploadNotification.setReportBatch(reportBatchBack);
        assertThat(moneyMarketUploadNotification.getReportBatch()).isEqualTo(reportBatchBack);

        moneyMarketUploadNotification.reportBatch(null);
        assertThat(moneyMarketUploadNotification.getReportBatch()).isNull();
    }

    @Test
    void placeholderTest() {
        MoneyMarketUploadNotification moneyMarketUploadNotification = getMoneyMarketUploadNotificationRandomSampleGenerator();
        Placeholder placeholderBack = getPlaceholderRandomSampleGenerator();

        moneyMarketUploadNotification.addPlaceholder(placeholderBack);
        assertThat(moneyMarketUploadNotification.getPlaceholders()).containsOnly(placeholderBack);

        moneyMarketUploadNotification.removePlaceholder(placeholderBack);
        assertThat(moneyMarketUploadNotification.getPlaceholders()).doesNotContain(placeholderBack);

        moneyMarketUploadNotification.placeholders(new HashSet<>(Set.of(placeholderBack)));
        assertThat(moneyMarketUploadNotification.getPlaceholders()).containsOnly(placeholderBack);

        moneyMarketUploadNotification.setPlaceholders(new HashSet<>());
        assertThat(moneyMarketUploadNotification.getPlaceholders()).doesNotContain(placeholderBack);
    }
}
