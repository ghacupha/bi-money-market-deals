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

import { IFiscalYear } from 'app/entities/maintenance/fiscal-year/fiscal-year.model';
import { IFiscalQuarter } from 'app/entities/maintenance/fiscal-quarter/fiscal-quarter.model';
import { IFiscalMonth } from 'app/entities/maintenance/fiscal-month/fiscal-month.model';
import { IMoneyMarketList } from 'app/entities/moneyMarketBi/money-market-list/money-market-list.model';

export interface IPlaceholder {
  id: number;
  description?: string | null;
  token?: string | null;
  containingPlaceholder?: Pick<IPlaceholder, 'id' | 'description'> | null;
  fiscalYears?: Pick<IFiscalYear, 'id'>[] | null;
  fiscalQuarters?: Pick<IFiscalQuarter, 'id'>[] | null;
  fiscalMonths?: Pick<IFiscalMonth, 'id'>[] | null;
  moneyMarketLists?: Pick<IMoneyMarketList, 'id'>[] | null;
}

export type NewPlaceholder = Omit<IPlaceholder, 'id'> & { id: null };
