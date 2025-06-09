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
import { IMoneyMarketList } from 'app/entities/moneyMarketBi/money-market-list/money-market-list.model';
import { MoneyMarketListService } from 'app/entities/moneyMarketBi/money-market-list/service/money-market-list.service';
import { IReportBatch } from 'app/entities/moneyMarketBi/report-batch/report-batch.model';
import { ReportBatchService } from 'app/entities/moneyMarketBi/report-batch/service/report-batch.service';
import { IPlaceholder } from 'app/entities/maintenance/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/maintenance/placeholder/service/placeholder.service';
import { MoneyMarketUploadNotificationService } from '../service/money-market-upload-notification.service';
import { IMoneyMarketUploadNotification } from '../money-market-upload-notification.model';
import {
  MoneyMarketUploadNotificationFormGroup,
  MoneyMarketUploadNotificationFormService,
} from './money-market-upload-notification-form.service';

@Component({
  selector: 'jhi-money-market-upload-notification-update',
  templateUrl: './money-market-upload-notification-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MoneyMarketUploadNotificationUpdateComponent implements OnInit {
  isSaving = false;
  moneyMarketUploadNotification: IMoneyMarketUploadNotification | null = null;

  moneyMarketListsSharedCollection: IMoneyMarketList[] = [];
  reportBatchesSharedCollection: IReportBatch[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected moneyMarketUploadNotificationService = inject(MoneyMarketUploadNotificationService);
  protected moneyMarketUploadNotificationFormService = inject(MoneyMarketUploadNotificationFormService);
  protected moneyMarketListService = inject(MoneyMarketListService);
  protected reportBatchService = inject(ReportBatchService);
  protected placeholderService = inject(PlaceholderService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MoneyMarketUploadNotificationFormGroup =
    this.moneyMarketUploadNotificationFormService.createMoneyMarketUploadNotificationFormGroup();

  compareMoneyMarketList = (o1: IMoneyMarketList | null, o2: IMoneyMarketList | null): boolean =>
    this.moneyMarketListService.compareMoneyMarketList(o1, o2);

  compareReportBatch = (o1: IReportBatch | null, o2: IReportBatch | null): boolean => this.reportBatchService.compareReportBatch(o1, o2);

  comparePlaceholder = (o1: IPlaceholder | null, o2: IPlaceholder | null): boolean => this.placeholderService.comparePlaceholder(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ moneyMarketUploadNotification }) => {
      this.moneyMarketUploadNotification = moneyMarketUploadNotification;
      if (moneyMarketUploadNotification) {
        this.updateForm(moneyMarketUploadNotification);
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
    const moneyMarketUploadNotification = this.moneyMarketUploadNotificationFormService.getMoneyMarketUploadNotification(this.editForm);
    if (moneyMarketUploadNotification.id !== null) {
      this.subscribeToSaveResponse(this.moneyMarketUploadNotificationService.update(moneyMarketUploadNotification));
    } else {
      this.subscribeToSaveResponse(this.moneyMarketUploadNotificationService.create(moneyMarketUploadNotification));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMoneyMarketUploadNotification>>): void {
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

  protected updateForm(moneyMarketUploadNotification: IMoneyMarketUploadNotification): void {
    this.moneyMarketUploadNotification = moneyMarketUploadNotification;
    this.moneyMarketUploadNotificationFormService.resetForm(this.editForm, moneyMarketUploadNotification);

    this.moneyMarketListsSharedCollection = this.moneyMarketListService.addMoneyMarketListToCollectionIfMissing<IMoneyMarketList>(
      this.moneyMarketListsSharedCollection,
      moneyMarketUploadNotification.moneyMarketList,
    );
    this.reportBatchesSharedCollection = this.reportBatchService.addReportBatchToCollectionIfMissing<IReportBatch>(
      this.reportBatchesSharedCollection,
      moneyMarketUploadNotification.reportBatch,
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing<IPlaceholder>(
      this.placeholdersSharedCollection,
      ...(moneyMarketUploadNotification.placeholders ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.moneyMarketListService
      .query()
      .pipe(map((res: HttpResponse<IMoneyMarketList[]>) => res.body ?? []))
      .pipe(
        map((moneyMarketLists: IMoneyMarketList[]) =>
          this.moneyMarketListService.addMoneyMarketListToCollectionIfMissing<IMoneyMarketList>(
            moneyMarketLists,
            this.moneyMarketUploadNotification?.moneyMarketList,
          ),
        ),
      )
      .subscribe((moneyMarketLists: IMoneyMarketList[]) => (this.moneyMarketListsSharedCollection = moneyMarketLists));

    this.reportBatchService
      .query()
      .pipe(map((res: HttpResponse<IReportBatch[]>) => res.body ?? []))
      .pipe(
        map((reportBatches: IReportBatch[]) =>
          this.reportBatchService.addReportBatchToCollectionIfMissing<IReportBatch>(
            reportBatches,
            this.moneyMarketUploadNotification?.reportBatch,
          ),
        ),
      )
      .subscribe((reportBatches: IReportBatch[]) => (this.reportBatchesSharedCollection = reportBatches));

    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing<IPlaceholder>(
            placeholders,
            ...(this.moneyMarketUploadNotification?.placeholders ?? []),
          ),
        ),
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));
  }
}
