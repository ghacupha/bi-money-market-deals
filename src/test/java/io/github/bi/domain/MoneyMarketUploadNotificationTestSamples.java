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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MoneyMarketUploadNotificationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MoneyMarketUploadNotification getMoneyMarketUploadNotificationSample1() {
        return new MoneyMarketUploadNotification()
            .id(1L)
            .rowNumber(1)
            .referenceNumber(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static MoneyMarketUploadNotification getMoneyMarketUploadNotificationSample2() {
        return new MoneyMarketUploadNotification()
            .id(2L)
            .rowNumber(2)
            .referenceNumber(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static MoneyMarketUploadNotification getMoneyMarketUploadNotificationRandomSampleGenerator() {
        return new MoneyMarketUploadNotification()
            .id(longCount.incrementAndGet())
            .rowNumber(intCount.incrementAndGet())
            .referenceNumber(UUID.randomUUID());
    }
}
