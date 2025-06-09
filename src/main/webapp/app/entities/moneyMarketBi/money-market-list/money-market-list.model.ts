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
import { IPlaceholder } from 'app/entities/maintenance/placeholder/placeholder.model';
import { IApplicationUser } from 'app/entities/maintenance/application-user/application-user.model';
import { IReportBatch } from 'app/entities/moneyMarketBi/report-batch/report-batch.model';
import { reportBatchStatus } from 'app/entities/enumerations/report-batch-status.model';

export interface IMoneyMarketList {
  id: number;
  reportDate?: dayjs.Dayjs | null;
  uploadTimeStamp?: dayjs.Dayjs | null;
  status?: keyof typeof reportBatchStatus | null;
  description?: string | null;
  active?: boolean | null;
  placeholders?: Pick<IPlaceholder, 'id' | 'token'>[] | null;
  uploadedBy?: Pick<IApplicationUser, 'id' | 'applicationIdentity'> | null;
  reportBatch?: Pick<IReportBatch, 'id' | 'description'> | null;
}

export type NewMoneyMarketList = Omit<IMoneyMarketList, 'id'> & { id: null };
