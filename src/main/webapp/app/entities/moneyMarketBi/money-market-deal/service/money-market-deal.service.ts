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
import { IMoneyMarketDeal, NewMoneyMarketDeal } from '../money-market-deal.model';

export type PartialUpdateMoneyMarketDeal = Partial<IMoneyMarketDeal> & Pick<IMoneyMarketDeal, 'id'>;

type RestOf<T extends IMoneyMarketDeal | NewMoneyMarketDeal> = Omit<
  T,
  'finalInterestAccrualDate' | 'endDate' | 'settlementDate' | 'maturityDate' | 'reportDate'
> & {
  finalInterestAccrualDate?: string | null;
  endDate?: string | null;
  settlementDate?: string | null;
  maturityDate?: string | null;
  reportDate?: string | null;
};

export type RestMoneyMarketDeal = RestOf<IMoneyMarketDeal>;

export type NewRestMoneyMarketDeal = RestOf<NewMoneyMarketDeal>;

export type PartialUpdateRestMoneyMarketDeal = RestOf<PartialUpdateMoneyMarketDeal>;

export type EntityResponseType = HttpResponse<IMoneyMarketDeal>;
export type EntityArrayResponseType = HttpResponse<IMoneyMarketDeal[]>;

@Injectable({ providedIn: 'root' })
export class MoneyMarketDealService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/money-market-deals', 'moneymarketbi');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/money-market-deals/_search', 'moneymarketbi');

  create(moneyMarketDeal: NewMoneyMarketDeal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(moneyMarketDeal);
    return this.http
      .post<RestMoneyMarketDeal>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(moneyMarketDeal: IMoneyMarketDeal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(moneyMarketDeal);
    return this.http
      .put<RestMoneyMarketDeal>(`${this.resourceUrl}/${this.getMoneyMarketDealIdentifier(moneyMarketDeal)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(moneyMarketDeal: PartialUpdateMoneyMarketDeal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(moneyMarketDeal);
    return this.http
      .patch<RestMoneyMarketDeal>(`${this.resourceUrl}/${this.getMoneyMarketDealIdentifier(moneyMarketDeal)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMoneyMarketDeal>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMoneyMarketDeal[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestMoneyMarketDeal[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IMoneyMarketDeal[]>()], asapScheduler)),
    );
  }

  getMoneyMarketDealIdentifier(moneyMarketDeal: Pick<IMoneyMarketDeal, 'id'>): number {
    return moneyMarketDeal.id;
  }

  compareMoneyMarketDeal(o1: Pick<IMoneyMarketDeal, 'id'> | null, o2: Pick<IMoneyMarketDeal, 'id'> | null): boolean {
    return o1 && o2 ? this.getMoneyMarketDealIdentifier(o1) === this.getMoneyMarketDealIdentifier(o2) : o1 === o2;
  }

  addMoneyMarketDealToCollectionIfMissing<Type extends Pick<IMoneyMarketDeal, 'id'>>(
    moneyMarketDealCollection: Type[],
    ...moneyMarketDealsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const moneyMarketDeals: Type[] = moneyMarketDealsToCheck.filter(isPresent);
    if (moneyMarketDeals.length > 0) {
      const moneyMarketDealCollectionIdentifiers = moneyMarketDealCollection.map(moneyMarketDealItem =>
        this.getMoneyMarketDealIdentifier(moneyMarketDealItem),
      );
      const moneyMarketDealsToAdd = moneyMarketDeals.filter(moneyMarketDealItem => {
        const moneyMarketDealIdentifier = this.getMoneyMarketDealIdentifier(moneyMarketDealItem);
        if (moneyMarketDealCollectionIdentifiers.includes(moneyMarketDealIdentifier)) {
          return false;
        }
        moneyMarketDealCollectionIdentifiers.push(moneyMarketDealIdentifier);
        return true;
      });
      return [...moneyMarketDealsToAdd, ...moneyMarketDealCollection];
    }
    return moneyMarketDealCollection;
  }

  protected convertDateFromClient<T extends IMoneyMarketDeal | NewMoneyMarketDeal | PartialUpdateMoneyMarketDeal>(
    moneyMarketDeal: T,
  ): RestOf<T> {
    return {
      ...moneyMarketDeal,
      finalInterestAccrualDate: moneyMarketDeal.finalInterestAccrualDate?.format(DATE_FORMAT) ?? null,
      endDate: moneyMarketDeal.endDate?.format(DATE_FORMAT) ?? null,
      settlementDate: moneyMarketDeal.settlementDate?.format(DATE_FORMAT) ?? null,
      maturityDate: moneyMarketDeal.maturityDate?.format(DATE_FORMAT) ?? null,
      reportDate: moneyMarketDeal.reportDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restMoneyMarketDeal: RestMoneyMarketDeal): IMoneyMarketDeal {
    return {
      ...restMoneyMarketDeal,
      finalInterestAccrualDate: restMoneyMarketDeal.finalInterestAccrualDate
        ? dayjs(restMoneyMarketDeal.finalInterestAccrualDate)
        : undefined,
      endDate: restMoneyMarketDeal.endDate ? dayjs(restMoneyMarketDeal.endDate) : undefined,
      settlementDate: restMoneyMarketDeal.settlementDate ? dayjs(restMoneyMarketDeal.settlementDate) : undefined,
      maturityDate: restMoneyMarketDeal.maturityDate ? dayjs(restMoneyMarketDeal.maturityDate) : undefined,
      reportDate: restMoneyMarketDeal.reportDate ? dayjs(restMoneyMarketDeal.reportDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMoneyMarketDeal>): HttpResponse<IMoneyMarketDeal> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMoneyMarketDeal[]>): HttpResponse<IMoneyMarketDeal[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
