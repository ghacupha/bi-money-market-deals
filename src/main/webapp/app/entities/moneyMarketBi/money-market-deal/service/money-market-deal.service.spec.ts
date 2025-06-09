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
import { IMoneyMarketDeal } from '../money-market-deal.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../money-market-deal.test-samples';

import { MoneyMarketDealService, RestMoneyMarketDeal } from './money-market-deal.service';

const requireRestSample: RestMoneyMarketDeal = {
  ...sampleWithRequiredData,
  finalInterestAccrualDate: sampleWithRequiredData.finalInterestAccrualDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
  settlementDate: sampleWithRequiredData.settlementDate?.format(DATE_FORMAT),
  maturityDate: sampleWithRequiredData.maturityDate?.format(DATE_FORMAT),
  reportDate: sampleWithRequiredData.reportDate?.format(DATE_FORMAT),
};

describe('MoneyMarketDeal Service', () => {
  let service: MoneyMarketDealService;
  let httpMock: HttpTestingController;
  let expectedResult: IMoneyMarketDeal | IMoneyMarketDeal[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MoneyMarketDealService);
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

    it('should create a MoneyMarketDeal', () => {
      const moneyMarketDeal = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(moneyMarketDeal).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MoneyMarketDeal', () => {
      const moneyMarketDeal = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(moneyMarketDeal).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MoneyMarketDeal', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MoneyMarketDeal', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MoneyMarketDeal', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a MoneyMarketDeal', () => {
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

    describe('addMoneyMarketDealToCollectionIfMissing', () => {
      it('should add a MoneyMarketDeal to an empty array', () => {
        const moneyMarketDeal: IMoneyMarketDeal = sampleWithRequiredData;
        expectedResult = service.addMoneyMarketDealToCollectionIfMissing([], moneyMarketDeal);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(moneyMarketDeal);
      });

      it('should not add a MoneyMarketDeal to an array that contains it', () => {
        const moneyMarketDeal: IMoneyMarketDeal = sampleWithRequiredData;
        const moneyMarketDealCollection: IMoneyMarketDeal[] = [
          {
            ...moneyMarketDeal,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMoneyMarketDealToCollectionIfMissing(moneyMarketDealCollection, moneyMarketDeal);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MoneyMarketDeal to an array that doesn't contain it", () => {
        const moneyMarketDeal: IMoneyMarketDeal = sampleWithRequiredData;
        const moneyMarketDealCollection: IMoneyMarketDeal[] = [sampleWithPartialData];
        expectedResult = service.addMoneyMarketDealToCollectionIfMissing(moneyMarketDealCollection, moneyMarketDeal);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(moneyMarketDeal);
      });

      it('should add only unique MoneyMarketDeal to an array', () => {
        const moneyMarketDealArray: IMoneyMarketDeal[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const moneyMarketDealCollection: IMoneyMarketDeal[] = [sampleWithRequiredData];
        expectedResult = service.addMoneyMarketDealToCollectionIfMissing(moneyMarketDealCollection, ...moneyMarketDealArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const moneyMarketDeal: IMoneyMarketDeal = sampleWithRequiredData;
        const moneyMarketDeal2: IMoneyMarketDeal = sampleWithPartialData;
        expectedResult = service.addMoneyMarketDealToCollectionIfMissing([], moneyMarketDeal, moneyMarketDeal2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(moneyMarketDeal);
        expect(expectedResult).toContain(moneyMarketDeal2);
      });

      it('should accept null and undefined values', () => {
        const moneyMarketDeal: IMoneyMarketDeal = sampleWithRequiredData;
        expectedResult = service.addMoneyMarketDealToCollectionIfMissing([], null, moneyMarketDeal, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(moneyMarketDeal);
      });

      it('should return initial array if no MoneyMarketDeal is added', () => {
        const moneyMarketDealCollection: IMoneyMarketDeal[] = [sampleWithRequiredData];
        expectedResult = service.addMoneyMarketDealToCollectionIfMissing(moneyMarketDealCollection, undefined, null);
        expect(expectedResult).toEqual(moneyMarketDealCollection);
      });
    });

    describe('compareMoneyMarketDeal', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMoneyMarketDeal(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 21325 };
        const entity2 = null;

        const compareResult1 = service.compareMoneyMarketDeal(entity1, entity2);
        const compareResult2 = service.compareMoneyMarketDeal(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 21325 };
        const entity2 = { id: 4189 };

        const compareResult1 = service.compareMoneyMarketDeal(entity1, entity2);
        const compareResult2 = service.compareMoneyMarketDeal(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 21325 };
        const entity2 = { id: 21325 };

        const compareResult1 = service.compareMoneyMarketDeal(entity1, entity2);
        const compareResult2 = service.compareMoneyMarketDeal(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
