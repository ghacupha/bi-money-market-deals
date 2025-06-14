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
import { ISecurityClearance, NewSecurityClearance } from '../security-clearance.model';

export type PartialUpdateSecurityClearance = Partial<ISecurityClearance> & Pick<ISecurityClearance, 'id'>;

export type EntityResponseType = HttpResponse<ISecurityClearance>;
export type EntityArrayResponseType = HttpResponse<ISecurityClearance[]>;

@Injectable({ providedIn: 'root' })
export class SecurityClearanceService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/security-clearances', 'moneymarketbi');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/security-clearances/_search', 'moneymarketbi');

  create(securityClearance: NewSecurityClearance): Observable<EntityResponseType> {
    return this.http.post<ISecurityClearance>(this.resourceUrl, securityClearance, { observe: 'response' });
  }

  update(securityClearance: ISecurityClearance): Observable<EntityResponseType> {
    return this.http.put<ISecurityClearance>(
      `${this.resourceUrl}/${this.getSecurityClearanceIdentifier(securityClearance)}`,
      securityClearance,
      { observe: 'response' },
    );
  }

  partialUpdate(securityClearance: PartialUpdateSecurityClearance): Observable<EntityResponseType> {
    return this.http.patch<ISecurityClearance>(
      `${this.resourceUrl}/${this.getSecurityClearanceIdentifier(securityClearance)}`,
      securityClearance,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISecurityClearance>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISecurityClearance[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISecurityClearance[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<ISecurityClearance[]>()], asapScheduler)));
  }

  getSecurityClearanceIdentifier(securityClearance: Pick<ISecurityClearance, 'id'>): number {
    return securityClearance.id;
  }

  compareSecurityClearance(o1: Pick<ISecurityClearance, 'id'> | null, o2: Pick<ISecurityClearance, 'id'> | null): boolean {
    return o1 && o2 ? this.getSecurityClearanceIdentifier(o1) === this.getSecurityClearanceIdentifier(o2) : o1 === o2;
  }

  addSecurityClearanceToCollectionIfMissing<Type extends Pick<ISecurityClearance, 'id'>>(
    securityClearanceCollection: Type[],
    ...securityClearancesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const securityClearances: Type[] = securityClearancesToCheck.filter(isPresent);
    if (securityClearances.length > 0) {
      const securityClearanceCollectionIdentifiers = securityClearanceCollection.map(securityClearanceItem =>
        this.getSecurityClearanceIdentifier(securityClearanceItem),
      );
      const securityClearancesToAdd = securityClearances.filter(securityClearanceItem => {
        const securityClearanceIdentifier = this.getSecurityClearanceIdentifier(securityClearanceItem);
        if (securityClearanceCollectionIdentifiers.includes(securityClearanceIdentifier)) {
          return false;
        }
        securityClearanceCollectionIdentifiers.push(securityClearanceIdentifier);
        return true;
      });
      return [...securityClearancesToAdd, ...securityClearanceCollection];
    }
    return securityClearanceCollection;
  }
}
