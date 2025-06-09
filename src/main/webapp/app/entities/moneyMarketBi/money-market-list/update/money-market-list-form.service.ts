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

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMoneyMarketList, NewMoneyMarketList } from '../money-market-list.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMoneyMarketList for edit and NewMoneyMarketListFormGroupInput for create.
 */
type MoneyMarketListFormGroupInput = IMoneyMarketList | PartialWithRequiredKeyOf<NewMoneyMarketList>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMoneyMarketList | NewMoneyMarketList> = Omit<T, 'uploadTimeStamp'> & {
  uploadTimeStamp?: string | null;
};

type MoneyMarketListFormRawValue = FormValueOf<IMoneyMarketList>;

type NewMoneyMarketListFormRawValue = FormValueOf<NewMoneyMarketList>;

type MoneyMarketListFormDefaults = Pick<NewMoneyMarketList, 'id' | 'uploadTimeStamp' | 'active' | 'placeholders'>;

type MoneyMarketListFormGroupContent = {
  id: FormControl<MoneyMarketListFormRawValue['id'] | NewMoneyMarketList['id']>;
  reportDate: FormControl<MoneyMarketListFormRawValue['reportDate']>;
  uploadTimeStamp: FormControl<MoneyMarketListFormRawValue['uploadTimeStamp']>;
  status: FormControl<MoneyMarketListFormRawValue['status']>;
  description: FormControl<MoneyMarketListFormRawValue['description']>;
  active: FormControl<MoneyMarketListFormRawValue['active']>;
  placeholders: FormControl<MoneyMarketListFormRawValue['placeholders']>;
  uploadedBy: FormControl<MoneyMarketListFormRawValue['uploadedBy']>;
  reportBatch: FormControl<MoneyMarketListFormRawValue['reportBatch']>;
};

export type MoneyMarketListFormGroup = FormGroup<MoneyMarketListFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MoneyMarketListFormService {
  createMoneyMarketListFormGroup(moneyMarketList: MoneyMarketListFormGroupInput = { id: null }): MoneyMarketListFormGroup {
    const moneyMarketListRawValue = this.convertMoneyMarketListToMoneyMarketListRawValue({
      ...this.getFormDefaults(),
      ...moneyMarketList,
    });
    return new FormGroup<MoneyMarketListFormGroupContent>({
      id: new FormControl(
        { value: moneyMarketListRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      reportDate: new FormControl(moneyMarketListRawValue.reportDate, {
        validators: [Validators.required],
      }),
      uploadTimeStamp: new FormControl(moneyMarketListRawValue.uploadTimeStamp, {
        validators: [Validators.required],
      }),
      status: new FormControl(moneyMarketListRawValue.status, {
        validators: [Validators.required],
      }),
      description: new FormControl(moneyMarketListRawValue.description),
      active: new FormControl(moneyMarketListRawValue.active, {
        validators: [Validators.required],
      }),
      placeholders: new FormControl(moneyMarketListRawValue.placeholders ?? []),
      uploadedBy: new FormControl(moneyMarketListRawValue.uploadedBy, {
        validators: [Validators.required],
      }),
      reportBatch: new FormControl(moneyMarketListRawValue.reportBatch, {
        validators: [Validators.required],
      }),
    });
  }

  getMoneyMarketList(form: MoneyMarketListFormGroup): IMoneyMarketList | NewMoneyMarketList {
    return this.convertMoneyMarketListRawValueToMoneyMarketList(
      form.getRawValue() as MoneyMarketListFormRawValue | NewMoneyMarketListFormRawValue,
    );
  }

  resetForm(form: MoneyMarketListFormGroup, moneyMarketList: MoneyMarketListFormGroupInput): void {
    const moneyMarketListRawValue = this.convertMoneyMarketListToMoneyMarketListRawValue({ ...this.getFormDefaults(), ...moneyMarketList });
    form.reset(
      {
        ...moneyMarketListRawValue,
        id: { value: moneyMarketListRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MoneyMarketListFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      uploadTimeStamp: currentTime,
      active: false,
      placeholders: [],
    };
  }

  private convertMoneyMarketListRawValueToMoneyMarketList(
    rawMoneyMarketList: MoneyMarketListFormRawValue | NewMoneyMarketListFormRawValue,
  ): IMoneyMarketList | NewMoneyMarketList {
    return {
      ...rawMoneyMarketList,
      uploadTimeStamp: dayjs(rawMoneyMarketList.uploadTimeStamp, DATE_TIME_FORMAT),
    };
  }

  private convertMoneyMarketListToMoneyMarketListRawValue(
    moneyMarketList: IMoneyMarketList | (Partial<NewMoneyMarketList> & MoneyMarketListFormDefaults),
  ): MoneyMarketListFormRawValue | PartialWithRequiredKeyOf<NewMoneyMarketListFormRawValue> {
    return {
      ...moneyMarketList,
      uploadTimeStamp: moneyMarketList.uploadTimeStamp ? moneyMarketList.uploadTimeStamp.format(DATE_TIME_FORMAT) : undefined,
      placeholders: moneyMarketList.placeholders ?? [],
    };
  }
}
