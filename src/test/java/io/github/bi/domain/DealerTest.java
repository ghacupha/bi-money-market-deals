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
import static io.github.bi.domain.DealerTestSamples.*;
import static io.github.bi.domain.DealerTestSamples.*;
import static io.github.bi.domain.PlaceholderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.bi.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DealerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dealer.class);
        Dealer dealer1 = getDealerSample1();
        Dealer dealer2 = new Dealer();
        assertThat(dealer1).isNotEqualTo(dealer2);

        dealer2.setId(dealer1.getId());
        assertThat(dealer1).isEqualTo(dealer2);

        dealer2 = getDealerSample2();
        assertThat(dealer1).isNotEqualTo(dealer2);
    }

    @Test
    void dealerGroupTest() {
        Dealer dealer = getDealerRandomSampleGenerator();
        Dealer dealerBack = getDealerRandomSampleGenerator();

        dealer.setDealerGroup(dealerBack);
        assertThat(dealer.getDealerGroup()).isEqualTo(dealerBack);

        dealer.dealerGroup(null);
        assertThat(dealer.getDealerGroup()).isNull();
    }

    @Test
    void placeholderTest() {
        Dealer dealer = getDealerRandomSampleGenerator();
        Placeholder placeholderBack = getPlaceholderRandomSampleGenerator();

        dealer.addPlaceholder(placeholderBack);
        assertThat(dealer.getPlaceholders()).containsOnly(placeholderBack);

        dealer.removePlaceholder(placeholderBack);
        assertThat(dealer.getPlaceholders()).doesNotContain(placeholderBack);

        dealer.placeholders(new HashSet<>(Set.of(placeholderBack)));
        assertThat(dealer.getPlaceholders()).containsOnly(placeholderBack);

        dealer.setPlaceholders(new HashSet<>());
        assertThat(dealer.getPlaceholders()).doesNotContain(placeholderBack);
    }

    @Test
    void applicationUserTest() {
        Dealer dealer = getDealerRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        dealer.setApplicationUser(applicationUserBack);
        assertThat(dealer.getApplicationUser()).isEqualTo(applicationUserBack);
        assertThat(applicationUserBack.getDealerIdentity()).isEqualTo(dealer);

        dealer.applicationUser(null);
        assertThat(dealer.getApplicationUser()).isNull();
        assertThat(applicationUserBack.getDealerIdentity()).isNull();
    }
}
