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

import static io.github.bi.domain.FiscalMonthTestSamples.*;
import static io.github.bi.domain.FiscalQuarterTestSamples.*;
import static io.github.bi.domain.FiscalYearTestSamples.*;
import static io.github.bi.domain.MoneyMarketListTestSamples.*;
import static io.github.bi.domain.PlaceholderTestSamples.*;
import static io.github.bi.domain.PlaceholderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.bi.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PlaceholderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Placeholder.class);
        Placeholder placeholder1 = getPlaceholderSample1();
        Placeholder placeholder2 = new Placeholder();
        assertThat(placeholder1).isNotEqualTo(placeholder2);

        placeholder2.setId(placeholder1.getId());
        assertThat(placeholder1).isEqualTo(placeholder2);

        placeholder2 = getPlaceholderSample2();
        assertThat(placeholder1).isNotEqualTo(placeholder2);
    }

    @Test
    void containingPlaceholderTest() {
        Placeholder placeholder = getPlaceholderRandomSampleGenerator();
        Placeholder placeholderBack = getPlaceholderRandomSampleGenerator();

        placeholder.setContainingPlaceholder(placeholderBack);
        assertThat(placeholder.getContainingPlaceholder()).isEqualTo(placeholderBack);

        placeholder.containingPlaceholder(null);
        assertThat(placeholder.getContainingPlaceholder()).isNull();
    }

    @Test
    void placeholderTest() {
        Placeholder placeholder = getPlaceholderRandomSampleGenerator();
        Placeholder placeholderBack = getPlaceholderRandomSampleGenerator();

        placeholder.addPlaceholder(placeholderBack);
        assertThat(placeholder.getPlaceholders()).containsOnly(placeholderBack);
        assertThat(placeholderBack.getContainingPlaceholder()).isEqualTo(placeholder);

        placeholder.removePlaceholder(placeholderBack);
        assertThat(placeholder.getPlaceholders()).doesNotContain(placeholderBack);
        assertThat(placeholderBack.getContainingPlaceholder()).isNull();

        placeholder.placeholders(new HashSet<>(Set.of(placeholderBack)));
        assertThat(placeholder.getPlaceholders()).containsOnly(placeholderBack);
        assertThat(placeholderBack.getContainingPlaceholder()).isEqualTo(placeholder);

        placeholder.setPlaceholders(new HashSet<>());
        assertThat(placeholder.getPlaceholders()).doesNotContain(placeholderBack);
        assertThat(placeholderBack.getContainingPlaceholder()).isNull();
    }

    @Test
    void fiscalYearTest() {
        Placeholder placeholder = getPlaceholderRandomSampleGenerator();
        FiscalYear fiscalYearBack = getFiscalYearRandomSampleGenerator();

        placeholder.addFiscalYear(fiscalYearBack);
        assertThat(placeholder.getFiscalYears()).containsOnly(fiscalYearBack);
        assertThat(fiscalYearBack.getPlaceholders()).containsOnly(placeholder);

        placeholder.removeFiscalYear(fiscalYearBack);
        assertThat(placeholder.getFiscalYears()).doesNotContain(fiscalYearBack);
        assertThat(fiscalYearBack.getPlaceholders()).doesNotContain(placeholder);

        placeholder.fiscalYears(new HashSet<>(Set.of(fiscalYearBack)));
        assertThat(placeholder.getFiscalYears()).containsOnly(fiscalYearBack);
        assertThat(fiscalYearBack.getPlaceholders()).containsOnly(placeholder);

        placeholder.setFiscalYears(new HashSet<>());
        assertThat(placeholder.getFiscalYears()).doesNotContain(fiscalYearBack);
        assertThat(fiscalYearBack.getPlaceholders()).doesNotContain(placeholder);
    }

    @Test
    void fiscalQuarterTest() {
        Placeholder placeholder = getPlaceholderRandomSampleGenerator();
        FiscalQuarter fiscalQuarterBack = getFiscalQuarterRandomSampleGenerator();

        placeholder.addFiscalQuarter(fiscalQuarterBack);
        assertThat(placeholder.getFiscalQuarters()).containsOnly(fiscalQuarterBack);
        assertThat(fiscalQuarterBack.getPlaceholders()).containsOnly(placeholder);

        placeholder.removeFiscalQuarter(fiscalQuarterBack);
        assertThat(placeholder.getFiscalQuarters()).doesNotContain(fiscalQuarterBack);
        assertThat(fiscalQuarterBack.getPlaceholders()).doesNotContain(placeholder);

        placeholder.fiscalQuarters(new HashSet<>(Set.of(fiscalQuarterBack)));
        assertThat(placeholder.getFiscalQuarters()).containsOnly(fiscalQuarterBack);
        assertThat(fiscalQuarterBack.getPlaceholders()).containsOnly(placeholder);

        placeholder.setFiscalQuarters(new HashSet<>());
        assertThat(placeholder.getFiscalQuarters()).doesNotContain(fiscalQuarterBack);
        assertThat(fiscalQuarterBack.getPlaceholders()).doesNotContain(placeholder);
    }

    @Test
    void fiscalMonthTest() {
        Placeholder placeholder = getPlaceholderRandomSampleGenerator();
        FiscalMonth fiscalMonthBack = getFiscalMonthRandomSampleGenerator();

        placeholder.addFiscalMonth(fiscalMonthBack);
        assertThat(placeholder.getFiscalMonths()).containsOnly(fiscalMonthBack);
        assertThat(fiscalMonthBack.getPlaceholders()).containsOnly(placeholder);

        placeholder.removeFiscalMonth(fiscalMonthBack);
        assertThat(placeholder.getFiscalMonths()).doesNotContain(fiscalMonthBack);
        assertThat(fiscalMonthBack.getPlaceholders()).doesNotContain(placeholder);

        placeholder.fiscalMonths(new HashSet<>(Set.of(fiscalMonthBack)));
        assertThat(placeholder.getFiscalMonths()).containsOnly(fiscalMonthBack);
        assertThat(fiscalMonthBack.getPlaceholders()).containsOnly(placeholder);

        placeholder.setFiscalMonths(new HashSet<>());
        assertThat(placeholder.getFiscalMonths()).doesNotContain(fiscalMonthBack);
        assertThat(fiscalMonthBack.getPlaceholders()).doesNotContain(placeholder);
    }

    @Test
    void moneyMarketListTest() {
        Placeholder placeholder = getPlaceholderRandomSampleGenerator();
        MoneyMarketList moneyMarketListBack = getMoneyMarketListRandomSampleGenerator();

        placeholder.addMoneyMarketList(moneyMarketListBack);
        assertThat(placeholder.getMoneyMarketLists()).containsOnly(moneyMarketListBack);
        assertThat(moneyMarketListBack.getPlaceholders()).containsOnly(placeholder);

        placeholder.removeMoneyMarketList(moneyMarketListBack);
        assertThat(placeholder.getMoneyMarketLists()).doesNotContain(moneyMarketListBack);
        assertThat(moneyMarketListBack.getPlaceholders()).doesNotContain(placeholder);

        placeholder.moneyMarketLists(new HashSet<>(Set.of(moneyMarketListBack)));
        assertThat(placeholder.getMoneyMarketLists()).containsOnly(moneyMarketListBack);
        assertThat(moneyMarketListBack.getPlaceholders()).containsOnly(placeholder);

        placeholder.setMoneyMarketLists(new HashSet<>());
        assertThat(placeholder.getMoneyMarketLists()).doesNotContain(moneyMarketListBack);
        assertThat(moneyMarketListBack.getPlaceholders()).doesNotContain(placeholder);
    }
}
