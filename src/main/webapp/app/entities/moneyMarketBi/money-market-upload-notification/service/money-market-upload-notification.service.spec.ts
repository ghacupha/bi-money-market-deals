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

import { IMoneyMarketUploadNotification } from '../money-market-upload-notification.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../money-market-upload-notification.test-samples';

import { MoneyMarketUploadNotificationService } from './money-market-upload-notification.service';

const requireRestSample: IMoneyMarketUploadNotification = {
  ...sampleWithRequiredData,
};

describe('MoneyMarketUploadNotification Service', () => {
  let service: MoneyMarketUploadNotificationService;
  let httpMock: HttpTestingController;
  let expectedResult: IMoneyMarketUploadNotification | IMoneyMarketUploadNotification[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MoneyMarketUploadNotificationService);
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

    it('should create a MoneyMarketUploadNotification', () => {
      const moneyMarketUploadNotification = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(moneyMarketUploadNotification).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MoneyMarketUploadNotification', () => {
      const moneyMarketUploadNotification = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(moneyMarketUploadNotification).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MoneyMarketUploadNotification', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MoneyMarketUploadNotification', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MoneyMarketUploadNotification', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a MoneyMarketUploadNotification', () => {
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

    describe('addMoneyMarketUploadNotificationToCollectionIfMissing', () => {
      it('should add a MoneyMarketUploadNotification to an empty array', () => {
        const moneyMarketUploadNotification: IMoneyMarketUploadNotification = sampleWithRequiredData;
        expectedResult = service.addMoneyMarketUploadNotificationToCollectionIfMissing([], moneyMarketUploadNotification);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(moneyMarketUploadNotification);
      });

      it('should not add a MoneyMarketUploadNotification to an array that contains it', () => {
        const moneyMarketUploadNotification: IMoneyMarketUploadNotification = sampleWithRequiredData;
        const moneyMarketUploadNotificationCollection: IMoneyMarketUploadNotification[] = [
          {
            ...moneyMarketUploadNotification,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMoneyMarketUploadNotificationToCollectionIfMissing(
          moneyMarketUploadNotificationCollection,
          moneyMarketUploadNotification,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MoneyMarketUploadNotification to an array that doesn't contain it", () => {
        const moneyMarketUploadNotification: IMoneyMarketUploadNotification = sampleWithRequiredData;
        const moneyMarketUploadNotificationCollection: IMoneyMarketUploadNotification[] = [sampleWithPartialData];
        expectedResult = service.addMoneyMarketUploadNotificationToCollectionIfMissing(
          moneyMarketUploadNotificationCollection,
          moneyMarketUploadNotification,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(moneyMarketUploadNotification);
      });

      it('should add only unique MoneyMarketUploadNotification to an array', () => {
        const moneyMarketUploadNotificationArray: IMoneyMarketUploadNotification[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const moneyMarketUploadNotificationCollection: IMoneyMarketUploadNotification[] = [sampleWithRequiredData];
        expectedResult = service.addMoneyMarketUploadNotificationToCollectionIfMissing(
          moneyMarketUploadNotificationCollection,
          ...moneyMarketUploadNotificationArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const moneyMarketUploadNotification: IMoneyMarketUploadNotification = sampleWithRequiredData;
        const moneyMarketUploadNotification2: IMoneyMarketUploadNotification = sampleWithPartialData;
        expectedResult = service.addMoneyMarketUploadNotificationToCollectionIfMissing(
          [],
          moneyMarketUploadNotification,
          moneyMarketUploadNotification2,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(moneyMarketUploadNotification);
        expect(expectedResult).toContain(moneyMarketUploadNotification2);
      });

      it('should accept null and undefined values', () => {
        const moneyMarketUploadNotification: IMoneyMarketUploadNotification = sampleWithRequiredData;
        expectedResult = service.addMoneyMarketUploadNotificationToCollectionIfMissing([], null, moneyMarketUploadNotification, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(moneyMarketUploadNotification);
      });

      it('should return initial array if no MoneyMarketUploadNotification is added', () => {
        const moneyMarketUploadNotificationCollection: IMoneyMarketUploadNotification[] = [sampleWithRequiredData];
        expectedResult = service.addMoneyMarketUploadNotificationToCollectionIfMissing(
          moneyMarketUploadNotificationCollection,
          undefined,
          null,
        );
        expect(expectedResult).toEqual(moneyMarketUploadNotificationCollection);
      });
    });

    describe('compareMoneyMarketUploadNotification', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMoneyMarketUploadNotification(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 20382 };
        const entity2 = null;

        const compareResult1 = service.compareMoneyMarketUploadNotification(entity1, entity2);
        const compareResult2 = service.compareMoneyMarketUploadNotification(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 20382 };
        const entity2 = { id: 10273 };

        const compareResult1 = service.compareMoneyMarketUploadNotification(entity1, entity2);
        const compareResult2 = service.compareMoneyMarketUploadNotification(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 20382 };
        const entity2 = { id: 20382 };

        const compareResult1 = service.compareMoneyMarketUploadNotification(entity1, entity2);
        const compareResult2 = service.compareMoneyMarketUploadNotification(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
