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

import { IMoneyMarketUploadNotification, NewMoneyMarketUploadNotification } from '../money-market-upload-notification.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMoneyMarketUploadNotification for edit and NewMoneyMarketUploadNotificationFormGroupInput for create.
 */
type MoneyMarketUploadNotificationFormGroupInput =
  | IMoneyMarketUploadNotification
  | PartialWithRequiredKeyOf<NewMoneyMarketUploadNotification>;

type MoneyMarketUploadNotificationFormDefaults = Pick<NewMoneyMarketUploadNotification, 'id' | 'placeholders'>;

type MoneyMarketUploadNotificationFormGroupContent = {
  id: FormControl<IMoneyMarketUploadNotification['id'] | NewMoneyMarketUploadNotification['id']>;
  errorMessage: FormControl<IMoneyMarketUploadNotification['errorMessage']>;
  rowNumber: FormControl<IMoneyMarketUploadNotification['rowNumber']>;
  referenceNumber: FormControl<IMoneyMarketUploadNotification['referenceNumber']>;
  moneyMarketList: FormControl<IMoneyMarketUploadNotification['moneyMarketList']>;
  reportBatch: FormControl<IMoneyMarketUploadNotification['reportBatch']>;
  placeholders: FormControl<IMoneyMarketUploadNotification['placeholders']>;
};

export type MoneyMarketUploadNotificationFormGroup = FormGroup<MoneyMarketUploadNotificationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MoneyMarketUploadNotificationFormService {
  createMoneyMarketUploadNotificationFormGroup(
    moneyMarketUploadNotification: MoneyMarketUploadNotificationFormGroupInput = { id: null },
  ): MoneyMarketUploadNotificationFormGroup {
    const moneyMarketUploadNotificationRawValue = {
      ...this.getFormDefaults(),
      ...moneyMarketUploadNotification,
    };
    return new FormGroup<MoneyMarketUploadNotificationFormGroupContent>({
      id: new FormControl(
        { value: moneyMarketUploadNotificationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      errorMessage: new FormControl(moneyMarketUploadNotificationRawValue.errorMessage),
      rowNumber: new FormControl(moneyMarketUploadNotificationRawValue.rowNumber),
      referenceNumber: new FormControl(moneyMarketUploadNotificationRawValue.referenceNumber, {
        validators: [Validators.required],
      }),
      moneyMarketList: new FormControl(moneyMarketUploadNotificationRawValue.moneyMarketList),
      reportBatch: new FormControl(moneyMarketUploadNotificationRawValue.reportBatch),
      placeholders: new FormControl(moneyMarketUploadNotificationRawValue.placeholders ?? []),
    });
  }

  getMoneyMarketUploadNotification(
    form: MoneyMarketUploadNotificationFormGroup,
  ): IMoneyMarketUploadNotification | NewMoneyMarketUploadNotification {
    return form.getRawValue() as IMoneyMarketUploadNotification | NewMoneyMarketUploadNotification;
  }

  resetForm(
    form: MoneyMarketUploadNotificationFormGroup,
    moneyMarketUploadNotification: MoneyMarketUploadNotificationFormGroupInput,
  ): void {
    const moneyMarketUploadNotificationRawValue = { ...this.getFormDefaults(), ...moneyMarketUploadNotification };
    form.reset(
      {
        ...moneyMarketUploadNotificationRawValue,
        id: { value: moneyMarketUploadNotificationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MoneyMarketUploadNotificationFormDefaults {
    return {
      id: null,
      placeholders: [],
    };
  }
}
