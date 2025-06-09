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

import { IDealer } from 'app/entities/moneyMarketBi/dealer/dealer.model';
import { ISecurityClearance } from 'app/entities/moneyMarketBi/security-clearance/security-clearance.model';
import { IApplicationUser } from 'app/entities/maintenance/application-user/application-user.model';
import { IFiscalYear } from 'app/entities/maintenance/fiscal-year/fiscal-year.model';
import { IFiscalQuarter } from 'app/entities/maintenance/fiscal-quarter/fiscal-quarter.model';
import { IFiscalMonth } from 'app/entities/maintenance/fiscal-month/fiscal-month.model';
import { IReportBatch } from 'app/entities/moneyMarketBi/report-batch/report-batch.model';
import { IMoneyMarketList } from 'app/entities/moneyMarketBi/money-market-list/money-market-list.model';
import { IMoneyMarketUploadNotification } from 'app/entities/moneyMarketBi/money-market-upload-notification/money-market-upload-notification.model';

export interface IPlaceholder {
  id: number;
  description?: string | null;
  token?: string | null;
  containingPlaceholder?: Pick<IPlaceholder, 'id' | 'description'> | null;
  dealers?: Pick<IDealer, 'id'>[] | null;
  securityClearances?: Pick<ISecurityClearance, 'id'>[] | null;
  applicationUsers?: Pick<IApplicationUser, 'id'>[] | null;
  fiscalYears?: Pick<IFiscalYear, 'id'>[] | null;
  fiscalQuarters?: Pick<IFiscalQuarter, 'id'>[] | null;
  fiscalMonths?: Pick<IFiscalMonth, 'id'>[] | null;
  reportBatches?: Pick<IReportBatch, 'id'>[] | null;
  moneyMarketLists?: Pick<IMoneyMarketList, 'id'>[] | null;
  moneyMarketUploadNotifications?: Pick<IMoneyMarketUploadNotification, 'id'>[] | null;
}

export type NewPlaceholder = Omit<IPlaceholder, 'id'> & { id: null };
