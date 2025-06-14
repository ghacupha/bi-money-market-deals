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

class FiscalMonthDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FiscalMonthDTO.class);
        FiscalMonthDTO fiscalMonthDTO1 = new FiscalMonthDTO();
        fiscalMonthDTO1.setId(1L);
        FiscalMonthDTO fiscalMonthDTO2 = new FiscalMonthDTO();
        assertThat(fiscalMonthDTO1).isNotEqualTo(fiscalMonthDTO2);
        fiscalMonthDTO2.setId(fiscalMonthDTO1.getId());
        assertThat(fiscalMonthDTO1).isEqualTo(fiscalMonthDTO2);
        fiscalMonthDTO2.setId(2L);
        assertThat(fiscalMonthDTO1).isNotEqualTo(fiscalMonthDTO2);
        fiscalMonthDTO1.setId(null);
        assertThat(fiscalMonthDTO1).isNotEqualTo(fiscalMonthDTO2);
    }
}
