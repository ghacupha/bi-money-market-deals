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

import { IFiscalYear, NewFiscalYear } from '../fiscal-year.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFiscalYear for edit and NewFiscalYearFormGroupInput for create.
 */
type FiscalYearFormGroupInput = IFiscalYear | PartialWithRequiredKeyOf<NewFiscalYear>;

type FiscalYearFormDefaults = Pick<NewFiscalYear, 'id' | 'placeholders'>;

type FiscalYearFormGroupContent = {
  id: FormControl<IFiscalYear['id'] | NewFiscalYear['id']>;
  fiscalYearCode: FormControl<IFiscalYear['fiscalYearCode']>;
  startDate: FormControl<IFiscalYear['startDate']>;
  endDate: FormControl<IFiscalYear['endDate']>;
  fiscalYearStatus: FormControl<IFiscalYear['fiscalYearStatus']>;
  placeholders: FormControl<IFiscalYear['placeholders']>;
};

export type FiscalYearFormGroup = FormGroup<FiscalYearFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FiscalYearFormService {
  createFiscalYearFormGroup(fiscalYear: FiscalYearFormGroupInput = { id: null }): FiscalYearFormGroup {
    const fiscalYearRawValue = {
      ...this.getFormDefaults(),
      ...fiscalYear,
    };
    return new FormGroup<FiscalYearFormGroupContent>({
      id: new FormControl(
        { value: fiscalYearRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fiscalYearCode: new FormControl(fiscalYearRawValue.fiscalYearCode, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(fiscalYearRawValue.startDate, {
        validators: [Validators.required],
      }),
      endDate: new FormControl(fiscalYearRawValue.endDate, {
        validators: [Validators.required],
      }),
      fiscalYearStatus: new FormControl(fiscalYearRawValue.fiscalYearStatus),
      placeholders: new FormControl(fiscalYearRawValue.placeholders ?? []),
    });
  }

  getFiscalYear(form: FiscalYearFormGroup): IFiscalYear | NewFiscalYear {
    return form.getRawValue() as IFiscalYear | NewFiscalYear;
  }

  resetForm(form: FiscalYearFormGroup, fiscalYear: FiscalYearFormGroupInput): void {
    const fiscalYearRawValue = { ...this.getFormDefaults(), ...fiscalYear };
    form.reset(
      {
        ...fiscalYearRawValue,
        id: { value: fiscalYearRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FiscalYearFormDefaults {
    return {
      id: null,
      placeholders: [],
    };
  }
}
