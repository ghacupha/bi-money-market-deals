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
import { FiscalYearStatusType } from 'app/entities/enumerations/fiscal-year-status-type.model';
import { FiscalYearService } from '../service/fiscal-year.service';
import { IFiscalYear } from '../fiscal-year.model';
import { FiscalYearFormGroup, FiscalYearFormService } from './fiscal-year-form.service';

@Component({
  selector: 'jhi-fiscal-year-update',
  templateUrl: './fiscal-year-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FiscalYearUpdateComponent implements OnInit {
  isSaving = false;
  fiscalYear: IFiscalYear | null = null;
  fiscalYearStatusTypeValues = Object.keys(FiscalYearStatusType);

  placeholdersSharedCollection: IPlaceholder[] = [];

  protected fiscalYearService = inject(FiscalYearService);
  protected fiscalYearFormService = inject(FiscalYearFormService);
  protected placeholderService = inject(PlaceholderService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FiscalYearFormGroup = this.fiscalYearFormService.createFiscalYearFormGroup();

  comparePlaceholder = (o1: IPlaceholder | null, o2: IPlaceholder | null): boolean => this.placeholderService.comparePlaceholder(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fiscalYear }) => {
      this.fiscalYear = fiscalYear;
      if (fiscalYear) {
        this.updateForm(fiscalYear);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fiscalYear = this.fiscalYearFormService.getFiscalYear(this.editForm);
    if (fiscalYear.id !== null) {
      this.subscribeToSaveResponse(this.fiscalYearService.update(fiscalYear));
    } else {
      this.subscribeToSaveResponse(this.fiscalYearService.create(fiscalYear));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFiscalYear>>): void {
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

  protected updateForm(fiscalYear: IFiscalYear): void {
    this.fiscalYear = fiscalYear;
    this.fiscalYearFormService.resetForm(this.editForm, fiscalYear);

    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing<IPlaceholder>(
      this.placeholdersSharedCollection,
      ...(fiscalYear.placeholders ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing<IPlaceholder>(placeholders, ...(this.fiscalYear?.placeholders ?? [])),
        ),
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));
  }
}
