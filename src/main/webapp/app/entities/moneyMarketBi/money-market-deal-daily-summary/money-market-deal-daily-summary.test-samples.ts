///
/// Money Market Bi - BI Microservice for Money Market Bi deals is part of the Granular Bi System
/// Copyright © 2025 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import dayjs from 'dayjs/esm';

import { IMoneyMarketDealDailySummary } from './money-market-deal-daily-summary.model';

export const sampleWithRequiredData: IMoneyMarketDealDailySummary = {
  id: 9389,
};

export const sampleWithPartialData: IMoneyMarketDealDailySummary = {
  id: 3826,
  reportDate: dayjs('2025-06-03'),
  numberOfDeals: 15601,
  interestAccrued: 17010.88,
};

export const sampleWithFullData: IMoneyMarketDealDailySummary = {
  id: 11557,
  reportDate: dayjs('2025-06-03'),
  ledger: 'slather because',
  numberOfDeals: 32169,
  totalPrincipal: 12590.57,
  interestAccrued: 10474.01,
  totalPVFull: 3078.74,
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
