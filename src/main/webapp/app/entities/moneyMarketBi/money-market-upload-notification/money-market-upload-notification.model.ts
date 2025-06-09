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

import { IMoneyMarketList } from 'app/entities/moneyMarketBi/money-market-list/money-market-list.model';
import { IReportBatch } from 'app/entities/moneyMarketBi/report-batch/report-batch.model';
import { IPlaceholder } from 'app/entities/maintenance/placeholder/placeholder.model';

export interface IMoneyMarketUploadNotification {
  id: number;
  errorMessage?: string | null;
  rowNumber?: number | null;
  referenceNumber?: string | null;
  moneyMarketList?: Pick<IMoneyMarketList, 'id' | 'description'> | null;
  reportBatch?: Pick<IReportBatch, 'id' | 'description'> | null;
  placeholders?: Pick<IPlaceholder, 'id' | 'token'>[] | null;
}

export type NewMoneyMarketUploadNotification = Omit<IMoneyMarketUploadNotification, 'id'> & { id: null };
