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

import static io.github.bi.domain.FiscalYearTestSamples.*;
import static io.github.bi.domain.PlaceholderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.bi.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FiscalYearTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FiscalYear.class);
        FiscalYear fiscalYear1 = getFiscalYearSample1();
        FiscalYear fiscalYear2 = new FiscalYear();
        assertThat(fiscalYear1).isNotEqualTo(fiscalYear2);

        fiscalYear2.setId(fiscalYear1.getId());
        assertThat(fiscalYear1).isEqualTo(fiscalYear2);

        fiscalYear2 = getFiscalYearSample2();
        assertThat(fiscalYear1).isNotEqualTo(fiscalYear2);
    }

    @Test
    void placeholderTest() {
        FiscalYear fiscalYear = getFiscalYearRandomSampleGenerator();
        Placeholder placeholderBack = getPlaceholderRandomSampleGenerator();

        fiscalYear.addPlaceholder(placeholderBack);
        assertThat(fiscalYear.getPlaceholders()).containsOnly(placeholderBack);

        fiscalYear.removePlaceholder(placeholderBack);
        assertThat(fiscalYear.getPlaceholders()).doesNotContain(placeholderBack);

        fiscalYear.placeholders(new HashSet<>(Set.of(placeholderBack)));
        assertThat(fiscalYear.getPlaceholders()).containsOnly(placeholderBack);

        fiscalYear.setPlaceholders(new HashSet<>());
        assertThat(fiscalYear.getPlaceholders()).doesNotContain(placeholderBack);
    }
}
