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
import { IMoneyMarketDealDailySummary } from '../money-market-deal-daily-summary.model';
import { sampleWithFullData, sampleWithPartialData, sampleWithRequiredData } from '../money-market-deal-daily-summary.test-samples';

import { MoneyMarketDealDailySummaryService, RestMoneyMarketDealDailySummary } from './money-market-deal-daily-summary.service';

const requireRestSample: RestMoneyMarketDealDailySummary = {
  ...sampleWithRequiredData,
  reportDate: sampleWithRequiredData.reportDate?.format(DATE_FORMAT),
};

describe('MoneyMarketDealDailySummary Service', () => {
  let service: MoneyMarketDealDailySummaryService;
  let httpMock: HttpTestingController;
  let expectedResult: IMoneyMarketDealDailySummary | IMoneyMarketDealDailySummary[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MoneyMarketDealDailySummaryService);
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

    it('should return a list of MoneyMarketDealDailySummary', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should handle exceptions for searching a MoneyMarketDealDailySummary', () => {
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

    describe('addMoneyMarketDealDailySummaryToCollectionIfMissing', () => {
      it('should add a MoneyMarketDealDailySummary to an empty array', () => {
        const moneyMarketDealDailySummary: IMoneyMarketDealDailySummary = sampleWithRequiredData;
        expectedResult = service.addMoneyMarketDealDailySummaryToCollectionIfMissing([], moneyMarketDealDailySummary);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(moneyMarketDealDailySummary);
      });

      it('should not add a MoneyMarketDealDailySummary to an array that contains it', () => {
        const moneyMarketDealDailySummary: IMoneyMarketDealDailySummary = sampleWithRequiredData;
        const moneyMarketDealDailySummaryCollection: IMoneyMarketDealDailySummary[] = [
          {
            ...moneyMarketDealDailySummary,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMoneyMarketDealDailySummaryToCollectionIfMissing(
          moneyMarketDealDailySummaryCollection,
          moneyMarketDealDailySummary,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MoneyMarketDealDailySummary to an array that doesn't contain it", () => {
        const moneyMarketDealDailySummary: IMoneyMarketDealDailySummary = sampleWithRequiredData;
        const moneyMarketDealDailySummaryCollection: IMoneyMarketDealDailySummary[] = [sampleWithPartialData];
        expectedResult = service.addMoneyMarketDealDailySummaryToCollectionIfMissing(
          moneyMarketDealDailySummaryCollection,
          moneyMarketDealDailySummary,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(moneyMarketDealDailySummary);
      });

      it('should add only unique MoneyMarketDealDailySummary to an array', () => {
        const moneyMarketDealDailySummaryArray: IMoneyMarketDealDailySummary[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const moneyMarketDealDailySummaryCollection: IMoneyMarketDealDailySummary[] = [sampleWithRequiredData];
        expectedResult = service.addMoneyMarketDealDailySummaryToCollectionIfMissing(
          moneyMarketDealDailySummaryCollection,
          ...moneyMarketDealDailySummaryArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const moneyMarketDealDailySummary: IMoneyMarketDealDailySummary = sampleWithRequiredData;
        const moneyMarketDealDailySummary2: IMoneyMarketDealDailySummary = sampleWithPartialData;
        expectedResult = service.addMoneyMarketDealDailySummaryToCollectionIfMissing(
          [],
          moneyMarketDealDailySummary,
          moneyMarketDealDailySummary2,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(moneyMarketDealDailySummary);
        expect(expectedResult).toContain(moneyMarketDealDailySummary2);
      });

      it('should accept null and undefined values', () => {
        const moneyMarketDealDailySummary: IMoneyMarketDealDailySummary = sampleWithRequiredData;
        expectedResult = service.addMoneyMarketDealDailySummaryToCollectionIfMissing([], null, moneyMarketDealDailySummary, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(moneyMarketDealDailySummary);
      });

      it('should return initial array if no MoneyMarketDealDailySummary is added', () => {
        const moneyMarketDealDailySummaryCollection: IMoneyMarketDealDailySummary[] = [sampleWithRequiredData];
        expectedResult = service.addMoneyMarketDealDailySummaryToCollectionIfMissing(
          moneyMarketDealDailySummaryCollection,
          undefined,
          null,
        );
        expect(expectedResult).toEqual(moneyMarketDealDailySummaryCollection);
      });
    });

    describe('compareMoneyMarketDealDailySummary', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMoneyMarketDealDailySummary(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 23012 };
        const entity2 = null;

        const compareResult1 = service.compareMoneyMarketDealDailySummary(entity1, entity2);
        const compareResult2 = service.compareMoneyMarketDealDailySummary(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 23012 };
        const entity2 = { id: 17881 };

        const compareResult1 = service.compareMoneyMarketDealDailySummary(entity1, entity2);
        const compareResult2 = service.compareMoneyMarketDealDailySummary(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 23012 };
        const entity2 = { id: 23012 };

        const compareResult1 = service.compareMoneyMarketDealDailySummary(entity1, entity2);
        const compareResult2 = service.compareMoneyMarketDealDailySummary(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
