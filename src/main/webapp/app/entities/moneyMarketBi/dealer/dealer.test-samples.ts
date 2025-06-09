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

import { IDealer, NewDealer } from './dealer.model';

export const sampleWithRequiredData: IDealer = {
  id: 22851,
  dealerName: 'brr',
};

export const sampleWithPartialData: IDealer = {
  id: 29031,
  dealerName: 'helpfully',
  taxNumber: 'upon hmph',
  organizationName: 'modulo ideal yak',
  department: 'standard range uncomfortable',
  postalAddress: 'jury upward',
  accountName: 'Savings Account',
  accountNumber: 'besides',
  bankersName: 'unaccountably worthwhile igloo',
  remarks: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IDealer = {
  id: 4426,
  dealerName: 'against',
  taxNumber: 'creative yum when',
  identificationDocumentNumber: 'usher unaware bad',
  organizationName: 'other',
  department: 'whether',
  position: 'second',
  postalAddress: 'fat neglected',
  physicalAddress: 'meh',
  accountName: 'Money Market Account',
  accountNumber: 'redesign cleverly almighty',
  bankersName: 'refute except',
  bankersBranch: 'mostly bleak',
  bankersSwiftCode: 'handy as bah',
  fileUploadToken: 'amongst',
  compilationToken: 'whereas retrospectivity',
  remarks: '../fake-data/blob/hipster.txt',
  otherNames: 'across whimsical fussy',
};

export const sampleWithNewData: NewDealer = {
  dealerName: 'foolish',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
