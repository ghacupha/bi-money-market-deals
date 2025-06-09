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

import static io.github.bi.domain.PlaceholderTestSamples.*;
import static io.github.bi.domain.SecurityClearanceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.bi.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SecurityClearanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SecurityClearance.class);
        SecurityClearance securityClearance1 = getSecurityClearanceSample1();
        SecurityClearance securityClearance2 = new SecurityClearance();
        assertThat(securityClearance1).isNotEqualTo(securityClearance2);

        securityClearance2.setId(securityClearance1.getId());
        assertThat(securityClearance1).isEqualTo(securityClearance2);

        securityClearance2 = getSecurityClearanceSample2();
        assertThat(securityClearance1).isNotEqualTo(securityClearance2);
    }

    @Test
    void placeholderTest() {
        SecurityClearance securityClearance = getSecurityClearanceRandomSampleGenerator();
        Placeholder placeholderBack = getPlaceholderRandomSampleGenerator();

        securityClearance.addPlaceholder(placeholderBack);
        assertThat(securityClearance.getPlaceholders()).containsOnly(placeholderBack);

        securityClearance.removePlaceholder(placeholderBack);
        assertThat(securityClearance.getPlaceholders()).doesNotContain(placeholderBack);

        securityClearance.placeholders(new HashSet<>(Set.of(placeholderBack)));
        assertThat(securityClearance.getPlaceholders()).containsOnly(placeholderBack);

        securityClearance.setPlaceholders(new HashSet<>());
        assertThat(securityClearance.getPlaceholders()).doesNotContain(placeholderBack);
    }
}
