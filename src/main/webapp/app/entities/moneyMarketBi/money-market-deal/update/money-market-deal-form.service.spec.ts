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

import { sampleWithNewData, sampleWithRequiredData } from '../money-market-deal.test-samples';

import { MoneyMarketDealFormService } from './money-market-deal-form.service';

describe('MoneyMarketDeal Form Service', () => {
  let service: MoneyMarketDealFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MoneyMarketDealFormService);
  });

  describe('Service methods', () => {
    describe('createMoneyMarketDealFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMoneyMarketDealFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dealNumber: expect.any(Object),
            tradingBook: expect.any(Object),
            counterPartyName: expect.any(Object),
            finalInterestAccrualDate: expect.any(Object),
            counterPartySideType: expect.any(Object),
            dateOfCollectionStatement: expect.any(Object),
            currencyCode: expect.any(Object),
            principalAmount: expect.any(Object),
            interestRate: expect.any(Object),
            interestAccruedAmount: expect.any(Object),
            totalInterestAtMaturity: expect.any(Object),
            counterpartyNationality: expect.any(Object),
            endDate: expect.any(Object),
            treasuryLedger: expect.any(Object),
            dealSubtype: expect.any(Object),
            shillingEquivalentPrincipal: expect.any(Object),
            shillingEquivalentInterestAccrued: expect.any(Object),
            shillingEquivalentPVFull: expect.any(Object),
            counterpartyDomicile: expect.any(Object),
            settlementDate: expect.any(Object),
            transactionCollateral: expect.any(Object),
            institutionType: expect.any(Object),
            maturityDate: expect.any(Object),
            institutionReportName: expect.any(Object),
            transactionType: expect.any(Object),
            reportDate: expect.any(Object),
            active: expect.any(Object),
            moneyMarketList: expect.any(Object),
          }),
        );
      });

      it('passing IMoneyMarketDeal should create a new form with FormGroup', () => {
        const formGroup = service.createMoneyMarketDealFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dealNumber: expect.any(Object),
            tradingBook: expect.any(Object),
            counterPartyName: expect.any(Object),
            finalInterestAccrualDate: expect.any(Object),
            counterPartySideType: expect.any(Object),
            dateOfCollectionStatement: expect.any(Object),
            currencyCode: expect.any(Object),
            principalAmount: expect.any(Object),
            interestRate: expect.any(Object),
            interestAccruedAmount: expect.any(Object),
            totalInterestAtMaturity: expect.any(Object),
            counterpartyNationality: expect.any(Object),
            endDate: expect.any(Object),
            treasuryLedger: expect.any(Object),
            dealSubtype: expect.any(Object),
            shillingEquivalentPrincipal: expect.any(Object),
            shillingEquivalentInterestAccrued: expect.any(Object),
            shillingEquivalentPVFull: expect.any(Object),
            counterpartyDomicile: expect.any(Object),
            settlementDate: expect.any(Object),
            transactionCollateral: expect.any(Object),
            institutionType: expect.any(Object),
            maturityDate: expect.any(Object),
            institutionReportName: expect.any(Object),
            transactionType: expect.any(Object),
            reportDate: expect.any(Object),
            active: expect.any(Object),
            moneyMarketList: expect.any(Object),
          }),
        );
      });
    });

    describe('getMoneyMarketDeal', () => {
      it('should return NewMoneyMarketDeal for default MoneyMarketDeal initial value', () => {
        const formGroup = service.createMoneyMarketDealFormGroup(sampleWithNewData);

        const moneyMarketDeal = service.getMoneyMarketDeal(formGroup) as any;

        expect(moneyMarketDeal).toMatchObject(sampleWithNewData);
      });

      it('should return NewMoneyMarketDeal for empty MoneyMarketDeal initial value', () => {
        const formGroup = service.createMoneyMarketDealFormGroup();

        const moneyMarketDeal = service.getMoneyMarketDeal(formGroup) as any;

        expect(moneyMarketDeal).toMatchObject({});
      });

      it('should return IMoneyMarketDeal', () => {
        const formGroup = service.createMoneyMarketDealFormGroup(sampleWithRequiredData);

        const moneyMarketDeal = service.getMoneyMarketDeal(formGroup) as any;

        expect(moneyMarketDeal).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMoneyMarketDeal should not enable id FormControl', () => {
        const formGroup = service.createMoneyMarketDealFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMoneyMarketDeal should disable id FormControl', () => {
        const formGroup = service.createMoneyMarketDealFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
