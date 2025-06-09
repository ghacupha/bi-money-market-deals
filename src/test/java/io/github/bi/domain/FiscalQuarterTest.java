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

import static io.github.bi.domain.FiscalQuarterTestSamples.*;
import static io.github.bi.domain.FiscalYearTestSamples.*;
import static io.github.bi.domain.PlaceholderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.bi.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FiscalQuarterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FiscalQuarter.class);
        FiscalQuarter fiscalQuarter1 = getFiscalQuarterSample1();
        FiscalQuarter fiscalQuarter2 = new FiscalQuarter();
        assertThat(fiscalQuarter1).isNotEqualTo(fiscalQuarter2);

        fiscalQuarter2.setId(fiscalQuarter1.getId());
        assertThat(fiscalQuarter1).isEqualTo(fiscalQuarter2);

        fiscalQuarter2 = getFiscalQuarterSample2();
        assertThat(fiscalQuarter1).isNotEqualTo(fiscalQuarter2);
    }

    @Test
    void fiscalYearTest() {
        FiscalQuarter fiscalQuarter = getFiscalQuarterRandomSampleGenerator();
        FiscalYear fiscalYearBack = getFiscalYearRandomSampleGenerator();

        fiscalQuarter.setFiscalYear(fiscalYearBack);
        assertThat(fiscalQuarter.getFiscalYear()).isEqualTo(fiscalYearBack);

        fiscalQuarter.fiscalYear(null);
        assertThat(fiscalQuarter.getFiscalYear()).isNull();
    }

    @Test
    void placeholderTest() {
        FiscalQuarter fiscalQuarter = getFiscalQuarterRandomSampleGenerator();
        Placeholder placeholderBack = getPlaceholderRandomSampleGenerator();

        fiscalQuarter.addPlaceholder(placeholderBack);
        assertThat(fiscalQuarter.getPlaceholders()).containsOnly(placeholderBack);

        fiscalQuarter.removePlaceholder(placeholderBack);
        assertThat(fiscalQuarter.getPlaceholders()).doesNotContain(placeholderBack);

        fiscalQuarter.placeholders(new HashSet<>(Set.of(placeholderBack)));
        assertThat(fiscalQuarter.getPlaceholders()).containsOnly(placeholderBack);

        fiscalQuarter.setPlaceholders(new HashSet<>());
        assertThat(fiscalQuarter.getPlaceholders()).doesNotContain(placeholderBack);
    }
}
