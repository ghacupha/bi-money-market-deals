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

class FiscalYearDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FiscalYearDTO.class);
        FiscalYearDTO fiscalYearDTO1 = new FiscalYearDTO();
        fiscalYearDTO1.setId(1L);
        FiscalYearDTO fiscalYearDTO2 = new FiscalYearDTO();
        assertThat(fiscalYearDTO1).isNotEqualTo(fiscalYearDTO2);
        fiscalYearDTO2.setId(fiscalYearDTO1.getId());
        assertThat(fiscalYearDTO1).isEqualTo(fiscalYearDTO2);
        fiscalYearDTO2.setId(2L);
        assertThat(fiscalYearDTO1).isNotEqualTo(fiscalYearDTO2);
        fiscalYearDTO1.setId(null);
        assertThat(fiscalYearDTO1).isNotEqualTo(fiscalYearDTO2);
    }
}
