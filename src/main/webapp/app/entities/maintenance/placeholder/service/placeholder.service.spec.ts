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

import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPlaceholder } from '../placeholder.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../placeholder.test-samples';

import { PlaceholderService } from './placeholder.service';

const requireRestSample: IPlaceholder = {
  ...sampleWithRequiredData,
};

describe('Placeholder Service', () => {
  let service: PlaceholderService;
  let httpMock: HttpTestingController;
  let expectedResult: IPlaceholder | IPlaceholder[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PlaceholderService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Placeholder', () => {
      const placeholder = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(placeholder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Placeholder', () => {
      const placeholder = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(placeholder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Placeholder', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Placeholder', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Placeholder', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Placeholder', () => {
      const queryObject: any = {
        page: 0,
        size: 20,
        query: '',
        sort: [],
      };
      service.search(queryObject).subscribe(() => expectedResult);

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(null, { status: 500, statusText: 'Internal Server Error' });
      expect(expectedResult).toBe(null);
    });

    describe('addPlaceholderToCollectionIfMissing', () => {
      it('should add a Placeholder to an empty array', () => {
        const placeholder: IPlaceholder = sampleWithRequiredData;
        expectedResult = service.addPlaceholderToCollectionIfMissing([], placeholder);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(placeholder);
      });

      it('should not add a Placeholder to an array that contains it', () => {
        const placeholder: IPlaceholder = sampleWithRequiredData;
        const placeholderCollection: IPlaceholder[] = [
          {
            ...placeholder,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPlaceholderToCollectionIfMissing(placeholderCollection, placeholder);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Placeholder to an array that doesn't contain it", () => {
        const placeholder: IPlaceholder = sampleWithRequiredData;
        const placeholderCollection: IPlaceholder[] = [sampleWithPartialData];
        expectedResult = service.addPlaceholderToCollectionIfMissing(placeholderCollection, placeholder);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(placeholder);
      });

      it('should add only unique Placeholder to an array', () => {
        const placeholderArray: IPlaceholder[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const placeholderCollection: IPlaceholder[] = [sampleWithRequiredData];
        expectedResult = service.addPlaceholderToCollectionIfMissing(placeholderCollection, ...placeholderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const placeholder: IPlaceholder = sampleWithRequiredData;
        const placeholder2: IPlaceholder = sampleWithPartialData;
        expectedResult = service.addPlaceholderToCollectionIfMissing([], placeholder, placeholder2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(placeholder);
        expect(expectedResult).toContain(placeholder2);
      });

      it('should accept null and undefined values', () => {
        const placeholder: IPlaceholder = sampleWithRequiredData;
        expectedResult = service.addPlaceholderToCollectionIfMissing([], null, placeholder, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(placeholder);
      });

      it('should return initial array if no Placeholder is added', () => {
        const placeholderCollection: IPlaceholder[] = [sampleWithRequiredData];
        expectedResult = service.addPlaceholderToCollectionIfMissing(placeholderCollection, undefined, null);
        expect(expectedResult).toEqual(placeholderCollection);
      });
    });

    describe('comparePlaceholder', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePlaceholder(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 13408 };
        const entity2 = null;

        const compareResult1 = service.comparePlaceholder(entity1, entity2);
        const compareResult2 = service.comparePlaceholder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 13408 };
        const entity2 = { id: 24257 };

        const compareResult1 = service.comparePlaceholder(entity1, entity2);
        const compareResult2 = service.comparePlaceholder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 13408 };
        const entity2 = { id: 13408 };

        const compareResult1 = service.comparePlaceholder(entity1, entity2);
        const compareResult2 = service.comparePlaceholder(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
