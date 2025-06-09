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

import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'dealer',
    data: { pageTitle: 'Dealers' },
    loadChildren: () => import('./moneyMarketBi/dealer/dealer.routes'),
  },
  {
    path: 'placeholder',
    data: { pageTitle: 'Placeholders' },
    loadChildren: () => import('./maintenance/placeholder/placeholder.routes'),
  },
  {
    path: 'security-clearance',
    data: { pageTitle: 'SecurityClearances' },
    loadChildren: () => import('./moneyMarketBi/security-clearance/security-clearance.routes'),
  },
  {
    path: 'application-user',
    data: { pageTitle: 'ApplicationUsers' },
    loadChildren: () => import('./maintenance/application-user/application-user.routes'),
  },
  {
    path: 'fiscal-year',
    data: { pageTitle: 'FiscalYears' },
    loadChildren: () => import('./maintenance/fiscal-year/fiscal-year.routes'),
  },
  {
    path: 'fiscal-quarter',
    data: { pageTitle: 'FiscalQuarters' },
    loadChildren: () => import('./maintenance/fiscal-quarter/fiscal-quarter.routes'),
  },
  {
    path: 'fiscal-month',
    data: { pageTitle: 'FiscalMonths' },
    loadChildren: () => import('./maintenance/fiscal-month/fiscal-month.routes'),
  },
  {
    path: 'money-market-deal',
    data: { pageTitle: 'MoneyMarketDeals' },
    loadChildren: () => import('./moneyMarketBi/money-market-deal/money-market-deal.routes'),
  },
  {
    path: 'report-batch',
    data: { pageTitle: 'ReportBatches' },
    loadChildren: () => import('./moneyMarketBi/report-batch/report-batch.routes'),
  },
  {
    path: 'money-market-list',
    data: { pageTitle: 'MoneyMarketLists' },
    loadChildren: () => import('./moneyMarketBi/money-market-list/money-market-list.routes'),
  },
  {
    path: 'money-market-upload-notification',
    data: { pageTitle: 'MoneyMarketUploadNotifications' },
    loadChildren: () => import('./moneyMarketBi/money-market-upload-notification/money-market-upload-notification.routes'),
  },
  {
    path: 'money-market-deal-daily-summary',
    data: { pageTitle: 'MoneyMarketDealDailySummaries' },
    loadChildren: () => import('./moneyMarketBi/money-market-deal-daily-summary/money-market-deal-daily-summary.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
