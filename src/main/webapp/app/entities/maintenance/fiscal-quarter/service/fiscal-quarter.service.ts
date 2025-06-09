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
import { IFiscalQuarter, NewFiscalQuarter } from '../fiscal-quarter.model';

export type PartialUpdateFiscalQuarter = Partial<IFiscalQuarter> & Pick<IFiscalQuarter, 'id'>;

type RestOf<T extends IFiscalQuarter | NewFiscalQuarter> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestFiscalQuarter = RestOf<IFiscalQuarter>;

export type NewRestFiscalQuarter = RestOf<NewFiscalQuarter>;

export type PartialUpdateRestFiscalQuarter = RestOf<PartialUpdateFiscalQuarter>;

export type EntityResponseType = HttpResponse<IFiscalQuarter>;
export type EntityArrayResponseType = HttpResponse<IFiscalQuarter[]>;

@Injectable({ providedIn: 'root' })
export class FiscalQuarterService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fiscal-quarters', 'moneymarketbi');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/fiscal-quarters/_search', 'moneymarketbi');

  create(fiscalQuarter: NewFiscalQuarter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fiscalQuarter);
    return this.http
      .post<RestFiscalQuarter>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(fiscalQuarter: IFiscalQuarter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fiscalQuarter);
    return this.http
      .put<RestFiscalQuarter>(`${this.resourceUrl}/${this.getFiscalQuarterIdentifier(fiscalQuarter)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(fiscalQuarter: PartialUpdateFiscalQuarter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fiscalQuarter);
    return this.http
      .patch<RestFiscalQuarter>(`${this.resourceUrl}/${this.getFiscalQuarterIdentifier(fiscalQuarter)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFiscalQuarter>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFiscalQuarter[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestFiscalQuarter[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IFiscalQuarter[]>()], asapScheduler)),
    );
  }

  getFiscalQuarterIdentifier(fiscalQuarter: Pick<IFiscalQuarter, 'id'>): number {
    return fiscalQuarter.id;
  }

  compareFiscalQuarter(o1: Pick<IFiscalQuarter, 'id'> | null, o2: Pick<IFiscalQuarter, 'id'> | null): boolean {
    return o1 && o2 ? this.getFiscalQuarterIdentifier(o1) === this.getFiscalQuarterIdentifier(o2) : o1 === o2;
  }

  addFiscalQuarterToCollectionIfMissing<Type extends Pick<IFiscalQuarter, 'id'>>(
    fiscalQuarterCollection: Type[],
    ...fiscalQuartersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fiscalQuarters: Type[] = fiscalQuartersToCheck.filter(isPresent);
    if (fiscalQuarters.length > 0) {
      const fiscalQuarterCollectionIdentifiers = fiscalQuarterCollection.map(fiscalQuarterItem =>
        this.getFiscalQuarterIdentifier(fiscalQuarterItem),
      );
      const fiscalQuartersToAdd = fiscalQuarters.filter(fiscalQuarterItem => {
        const fiscalQuarterIdentifier = this.getFiscalQuarterIdentifier(fiscalQuarterItem);
        if (fiscalQuarterCollectionIdentifiers.includes(fiscalQuarterIdentifier)) {
          return false;
        }
        fiscalQuarterCollectionIdentifiers.push(fiscalQuarterIdentifier);
        return true;
      });
      return [...fiscalQuartersToAdd, ...fiscalQuarterCollection];
    }
    return fiscalQuarterCollection;
  }

  protected convertDateFromClient<T extends IFiscalQuarter | NewFiscalQuarter | PartialUpdateFiscalQuarter>(fiscalQuarter: T): RestOf<T> {
    return {
      ...fiscalQuarter,
      startDate: fiscalQuarter.startDate?.format(DATE_FORMAT) ?? null,
      endDate: fiscalQuarter.endDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restFiscalQuarter: RestFiscalQuarter): IFiscalQuarter {
    return {
      ...restFiscalQuarter,
      startDate: restFiscalQuarter.startDate ? dayjs(restFiscalQuarter.startDate) : undefined,
      endDate: restFiscalQuarter.endDate ? dayjs(restFiscalQuarter.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFiscalQuarter>): HttpResponse<IFiscalQuarter> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFiscalQuarter[]>): HttpResponse<IFiscalQuarter[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
