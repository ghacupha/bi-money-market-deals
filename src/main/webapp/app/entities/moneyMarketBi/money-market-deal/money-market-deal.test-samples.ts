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

import { IMoneyMarketDeal, NewMoneyMarketDeal } from './money-market-deal.model';

export const sampleWithRequiredData: IMoneyMarketDeal = {
  id: 1541,
  dealNumber: 'like youthful',
  finalInterestAccrualDate: dayjs('2025-05-16'),
  endDate: dayjs('2025-05-15'),
  settlementDate: dayjs('2025-05-16'),
  maturityDate: dayjs('2025-05-15'),
  reportDate: dayjs('2025-05-15'),
  active: false,
};

export const sampleWithPartialData: IMoneyMarketDeal = {
  id: 12635,
  dealNumber: 'greatly',
  tradingBook: 'solace during below',
  finalInterestAccrualDate: dayjs('2025-05-15'),
  dateOfCollectionStatement: 'sticky',
  currencyCode: 'BTN',
  endDate: dayjs('2025-05-16'),
  dealSubtype: 'deceivingly',
  shillingEquivalentInterestAccrued: 21395.7,
  shillingEquivalentPVFull: 8846.68,
  counterpartyDomicile: 'a psst whoa',
  settlementDate: dayjs('2025-05-16'),
  transactionCollateral: 'ew meaty yippee',
  institutionType: 'nucleotidase oof',
  maturityDate: dayjs('2025-05-15'),
  institutionReportName: 'embalm',
  transactionType: 'notwithstanding pulverize',
  reportDate: dayjs('2025-05-15'),
  active: true,
};

export const sampleWithFullData: IMoneyMarketDeal = {
  id: 4657,
  dealNumber: 'besides',
  tradingBook: 'like',
  counterPartyName: 'after',
  finalInterestAccrualDate: dayjs('2025-05-15'),
  counterPartySideType: 'cod vibration dispose',
  dateOfCollectionStatement: 'overcoat huddle fat',
  currencyCode: 'FJD',
  principalAmount: 26217.34,
  interestRate: 1521.36,
  interestAccruedAmount: 791.26,
  totalInterestAtMaturity: 26217.34,
  counterpartyNationality: 'phooey parsnip jubilantly',
  endDate: dayjs('2025-05-15'),
  treasuryLedger: 'jut nor',
  dealSubtype: 'sadly inasmuch',
  shillingEquivalentPrincipal: 31286.89,
  shillingEquivalentInterestAccrued: 1274.95,
  shillingEquivalentPVFull: 22765.03,
  counterpartyDomicile: 'slake',
  settlementDate: dayjs('2025-05-16'),
  transactionCollateral: 'drat coordination forgather',
  institutionType: 'really yum',
  maturityDate: dayjs('2025-05-16'),
  institutionReportName: 'where',
  transactionType: 'alb',
  reportDate: dayjs('2025-05-15'),
  active: true,
};

export const sampleWithNewData: NewMoneyMarketDeal = {
  dealNumber: 'husky',
  finalInterestAccrualDate: dayjs('2025-05-16'),
  endDate: dayjs('2025-05-16'),
  settlementDate: dayjs('2025-05-15'),
  maturityDate: dayjs('2025-05-15'),
  reportDate: dayjs('2025-05-16'),
  active: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
