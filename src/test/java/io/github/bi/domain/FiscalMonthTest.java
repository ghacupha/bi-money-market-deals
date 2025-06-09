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
import static io.github.bi.domain.PlaceholderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.bi.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FiscalMonthTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FiscalMonth.class);
        FiscalMonth fiscalMonth1 = getFiscalMonthSample1();
        FiscalMonth fiscalMonth2 = new FiscalMonth();
        assertThat(fiscalMonth1).isNotEqualTo(fiscalMonth2);

        fiscalMonth2.setId(fiscalMonth1.getId());
        assertThat(fiscalMonth1).isEqualTo(fiscalMonth2);

        fiscalMonth2 = getFiscalMonthSample2();
        assertThat(fiscalMonth1).isNotEqualTo(fiscalMonth2);
    }

    @Test
    void fiscalYearTest() {
        FiscalMonth fiscalMonth = getFiscalMonthRandomSampleGenerator();
        FiscalYear fiscalYearBack = getFiscalYearRandomSampleGenerator();

        fiscalMonth.setFiscalYear(fiscalYearBack);
        assertThat(fiscalMonth.getFiscalYear()).isEqualTo(fiscalYearBack);

        fiscalMonth.fiscalYear(null);
        assertThat(fiscalMonth.getFiscalYear()).isNull();
    }

    @Test
    void placeholderTest() {
        FiscalMonth fiscalMonth = getFiscalMonthRandomSampleGenerator();
        Placeholder placeholderBack = getPlaceholderRandomSampleGenerator();

        fiscalMonth.addPlaceholder(placeholderBack);
        assertThat(fiscalMonth.getPlaceholders()).containsOnly(placeholderBack);

        fiscalMonth.removePlaceholder(placeholderBack);
        assertThat(fiscalMonth.getPlaceholders()).doesNotContain(placeholderBack);

        fiscalMonth.placeholders(new HashSet<>(Set.of(placeholderBack)));
        assertThat(fiscalMonth.getPlaceholders()).containsOnly(placeholderBack);

        fiscalMonth.setPlaceholders(new HashSet<>());
        assertThat(fiscalMonth.getPlaceholders()).doesNotContain(placeholderBack);
    }

    @Test
    void fiscalQuarterTest() {
        FiscalMonth fiscalMonth = getFiscalMonthRandomSampleGenerator();
        FiscalQuarter fiscalQuarterBack = getFiscalQuarterRandomSampleGenerator();

        fiscalMonth.setFiscalQuarter(fiscalQuarterBack);
        assertThat(fiscalMonth.getFiscalQuarter()).isEqualTo(fiscalQuarterBack);

        fiscalMonth.fiscalQuarter(null);
        assertThat(fiscalMonth.getFiscalQuarter()).isNull();
    }
}
