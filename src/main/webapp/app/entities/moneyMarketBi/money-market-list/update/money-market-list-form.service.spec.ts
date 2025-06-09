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

import { sampleWithNewData, sampleWithRequiredData } from '../money-market-list.test-samples';

import { MoneyMarketListFormService } from './money-market-list-form.service';

describe('MoneyMarketList Form Service', () => {
  let service: MoneyMarketListFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MoneyMarketListFormService);
  });

  describe('Service methods', () => {
    describe('createMoneyMarketListFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMoneyMarketListFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            reportDate: expect.any(Object),
            uploadTimeStamp: expect.any(Object),
            status: expect.any(Object),
            description: expect.any(Object),
            active: expect.any(Object),
            placeholders: expect.any(Object),
            uploadedBy: expect.any(Object),
            reportBatch: expect.any(Object),
          }),
        );
      });

      it('passing IMoneyMarketList should create a new form with FormGroup', () => {
        const formGroup = service.createMoneyMarketListFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            reportDate: expect.any(Object),
            uploadTimeStamp: expect.any(Object),
            status: expect.any(Object),
            description: expect.any(Object),
            active: expect.any(Object),
            placeholders: expect.any(Object),
            uploadedBy: expect.any(Object),
            reportBatch: expect.any(Object),
          }),
        );
      });
    });

    describe('getMoneyMarketList', () => {
      it('should return NewMoneyMarketList for default MoneyMarketList initial value', () => {
        const formGroup = service.createMoneyMarketListFormGroup(sampleWithNewData);

        const moneyMarketList = service.getMoneyMarketList(formGroup) as any;

        expect(moneyMarketList).toMatchObject(sampleWithNewData);
      });

      it('should return NewMoneyMarketList for empty MoneyMarketList initial value', () => {
        const formGroup = service.createMoneyMarketListFormGroup();

        const moneyMarketList = service.getMoneyMarketList(formGroup) as any;

        expect(moneyMarketList).toMatchObject({});
      });

      it('should return IMoneyMarketList', () => {
        const formGroup = service.createMoneyMarketListFormGroup(sampleWithRequiredData);

        const moneyMarketList = service.getMoneyMarketList(formGroup) as any;

        expect(moneyMarketList).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMoneyMarketList should not enable id FormControl', () => {
        const formGroup = service.createMoneyMarketListFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMoneyMarketList should disable id FormControl', () => {
        const formGroup = service.createMoneyMarketListFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
