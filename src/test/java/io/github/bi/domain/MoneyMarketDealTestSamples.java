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

public class MoneyMarketDealTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MoneyMarketDeal getMoneyMarketDealSample1() {
        return new MoneyMarketDeal()
            .id(1L)
            .dealNumber("dealNumber1")
            .tradingBook("tradingBook1")
            .counterPartyName("counterPartyName1")
            .counterPartySideType("counterPartySideType1")
            .dateOfCollectionStatement("dateOfCollectionStatement1")
            .currencyCode("currencyCode1")
            .counterpartyNationality("counterpartyNationality1")
            .treasuryLedger("treasuryLedger1")
            .dealSubtype("dealSubtype1")
            .counterpartyDomicile("counterpartyDomicile1")
            .transactionCollateral("transactionCollateral1")
            .institutionType("institutionType1")
            .institutionReportName("institutionReportName1")
            .transactionType("transactionType1");
    }

    public static MoneyMarketDeal getMoneyMarketDealSample2() {
        return new MoneyMarketDeal()
            .id(2L)
            .dealNumber("dealNumber2")
            .tradingBook("tradingBook2")
            .counterPartyName("counterPartyName2")
            .counterPartySideType("counterPartySideType2")
            .dateOfCollectionStatement("dateOfCollectionStatement2")
            .currencyCode("currencyCode2")
            .counterpartyNationality("counterpartyNationality2")
            .treasuryLedger("treasuryLedger2")
            .dealSubtype("dealSubtype2")
            .counterpartyDomicile("counterpartyDomicile2")
            .transactionCollateral("transactionCollateral2")
            .institutionType("institutionType2")
            .institutionReportName("institutionReportName2")
            .transactionType("transactionType2");
    }

    public static MoneyMarketDeal getMoneyMarketDealRandomSampleGenerator() {
        return new MoneyMarketDeal()
            .id(longCount.incrementAndGet())
            .dealNumber(UUID.randomUUID().toString())
            .tradingBook(UUID.randomUUID().toString())
            .counterPartyName(UUID.randomUUID().toString())
            .counterPartySideType(UUID.randomUUID().toString())
            .dateOfCollectionStatement(UUID.randomUUID().toString())
            .currencyCode(UUID.randomUUID().toString())
            .counterpartyNationality(UUID.randomUUID().toString())
            .treasuryLedger(UUID.randomUUID().toString())
            .dealSubtype(UUID.randomUUID().toString())
            .counterpartyDomicile(UUID.randomUUID().toString())
            .transactionCollateral(UUID.randomUUID().toString())
            .institutionType(UUID.randomUUID().toString())
            .institutionReportName(UUID.randomUUID().toString())
            .transactionType(UUID.randomUUID().toString());
    }
}
