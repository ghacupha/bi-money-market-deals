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

import { sampleWithNewData, sampleWithRequiredData } from '../fiscal-quarter.test-samples';

import { FiscalQuarterFormService } from './fiscal-quarter-form.service';

describe('FiscalQuarter Form Service', () => {
  let service: FiscalQuarterFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FiscalQuarterFormService);
  });

  describe('Service methods', () => {
    describe('createFiscalQuarterFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFiscalQuarterFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quarterNumber: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            fiscalQuarterCode: expect.any(Object),
            fiscalYear: expect.any(Object),
            placeholders: expect.any(Object),
          }),
        );
      });

      it('passing IFiscalQuarter should create a new form with FormGroup', () => {
        const formGroup = service.createFiscalQuarterFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quarterNumber: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            fiscalQuarterCode: expect.any(Object),
            fiscalYear: expect.any(Object),
            placeholders: expect.any(Object),
          }),
        );
      });
    });

    describe('getFiscalQuarter', () => {
      it('should return NewFiscalQuarter for default FiscalQuarter initial value', () => {
        const formGroup = service.createFiscalQuarterFormGroup(sampleWithNewData);

        const fiscalQuarter = service.getFiscalQuarter(formGroup) as any;

        expect(fiscalQuarter).toMatchObject(sampleWithNewData);
      });

      it('should return NewFiscalQuarter for empty FiscalQuarter initial value', () => {
        const formGroup = service.createFiscalQuarterFormGroup();

        const fiscalQuarter = service.getFiscalQuarter(formGroup) as any;

        expect(fiscalQuarter).toMatchObject({});
      });

      it('should return IFiscalQuarter', () => {
        const formGroup = service.createFiscalQuarterFormGroup(sampleWithRequiredData);

        const fiscalQuarter = service.getFiscalQuarter(formGroup) as any;

        expect(fiscalQuarter).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFiscalQuarter should not enable id FormControl', () => {
        const formGroup = service.createFiscalQuarterFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFiscalQuarter should disable id FormControl', () => {
        const formGroup = service.createFiscalQuarterFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
