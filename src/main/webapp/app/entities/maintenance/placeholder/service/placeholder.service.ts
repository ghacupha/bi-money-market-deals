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
import { IPlaceholder, NewPlaceholder } from '../placeholder.model';

export type PartialUpdatePlaceholder = Partial<IPlaceholder> & Pick<IPlaceholder, 'id'>;

export type EntityResponseType = HttpResponse<IPlaceholder>;
export type EntityArrayResponseType = HttpResponse<IPlaceholder[]>;

@Injectable({ providedIn: 'root' })
export class PlaceholderService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/placeholders', 'moneymarketbi');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/placeholders/_search', 'moneymarketbi');

  create(placeholder: NewPlaceholder): Observable<EntityResponseType> {
    return this.http.post<IPlaceholder>(this.resourceUrl, placeholder, { observe: 'response' });
  }

  update(placeholder: IPlaceholder): Observable<EntityResponseType> {
    return this.http.put<IPlaceholder>(`${this.resourceUrl}/${this.getPlaceholderIdentifier(placeholder)}`, placeholder, {
      observe: 'response',
    });
  }

  partialUpdate(placeholder: PartialUpdatePlaceholder): Observable<EntityResponseType> {
    return this.http.patch<IPlaceholder>(`${this.resourceUrl}/${this.getPlaceholderIdentifier(placeholder)}`, placeholder, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPlaceholder>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlaceholder[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPlaceholder[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IPlaceholder[]>()], asapScheduler)));
  }

  getPlaceholderIdentifier(placeholder: Pick<IPlaceholder, 'id'>): number {
    return placeholder.id;
  }

  comparePlaceholder(o1: Pick<IPlaceholder, 'id'> | null, o2: Pick<IPlaceholder, 'id'> | null): boolean {
    return o1 && o2 ? this.getPlaceholderIdentifier(o1) === this.getPlaceholderIdentifier(o2) : o1 === o2;
  }

  addPlaceholderToCollectionIfMissing<Type extends Pick<IPlaceholder, 'id'>>(
    placeholderCollection: Type[],
    ...placeholdersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const placeholders: Type[] = placeholdersToCheck.filter(isPresent);
    if (placeholders.length > 0) {
      const placeholderCollectionIdentifiers = placeholderCollection.map(placeholderItem => this.getPlaceholderIdentifier(placeholderItem));
      const placeholdersToAdd = placeholders.filter(placeholderItem => {
        const placeholderIdentifier = this.getPlaceholderIdentifier(placeholderItem);
        if (placeholderCollectionIdentifiers.includes(placeholderIdentifier)) {
          return false;
        }
        placeholderCollectionIdentifiers.push(placeholderIdentifier);
        return true;
      });
      return [...placeholdersToAdd, ...placeholderCollection];
    }
    return placeholderCollection;
  }
}
