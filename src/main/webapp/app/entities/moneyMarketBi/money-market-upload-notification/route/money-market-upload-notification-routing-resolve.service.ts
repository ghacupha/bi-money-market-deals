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

import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMoneyMarketUploadNotification } from '../money-market-upload-notification.model';
import { MoneyMarketUploadNotificationService } from '../service/money-market-upload-notification.service';

const moneyMarketUploadNotificationResolve = (route: ActivatedRouteSnapshot): Observable<null | IMoneyMarketUploadNotification> => {
  const id = route.params.id;
  if (id) {
    return inject(MoneyMarketUploadNotificationService)
      .find(id)
      .pipe(
        mergeMap((moneyMarketUploadNotification: HttpResponse<IMoneyMarketUploadNotification>) => {
          if (moneyMarketUploadNotification.body) {
            return of(moneyMarketUploadNotification.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default moneyMarketUploadNotificationResolve;
