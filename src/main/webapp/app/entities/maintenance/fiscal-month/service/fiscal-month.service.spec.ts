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
import { IFiscalMonth } from '../fiscal-month.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../fiscal-month.test-samples';

import { FiscalMonthService, RestFiscalMonth } from './fiscal-month.service';

const requireRestSample: RestFiscalMonth = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
};

describe('FiscalMonth Service', () => {
  let service: FiscalMonthService;
  let httpMock: HttpTestingController;
  let expectedResult: IFiscalMonth | IFiscalMonth[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FiscalMonthService);
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

    it('should create a FiscalMonth', () => {
      const fiscalMonth = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(fiscalMonth).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FiscalMonth', () => {
      const fiscalMonth = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(fiscalMonth).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FiscalMonth', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FiscalMonth', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FiscalMonth', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a FiscalMonth', () => {
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

    describe('addFiscalMonthToCollectionIfMissing', () => {
      it('should add a FiscalMonth to an empty array', () => {
        const fiscalMonth: IFiscalMonth = sampleWithRequiredData;
        expectedResult = service.addFiscalMonthToCollectionIfMissing([], fiscalMonth);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fiscalMonth);
      });

      it('should not add a FiscalMonth to an array that contains it', () => {
        const fiscalMonth: IFiscalMonth = sampleWithRequiredData;
        const fiscalMonthCollection: IFiscalMonth[] = [
          {
            ...fiscalMonth,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFiscalMonthToCollectionIfMissing(fiscalMonthCollection, fiscalMonth);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FiscalMonth to an array that doesn't contain it", () => {
        const fiscalMonth: IFiscalMonth = sampleWithRequiredData;
        const fiscalMonthCollection: IFiscalMonth[] = [sampleWithPartialData];
        expectedResult = service.addFiscalMonthToCollectionIfMissing(fiscalMonthCollection, fiscalMonth);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fiscalMonth);
      });

      it('should add only unique FiscalMonth to an array', () => {
        const fiscalMonthArray: IFiscalMonth[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const fiscalMonthCollection: IFiscalMonth[] = [sampleWithRequiredData];
        expectedResult = service.addFiscalMonthToCollectionIfMissing(fiscalMonthCollection, ...fiscalMonthArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fiscalMonth: IFiscalMonth = sampleWithRequiredData;
        const fiscalMonth2: IFiscalMonth = sampleWithPartialData;
        expectedResult = service.addFiscalMonthToCollectionIfMissing([], fiscalMonth, fiscalMonth2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fiscalMonth);
        expect(expectedResult).toContain(fiscalMonth2);
      });

      it('should accept null and undefined values', () => {
        const fiscalMonth: IFiscalMonth = sampleWithRequiredData;
        expectedResult = service.addFiscalMonthToCollectionIfMissing([], null, fiscalMonth, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fiscalMonth);
      });

      it('should return initial array if no FiscalMonth is added', () => {
        const fiscalMonthCollection: IFiscalMonth[] = [sampleWithRequiredData];
        expectedResult = service.addFiscalMonthToCollectionIfMissing(fiscalMonthCollection, undefined, null);
        expect(expectedResult).toEqual(fiscalMonthCollection);
      });
    });

    describe('compareFiscalMonth', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFiscalMonth(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 13140 };
        const entity2 = null;

        const compareResult1 = service.compareFiscalMonth(entity1, entity2);
        const compareResult2 = service.compareFiscalMonth(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 13140 };
        const entity2 = { id: 17896 };

        const compareResult1 = service.compareFiscalMonth(entity1, entity2);
        const compareResult2 = service.compareFiscalMonth(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 13140 };
        const entity2 = { id: 13140 };

        const compareResult1 = service.compareFiscalMonth(entity1, entity2);
        const compareResult2 = service.compareFiscalMonth(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
