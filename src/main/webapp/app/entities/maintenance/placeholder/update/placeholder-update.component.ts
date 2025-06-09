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

import { IDealer } from 'app/entities/moneyMarketBi/dealer/dealer.model';
import { DealerService } from 'app/entities/moneyMarketBi/dealer/service/dealer.service';
import { ISecurityClearance } from 'app/entities/moneyMarketBi/security-clearance/security-clearance.model';
import { SecurityClearanceService } from 'app/entities/moneyMarketBi/security-clearance/service/security-clearance.service';
import { IApplicationUser } from 'app/entities/maintenance/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/maintenance/application-user/service/application-user.service';
import { IFiscalYear } from 'app/entities/maintenance/fiscal-year/fiscal-year.model';
import { FiscalYearService } from 'app/entities/maintenance/fiscal-year/service/fiscal-year.service';
import { IFiscalQuarter } from 'app/entities/maintenance/fiscal-quarter/fiscal-quarter.model';
import { FiscalQuarterService } from 'app/entities/maintenance/fiscal-quarter/service/fiscal-quarter.service';
import { IFiscalMonth } from 'app/entities/maintenance/fiscal-month/fiscal-month.model';
import { FiscalMonthService } from 'app/entities/maintenance/fiscal-month/service/fiscal-month.service';
import { IReportBatch } from 'app/entities/moneyMarketBi/report-batch/report-batch.model';
import { ReportBatchService } from 'app/entities/moneyMarketBi/report-batch/service/report-batch.service';
import { IMoneyMarketList } from 'app/entities/moneyMarketBi/money-market-list/money-market-list.model';
import { MoneyMarketListService } from 'app/entities/moneyMarketBi/money-market-list/service/money-market-list.service';
import { IMoneyMarketUploadNotification } from 'app/entities/moneyMarketBi/money-market-upload-notification/money-market-upload-notification.model';
import { MoneyMarketUploadNotificationService } from 'app/entities/moneyMarketBi/money-market-upload-notification/service/money-market-upload-notification.service';
import { PlaceholderService } from '../service/placeholder.service';
import { IPlaceholder } from '../placeholder.model';
import { PlaceholderFormGroup, PlaceholderFormService } from './placeholder-form.service';

