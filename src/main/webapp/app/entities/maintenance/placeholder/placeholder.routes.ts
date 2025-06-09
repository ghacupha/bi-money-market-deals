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

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PlaceholderResolve from './route/placeholder-routing-resolve.service';

const placeholderRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/placeholder.component').then(m => m.PlaceholderComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/placeholder-detail.component').then(m => m.PlaceholderDetailComponent),
    resolve: {
      placeholder: PlaceholderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/placeholder-update.component').then(m => m.PlaceholderUpdateComponent),
    resolve: {
      placeholder: PlaceholderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/placeholder-update.component').then(m => m.PlaceholderUpdateComponent),
    resolve: {
      placeholder: PlaceholderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default placeholderRoute;
