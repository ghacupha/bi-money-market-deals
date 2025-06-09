package io.github.bi.service.dto;

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

import static org.assertj.core.api.Assertions.assertThat;

import io.github.bi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MoneyMarketListDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MoneyMarketListDTO.class);
        MoneyMarketListDTO moneyMarketListDTO1 = new MoneyMarketListDTO();
        moneyMarketListDTO1.setId(1L);
        MoneyMarketListDTO moneyMarketListDTO2 = new MoneyMarketListDTO();
        assertThat(moneyMarketListDTO1).isNotEqualTo(moneyMarketListDTO2);
        moneyMarketListDTO2.setId(moneyMarketListDTO1.getId());
        assertThat(moneyMarketListDTO1).isEqualTo(moneyMarketListDTO2);
        moneyMarketListDTO2.setId(2L);
        assertThat(moneyMarketListDTO1).isNotEqualTo(moneyMarketListDTO2);
        moneyMarketListDTO1.setId(null);
        assertThat(moneyMarketListDTO1).isNotEqualTo(moneyMarketListDTO2);
    }
}
