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

import { IPlaceholder } from 'app/entities/maintenance/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/maintenance/placeholder/service/placeholder.service';
import { IApplicationUser } from 'app/entities/maintenance/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/maintenance/application-user/service/application-user.service';
import { IReportBatch } from 'app/entities/moneyMarketBi/report-batch/report-batch.model';
import { ReportBatchService } from 'app/entities/moneyMarketBi/report-batch/service/report-batch.service';
import { reportBatchStatus } from 'app/entities/enumerations/report-batch-status.model';
import { MoneyMarketListService } from '../service/money-market-list.service';
import { IMoneyMarketList } from '../money-market-list.model';
import { MoneyMarketListFormGroup, MoneyMarketListFormService } from './money-market-list-form.service';

@Component({
  selector: 'jhi-money-market-list-update',
  templateUrl: './money-market-list-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MoneyMarketListUpdateComponent implements OnInit {
  isSaving = false;
  moneyMarketList: IMoneyMarketList | null = null;
  reportBatchStatusValues = Object.keys(reportBatchStatus);

  placeholdersSharedCollection: IPlaceholder[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];
  reportBatchesCollection: IReportBatch[] = [];

  protected moneyMarketListService = inject(MoneyMarketListService);
  protected moneyMarketListFormService = inject(MoneyMarketListFormService);
  protected placeholderService = inject(PlaceholderService);
  protected applicationUserService = inject(ApplicationUserService);
  protected reportBatchService = inject(ReportBatchService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MoneyMarketListFormGroup = this.moneyMarketListFormService.createMoneyMarketListFormGroup();

  comparePlaceholder = (o1: IPlaceholder | null, o2: IPlaceholder | null): boolean => this.placeholderService.comparePlaceholder(o1, o2);

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  compareReportBatch = (o1: IReportBatch | null, o2: IReportBatch | null): boolean => this.reportBatchService.compareReportBatch(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ moneyMarketList }) => {
      this.moneyMarketList = moneyMarketList;
      if (moneyMarketList) {
        this.updateForm(moneyMarketList);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const moneyMarketList = this.moneyMarketListFormService.getMoneyMarketList(this.editForm);
    if (moneyMarketList.id !== null) {
      this.subscribeToSaveResponse(this.moneyMarketListService.update(moneyMarketList));
    } else {
      this.subscribeToSaveResponse(this.moneyMarketListService.create(moneyMarketList));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMoneyMarketList>>): void {
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

  protected updateForm(moneyMarketList: IMoneyMarketList): void {
    this.moneyMarketList = moneyMarketList;
    this.moneyMarketListFormService.resetForm(this.editForm, moneyMarketList);

    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing<IPlaceholder>(
      this.placeholdersSharedCollection,
      ...(moneyMarketList.placeholders ?? []),
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      moneyMarketList.uploadedBy,
    );
    this.reportBatchesCollection = this.reportBatchService.addReportBatchToCollectionIfMissing<IReportBatch>(
      this.reportBatchesCollection,
      moneyMarketList.reportBatch,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing<IPlaceholder>(
            placeholders,
            ...(this.moneyMarketList?.placeholders ?? []),
          ),
        ),
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));

    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
            applicationUsers,
            this.moneyMarketList?.uploadedBy,
          ),
        ),
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.reportBatchService
      .query({ 'moneyMarketListId.specified': 'false' })
      .pipe(map((res: HttpResponse<IReportBatch[]>) => res.body ?? []))
      .pipe(
        map((reportBatches: IReportBatch[]) =>
          this.reportBatchService.addReportBatchToCollectionIfMissing<IReportBatch>(reportBatches, this.moneyMarketList?.reportBatch),
        ),
      )
      .subscribe((reportBatches: IReportBatch[]) => (this.reportBatchesCollection = reportBatches));
  }
}
