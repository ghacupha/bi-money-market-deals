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

import { IFiscalYear, NewFiscalYear } from './fiscal-year.model';

export const sampleWithRequiredData: IFiscalYear = {
  id: 30100,
  fiscalYearCode: 'spiteful physically',
  startDate: dayjs('2023-08-15'),
  endDate: dayjs('2023-08-16'),
};

export const sampleWithPartialData: IFiscalYear = {
  id: 13755,
  fiscalYearCode: 'whopping lift',
  startDate: dayjs('2023-08-16'),
  endDate: dayjs('2023-08-15'),
  fiscalYearStatus: 'OPEN',
};

export const sampleWithFullData: IFiscalYear = {
  id: 2821,
  fiscalYearCode: 'quicker round overproduce',
  startDate: dayjs('2023-08-15'),
  endDate: dayjs('2023-08-16'),
  fiscalYearStatus: 'CLOSED',
};

export const sampleWithNewData: NewFiscalYear = {
  fiscalYearCode: 'infinite',
  startDate: dayjs('2023-08-16'),
  endDate: dayjs('2023-08-15'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
