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
import { IReportBatch, NewReportBatch } from '../report-batch.model';

export type PartialUpdateReportBatch = Partial<IReportBatch> & Pick<IReportBatch, 'id'>;

type RestOf<T extends IReportBatch | NewReportBatch> = Omit<T, 'reportDate' | 'uploadTimeStamp'> & {
  reportDate?: string | null;
  uploadTimeStamp?: string | null;
};

export type RestReportBatch = RestOf<IReportBatch>;

export type NewRestReportBatch = RestOf<NewReportBatch>;

export type PartialUpdateRestReportBatch = RestOf<PartialUpdateReportBatch>;

export type EntityResponseType = HttpResponse<IReportBatch>;
export type EntityArrayResponseType = HttpResponse<IReportBatch[]>;

@Injectable({ providedIn: 'root' })
export class ReportBatchService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/report-batches', 'moneymarketbi');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/report-batches/_search', 'moneymarketbi');

  create(reportBatch: NewReportBatch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reportBatch);
    return this.http
      .post<RestReportBatch>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(reportBatch: IReportBatch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reportBatch);
    return this.http
      .put<RestReportBatch>(`${this.resourceUrl}/${this.getReportBatchIdentifier(reportBatch)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(reportBatch: PartialUpdateReportBatch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reportBatch);
    return this.http
      .patch<RestReportBatch>(`${this.resourceUrl}/${this.getReportBatchIdentifier(reportBatch)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestReportBatch>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestReportBatch[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestReportBatch[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IReportBatch[]>()], asapScheduler)),
    );
  }

  getReportBatchIdentifier(reportBatch: Pick<IReportBatch, 'id'>): number {
    return reportBatch.id;
  }

  compareReportBatch(o1: Pick<IReportBatch, 'id'> | null, o2: Pick<IReportBatch, 'id'> | null): boolean {
    return o1 && o2 ? this.getReportBatchIdentifier(o1) === this.getReportBatchIdentifier(o2) : o1 === o2;
  }

  addReportBatchToCollectionIfMissing<Type extends Pick<IReportBatch, 'id'>>(
    reportBatchCollection: Type[],
    ...reportBatchesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const reportBatches: Type[] = reportBatchesToCheck.filter(isPresent);
    if (reportBatches.length > 0) {
      const reportBatchCollectionIdentifiers = reportBatchCollection.map(reportBatchItem => this.getReportBatchIdentifier(reportBatchItem));
      const reportBatchesToAdd = reportBatches.filter(reportBatchItem => {
        const reportBatchIdentifier = this.getReportBatchIdentifier(reportBatchItem);
        if (reportBatchCollectionIdentifiers.includes(reportBatchIdentifier)) {
          return false;
        }
        reportBatchCollectionIdentifiers.push(reportBatchIdentifier);
        return true;
      });
      return [...reportBatchesToAdd, ...reportBatchCollection];
    }
    return reportBatchCollection;
  }

  protected convertDateFromClient<T extends IReportBatch | NewReportBatch | PartialUpdateReportBatch>(reportBatch: T): RestOf<T> {
    return {
      ...reportBatch,
      reportDate: reportBatch.reportDate?.format(DATE_FORMAT) ?? null,
      uploadTimeStamp: reportBatch.uploadTimeStamp?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restReportBatch: RestReportBatch): IReportBatch {
    return {
      ...restReportBatch,
      reportDate: restReportBatch.reportDate ? dayjs(restReportBatch.reportDate) : undefined,
      uploadTimeStamp: restReportBatch.uploadTimeStamp ? dayjs(restReportBatch.uploadTimeStamp) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestReportBatch>): HttpResponse<IReportBatch> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestReportBatch[]>): HttpResponse<IReportBatch[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
