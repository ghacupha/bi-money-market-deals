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

import { IMoneyMarketUploadNotification, NewMoneyMarketUploadNotification } from './money-market-upload-notification.model';

export const sampleWithRequiredData: IMoneyMarketUploadNotification = {
  id: 31205,
  referenceNumber: '08c82d81-b9ae-4509-a132-9fd2ce8abf04',
};

export const sampleWithPartialData: IMoneyMarketUploadNotification = {
  id: 1728,
  referenceNumber: '8b1576b2-80ae-4169-b6f2-8811758edfee',
};

export const sampleWithFullData: IMoneyMarketUploadNotification = {
  id: 1987,
  errorMessage: '../fake-data/blob/hipster.txt',
  rowNumber: 22330,
  referenceNumber: '29abe51b-98e4-41c7-b15e-f8a13bf370f8',
};

export const sampleWithNewData: NewMoneyMarketUploadNotification = {
  referenceNumber: '9fb55566-ee9e-49b1-821f-0d797ec61fb0',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
