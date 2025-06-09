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

import { IFiscalMonth, NewFiscalMonth } from './fiscal-month.model';

export const sampleWithRequiredData: IFiscalMonth = {
  id: 20605,
  monthNumber: 26673,
  startDate: dayjs('2023-08-15'),
  endDate: dayjs('2023-08-16'),
  fiscalMonthCode: 'joyously atop altruistic',
};

export const sampleWithPartialData: IFiscalMonth = {
  id: 19171,
  monthNumber: 31087,
  startDate: dayjs('2023-08-16'),
  endDate: dayjs('2023-08-16'),
  fiscalMonthCode: 'gosh goat justly',
};

export const sampleWithFullData: IFiscalMonth = {
  id: 7435,
  monthNumber: 30150,
  startDate: dayjs('2023-08-15'),
  endDate: dayjs('2023-08-16'),
  fiscalMonthCode: 'imaginary flimsy on',
};

export const sampleWithNewData: NewFiscalMonth = {
  monthNumber: 1059,
  startDate: dayjs('2023-08-16'),
  endDate: dayjs('2023-08-16'),
  fiscalMonthCode: 'aware pfft',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
