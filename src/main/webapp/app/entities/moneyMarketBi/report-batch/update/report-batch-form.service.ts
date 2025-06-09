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
import { IReportBatch, NewReportBatch } from '../report-batch.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReportBatch for edit and NewReportBatchFormGroupInput for create.
 */
type ReportBatchFormGroupInput = IReportBatch | PartialWithRequiredKeyOf<NewReportBatch>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IReportBatch | NewReportBatch> = Omit<T, 'uploadTimeStamp'> & {
  uploadTimeStamp?: string | null;
};

type ReportBatchFormRawValue = FormValueOf<IReportBatch>;

type NewReportBatchFormRawValue = FormValueOf<NewReportBatch>;

type ReportBatchFormDefaults = Pick<NewReportBatch, 'id' | 'uploadTimeStamp' | 'active' | 'placeholders'>;

type ReportBatchFormGroupContent = {
  id: FormControl<ReportBatchFormRawValue['id'] | NewReportBatch['id']>;
  reportDate: FormControl<ReportBatchFormRawValue['reportDate']>;
  uploadTimeStamp: FormControl<ReportBatchFormRawValue['uploadTimeStamp']>;
  status: FormControl<ReportBatchFormRawValue['status']>;
  active: FormControl<ReportBatchFormRawValue['active']>;
  description: FormControl<ReportBatchFormRawValue['description']>;
  fileIdentifier: FormControl<ReportBatchFormRawValue['fileIdentifier']>;
  processFlag: FormControl<ReportBatchFormRawValue['processFlag']>;
  csvFileAttachment: FormControl<ReportBatchFormRawValue['csvFileAttachment']>;
  csvFileAttachmentContentType: FormControl<ReportBatchFormRawValue['csvFileAttachmentContentType']>;
  uploadedBy: FormControl<ReportBatchFormRawValue['uploadedBy']>;
  placeholders: FormControl<ReportBatchFormRawValue['placeholders']>;
};

export type ReportBatchFormGroup = FormGroup<ReportBatchFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReportBatchFormService {
  createReportBatchFormGroup(reportBatch: ReportBatchFormGroupInput = { id: null }): ReportBatchFormGroup {
    const reportBatchRawValue = this.convertReportBatchToReportBatchRawValue({
      ...this.getFormDefaults(),
      ...reportBatch,
    });
    return new FormGroup<ReportBatchFormGroupContent>({
      id: new FormControl(
        { value: reportBatchRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      reportDate: new FormControl(reportBatchRawValue.reportDate, {
        validators: [Validators.required],
      }),
      uploadTimeStamp: new FormControl(reportBatchRawValue.uploadTimeStamp, {
        validators: [Validators.required],
      }),
      status: new FormControl(reportBatchRawValue.status, {
        validators: [Validators.required],
      }),
      active: new FormControl(reportBatchRawValue.active, {
        validators: [Validators.required],
      }),
      description: new FormControl(reportBatchRawValue.description, {
        validators: [Validators.required],
      }),
      fileIdentifier: new FormControl(reportBatchRawValue.fileIdentifier, {
        validators: [Validators.required],
      }),
      processFlag: new FormControl(reportBatchRawValue.processFlag),
      csvFileAttachment: new FormControl(reportBatchRawValue.csvFileAttachment, {
        validators: [Validators.required],
      }),
      csvFileAttachmentContentType: new FormControl(reportBatchRawValue.csvFileAttachmentContentType),
      uploadedBy: new FormControl(reportBatchRawValue.uploadedBy, {
        validators: [Validators.required],
      }),
      placeholders: new FormControl(reportBatchRawValue.placeholders ?? []),
    });
  }

  getReportBatch(form: ReportBatchFormGroup): IReportBatch | NewReportBatch {
    return this.convertReportBatchRawValueToReportBatch(form.getRawValue() as ReportBatchFormRawValue | NewReportBatchFormRawValue);
  }

  resetForm(form: ReportBatchFormGroup, reportBatch: ReportBatchFormGroupInput): void {
    const reportBatchRawValue = this.convertReportBatchToReportBatchRawValue({ ...this.getFormDefaults(), ...reportBatch });
    form.reset(
      {
        ...reportBatchRawValue,
        id: { value: reportBatchRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReportBatchFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      uploadTimeStamp: currentTime,
      active: false,
      placeholders: [],
    };
  }

  private convertReportBatchRawValueToReportBatch(
    rawReportBatch: ReportBatchFormRawValue | NewReportBatchFormRawValue,
  ): IReportBatch | NewReportBatch {
    return {
      ...rawReportBatch,
      uploadTimeStamp: dayjs(rawReportBatch.uploadTimeStamp, DATE_TIME_FORMAT),
    };
  }

  private convertReportBatchToReportBatchRawValue(
    reportBatch: IReportBatch | (Partial<NewReportBatch> & ReportBatchFormDefaults),
  ): ReportBatchFormRawValue | PartialWithRequiredKeyOf<NewReportBatchFormRawValue> {
    return {
      ...reportBatch,
      uploadTimeStamp: reportBatch.uploadTimeStamp ? reportBatch.uploadTimeStamp.format(DATE_TIME_FORMAT) : undefined,
      placeholders: reportBatch.placeholders ?? [],
    };
  }
}
