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

class ApplicationUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationUserDTO.class);
        ApplicationUserDTO applicationUserDTO1 = new ApplicationUserDTO();
        applicationUserDTO1.setId(1L);
        ApplicationUserDTO applicationUserDTO2 = new ApplicationUserDTO();
        assertThat(applicationUserDTO1).isNotEqualTo(applicationUserDTO2);
        applicationUserDTO2.setId(applicationUserDTO1.getId());
        assertThat(applicationUserDTO1).isEqualTo(applicationUserDTO2);
        applicationUserDTO2.setId(2L);
        assertThat(applicationUserDTO1).isNotEqualTo(applicationUserDTO2);
        applicationUserDTO1.setId(null);
        assertThat(applicationUserDTO1).isNotEqualTo(applicationUserDTO2);
    }
}
