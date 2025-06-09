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

public class DealerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Dealer getDealerSample1() {
        return new Dealer()
            .id(1L)
            .dealerName("dealerName1")
            .taxNumber("taxNumber1")
            .identificationDocumentNumber("identificationDocumentNumber1")
            .organizationName("organizationName1")
            .department("department1")
            .position("position1")
            .postalAddress("postalAddress1")
            .physicalAddress("physicalAddress1")
            .accountName("accountName1")
            .accountNumber("accountNumber1")
            .bankersName("bankersName1")
            .bankersBranch("bankersBranch1")
            .bankersSwiftCode("bankersSwiftCode1")
            .fileUploadToken("fileUploadToken1")
            .compilationToken("compilationToken1")
            .otherNames("otherNames1");
    }

    public static Dealer getDealerSample2() {
        return new Dealer()
            .id(2L)
            .dealerName("dealerName2")
            .taxNumber("taxNumber2")
            .identificationDocumentNumber("identificationDocumentNumber2")
            .organizationName("organizationName2")
            .department("department2")
            .position("position2")
            .postalAddress("postalAddress2")
            .physicalAddress("physicalAddress2")
            .accountName("accountName2")
            .accountNumber("accountNumber2")
            .bankersName("bankersName2")
            .bankersBranch("bankersBranch2")
            .bankersSwiftCode("bankersSwiftCode2")
            .fileUploadToken("fileUploadToken2")
            .compilationToken("compilationToken2")
            .otherNames("otherNames2");
    }

    public static Dealer getDealerRandomSampleGenerator() {
        return new Dealer()
            .id(longCount.incrementAndGet())
            .dealerName(UUID.randomUUID().toString())
            .taxNumber(UUID.randomUUID().toString())
            .identificationDocumentNumber(UUID.randomUUID().toString())
            .organizationName(UUID.randomUUID().toString())
            .department(UUID.randomUUID().toString())
            .position(UUID.randomUUID().toString())
            .postalAddress(UUID.randomUUID().toString())
            .physicalAddress(UUID.randomUUID().toString())
            .accountName(UUID.randomUUID().toString())
            .accountNumber(UUID.randomUUID().toString())
            .bankersName(UUID.randomUUID().toString())
            .bankersBranch(UUID.randomUUID().toString())
            .bankersSwiftCode(UUID.randomUUID().toString())
            .fileUploadToken(UUID.randomUUID().toString())
            .compilationToken(UUID.randomUUID().toString())
            .otherNames(UUID.randomUUID().toString());
    }
}
