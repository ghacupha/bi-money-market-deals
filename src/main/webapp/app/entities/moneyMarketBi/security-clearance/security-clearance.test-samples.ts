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

import { ISecurityClearance, NewSecurityClearance } from './security-clearance.model';

export const sampleWithRequiredData: ISecurityClearance = {
  id: 14707,
  clearanceLevel: 'qua whereas',
  level: 2677,
};

export const sampleWithPartialData: ISecurityClearance = {
  id: 25697,
  clearanceLevel: 'hm pant sympathetically',
  level: 25862,
};

export const sampleWithFullData: ISecurityClearance = {
  id: 3309,
  clearanceLevel: 'along how',
  level: 535,
};

export const sampleWithNewData: NewSecurityClearance = {
  clearanceLevel: 'politely',
  level: 7910,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
