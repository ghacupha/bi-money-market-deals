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

import { sampleWithNewData, sampleWithRequiredData } from '../report-batch.test-samples';

import { ReportBatchFormService } from './report-batch-form.service';

describe('ReportBatch Form Service', () => {
  let service: ReportBatchFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportBatchFormService);
  });

  describe('Service methods', () => {
    describe('createReportBatchFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReportBatchFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            reportDate: expect.any(Object),
            uploadTimeStamp: expect.any(Object),
            status: expect.any(Object),
            active: expect.any(Object),
            description: expect.any(Object),
            fileIdentifier: expect.any(Object),
            processFlag: expect.any(Object),
            csvFileAttachment: expect.any(Object),
            uploadedBy: expect.any(Object),
            placeholders: expect.any(Object),
          }),
        );
      });

      it('passing IReportBatch should create a new form with FormGroup', () => {
        const formGroup = service.createReportBatchFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            reportDate: expect.any(Object),
            uploadTimeStamp: expect.any(Object),
            status: expect.any(Object),
            active: expect.any(Object),
            description: expect.any(Object),
            fileIdentifier: expect.any(Object),
            processFlag: expect.any(Object),
            csvFileAttachment: expect.any(Object),
            uploadedBy: expect.any(Object),
            placeholders: expect.any(Object),
          }),
        );
      });
    });

    describe('getReportBatch', () => {
      it('should return NewReportBatch for default ReportBatch initial value', () => {
        const formGroup = service.createReportBatchFormGroup(sampleWithNewData);

        const reportBatch = service.getReportBatch(formGroup) as any;

        expect(reportBatch).toMatchObject(sampleWithNewData);
      });

      it('should return NewReportBatch for empty ReportBatch initial value', () => {
        const formGroup = service.createReportBatchFormGroup();

        const reportBatch = service.getReportBatch(formGroup) as any;

        expect(reportBatch).toMatchObject({});
      });

      it('should return IReportBatch', () => {
        const formGroup = service.createReportBatchFormGroup(sampleWithRequiredData);

        const reportBatch = service.getReportBatch(formGroup) as any;

        expect(reportBatch).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReportBatch should not enable id FormControl', () => {
        const formGroup = service.createReportBatchFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReportBatch should disable id FormControl', () => {
        const formGroup = service.createReportBatchFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
