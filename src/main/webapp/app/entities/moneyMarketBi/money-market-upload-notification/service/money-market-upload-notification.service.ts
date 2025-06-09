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

import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IMoneyMarketUploadNotification, NewMoneyMarketUploadNotification } from '../money-market-upload-notification.model';

export type PartialUpdateMoneyMarketUploadNotification = Partial<IMoneyMarketUploadNotification> &
  Pick<IMoneyMarketUploadNotification, 'id'>;

export type EntityResponseType = HttpResponse<IMoneyMarketUploadNotification>;
export type EntityArrayResponseType = HttpResponse<IMoneyMarketUploadNotification[]>;

@Injectable({ providedIn: 'root' })
export class MoneyMarketUploadNotificationService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/money-market-upload-notifications', 'moneymarketbi');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor(
    'api/money-market-upload-notifications/_search',
    'moneymarketbi',
  );

  create(moneyMarketUploadNotification: NewMoneyMarketUploadNotification): Observable<EntityResponseType> {
    return this.http.post<IMoneyMarketUploadNotification>(this.resourceUrl, moneyMarketUploadNotification, { observe: 'response' });
  }

  update(moneyMarketUploadNotification: IMoneyMarketUploadNotification): Observable<EntityResponseType> {
    return this.http.put<IMoneyMarketUploadNotification>(
      `${this.resourceUrl}/${this.getMoneyMarketUploadNotificationIdentifier(moneyMarketUploadNotification)}`,
      moneyMarketUploadNotification,
      { observe: 'response' },
    );
  }

  partialUpdate(moneyMarketUploadNotification: PartialUpdateMoneyMarketUploadNotification): Observable<EntityResponseType> {
    return this.http.patch<IMoneyMarketUploadNotification>(
      `${this.resourceUrl}/${this.getMoneyMarketUploadNotificationIdentifier(moneyMarketUploadNotification)}`,
      moneyMarketUploadNotification,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMoneyMarketUploadNotification>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMoneyMarketUploadNotification[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMoneyMarketUploadNotification[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IMoneyMarketUploadNotification[]>()], asapScheduler)));
  }

  getMoneyMarketUploadNotificationIdentifier(moneyMarketUploadNotification: Pick<IMoneyMarketUploadNotification, 'id'>): number {
    return moneyMarketUploadNotification.id;
  }

  compareMoneyMarketUploadNotification(
    o1: Pick<IMoneyMarketUploadNotification, 'id'> | null,
    o2: Pick<IMoneyMarketUploadNotification, 'id'> | null,
  ): boolean {
    return o1 && o2
      ? this.getMoneyMarketUploadNotificationIdentifier(o1) === this.getMoneyMarketUploadNotificationIdentifier(o2)
      : o1 === o2;
  }

  addMoneyMarketUploadNotificationToCollectionIfMissing<Type extends Pick<IMoneyMarketUploadNotification, 'id'>>(
    moneyMarketUploadNotificationCollection: Type[],
    ...moneyMarketUploadNotificationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const moneyMarketUploadNotifications: Type[] = moneyMarketUploadNotificationsToCheck.filter(isPresent);
    if (moneyMarketUploadNotifications.length > 0) {
      const moneyMarketUploadNotificationCollectionIdentifiers = moneyMarketUploadNotificationCollection.map(
        moneyMarketUploadNotificationItem => this.getMoneyMarketUploadNotificationIdentifier(moneyMarketUploadNotificationItem),
      );
      const moneyMarketUploadNotificationsToAdd = moneyMarketUploadNotifications.filter(moneyMarketUploadNotificationItem => {
        const moneyMarketUploadNotificationIdentifier = this.getMoneyMarketUploadNotificationIdentifier(moneyMarketUploadNotificationItem);
        if (moneyMarketUploadNotificationCollectionIdentifiers.includes(moneyMarketUploadNotificationIdentifier)) {
          return false;
        }
        moneyMarketUploadNotificationCollectionIdentifiers.push(moneyMarketUploadNotificationIdentifier);
        return true;
      });
      return [...moneyMarketUploadNotificationsToAdd, ...moneyMarketUploadNotificationCollection];
    }
    return moneyMarketUploadNotificationCollection;
  }
}
