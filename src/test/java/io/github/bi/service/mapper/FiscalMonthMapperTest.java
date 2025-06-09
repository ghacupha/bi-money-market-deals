package io.github.bi.service.mapper;

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

import static io.github.bi.domain.FiscalMonthAsserts.*;
import static io.github.bi.domain.FiscalMonthTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FiscalMonthMapperTest {

    private FiscalMonthMapper fiscalMonthMapper;

    @BeforeEach
    void setUp() {
        fiscalMonthMapper = new FiscalMonthMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFiscalMonthSample1();
        var actual = fiscalMonthMapper.toEntity(fiscalMonthMapper.toDto(expected));
        assertFiscalMonthAllPropertiesEquals(expected, actual);
    }
}
