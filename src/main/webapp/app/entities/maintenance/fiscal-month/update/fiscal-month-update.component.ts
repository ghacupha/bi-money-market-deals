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
import { IPlaceholder } from 'app/entities/maintenance/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/maintenance/placeholder/service/placeholder.service';
import { IFiscalQuarter } from 'app/entities/maintenance/fiscal-quarter/fiscal-quarter.model';
import { FiscalQuarterService } from 'app/entities/maintenance/fiscal-quarter/service/fiscal-quarter.service';
import { FiscalMonthService } from '../service/fiscal-month.service';
import { IFiscalMonth } from '../fiscal-month.model';
import { FiscalMonthFormGroup, FiscalMonthFormService } from './fiscal-month-form.service';

@Component({
  selector: 'jhi-fiscal-month-update',
  templateUrl: './fiscal-month-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FiscalMonthUpdateComponent implements OnInit {
  isSaving = false;
  fiscalMonth: IFiscalMonth | null = null;

  fiscalYearsSharedCollection: IFiscalYear[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];
  fiscalQuartersSharedCollection: IFiscalQuarter[] = [];

  protected fiscalMonthService = inject(FiscalMonthService);
  protected fiscalMonthFormService = inject(FiscalMonthFormService);
  protected fiscalYearService = inject(FiscalYearService);
  protected placeholderService = inject(PlaceholderService);
  protected fiscalQuarterService = inject(FiscalQuarterService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FiscalMonthFormGroup = this.fiscalMonthFormService.createFiscalMonthFormGroup();

  compareFiscalYear = (o1: IFiscalYear | null, o2: IFiscalYear | null): boolean => this.fiscalYearService.compareFiscalYear(o1, o2);

  comparePlaceholder = (o1: IPlaceholder | null, o2: IPlaceholder | null): boolean => this.placeholderService.comparePlaceholder(o1, o2);

  compareFiscalQuarter = (o1: IFiscalQuarter | null, o2: IFiscalQuarter | null): boolean =>
    this.fiscalQuarterService.compareFiscalQuarter(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fiscalMonth }) => {
      this.fiscalMonth = fiscalMonth;
      if (fiscalMonth) {
        this.updateForm(fiscalMonth);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fiscalMonth = this.fiscalMonthFormService.getFiscalMonth(this.editForm);
    if (fiscalMonth.id !== null) {
      this.subscribeToSaveResponse(this.fiscalMonthService.update(fiscalMonth));
    } else {
      this.subscribeToSaveResponse(this.fiscalMonthService.create(fiscalMonth));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFiscalMonth>>): void {
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

  protected updateForm(fiscalMonth: IFiscalMonth): void {
    this.fiscalMonth = fiscalMonth;
    this.fiscalMonthFormService.resetForm(this.editForm, fiscalMonth);

    this.fiscalYearsSharedCollection = this.fiscalYearService.addFiscalYearToCollectionIfMissing<IFiscalYear>(
      this.fiscalYearsSharedCollection,
      fiscalMonth.fiscalYear,
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing<IPlaceholder>(
      this.placeholdersSharedCollection,
      ...(fiscalMonth.placeholders ?? []),
    );
    this.fiscalQuartersSharedCollection = this.fiscalQuarterService.addFiscalQuarterToCollectionIfMissing<IFiscalQuarter>(
      this.fiscalQuartersSharedCollection,
      fiscalMonth.fiscalQuarter,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.fiscalYearService
      .query()
      .pipe(map((res: HttpResponse<IFiscalYear[]>) => res.body ?? []))
      .pipe(
        map((fiscalYears: IFiscalYear[]) =>
          this.fiscalYearService.addFiscalYearToCollectionIfMissing<IFiscalYear>(fiscalYears, this.fiscalMonth?.fiscalYear),
        ),
      )
      .subscribe((fiscalYears: IFiscalYear[]) => (this.fiscalYearsSharedCollection = fiscalYears));

    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing<IPlaceholder>(
            placeholders,
            ...(this.fiscalMonth?.placeholders ?? []),
          ),
        ),
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));

    this.fiscalQuarterService
      .query()
      .pipe(map((res: HttpResponse<IFiscalQuarter[]>) => res.body ?? []))
      .pipe(
        map((fiscalQuarters: IFiscalQuarter[]) =>
          this.fiscalQuarterService.addFiscalQuarterToCollectionIfMissing<IFiscalQuarter>(fiscalQuarters, this.fiscalMonth?.fiscalQuarter),
        ),
      )
      .subscribe((fiscalQuarters: IFiscalQuarter[]) => (this.fiscalQuartersSharedCollection = fiscalQuarters));
  }
}
