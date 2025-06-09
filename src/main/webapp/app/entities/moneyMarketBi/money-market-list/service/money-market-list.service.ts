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
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IMoneyMarketList, NewMoneyMarketList } from '../money-market-list.model';

export type PartialUpdateMoneyMarketList = Partial<IMoneyMarketList> & Pick<IMoneyMarketList, 'id'>;

type RestOf<T extends IMoneyMarketList | NewMoneyMarketList> = Omit<T, 'reportDate' | 'uploadTimeStamp'> & {
  reportDate?: string | null;
  uploadTimeStamp?: string | null;
};

export type RestMoneyMarketList = RestOf<IMoneyMarketList>;

export type NewRestMoneyMarketList = RestOf<NewMoneyMarketList>;

export type PartialUpdateRestMoneyMarketList = RestOf<PartialUpdateMoneyMarketList>;

export type EntityResponseType = HttpResponse<IMoneyMarketList>;
export type EntityArrayResponseType = HttpResponse<IMoneyMarketList[]>;

@Injectable({ providedIn: 'root' })
export class MoneyMarketListService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/money-market-lists', 'moneymarketbi');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/money-market-lists/_search', 'moneymarketbi');

  create(moneyMarketList: NewMoneyMarketList): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(moneyMarketList);
    return this.http
      .post<RestMoneyMarketList>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(moneyMarketList: IMoneyMarketList): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(moneyMarketList);
    return this.http
      .put<RestMoneyMarketList>(`${this.resourceUrl}/${this.getMoneyMarketListIdentifier(moneyMarketList)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(moneyMarketList: PartialUpdateMoneyMarketList): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(moneyMarketList);
    return this.http
      .patch<RestMoneyMarketList>(`${this.resourceUrl}/${this.getMoneyMarketListIdentifier(moneyMarketList)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMoneyMarketList>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMoneyMarketList[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestMoneyMarketList[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IMoneyMarketList[]>()], asapScheduler)),
    );
  }

  getMoneyMarketListIdentifier(moneyMarketList: Pick<IMoneyMarketList, 'id'>): number {
    return moneyMarketList.id;
  }

  compareMoneyMarketList(o1: Pick<IMoneyMarketList, 'id'> | null, o2: Pick<IMoneyMarketList, 'id'> | null): boolean {
    return o1 && o2 ? this.getMoneyMarketListIdentifier(o1) === this.getMoneyMarketListIdentifier(o2) : o1 === o2;
  }

  addMoneyMarketListToCollectionIfMissing<Type extends Pick<IMoneyMarketList, 'id'>>(
    moneyMarketListCollection: Type[],
    ...moneyMarketListsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const moneyMarketLists: Type[] = moneyMarketListsToCheck.filter(isPresent);
    if (moneyMarketLists.length > 0) {
      const moneyMarketListCollectionIdentifiers = moneyMarketListCollection.map(moneyMarketListItem =>
        this.getMoneyMarketListIdentifier(moneyMarketListItem),
      );
      const moneyMarketListsToAdd = moneyMarketLists.filter(moneyMarketListItem => {
        const moneyMarketListIdentifier = this.getMoneyMarketListIdentifier(moneyMarketListItem);
        if (moneyMarketListCollectionIdentifiers.includes(moneyMarketListIdentifier)) {
          return false;
        }
        moneyMarketListCollectionIdentifiers.push(moneyMarketListIdentifier);
        return true;
      });
      return [...moneyMarketListsToAdd, ...moneyMarketListCollection];
    }
    return moneyMarketListCollection;
  }

  protected convertDateFromClient<T extends IMoneyMarketList | NewMoneyMarketList | PartialUpdateMoneyMarketList>(
    moneyMarketList: T,
  ): RestOf<T> {
    return {
      ...moneyMarketList,
      reportDate: moneyMarketList.reportDate?.format(DATE_FORMAT) ?? null,
      uploadTimeStamp: moneyMarketList.uploadTimeStamp?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMoneyMarketList: RestMoneyMarketList): IMoneyMarketList {
    return {
      ...restMoneyMarketList,
      reportDate: restMoneyMarketList.reportDate ? dayjs(restMoneyMarketList.reportDate) : undefined,
      uploadTimeStamp: restMoneyMarketList.uploadTimeStamp ? dayjs(restMoneyMarketList.uploadTimeStamp) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMoneyMarketList>): HttpResponse<IMoneyMarketList> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMoneyMarketList[]>): HttpResponse<IMoneyMarketList[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
