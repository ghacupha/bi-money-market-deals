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

import { IFiscalQuarter, NewFiscalQuarter } from '../fiscal-quarter.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFiscalQuarter for edit and NewFiscalQuarterFormGroupInput for create.
 */
type FiscalQuarterFormGroupInput = IFiscalQuarter | PartialWithRequiredKeyOf<NewFiscalQuarter>;

type FiscalQuarterFormDefaults = Pick<NewFiscalQuarter, 'id' | 'placeholders'>;

type FiscalQuarterFormGroupContent = {
  id: FormControl<IFiscalQuarter['id'] | NewFiscalQuarter['id']>;
  quarterNumber: FormControl<IFiscalQuarter['quarterNumber']>;
  startDate: FormControl<IFiscalQuarter['startDate']>;
  endDate: FormControl<IFiscalQuarter['endDate']>;
  fiscalQuarterCode: FormControl<IFiscalQuarter['fiscalQuarterCode']>;
  fiscalYear: FormControl<IFiscalQuarter['fiscalYear']>;
  placeholders: FormControl<IFiscalQuarter['placeholders']>;
};

export type FiscalQuarterFormGroup = FormGroup<FiscalQuarterFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FiscalQuarterFormService {
  createFiscalQuarterFormGroup(fiscalQuarter: FiscalQuarterFormGroupInput = { id: null }): FiscalQuarterFormGroup {
    const fiscalQuarterRawValue = {
      ...this.getFormDefaults(),
      ...fiscalQuarter,
    };
    return new FormGroup<FiscalQuarterFormGroupContent>({
      id: new FormControl(
        { value: fiscalQuarterRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      quarterNumber: new FormControl(fiscalQuarterRawValue.quarterNumber, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(fiscalQuarterRawValue.startDate, {
        validators: [Validators.required],
      }),
      endDate: new FormControl(fiscalQuarterRawValue.endDate, {
        validators: [Validators.required],
      }),
      fiscalQuarterCode: new FormControl(fiscalQuarterRawValue.fiscalQuarterCode, {
        validators: [Validators.required],
      }),
      fiscalYear: new FormControl(fiscalQuarterRawValue.fiscalYear, {
        validators: [Validators.required],
      }),
      placeholders: new FormControl(fiscalQuarterRawValue.placeholders ?? []),
    });
  }

  getFiscalQuarter(form: FiscalQuarterFormGroup): IFiscalQuarter | NewFiscalQuarter {
    return form.getRawValue() as IFiscalQuarter | NewFiscalQuarter;
  }

  resetForm(form: FiscalQuarterFormGroup, fiscalQuarter: FiscalQuarterFormGroupInput): void {
    const fiscalQuarterRawValue = { ...this.getFormDefaults(), ...fiscalQuarter };
    form.reset(
      {
        ...fiscalQuarterRawValue,
        id: { value: fiscalQuarterRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FiscalQuarterFormDefaults {
    return {
      id: null,
      placeholders: [],
    };
  }
}
