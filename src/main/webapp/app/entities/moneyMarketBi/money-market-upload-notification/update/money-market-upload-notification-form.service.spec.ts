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

import { sampleWithNewData, sampleWithRequiredData } from '../money-market-upload-notification.test-samples';

import { MoneyMarketUploadNotificationFormService } from './money-market-upload-notification-form.service';

describe('MoneyMarketUploadNotification Form Service', () => {
  let service: MoneyMarketUploadNotificationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MoneyMarketUploadNotificationFormService);
  });

  describe('Service methods', () => {
    describe('createMoneyMarketUploadNotificationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMoneyMarketUploadNotificationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            errorMessage: expect.any(Object),
            rowNumber: expect.any(Object),
            referenceNumber: expect.any(Object),
            moneyMarketList: expect.any(Object),
            reportBatch: expect.any(Object),
            placeholders: expect.any(Object),
          }),
        );
      });

      it('passing IMoneyMarketUploadNotification should create a new form with FormGroup', () => {
        const formGroup = service.createMoneyMarketUploadNotificationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            errorMessage: expect.any(Object),
            rowNumber: expect.any(Object),
            referenceNumber: expect.any(Object),
            moneyMarketList: expect.any(Object),
            reportBatch: expect.any(Object),
            placeholders: expect.any(Object),
          }),
        );
      });
    });

    describe('getMoneyMarketUploadNotification', () => {
      it('should return NewMoneyMarketUploadNotification for default MoneyMarketUploadNotification initial value', () => {
        const formGroup = service.createMoneyMarketUploadNotificationFormGroup(sampleWithNewData);

        const moneyMarketUploadNotification = service.getMoneyMarketUploadNotification(formGroup) as any;

        expect(moneyMarketUploadNotification).toMatchObject(sampleWithNewData);
      });

      it('should return NewMoneyMarketUploadNotification for empty MoneyMarketUploadNotification initial value', () => {
        const formGroup = service.createMoneyMarketUploadNotificationFormGroup();

        const moneyMarketUploadNotification = service.getMoneyMarketUploadNotification(formGroup) as any;

        expect(moneyMarketUploadNotification).toMatchObject({});
      });

      it('should return IMoneyMarketUploadNotification', () => {
        const formGroup = service.createMoneyMarketUploadNotificationFormGroup(sampleWithRequiredData);

        const moneyMarketUploadNotification = service.getMoneyMarketUploadNotification(formGroup) as any;

        expect(moneyMarketUploadNotification).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMoneyMarketUploadNotification should not enable id FormControl', () => {
        const formGroup = service.createMoneyMarketUploadNotificationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMoneyMarketUploadNotification should disable id FormControl', () => {
        const formGroup = service.createMoneyMarketUploadNotificationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
