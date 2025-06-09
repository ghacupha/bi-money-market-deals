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

import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IApplicationUser } from 'app/entities/maintenance/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/maintenance/application-user/service/application-user.service';
import { IPlaceholder } from 'app/entities/maintenance/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/maintenance/placeholder/service/placeholder.service';
import { reportBatchStatus } from 'app/entities/enumerations/report-batch-status.model';
import { FileProcessFlag } from 'app/entities/enumerations/file-process-flag.model';
import { ReportBatchService } from '../service/report-batch.service';
import { IReportBatch } from '../report-batch.model';
import { ReportBatchFormGroup, ReportBatchFormService } from './report-batch-form.service';

@Component({
  selector: 'jhi-report-batch-update',
  templateUrl: './report-batch-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ReportBatchUpdateComponent implements OnInit {
  isSaving = false;
  reportBatch: IReportBatch | null = null;
  reportBatchStatusValues = Object.keys(reportBatchStatus);
  fileProcessFlagValues = Object.keys(FileProcessFlag);

  applicationUsersSharedCollection: IApplicationUser[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected reportBatchService = inject(ReportBatchService);
  protected reportBatchFormService = inject(ReportBatchFormService);
  protected applicationUserService = inject(ApplicationUserService);
  protected placeholderService = inject(PlaceholderService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReportBatchFormGroup = this.reportBatchFormService.createReportBatchFormGroup();

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  comparePlaceholder = (o1: IPlaceholder | null, o2: IPlaceholder | null): boolean => this.placeholderService.comparePlaceholder(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reportBatch }) => {
      this.reportBatch = reportBatch;
      if (reportBatch) {
        this.updateForm(reportBatch);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('moneyMarketBiApp.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reportBatch = this.reportBatchFormService.getReportBatch(this.editForm);
    if (reportBatch.id !== null) {
      this.subscribeToSaveResponse(this.reportBatchService.update(reportBatch));
    } else {
      this.subscribeToSaveResponse(this.reportBatchService.create(reportBatch));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReportBatch>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(reportBatch: IReportBatch): void {
    this.reportBatch = reportBatch;
    this.reportBatchFormService.resetForm(this.editForm, reportBatch);

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      reportBatch.uploadedBy,
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing<IPlaceholder>(
      this.placeholdersSharedCollection,
      ...(reportBatch.placeholders ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
            applicationUsers,
            this.reportBatch?.uploadedBy,
          ),
        ),
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing<IPlaceholder>(
            placeholders,
            ...(this.reportBatch?.placeholders ?? []),
          ),
        ),
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));
  }
}
