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

import { IFiscalQuarter, NewFiscalQuarter } from './fiscal-quarter.model';

export const sampleWithRequiredData: IFiscalQuarter = {
  id: 27303,
  quarterNumber: 17095,
  startDate: dayjs('2023-08-16'),
  endDate: dayjs('2023-08-16'),
  fiscalQuarterCode: 'dull',
};

export const sampleWithPartialData: IFiscalQuarter = {
  id: 2642,
  quarterNumber: 16758,
  startDate: dayjs('2023-08-16'),
  endDate: dayjs('2023-08-16'),
  fiscalQuarterCode: 'toward cute ouch',
};

export const sampleWithFullData: IFiscalQuarter = {
  id: 5212,
  quarterNumber: 26440,
  startDate: dayjs('2023-08-15'),
  endDate: dayjs('2023-08-16'),
  fiscalQuarterCode: 'though',
};

export const sampleWithNewData: NewFiscalQuarter = {
  quarterNumber: 32471,
  startDate: dayjs('2023-08-15'),
  endDate: dayjs('2023-08-15'),
  fiscalQuarterCode: 'of emergent apricot',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
