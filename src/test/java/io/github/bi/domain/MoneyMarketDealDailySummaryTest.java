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

import static io.github.bi.domain.MoneyMarketDealDailySummaryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.bi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MoneyMarketDealDailySummaryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MoneyMarketDealDailySummary.class);
        MoneyMarketDealDailySummary moneyMarketDealDailySummary1 = getMoneyMarketDealDailySummarySample1();
        MoneyMarketDealDailySummary moneyMarketDealDailySummary2 = new MoneyMarketDealDailySummary();
        assertThat(moneyMarketDealDailySummary1).isNotEqualTo(moneyMarketDealDailySummary2);

        moneyMarketDealDailySummary2.setId(moneyMarketDealDailySummary1.getId());
        assertThat(moneyMarketDealDailySummary1).isEqualTo(moneyMarketDealDailySummary2);

        moneyMarketDealDailySummary2 = getMoneyMarketDealDailySummarySample2();
        assertThat(moneyMarketDealDailySummary1).isNotEqualTo(moneyMarketDealDailySummary2);
    }
}
