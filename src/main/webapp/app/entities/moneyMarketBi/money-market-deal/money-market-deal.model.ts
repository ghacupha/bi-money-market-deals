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
import { IMoneyMarketList } from 'app/entities/moneyMarketBi/money-market-list/money-market-list.model';

export interface IMoneyMarketDeal {
  id: number;
  dealNumber?: string | null;
  tradingBook?: string | null;
  counterPartyName?: string | null;
  finalInterestAccrualDate?: dayjs.Dayjs | null;
  counterPartySideType?: string | null;
  dateOfCollectionStatement?: string | null;
  currencyCode?: string | null;
  principalAmount?: number | null;
  interestRate?: number | null;
  interestAccruedAmount?: number | null;
  totalInterestAtMaturity?: number | null;
  counterpartyNationality?: string | null;
  endDate?: dayjs.Dayjs | null;
  treasuryLedger?: string | null;
  dealSubtype?: string | null;
  shillingEquivalentPrincipal?: number | null;
  shillingEquivalentInterestAccrued?: number | null;
  shillingEquivalentPVFull?: number | null;
  counterpartyDomicile?: string | null;
  settlementDate?: dayjs.Dayjs | null;
  transactionCollateral?: string | null;
  institutionType?: string | null;
  maturityDate?: dayjs.Dayjs | null;
  institutionReportName?: string | null;
  transactionType?: string | null;
  reportDate?: dayjs.Dayjs | null;
  active?: boolean | null;
  moneyMarketList?: Pick<IMoneyMarketList, 'id' | 'description'> | null;
}

export type NewMoneyMarketDeal = Omit<IMoneyMarketDeal, 'id'> & { id: null };