@Component({
  selector: 'jhi-placeholder-update',
  templateUrl: './placeholder-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PlaceholderUpdateComponent implements OnInit {
  isSaving = false;
  placeholder: IPlaceholder | null = null;

  placeholdersSharedCollection: IPlaceholder[] = [];
  dealersSharedCollection: IDealer[] = [];
  securityClearancesSharedCollection: ISecurityClearance[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];
  fiscalYearsSharedCollection: IFiscalYear[] = [];
  fiscalQuartersSharedCollection: IFiscalQuarter[] = [];
  fiscalMonthsSharedCollection: IFiscalMonth[] = [];
  reportBatchesSharedCollection: IReportBatch[] = [];
  moneyMarketListsSharedCollection: IMoneyMarketList[] = [];
  moneyMarketUploadNotificationsSharedCollection: IMoneyMarketUploadNotification[] = [];

  protected placeholderService = inject(PlaceholderService);
  protected placeholderFormService = inject(PlaceholderFormService);
  protected dealerService = inject(DealerService);
  protected securityClearanceService = inject(SecurityClearanceService);
  protected applicationUserService = inject(ApplicationUserService);
  protected fiscalYearService = inject(FiscalYearService);
  protected fiscalQuarterService = inject(FiscalQuarterService);
  protected fiscalMonthService = inject(FiscalMonthService);
  protected reportBatchService = inject(ReportBatchService);
  protected moneyMarketListService = inject(MoneyMarketListService);
  protected moneyMarketUploadNotificationService = inject(MoneyMarketUploadNotificationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PlaceholderFormGroup = this.placeholderFormService.createPlaceholderFormGroup();

  comparePlaceholder = (o1: IPlaceholder | null, o2: IPlaceholder | null): boolean => this.placeholderService.comparePlaceholder(o1, o2);

  compareDealer = (o1: IDealer | null, o2: IDealer | null): boolean => this.dealerService.compareDealer(o1, o2);

  compareSecurityClearance = (o1: ISecurityClearance | null, o2: ISecurityClearance | null): boolean =>
    this.securityClearanceService.compareSecurityClearance(o1, o2);

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  compareFiscalYear = (o1: IFiscalYear | null, o2: IFiscalYear | null): boolean => this.fiscalYearService.compareFiscalYear(o1, o2);

  compareFiscalQuarter = (o1: IFiscalQuarter | null, o2: IFiscalQuarter | null): boolean =>
    this.fiscalQuarterService.compareFiscalQuarter(o1, o2);

  compareFiscalMonth = (o1: IFiscalMonth | null, o2: IFiscalMonth | null): boolean => this.fiscalMonthService.compareFiscalMonth(o1, o2);

  compareReportBatch = (o1: IReportBatch | null, o2: IReportBatch | null): boolean => this.reportBatchService.compareReportBatch(o1, o2);

  compareMoneyMarketList = (o1: IMoneyMarketList | null, o2: IMoneyMarketList | null): boolean =>
    this.moneyMarketListService.compareMoneyMarketList(o1, o2);

  compareMoneyMarketUploadNotification = (o1: IMoneyMarketUploadNotification | null, o2: IMoneyMarketUploadNotification | null): boolean =>
    this.moneyMarketUploadNotificationService.compareMoneyMarketUploadNotification(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ placeholder }) => {
      this.placeholder = placeholder;
      if (placeholder) {
        this.updateForm(placeholder);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const placeholder = this.placeholderFormService.getPlaceholder(this.editForm);
    if (placeholder.id !== null) {
      this.subscribeToSaveResponse(this.placeholderService.update(placeholder));
    } else {
      this.subscribeToSaveResponse(this.placeholderService.create(placeholder));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlaceholder>>): void {
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

  protected updateForm(placeholder: IPlaceholder): void {
    this.placeholder = placeholder;
    this.placeholderFormService.resetForm(this.editForm, placeholder);

    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing<IPlaceholder>(
      this.placeholdersSharedCollection,
      placeholder.containingPlaceholder,
    );
    this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing<IDealer>(
      this.dealersSharedCollection,
      ...(placeholder.dealers ?? []),
    );
    this.securityClearancesSharedCollection = this.securityClearanceService.addSecurityClearanceToCollectionIfMissing<ISecurityClearance>(
      this.securityClearancesSharedCollection,
      ...(placeholder.securityClearances ?? []),
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      ...(placeholder.applicationUsers ?? []),
    );
    this.fiscalYearsSharedCollection = this.fiscalYearService.addFiscalYearToCollectionIfMissing<IFiscalYear>(
      this.fiscalYearsSharedCollection,
      ...(placeholder.fiscalYears ?? []),
    );
    this.fiscalQuartersSharedCollection = this.fiscalQuarterService.addFiscalQuarterToCollectionIfMissing<IFiscalQuarter>(
      this.fiscalQuartersSharedCollection,
      ...(placeholder.fiscalQuarters ?? []),
    );
    this.fiscalMonthsSharedCollection = this.fiscalMonthService.addFiscalMonthToCollectionIfMissing<IFiscalMonth>(
      this.fiscalMonthsSharedCollection,
      ...(placeholder.fiscalMonths ?? []),
    );
    this.reportBatchesSharedCollection = this.reportBatchService.addReportBatchToCollectionIfMissing<IReportBatch>(
      this.reportBatchesSharedCollection,
      ...(placeholder.reportBatches ?? []),
    );
    this.moneyMarketListsSharedCollection = this.moneyMarketListService.addMoneyMarketListToCollectionIfMissing<IMoneyMarketList>(
      this.moneyMarketListsSharedCollection,
      ...(placeholder.moneyMarketLists ?? []),
    );
    this.moneyMarketUploadNotificationsSharedCollection =
      this.moneyMarketUploadNotificationService.addMoneyMarketUploadNotificationToCollectionIfMissing<IMoneyMarketUploadNotification>(
        this.moneyMarketUploadNotificationsSharedCollection,
        ...(placeholder.moneyMarketUploadNotifications ?? []),
      );
  }

  protected loadRelationshipsOptions(): void {
    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing<IPlaceholder>(placeholders, this.placeholder?.containingPlaceholder),
        ),
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));

    this.dealerService
      .query()
      .pipe(map((res: HttpResponse<IDealer[]>) => res.body ?? []))
      .pipe(
        map((dealers: IDealer[]) =>
          this.dealerService.addDealerToCollectionIfMissing<IDealer>(dealers, ...(this.placeholder?.dealers ?? [])),
        ),
      )
      .subscribe((dealers: IDealer[]) => (this.dealersSharedCollection = dealers));

    this.securityClearanceService
      .query()
      .pipe(map((res: HttpResponse<ISecurityClearance[]>) => res.body ?? []))
      .pipe(
        map((securityClearances: ISecurityClearance[]) =>
          this.securityClearanceService.addSecurityClearanceToCollectionIfMissing<ISecurityClearance>(
            securityClearances,
            ...(this.placeholder?.securityClearances ?? []),
          ),
        ),
      )
      .subscribe((securityClearances: ISecurityClearance[]) => (this.securityClearancesSharedCollection = securityClearances));

    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
            applicationUsers,
            ...(this.placeholder?.applicationUsers ?? []),
          ),
        ),
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.fiscalYearService
      .query()
      .pipe(map((res: HttpResponse<IFiscalYear[]>) => res.body ?? []))
      .pipe(
        map((fiscalYears: IFiscalYear[]) =>
          this.fiscalYearService.addFiscalYearToCollectionIfMissing<IFiscalYear>(fiscalYears, ...(this.placeholder?.fiscalYears ?? [])),
        ),
      )
      .subscribe((fiscalYears: IFiscalYear[]) => (this.fiscalYearsSharedCollection = fiscalYears));

    this.fiscalQuarterService
      .query()
      .pipe(map((res: HttpResponse<IFiscalQuarter[]>) => res.body ?? []))
      .pipe(
        map((fiscalQuarters: IFiscalQuarter[]) =>
          this.fiscalQuarterService.addFiscalQuarterToCollectionIfMissing<IFiscalQuarter>(
            fiscalQuarters,
            ...(this.placeholder?.fiscalQuarters ?? []),
          ),
        ),
      )
      .subscribe((fiscalQuarters: IFiscalQuarter[]) => (this.fiscalQuartersSharedCollection = fiscalQuarters));

    this.fiscalMonthService
      .query()
      .pipe(map((res: HttpResponse<IFiscalMonth[]>) => res.body ?? []))
      .pipe(
        map((fiscalMonths: IFiscalMonth[]) =>
          this.fiscalMonthService.addFiscalMonthToCollectionIfMissing<IFiscalMonth>(
            fiscalMonths,
            ...(this.placeholder?.fiscalMonths ?? []),
          ),
        ),
      )
      .subscribe((fiscalMonths: IFiscalMonth[]) => (this.fiscalMonthsSharedCollection = fiscalMonths));

    this.reportBatchService
      .query()
      .pipe(map((res: HttpResponse<IReportBatch[]>) => res.body ?? []))
      .pipe(
        map((reportBatches: IReportBatch[]) =>
          this.reportBatchService.addReportBatchToCollectionIfMissing<IReportBatch>(
            reportBatches,
            ...(this.placeholder?.reportBatches ?? []),
          ),
        ),
      )
      .subscribe((reportBatches: IReportBatch[]) => (this.reportBatchesSharedCollection = reportBatches));

    this.moneyMarketListService
      .query()
      .pipe(map((res: HttpResponse<IMoneyMarketList[]>) => res.body ?? []))
      .pipe(
        map((moneyMarketLists: IMoneyMarketList[]) =>
          this.moneyMarketListService.addMoneyMarketListToCollectionIfMissing<IMoneyMarketList>(
            moneyMarketLists,
            ...(this.placeholder?.moneyMarketLists ?? []),
          ),
        ),
      )
      .subscribe((moneyMarketLists: IMoneyMarketList[]) => (this.moneyMarketListsSharedCollection = moneyMarketLists));

    this.moneyMarketUploadNotificationService
      .query()
      .pipe(map((res: HttpResponse<IMoneyMarketUploadNotification[]>) => res.body ?? []))
      .pipe(
        map((moneyMarketUploadNotifications: IMoneyMarketUploadNotification[]) =>
          this.moneyMarketUploadNotificationService.addMoneyMarketUploadNotificationToCollectionIfMissing<IMoneyMarketUploadNotification>(
            moneyMarketUploadNotifications,
            ...(this.placeholder?.moneyMarketUploadNotifications ?? []),
          ),
        ),
      )
      .subscribe(
        (moneyMarketUploadNotifications: IMoneyMarketUploadNotification[]) =>
          (this.moneyMarketUploadNotificationsSharedCollection = moneyMarketUploadNotifications),
      );
  }
}
