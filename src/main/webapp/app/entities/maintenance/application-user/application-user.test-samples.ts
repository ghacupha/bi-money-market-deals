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

import { IApplicationUser, NewApplicationUser } from './application-user.model';

export const sampleWithRequiredData: IApplicationUser = {
  id: 15785,
  designation: '6c1464be-8d05-424d-a876-e7287b68cbba',
  applicationIdentity: 'affectionate',
};

export const sampleWithPartialData: IApplicationUser = {
  id: 18551,
  designation: '9686dc8d-b3fa-4f18-8841-c002ca38f95f',
  applicationIdentity: 'vaguely trustworthy encouragement',
};

export const sampleWithFullData: IApplicationUser = {
  id: 9548,
  designation: '65a3281e-bac5-4191-ae44-5433dd70a358',
  applicationIdentity: 'yet healthily vastly',
};

export const sampleWithNewData: NewApplicationUser = {
  designation: 'b22348c3-cf7f-47a7-98da-36a42f15061f',
  applicationIdentity: 'afore',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
