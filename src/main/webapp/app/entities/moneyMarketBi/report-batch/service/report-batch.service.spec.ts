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
import { IReportBatch } from '../report-batch.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../report-batch.test-samples';

import { ReportBatchService, RestReportBatch } from './report-batch.service';

const requireRestSample: RestReportBatch = {
  ...sampleWithRequiredData,
  reportDate: sampleWithRequiredData.reportDate?.format(DATE_FORMAT),
  uploadTimeStamp: sampleWithRequiredData.uploadTimeStamp?.toJSON(),
};

describe('ReportBatch Service', () => {
  let service: ReportBatchService;
  let httpMock: HttpTestingController;
  let expectedResult: IReportBatch | IReportBatch[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ReportBatchService);
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

    it('should create a ReportBatch', () => {
      const reportBatch = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(reportBatch).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReportBatch', () => {
      const reportBatch = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(reportBatch).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReportBatch', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReportBatch', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ReportBatch', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a ReportBatch', () => {
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

    describe('addReportBatchToCollectionIfMissing', () => {
      it('should add a ReportBatch to an empty array', () => {
        const reportBatch: IReportBatch = sampleWithRequiredData;
        expectedResult = service.addReportBatchToCollectionIfMissing([], reportBatch);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportBatch);
      });

      it('should not add a ReportBatch to an array that contains it', () => {
        const reportBatch: IReportBatch = sampleWithRequiredData;
        const reportBatchCollection: IReportBatch[] = [
          {
            ...reportBatch,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReportBatchToCollectionIfMissing(reportBatchCollection, reportBatch);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReportBatch to an array that doesn't contain it", () => {
        const reportBatch: IReportBatch = sampleWithRequiredData;
        const reportBatchCollection: IReportBatch[] = [sampleWithPartialData];
        expectedResult = service.addReportBatchToCollectionIfMissing(reportBatchCollection, reportBatch);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportBatch);
      });

      it('should add only unique ReportBatch to an array', () => {
        const reportBatchArray: IReportBatch[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const reportBatchCollection: IReportBatch[] = [sampleWithRequiredData];
        expectedResult = service.addReportBatchToCollectionIfMissing(reportBatchCollection, ...reportBatchArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reportBatch: IReportBatch = sampleWithRequiredData;
        const reportBatch2: IReportBatch = sampleWithPartialData;
        expectedResult = service.addReportBatchToCollectionIfMissing([], reportBatch, reportBatch2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportBatch);
        expect(expectedResult).toContain(reportBatch2);
      });

      it('should accept null and undefined values', () => {
        const reportBatch: IReportBatch = sampleWithRequiredData;
        expectedResult = service.addReportBatchToCollectionIfMissing([], null, reportBatch, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportBatch);
      });

      it('should return initial array if no ReportBatch is added', () => {
        const reportBatchCollection: IReportBatch[] = [sampleWithRequiredData];
        expectedResult = service.addReportBatchToCollectionIfMissing(reportBatchCollection, undefined, null);
        expect(expectedResult).toEqual(reportBatchCollection);
      });
    });

    describe('compareReportBatch', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReportBatch(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 28750 };
        const entity2 = null;

        const compareResult1 = service.compareReportBatch(entity1, entity2);
        const compareResult2 = service.compareReportBatch(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 28750 };
        const entity2 = { id: 19041 };

        const compareResult1 = service.compareReportBatch(entity1, entity2);
        const compareResult2 = service.compareReportBatch(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 28750 };
        const entity2 = { id: 28750 };

        const compareResult1 = service.compareReportBatch(entity1, entity2);
        const compareResult2 = service.compareReportBatch(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
