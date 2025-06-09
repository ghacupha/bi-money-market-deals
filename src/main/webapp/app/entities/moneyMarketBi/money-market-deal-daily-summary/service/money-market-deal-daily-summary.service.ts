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
import { IMoneyMarketDealDailySummary } from '../money-market-deal-daily-summary.model';

type RestOf<T extends IMoneyMarketDealDailySummary> = Omit<T, 'reportDate'> & {
  reportDate?: string | null;
};

export type RestMoneyMarketDealDailySummary = RestOf<IMoneyMarketDealDailySummary>;

export type EntityResponseType = HttpResponse<IMoneyMarketDealDailySummary>;
export type EntityArrayResponseType = HttpResponse<IMoneyMarketDealDailySummary[]>;

@Injectable({ providedIn: 'root' })
export class MoneyMarketDealDailySummaryService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/money-market-deal-daily-summaries', 'moneymarketbi');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor(
    'api/money-market-deal-daily-summaries/_search',
    'moneymarketbi',
  );

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMoneyMarketDealDailySummary>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMoneyMarketDealDailySummary[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestMoneyMarketDealDailySummary[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IMoneyMarketDealDailySummary[]>()], asapScheduler)),
    );
  }

  getMoneyMarketDealDailySummaryIdentifier(moneyMarketDealDailySummary: Pick<IMoneyMarketDealDailySummary, 'id'>): number {
    return moneyMarketDealDailySummary.id;
  }

  compareMoneyMarketDealDailySummary(
    o1: Pick<IMoneyMarketDealDailySummary, 'id'> | null,
    o2: Pick<IMoneyMarketDealDailySummary, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getMoneyMarketDealDailySummaryIdentifier(o1) === this.getMoneyMarketDealDailySummaryIdentifier(o2) : o1 === o2;
  }

  addMoneyMarketDealDailySummaryToCollectionIfMissing<Type extends Pick<IMoneyMarketDealDailySummary, 'id'>>(
    moneyMarketDealDailySummaryCollection: Type[],
    ...moneyMarketDealDailySummariesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const moneyMarketDealDailySummaries: Type[] = moneyMarketDealDailySummariesToCheck.filter(isPresent);
    if (moneyMarketDealDailySummaries.length > 0) {
      const moneyMarketDealDailySummaryCollectionIdentifiers = moneyMarketDealDailySummaryCollection.map(moneyMarketDealDailySummaryItem =>
        this.getMoneyMarketDealDailySummaryIdentifier(moneyMarketDealDailySummaryItem),
      );
      const moneyMarketDealDailySummariesToAdd = moneyMarketDealDailySummaries.filter(moneyMarketDealDailySummaryItem => {
        const moneyMarketDealDailySummaryIdentifier = this.getMoneyMarketDealDailySummaryIdentifier(moneyMarketDealDailySummaryItem);
        if (moneyMarketDealDailySummaryCollectionIdentifiers.includes(moneyMarketDealDailySummaryIdentifier)) {
          return false;
        }
        moneyMarketDealDailySummaryCollectionIdentifiers.push(moneyMarketDealDailySummaryIdentifier);
        return true;
      });
      return [...moneyMarketDealDailySummariesToAdd, ...moneyMarketDealDailySummaryCollection];
    }
    return moneyMarketDealDailySummaryCollection;
  }

  protected convertDateFromClient<T extends IMoneyMarketDealDailySummary>(moneyMarketDealDailySummary: T): RestOf<T> {
    return {
      ...moneyMarketDealDailySummary,
      reportDate: moneyMarketDealDailySummary.reportDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restMoneyMarketDealDailySummary: RestMoneyMarketDealDailySummary): IMoneyMarketDealDailySummary {
    return {
      ...restMoneyMarketDealDailySummary,
      reportDate: restMoneyMarketDealDailySummary.reportDate ? dayjs(restMoneyMarketDealDailySummary.reportDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMoneyMarketDealDailySummary>): HttpResponse<IMoneyMarketDealDailySummary> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(
    res: HttpResponse<RestMoneyMarketDealDailySummary[]>,
  ): HttpResponse<IMoneyMarketDealDailySummary[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
