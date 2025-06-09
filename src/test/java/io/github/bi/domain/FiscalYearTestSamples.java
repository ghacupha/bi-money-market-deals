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

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FiscalYearTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FiscalYear getFiscalYearSample1() {
        return new FiscalYear().id(1L).fiscalYearCode("fiscalYearCode1");
    }

    public static FiscalYear getFiscalYearSample2() {
        return new FiscalYear().id(2L).fiscalYearCode("fiscalYearCode2");
    }

    public static FiscalYear getFiscalYearRandomSampleGenerator() {
        return new FiscalYear().id(longCount.incrementAndGet()).fiscalYearCode(UUID.randomUUID().toString());
    }
}
