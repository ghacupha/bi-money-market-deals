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

import { sampleWithNewData, sampleWithRequiredData } from '../fiscal-year.test-samples';

import { FiscalYearFormService } from './fiscal-year-form.service';

describe('FiscalYear Form Service', () => {
  let service: FiscalYearFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FiscalYearFormService);
  });

  describe('Service methods', () => {
    describe('createFiscalYearFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFiscalYearFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fiscalYearCode: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            fiscalYearStatus: expect.any(Object),
            placeholders: expect.any(Object),
            createdBy: expect.any(Object),
            lastUpdatedBy: expect.any(Object),
          }),
        );
      });

      it('passing IFiscalYear should create a new form with FormGroup', () => {
        const formGroup = service.createFiscalYearFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fiscalYearCode: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            fiscalYearStatus: expect.any(Object),
            placeholders: expect.any(Object),
            createdBy: expect.any(Object),
            lastUpdatedBy: expect.any(Object),
          }),
        );
      });
    });

    describe('getFiscalYear', () => {
      it('should return NewFiscalYear for default FiscalYear initial value', () => {
        const formGroup = service.createFiscalYearFormGroup(sampleWithNewData);

        const fiscalYear = service.getFiscalYear(formGroup) as any;

        expect(fiscalYear).toMatchObject(sampleWithNewData);
      });

      it('should return NewFiscalYear for empty FiscalYear initial value', () => {
        const formGroup = service.createFiscalYearFormGroup();

        const fiscalYear = service.getFiscalYear(formGroup) as any;

        expect(fiscalYear).toMatchObject({});
      });

      it('should return IFiscalYear', () => {
        const formGroup = service.createFiscalYearFormGroup(sampleWithRequiredData);

        const fiscalYear = service.getFiscalYear(formGroup) as any;

        expect(fiscalYear).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFiscalYear should not enable id FormControl', () => {
        const formGroup = service.createFiscalYearFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFiscalYear should disable id FormControl', () => {
        const formGroup = service.createFiscalYearFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
