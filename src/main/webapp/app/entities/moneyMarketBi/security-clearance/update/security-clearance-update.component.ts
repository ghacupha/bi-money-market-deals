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
import { ISecurityClearance } from '../security-clearance.model';
import { SecurityClearanceService } from '../service/security-clearance.service';
import { SecurityClearanceFormGroup, SecurityClearanceFormService } from './security-clearance-form.service';

@Component({
  selector: 'jhi-security-clearance-update',
  templateUrl: './security-clearance-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SecurityClearanceUpdateComponent implements OnInit {
  isSaving = false;
  securityClearance: ISecurityClearance | null = null;

  placeholdersSharedCollection: IPlaceholder[] = [];

  protected securityClearanceService = inject(SecurityClearanceService);
  protected securityClearanceFormService = inject(SecurityClearanceFormService);
  protected placeholderService = inject(PlaceholderService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SecurityClearanceFormGroup = this.securityClearanceFormService.createSecurityClearanceFormGroup();

  comparePlaceholder = (o1: IPlaceholder | null, o2: IPlaceholder | null): boolean => this.placeholderService.comparePlaceholder(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ securityClearance }) => {
      this.securityClearance = securityClearance;
      if (securityClearance) {
        this.updateForm(securityClearance);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const securityClearance = this.securityClearanceFormService.getSecurityClearance(this.editForm);
    if (securityClearance.id !== null) {
      this.subscribeToSaveResponse(this.securityClearanceService.update(securityClearance));
    } else {
      this.subscribeToSaveResponse(this.securityClearanceService.create(securityClearance));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISecurityClearance>>): void {
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

  protected updateForm(securityClearance: ISecurityClearance): void {
    this.securityClearance = securityClearance;
    this.securityClearanceFormService.resetForm(this.editForm, securityClearance);

    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing<IPlaceholder>(
      this.placeholdersSharedCollection,
      ...(securityClearance.placeholders ?? []),
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
            ...(this.securityClearance?.placeholders ?? []),
          ),
        ),
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));
  }
}
