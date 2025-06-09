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

import { DATE_FORMAT } from 'app/config/input.constants';
import { IFiscalYear } from '../fiscal-year.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../fiscal-year.test-samples';

import { FiscalYearService, RestFiscalYear } from './fiscal-year.service';

const requireRestSample: RestFiscalYear = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
};

describe('FiscalYear Service', () => {
  let service: FiscalYearService;
  let httpMock: HttpTestingController;
  let expectedResult: IFiscalYear | IFiscalYear[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FiscalYearService);
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

    it('should create a FiscalYear', () => {
      const fiscalYear = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(fiscalYear).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FiscalYear', () => {
      const fiscalYear = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(fiscalYear).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FiscalYear', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FiscalYear', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FiscalYear', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a FiscalYear', () => {
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

    describe('addFiscalYearToCollectionIfMissing', () => {
      it('should add a FiscalYear to an empty array', () => {
        const fiscalYear: IFiscalYear = sampleWithRequiredData;
        expectedResult = service.addFiscalYearToCollectionIfMissing([], fiscalYear);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fiscalYear);
      });

      it('should not add a FiscalYear to an array that contains it', () => {
        const fiscalYear: IFiscalYear = sampleWithRequiredData;
        const fiscalYearCollection: IFiscalYear[] = [
          {
            ...fiscalYear,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFiscalYearToCollectionIfMissing(fiscalYearCollection, fiscalYear);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FiscalYear to an array that doesn't contain it", () => {
        const fiscalYear: IFiscalYear = sampleWithRequiredData;
        const fiscalYearCollection: IFiscalYear[] = [sampleWithPartialData];
        expectedResult = service.addFiscalYearToCollectionIfMissing(fiscalYearCollection, fiscalYear);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fiscalYear);
      });

      it('should add only unique FiscalYear to an array', () => {
        const fiscalYearArray: IFiscalYear[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const fiscalYearCollection: IFiscalYear[] = [sampleWithRequiredData];
        expectedResult = service.addFiscalYearToCollectionIfMissing(fiscalYearCollection, ...fiscalYearArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fiscalYear: IFiscalYear = sampleWithRequiredData;
        const fiscalYear2: IFiscalYear = sampleWithPartialData;
        expectedResult = service.addFiscalYearToCollectionIfMissing([], fiscalYear, fiscalYear2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fiscalYear);
        expect(expectedResult).toContain(fiscalYear2);
      });

      it('should accept null and undefined values', () => {
        const fiscalYear: IFiscalYear = sampleWithRequiredData;
        expectedResult = service.addFiscalYearToCollectionIfMissing([], null, fiscalYear, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fiscalYear);
      });

      it('should return initial array if no FiscalYear is added', () => {
        const fiscalYearCollection: IFiscalYear[] = [sampleWithRequiredData];
        expectedResult = service.addFiscalYearToCollectionIfMissing(fiscalYearCollection, undefined, null);
        expect(expectedResult).toEqual(fiscalYearCollection);
      });
    });

    describe('compareFiscalYear', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFiscalYear(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 1297 };
        const entity2 = null;

        const compareResult1 = service.compareFiscalYear(entity1, entity2);
        const compareResult2 = service.compareFiscalYear(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 1297 };
        const entity2 = { id: 1005 };

        const compareResult1 = service.compareFiscalYear(entity1, entity2);
        const compareResult2 = service.compareFiscalYear(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 1297 };
        const entity2 = { id: 1297 };

        const compareResult1 = service.compareFiscalYear(entity1, entity2);
        const compareResult2 = service.compareFiscalYear(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
