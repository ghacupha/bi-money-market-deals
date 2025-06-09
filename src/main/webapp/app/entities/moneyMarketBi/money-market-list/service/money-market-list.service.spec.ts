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
import { IMoneyMarketList } from '../money-market-list.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../money-market-list.test-samples';

import { MoneyMarketListService, RestMoneyMarketList } from './money-market-list.service';

const requireRestSample: RestMoneyMarketList = {
  ...sampleWithRequiredData,
  reportDate: sampleWithRequiredData.reportDate?.format(DATE_FORMAT),
  uploadTimeStamp: sampleWithRequiredData.uploadTimeStamp?.toJSON(),
};

describe('MoneyMarketList Service', () => {
  let service: MoneyMarketListService;
  let httpMock: HttpTestingController;
  let expectedResult: IMoneyMarketList | IMoneyMarketList[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MoneyMarketListService);
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

    it('should create a MoneyMarketList', () => {
      const moneyMarketList = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(moneyMarketList).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MoneyMarketList', () => {
      const moneyMarketList = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(moneyMarketList).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MoneyMarketList', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MoneyMarketList', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MoneyMarketList', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a MoneyMarketList', () => {
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

    describe('addMoneyMarketListToCollectionIfMissing', () => {
      it('should add a MoneyMarketList to an empty array', () => {
        const moneyMarketList: IMoneyMarketList = sampleWithRequiredData;
        expectedResult = service.addMoneyMarketListToCollectionIfMissing([], moneyMarketList);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(moneyMarketList);
      });

      it('should not add a MoneyMarketList to an array that contains it', () => {
        const moneyMarketList: IMoneyMarketList = sampleWithRequiredData;
        const moneyMarketListCollection: IMoneyMarketList[] = [
          {
            ...moneyMarketList,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMoneyMarketListToCollectionIfMissing(moneyMarketListCollection, moneyMarketList);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MoneyMarketList to an array that doesn't contain it", () => {
        const moneyMarketList: IMoneyMarketList = sampleWithRequiredData;
        const moneyMarketListCollection: IMoneyMarketList[] = [sampleWithPartialData];
        expectedResult = service.addMoneyMarketListToCollectionIfMissing(moneyMarketListCollection, moneyMarketList);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(moneyMarketList);
      });

      it('should add only unique MoneyMarketList to an array', () => {
        const moneyMarketListArray: IMoneyMarketList[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const moneyMarketListCollection: IMoneyMarketList[] = [sampleWithRequiredData];
        expectedResult = service.addMoneyMarketListToCollectionIfMissing(moneyMarketListCollection, ...moneyMarketListArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const moneyMarketList: IMoneyMarketList = sampleWithRequiredData;
        const moneyMarketList2: IMoneyMarketList = sampleWithPartialData;
        expectedResult = service.addMoneyMarketListToCollectionIfMissing([], moneyMarketList, moneyMarketList2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(moneyMarketList);
        expect(expectedResult).toContain(moneyMarketList2);
      });

      it('should accept null and undefined values', () => {
        const moneyMarketList: IMoneyMarketList = sampleWithRequiredData;
        expectedResult = service.addMoneyMarketListToCollectionIfMissing([], null, moneyMarketList, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(moneyMarketList);
      });

      it('should return initial array if no MoneyMarketList is added', () => {
        const moneyMarketListCollection: IMoneyMarketList[] = [sampleWithRequiredData];
        expectedResult = service.addMoneyMarketListToCollectionIfMissing(moneyMarketListCollection, undefined, null);
        expect(expectedResult).toEqual(moneyMarketListCollection);
      });
    });

    describe('compareMoneyMarketList', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMoneyMarketList(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 21196 };
        const entity2 = null;

        const compareResult1 = service.compareMoneyMarketList(entity1, entity2);
        const compareResult2 = service.compareMoneyMarketList(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 21196 };
        const entity2 = { id: 31570 };

        const compareResult1 = service.compareMoneyMarketList(entity1, entity2);
        const compareResult2 = service.compareMoneyMarketList(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 21196 };
        const entity2 = { id: 21196 };

        const compareResult1 = service.compareMoneyMarketList(entity1, entity2);
        const compareResult2 = service.compareMoneyMarketList(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
