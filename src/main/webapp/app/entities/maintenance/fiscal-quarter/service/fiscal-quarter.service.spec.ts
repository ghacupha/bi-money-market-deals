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
import { IFiscalQuarter } from '../fiscal-quarter.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../fiscal-quarter.test-samples';

import { FiscalQuarterService, RestFiscalQuarter } from './fiscal-quarter.service';

const requireRestSample: RestFiscalQuarter = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
};

describe('FiscalQuarter Service', () => {
  let service: FiscalQuarterService;
  let httpMock: HttpTestingController;
  let expectedResult: IFiscalQuarter | IFiscalQuarter[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FiscalQuarterService);
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

    it('should create a FiscalQuarter', () => {
      const fiscalQuarter = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(fiscalQuarter).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FiscalQuarter', () => {
      const fiscalQuarter = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(fiscalQuarter).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FiscalQuarter', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FiscalQuarter', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FiscalQuarter', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a FiscalQuarter', () => {
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

    describe('addFiscalQuarterToCollectionIfMissing', () => {
      it('should add a FiscalQuarter to an empty array', () => {
        const fiscalQuarter: IFiscalQuarter = sampleWithRequiredData;
        expectedResult = service.addFiscalQuarterToCollectionIfMissing([], fiscalQuarter);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fiscalQuarter);
      });

      it('should not add a FiscalQuarter to an array that contains it', () => {
        const fiscalQuarter: IFiscalQuarter = sampleWithRequiredData;
        const fiscalQuarterCollection: IFiscalQuarter[] = [
          {
            ...fiscalQuarter,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFiscalQuarterToCollectionIfMissing(fiscalQuarterCollection, fiscalQuarter);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FiscalQuarter to an array that doesn't contain it", () => {
        const fiscalQuarter: IFiscalQuarter = sampleWithRequiredData;
        const fiscalQuarterCollection: IFiscalQuarter[] = [sampleWithPartialData];
        expectedResult = service.addFiscalQuarterToCollectionIfMissing(fiscalQuarterCollection, fiscalQuarter);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fiscalQuarter);
      });

      it('should add only unique FiscalQuarter to an array', () => {
        const fiscalQuarterArray: IFiscalQuarter[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const fiscalQuarterCollection: IFiscalQuarter[] = [sampleWithRequiredData];
        expectedResult = service.addFiscalQuarterToCollectionIfMissing(fiscalQuarterCollection, ...fiscalQuarterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fiscalQuarter: IFiscalQuarter = sampleWithRequiredData;
        const fiscalQuarter2: IFiscalQuarter = sampleWithPartialData;
        expectedResult = service.addFiscalQuarterToCollectionIfMissing([], fiscalQuarter, fiscalQuarter2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fiscalQuarter);
        expect(expectedResult).toContain(fiscalQuarter2);
      });

      it('should accept null and undefined values', () => {
        const fiscalQuarter: IFiscalQuarter = sampleWithRequiredData;
        expectedResult = service.addFiscalQuarterToCollectionIfMissing([], null, fiscalQuarter, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fiscalQuarter);
      });

      it('should return initial array if no FiscalQuarter is added', () => {
        const fiscalQuarterCollection: IFiscalQuarter[] = [sampleWithRequiredData];
        expectedResult = service.addFiscalQuarterToCollectionIfMissing(fiscalQuarterCollection, undefined, null);
        expect(expectedResult).toEqual(fiscalQuarterCollection);
      });
    });

    describe('compareFiscalQuarter', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFiscalQuarter(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 15832 };
        const entity2 = null;

        const compareResult1 = service.compareFiscalQuarter(entity1, entity2);
        const compareResult2 = service.compareFiscalQuarter(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 15832 };
        const entity2 = { id: 12913 };

        const compareResult1 = service.compareFiscalQuarter(entity1, entity2);
        const compareResult2 = service.compareFiscalQuarter(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 15832 };
        const entity2 = { id: 15832 };

        const compareResult1 = service.compareFiscalQuarter(entity1, entity2);
        const compareResult2 = service.compareFiscalQuarter(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
