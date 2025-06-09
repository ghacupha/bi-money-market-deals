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

import static io.github.bi.domain.MoneyMarketDealTestSamples.*;
import static io.github.bi.domain.MoneyMarketListTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.bi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MoneyMarketDealTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MoneyMarketDeal.class);
        MoneyMarketDeal moneyMarketDeal1 = getMoneyMarketDealSample1();
        MoneyMarketDeal moneyMarketDeal2 = new MoneyMarketDeal();
        assertThat(moneyMarketDeal1).isNotEqualTo(moneyMarketDeal2);

        moneyMarketDeal2.setId(moneyMarketDeal1.getId());
        assertThat(moneyMarketDeal1).isEqualTo(moneyMarketDeal2);

        moneyMarketDeal2 = getMoneyMarketDealSample2();
        assertThat(moneyMarketDeal1).isNotEqualTo(moneyMarketDeal2);
    }

    @Test
    void moneyMarketListTest() {
        MoneyMarketDeal moneyMarketDeal = getMoneyMarketDealRandomSampleGenerator();
        MoneyMarketList moneyMarketListBack = getMoneyMarketListRandomSampleGenerator();

        moneyMarketDeal.setMoneyMarketList(moneyMarketListBack);
        assertThat(moneyMarketDeal.getMoneyMarketList()).isEqualTo(moneyMarketListBack);

        moneyMarketDeal.moneyMarketList(null);
        assertThat(moneyMarketDeal.getMoneyMarketList()).isNull();
    }
}
