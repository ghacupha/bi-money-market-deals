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

import { IApplicationUser, NewApplicationUser } from '../application-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IApplicationUser for edit and NewApplicationUserFormGroupInput for create.
 */
type ApplicationUserFormGroupInput = IApplicationUser | PartialWithRequiredKeyOf<NewApplicationUser>;

type ApplicationUserFormDefaults = Pick<NewApplicationUser, 'id' | 'placeholders'>;

type ApplicationUserFormGroupContent = {
  id: FormControl<IApplicationUser['id'] | NewApplicationUser['id']>;
  designation: FormControl<IApplicationUser['designation']>;
  applicationIdentity: FormControl<IApplicationUser['applicationIdentity']>;
  organization: FormControl<IApplicationUser['organization']>;
  department: FormControl<IApplicationUser['department']>;
  securityClearance: FormControl<IApplicationUser['securityClearance']>;
  dealerIdentity: FormControl<IApplicationUser['dealerIdentity']>;
  placeholders: FormControl<IApplicationUser['placeholders']>;
};

export type ApplicationUserFormGroup = FormGroup<ApplicationUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ApplicationUserFormService {
  createApplicationUserFormGroup(applicationUser: ApplicationUserFormGroupInput = { id: null }): ApplicationUserFormGroup {
    const applicationUserRawValue = {
      ...this.getFormDefaults(),
      ...applicationUser,
    };
    return new FormGroup<ApplicationUserFormGroupContent>({
      id: new FormControl(
        { value: applicationUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      designation: new FormControl(applicationUserRawValue.designation, {
        validators: [Validators.required],
      }),
      applicationIdentity: new FormControl(applicationUserRawValue.applicationIdentity, {
        validators: [Validators.required],
      }),
      organization: new FormControl(applicationUserRawValue.organization, {
        validators: [Validators.required],
      }),
      department: new FormControl(applicationUserRawValue.department, {
        validators: [Validators.required],
      }),
      securityClearance: new FormControl(applicationUserRawValue.securityClearance, {
        validators: [Validators.required],
      }),
      dealerIdentity: new FormControl(applicationUserRawValue.dealerIdentity, {
        validators: [Validators.required],
      }),
      placeholders: new FormControl(applicationUserRawValue.placeholders ?? []),
    });
  }

  getApplicationUser(form: ApplicationUserFormGroup): IApplicationUser | NewApplicationUser {
    return form.getRawValue() as IApplicationUser | NewApplicationUser;
  }

  resetForm(form: ApplicationUserFormGroup, applicationUser: ApplicationUserFormGroupInput): void {
    const applicationUserRawValue = { ...this.getFormDefaults(), ...applicationUser };
    form.reset(
      {
        ...applicationUserRawValue,
        id: { value: applicationUserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ApplicationUserFormDefaults {
    return {
      id: null,
      placeholders: [],
    };
  }
}
