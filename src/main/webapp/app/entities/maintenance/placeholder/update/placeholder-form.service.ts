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

import { IPlaceholder, NewPlaceholder } from '../placeholder.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPlaceholder for edit and NewPlaceholderFormGroupInput for create.
 */
type PlaceholderFormGroupInput = IPlaceholder | PartialWithRequiredKeyOf<NewPlaceholder>;

type PlaceholderFormDefaults = Pick<
  NewPlaceholder,
  | 'id'
  | 'dealers'
  | 'securityClearances'
  | 'applicationUsers'
  | 'fiscalYears'
  | 'fiscalQuarters'
  | 'fiscalMonths'
  | 'reportBatches'
  | 'moneyMarketLists'
  | 'moneyMarketUploadNotifications'
>;

type PlaceholderFormGroupContent = {
  id: FormControl<IPlaceholder['id'] | NewPlaceholder['id']>;
  description: FormControl<IPlaceholder['description']>;
  token: FormControl<IPlaceholder['token']>;
  containingPlaceholder: FormControl<IPlaceholder['containingPlaceholder']>;
  dealers: FormControl<IPlaceholder['dealers']>;
  securityClearances: FormControl<IPlaceholder['securityClearances']>;
  applicationUsers: FormControl<IPlaceholder['applicationUsers']>;
  fiscalYears: FormControl<IPlaceholder['fiscalYears']>;
  fiscalQuarters: FormControl<IPlaceholder['fiscalQuarters']>;
  fiscalMonths: FormControl<IPlaceholder['fiscalMonths']>;
  reportBatches: FormControl<IPlaceholder['reportBatches']>;
  moneyMarketLists: FormControl<IPlaceholder['moneyMarketLists']>;
  moneyMarketUploadNotifications: FormControl<IPlaceholder['moneyMarketUploadNotifications']>;
};

export type PlaceholderFormGroup = FormGroup<PlaceholderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PlaceholderFormService {
  createPlaceholderFormGroup(placeholder: PlaceholderFormGroupInput = { id: null }): PlaceholderFormGroup {
    const placeholderRawValue = {
      ...this.getFormDefaults(),
      ...placeholder,
    };
    return new FormGroup<PlaceholderFormGroupContent>({
      id: new FormControl(
        { value: placeholderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      description: new FormControl(placeholderRawValue.description, {
        validators: [Validators.required],
      }),
      token: new FormControl(placeholderRawValue.token),
      containingPlaceholder: new FormControl(placeholderRawValue.containingPlaceholder),
      dealers: new FormControl(placeholderRawValue.dealers ?? []),
      securityClearances: new FormControl(placeholderRawValue.securityClearances ?? []),
      applicationUsers: new FormControl(placeholderRawValue.applicationUsers ?? []),
      fiscalYears: new FormControl(placeholderRawValue.fiscalYears ?? []),
      fiscalQuarters: new FormControl(placeholderRawValue.fiscalQuarters ?? []),
      fiscalMonths: new FormControl(placeholderRawValue.fiscalMonths ?? []),
      reportBatches: new FormControl(placeholderRawValue.reportBatches ?? []),
      moneyMarketLists: new FormControl(placeholderRawValue.moneyMarketLists ?? []),
      moneyMarketUploadNotifications: new FormControl(placeholderRawValue.moneyMarketUploadNotifications ?? []),
    });
  }

  getPlaceholder(form: PlaceholderFormGroup): IPlaceholder | NewPlaceholder {
    return form.getRawValue() as IPlaceholder | NewPlaceholder;
  }

  resetForm(form: PlaceholderFormGroup, placeholder: PlaceholderFormGroupInput): void {
    const placeholderRawValue = { ...this.getFormDefaults(), ...placeholder };
    form.reset(
      {
        ...placeholderRawValue,
        id: { value: placeholderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PlaceholderFormDefaults {
    return {
      id: null,
      dealers: [],
      securityClearances: [],
      applicationUsers: [],
      fiscalYears: [],
      fiscalQuarters: [],
      fiscalMonths: [],
      reportBatches: [],
      moneyMarketLists: [],
      moneyMarketUploadNotifications: [],
    };
  }
}
