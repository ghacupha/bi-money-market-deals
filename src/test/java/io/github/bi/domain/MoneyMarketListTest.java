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
import static io.github.bi.domain.MoneyMarketDealTestSamples.*;
import static io.github.bi.domain.MoneyMarketListTestSamples.*;
import static io.github.bi.domain.MoneyMarketUploadNotificationTestSamples.*;
import static io.github.bi.domain.PlaceholderTestSamples.*;
import static io.github.bi.domain.ReportBatchTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.bi.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MoneyMarketListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MoneyMarketList.class);
        MoneyMarketList moneyMarketList1 = getMoneyMarketListSample1();
        MoneyMarketList moneyMarketList2 = new MoneyMarketList();
        assertThat(moneyMarketList1).isNotEqualTo(moneyMarketList2);

        moneyMarketList2.setId(moneyMarketList1.getId());
        assertThat(moneyMarketList1).isEqualTo(moneyMarketList2);

        moneyMarketList2 = getMoneyMarketListSample2();
        assertThat(moneyMarketList1).isNotEqualTo(moneyMarketList2);
    }

    @Test
    void placeholderTest() {
        MoneyMarketList moneyMarketList = getMoneyMarketListRandomSampleGenerator();
        Placeholder placeholderBack = getPlaceholderRandomSampleGenerator();

        moneyMarketList.addPlaceholder(placeholderBack);
        assertThat(moneyMarketList.getPlaceholders()).containsOnly(placeholderBack);

        moneyMarketList.removePlaceholder(placeholderBack);
        assertThat(moneyMarketList.getPlaceholders()).doesNotContain(placeholderBack);

        moneyMarketList.placeholders(new HashSet<>(Set.of(placeholderBack)));
        assertThat(moneyMarketList.getPlaceholders()).containsOnly(placeholderBack);

        moneyMarketList.setPlaceholders(new HashSet<>());
        assertThat(moneyMarketList.getPlaceholders()).doesNotContain(placeholderBack);
    }

    @Test
    void uploadedByTest() {
        MoneyMarketList moneyMarketList = getMoneyMarketListRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        moneyMarketList.setUploadedBy(applicationUserBack);
        assertThat(moneyMarketList.getUploadedBy()).isEqualTo(applicationUserBack);

        moneyMarketList.uploadedBy(null);
        assertThat(moneyMarketList.getUploadedBy()).isNull();
    }

    @Test
    void reportBatchTest() {
        MoneyMarketList moneyMarketList = getMoneyMarketListRandomSampleGenerator();
        ReportBatch reportBatchBack = getReportBatchRandomSampleGenerator();

        moneyMarketList.setReportBatch(reportBatchBack);
        assertThat(moneyMarketList.getReportBatch()).isEqualTo(reportBatchBack);

        moneyMarketList.reportBatch(null);
        assertThat(moneyMarketList.getReportBatch()).isNull();
    }

    @Test
    void moneyMarketDealTest() {
        MoneyMarketList moneyMarketList = getMoneyMarketListRandomSampleGenerator();
        MoneyMarketDeal moneyMarketDealBack = getMoneyMarketDealRandomSampleGenerator();

        moneyMarketList.addMoneyMarketDeal(moneyMarketDealBack);
        assertThat(moneyMarketList.getMoneyMarketDeals()).containsOnly(moneyMarketDealBack);
        assertThat(moneyMarketDealBack.getMoneyMarketList()).isEqualTo(moneyMarketList);

        moneyMarketList.removeMoneyMarketDeal(moneyMarketDealBack);
        assertThat(moneyMarketList.getMoneyMarketDeals()).doesNotContain(moneyMarketDealBack);
        assertThat(moneyMarketDealBack.getMoneyMarketList()).isNull();

        moneyMarketList.moneyMarketDeals(new HashSet<>(Set.of(moneyMarketDealBack)));
        assertThat(moneyMarketList.getMoneyMarketDeals()).containsOnly(moneyMarketDealBack);
        assertThat(moneyMarketDealBack.getMoneyMarketList()).isEqualTo(moneyMarketList);

        moneyMarketList.setMoneyMarketDeals(new HashSet<>());
        assertThat(moneyMarketList.getMoneyMarketDeals()).doesNotContain(moneyMarketDealBack);
        assertThat(moneyMarketDealBack.getMoneyMarketList()).isNull();
    }

    @Test
    void moneyMarketUploadNotificationTest() {
        MoneyMarketList moneyMarketList = getMoneyMarketListRandomSampleGenerator();
        MoneyMarketUploadNotification moneyMarketUploadNotificationBack = getMoneyMarketUploadNotificationRandomSampleGenerator();

        moneyMarketList.addMoneyMarketUploadNotification(moneyMarketUploadNotificationBack);
        assertThat(moneyMarketList.getMoneyMarketUploadNotifications()).containsOnly(moneyMarketUploadNotificationBack);
        assertThat(moneyMarketUploadNotificationBack.getMoneyMarketList()).isEqualTo(moneyMarketList);

        moneyMarketList.removeMoneyMarketUploadNotification(moneyMarketUploadNotificationBack);
        assertThat(moneyMarketList.getMoneyMarketUploadNotifications()).doesNotContain(moneyMarketUploadNotificationBack);
        assertThat(moneyMarketUploadNotificationBack.getMoneyMarketList()).isNull();

        moneyMarketList.moneyMarketUploadNotifications(new HashSet<>(Set.of(moneyMarketUploadNotificationBack)));
        assertThat(moneyMarketList.getMoneyMarketUploadNotifications()).containsOnly(moneyMarketUploadNotificationBack);
        assertThat(moneyMarketUploadNotificationBack.getMoneyMarketList()).isEqualTo(moneyMarketList);

        moneyMarketList.setMoneyMarketUploadNotifications(new HashSet<>());
        assertThat(moneyMarketList.getMoneyMarketUploadNotifications()).doesNotContain(moneyMarketUploadNotificationBack);
        assertThat(moneyMarketUploadNotificationBack.getMoneyMarketList()).isNull();
    }
}
