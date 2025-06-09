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

import { IFiscalYear } from 'app/entities/maintenance/fiscal-year/fiscal-year.model';
import { FiscalYearService } from 'app/entities/maintenance/fiscal-year/service/fiscal-year.service';
import { IFiscalQuarter } from 'app/entities/maintenance/fiscal-quarter/fiscal-quarter.model';
import { FiscalQuarterService } from 'app/entities/maintenance/fiscal-quarter/service/fiscal-quarter.service';
import { IFiscalMonth } from 'app/entities/maintenance/fiscal-month/fiscal-month.model';
import { FiscalMonthService } from 'app/entities/maintenance/fiscal-month/service/fiscal-month.service';
import { IMoneyMarketList } from 'app/entities/moneyMarketBi/money-market-list/money-market-list.model';
import { MoneyMarketListService } from 'app/entities/moneyMarketBi/money-market-list/service/money-market-list.service';
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
  fiscalYearsSharedCollection: IFiscalYear[] = [];
  fiscalQuartersSharedCollection: IFiscalQuarter[] = [];
  fiscalMonthsSharedCollection: IFiscalMonth[] = [];
  moneyMarketListsSharedCollection: IMoneyMarketList[] = [];

  protected placeholderService = inject(PlaceholderService);
  protected placeholderFormService = inject(PlaceholderFormService);
  protected fiscalYearService = inject(FiscalYearService);
  protected fiscalQuarterService = inject(FiscalQuarterService);
  protected fiscalMonthService = inject(FiscalMonthService);
  protected moneyMarketListService = inject(MoneyMarketListService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PlaceholderFormGroup = this.placeholderFormService.createPlaceholderFormGroup();

  comparePlaceholder = (o1: IPlaceholder | null, o2: IPlaceholder | null): boolean => this.placeholderService.comparePlaceholder(o1, o2);

  compareFiscalYear = (o1: IFiscalYear | null, o2: IFiscalYear | null): boolean => this.fiscalYearService.compareFiscalYear(o1, o2);

  compareFiscalQuarter = (o1: IFiscalQuarter | null, o2: IFiscalQuarter | null): boolean =>
    this.fiscalQuarterService.compareFiscalQuarter(o1, o2);

  compareFiscalMonth = (o1: IFiscalMonth | null, o2: IFiscalMonth | null): boolean => this.fiscalMonthService.compareFiscalMonth(o1, o2);

  compareMoneyMarketList = (o1: IMoneyMarketList | null, o2: IMoneyMarketList | null): boolean =>
    this.moneyMarketListService.compareMoneyMarketList(o1, o2);

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
    this.moneyMarketListsSharedCollection = this.moneyMarketListService.addMoneyMarketListToCollectionIfMissing<IMoneyMarketList>(
      this.moneyMarketListsSharedCollection,
      ...(placeholder.moneyMarketLists ?? []),
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
  }
}
