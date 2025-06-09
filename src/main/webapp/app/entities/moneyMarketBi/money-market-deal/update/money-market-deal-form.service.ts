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

import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMoneyMarketDeal, NewMoneyMarketDeal } from '../money-market-deal.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMoneyMarketDeal for edit and NewMoneyMarketDealFormGroupInput for create.
 */
type MoneyMarketDealFormGroupInput = IMoneyMarketDeal | PartialWithRequiredKeyOf<NewMoneyMarketDeal>;

type MoneyMarketDealFormDefaults = Pick<NewMoneyMarketDeal, 'id' | 'active'>;

type MoneyMarketDealFormGroupContent = {
  id: FormControl<IMoneyMarketDeal['id'] | NewMoneyMarketDeal['id']>;
  dealNumber: FormControl<IMoneyMarketDeal['dealNumber']>;
  tradingBook: FormControl<IMoneyMarketDeal['tradingBook']>;
  counterPartyName: FormControl<IMoneyMarketDeal['counterPartyName']>;
  finalInterestAccrualDate: FormControl<IMoneyMarketDeal['finalInterestAccrualDate']>;
  counterPartySideType: FormControl<IMoneyMarketDeal['counterPartySideType']>;
  dateOfCollectionStatement: FormControl<IMoneyMarketDeal['dateOfCollectionStatement']>;
  currencyCode: FormControl<IMoneyMarketDeal['currencyCode']>;
  principalAmount: FormControl<IMoneyMarketDeal['principalAmount']>;
  interestRate: FormControl<IMoneyMarketDeal['interestRate']>;
  interestAccruedAmount: FormControl<IMoneyMarketDeal['interestAccruedAmount']>;
  totalInterestAtMaturity: FormControl<IMoneyMarketDeal['totalInterestAtMaturity']>;
  counterpartyNationality: FormControl<IMoneyMarketDeal['counterpartyNationality']>;
  endDate: FormControl<IMoneyMarketDeal['endDate']>;
  treasuryLedger: FormControl<IMoneyMarketDeal['treasuryLedger']>;
  dealSubtype: FormControl<IMoneyMarketDeal['dealSubtype']>;
  shillingEquivalentPrincipal: FormControl<IMoneyMarketDeal['shillingEquivalentPrincipal']>;
  shillingEquivalentInterestAccrued: FormControl<IMoneyMarketDeal['shillingEquivalentInterestAccrued']>;
  shillingEquivalentPVFull: FormControl<IMoneyMarketDeal['shillingEquivalentPVFull']>;
  counterpartyDomicile: FormControl<IMoneyMarketDeal['counterpartyDomicile']>;
  settlementDate: FormControl<IMoneyMarketDeal['settlementDate']>;
  transactionCollateral: FormControl<IMoneyMarketDeal['transactionCollateral']>;
  institutionType: FormControl<IMoneyMarketDeal['institutionType']>;
  maturityDate: FormControl<IMoneyMarketDeal['maturityDate']>;
  institutionReportName: FormControl<IMoneyMarketDeal['institutionReportName']>;
  transactionType: FormControl<IMoneyMarketDeal['transactionType']>;
  reportDate: FormControl<IMoneyMarketDeal['reportDate']>;
  active: FormControl<IMoneyMarketDeal['active']>;
  moneyMarketList: FormControl<IMoneyMarketDeal['moneyMarketList']>;
};

export type MoneyMarketDealFormGroup = FormGroup<MoneyMarketDealFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MoneyMarketDealFormService {
  createMoneyMarketDealFormGroup(moneyMarketDeal: MoneyMarketDealFormGroupInput = { id: null }): MoneyMarketDealFormGroup {
    const moneyMarketDealRawValue = {
      ...this.getFormDefaults(),
      ...moneyMarketDeal,
    };
    return new FormGroup<MoneyMarketDealFormGroupContent>({
      id: new FormControl(
        { value: moneyMarketDealRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dealNumber: new FormControl(moneyMarketDealRawValue.dealNumber, {
        validators: [Validators.required],
      }),
      tradingBook: new FormControl(moneyMarketDealRawValue.tradingBook),
      counterPartyName: new FormControl(moneyMarketDealRawValue.counterPartyName),
      finalInterestAccrualDate: new FormControl(moneyMarketDealRawValue.finalInterestAccrualDate, {
        validators: [Validators.required],
      }),
      counterPartySideType: new FormControl(moneyMarketDealRawValue.counterPartySideType),
      dateOfCollectionStatement: new FormControl(moneyMarketDealRawValue.dateOfCollectionStatement),
      currencyCode: new FormControl(moneyMarketDealRawValue.currencyCode),
      principalAmount: new FormControl(moneyMarketDealRawValue.principalAmount),
      interestRate: new FormControl(moneyMarketDealRawValue.interestRate),
      interestAccruedAmount: new FormControl(moneyMarketDealRawValue.interestAccruedAmount),
      totalInterestAtMaturity: new FormControl(moneyMarketDealRawValue.totalInterestAtMaturity),
      counterpartyNationality: new FormControl(moneyMarketDealRawValue.counterpartyNationality),
      endDate: new FormControl(moneyMarketDealRawValue.endDate, {
        validators: [Validators.required],
      }),
      treasuryLedger: new FormControl(moneyMarketDealRawValue.treasuryLedger),
      dealSubtype: new FormControl(moneyMarketDealRawValue.dealSubtype),
      shillingEquivalentPrincipal: new FormControl(moneyMarketDealRawValue.shillingEquivalentPrincipal),
      shillingEquivalentInterestAccrued: new FormControl(moneyMarketDealRawValue.shillingEquivalentInterestAccrued),
      shillingEquivalentPVFull: new FormControl(moneyMarketDealRawValue.shillingEquivalentPVFull),
      counterpartyDomicile: new FormControl(moneyMarketDealRawValue.counterpartyDomicile),
      settlementDate: new FormControl(moneyMarketDealRawValue.settlementDate, {
        validators: [Validators.required],
      }),
      transactionCollateral: new FormControl(moneyMarketDealRawValue.transactionCollateral),
      institutionType: new FormControl(moneyMarketDealRawValue.institutionType),
      maturityDate: new FormControl(moneyMarketDealRawValue.maturityDate, {
        validators: [Validators.required],
      }),
      institutionReportName: new FormControl(moneyMarketDealRawValue.institutionReportName),
      transactionType: new FormControl(moneyMarketDealRawValue.transactionType),
      reportDate: new FormControl(moneyMarketDealRawValue.reportDate, {
        validators: [Validators.required],
      }),
      active: new FormControl(moneyMarketDealRawValue.active, {
        validators: [Validators.required],
      }),
      moneyMarketList: new FormControl(moneyMarketDealRawValue.moneyMarketList, {
        validators: [Validators.required],
      }),
    });
  }

  getMoneyMarketDeal(form: MoneyMarketDealFormGroup): IMoneyMarketDeal | NewMoneyMarketDeal {
    return form.getRawValue() as IMoneyMarketDeal | NewMoneyMarketDeal;
  }

  resetForm(form: MoneyMarketDealFormGroup, moneyMarketDeal: MoneyMarketDealFormGroupInput): void {
    const moneyMarketDealRawValue = { ...this.getFormDefaults(), ...moneyMarketDeal };
    form.reset(
      {
        ...moneyMarketDealRawValue,
        id: { value: moneyMarketDealRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MoneyMarketDealFormDefaults {
    return {
      id: null,
      active: false,
    };
  }
}
