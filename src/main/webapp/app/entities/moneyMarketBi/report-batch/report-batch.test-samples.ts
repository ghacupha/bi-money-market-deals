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

import { IReportBatch, NewReportBatch } from './report-batch.model';

export const sampleWithRequiredData: IReportBatch = {
  id: 1339,
  reportDate: dayjs('2025-05-18'),
  uploadTimeStamp: dayjs('2025-05-18T17:38'),
  status: 'REPLACED',
  active: false,
  description: 'amongst',
  fileIdentifier: '2fd9aefe-fe9d-47a4-8bd5-adfeccaeda42',
  csvFileAttachment: '../fake-data/blob/hipster.png',
  csvFileAttachmentContentType: 'unknown',
};

export const sampleWithPartialData: IReportBatch = {
  id: 1061,
  reportDate: dayjs('2025-05-18'),
  uploadTimeStamp: dayjs('2025-05-19T08:05'),
  status: 'ACTIVE',
  active: false,
  description: 'towards pointed which',
  fileIdentifier: '19cd53aa-88ac-426a-8251-27481f5e46dc',
  processFlag: 'PROCESSED',
  csvFileAttachment: '../fake-data/blob/hipster.png',
  csvFileAttachmentContentType: 'unknown',
};

export const sampleWithFullData: IReportBatch = {
  id: 22668,
  reportDate: dayjs('2025-05-19'),
  uploadTimeStamp: dayjs('2025-05-18T19:29'),
  status: 'REPLACED',
  active: false,
  description: 'dally',
  fileIdentifier: '8ddb3b74-f6c8-47e2-9879-a8d4cb4050d1',
  processFlag: 'FAILED',
  csvFileAttachment: '../fake-data/blob/hipster.png',
  csvFileAttachmentContentType: 'unknown',
};

export const sampleWithNewData: NewReportBatch = {
  reportDate: dayjs('2025-05-18'),
  uploadTimeStamp: dayjs('2025-05-19T04:01'),
  status: 'CANCELLED',
  active: true,
  description: 'justly fondly',
  fileIdentifier: '9cc7c609-c6be-4e14-ac31-203ca72f7587',
  csvFileAttachment: '../fake-data/blob/hipster.png',
  csvFileAttachmentContentType: 'unknown',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
