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

import { sampleWithNewData, sampleWithRequiredData } from '../fiscal-month.test-samples';

import { FiscalMonthFormService } from './fiscal-month-form.service';

describe('FiscalMonth Form Service', () => {
  let service: FiscalMonthFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FiscalMonthFormService);
  });

  describe('Service methods', () => {
    describe('createFiscalMonthFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFiscalMonthFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            monthNumber: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            fiscalMonthCode: expect.any(Object),
            fiscalYear: expect.any(Object),
            placeholders: expect.any(Object),
            fiscalQuarter: expect.any(Object),
          }),
        );
      });

      it('passing IFiscalMonth should create a new form with FormGroup', () => {
        const formGroup = service.createFiscalMonthFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            monthNumber: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            fiscalMonthCode: expect.any(Object),
            fiscalYear: expect.any(Object),
            placeholders: expect.any(Object),
            fiscalQuarter: expect.any(Object),
          }),
        );
      });
    });

    describe('getFiscalMonth', () => {
      it('should return NewFiscalMonth for default FiscalMonth initial value', () => {
        const formGroup = service.createFiscalMonthFormGroup(sampleWithNewData);

        const fiscalMonth = service.getFiscalMonth(formGroup) as any;

        expect(fiscalMonth).toMatchObject(sampleWithNewData);
      });

      it('should return NewFiscalMonth for empty FiscalMonth initial value', () => {
        const formGroup = service.createFiscalMonthFormGroup();

        const fiscalMonth = service.getFiscalMonth(formGroup) as any;

        expect(fiscalMonth).toMatchObject({});
      });

      it('should return IFiscalMonth', () => {
        const formGroup = service.createFiscalMonthFormGroup(sampleWithRequiredData);

        const fiscalMonth = service.getFiscalMonth(formGroup) as any;

        expect(fiscalMonth).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFiscalMonth should not enable id FormControl', () => {
        const formGroup = service.createFiscalMonthFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFiscalMonth should disable id FormControl', () => {
        const formGroup = service.createFiscalMonthFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
