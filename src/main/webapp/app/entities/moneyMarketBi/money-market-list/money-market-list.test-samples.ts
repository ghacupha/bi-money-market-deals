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

import { IMoneyMarketList, NewMoneyMarketList } from './money-market-list.model';

export const sampleWithRequiredData: IMoneyMarketList = {
  id: 22343,
  reportDate: dayjs('2025-05-19'),
  uploadTimeStamp: dayjs('2025-05-19T10:13'),
  status: 'CANCELLED',
  active: true,
};

export const sampleWithPartialData: IMoneyMarketList = {
  id: 12286,
  reportDate: dayjs('2025-05-19'),
  uploadTimeStamp: dayjs('2025-05-19T10:09'),
  status: 'CANCELLED',
  description: 'by under viciously',
  active: false,
};

export const sampleWithFullData: IMoneyMarketList = {
  id: 3963,
  reportDate: dayjs('2025-05-18'),
  uploadTimeStamp: dayjs('2025-05-19T05:49'),
  status: 'CANCELLED',
  description: 'tank pension sock',
  active: true,
};

export const sampleWithNewData: NewMoneyMarketList = {
  reportDate: dayjs('2025-05-18'),
  uploadTimeStamp: dayjs('2025-05-18T21:32'),
  status: 'CANCELLED',
  active: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
