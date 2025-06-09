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

import { IMoneyMarketList } from 'app/entities/moneyMarketBi/money-market-list/money-market-list.model';
import { MoneyMarketListService } from 'app/entities/moneyMarketBi/money-market-list/service/money-market-list.service';
import { IMoneyMarketDeal } from '../money-market-deal.model';
import { MoneyMarketDealService } from '../service/money-market-deal.service';
import { MoneyMarketDealFormGroup, MoneyMarketDealFormService } from './money-market-deal-form.service';

@Component({
  selector: 'jhi-money-market-deal-update',
  templateUrl: './money-market-deal-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MoneyMarketDealUpdateComponent implements OnInit {
  isSaving = false;
  moneyMarketDeal: IMoneyMarketDeal | null = null;

  moneyMarketListsSharedCollection: IMoneyMarketList[] = [];

  protected moneyMarketDealService = inject(MoneyMarketDealService);
  protected moneyMarketDealFormService = inject(MoneyMarketDealFormService);
  protected moneyMarketListService = inject(MoneyMarketListService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MoneyMarketDealFormGroup = this.moneyMarketDealFormService.createMoneyMarketDealFormGroup();

  compareMoneyMarketList = (o1: IMoneyMarketList | null, o2: IMoneyMarketList | null): boolean =>
    this.moneyMarketListService.compareMoneyMarketList(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ moneyMarketDeal }) => {
      this.moneyMarketDeal = moneyMarketDeal;
      if (moneyMarketDeal) {
        this.updateForm(moneyMarketDeal);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const moneyMarketDeal = this.moneyMarketDealFormService.getMoneyMarketDeal(this.editForm);
    if (moneyMarketDeal.id !== null) {
      this.subscribeToSaveResponse(this.moneyMarketDealService.update(moneyMarketDeal));
    } else {
      this.subscribeToSaveResponse(this.moneyMarketDealService.create(moneyMarketDeal));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMoneyMarketDeal>>): void {
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

  protected updateForm(moneyMarketDeal: IMoneyMarketDeal): void {
    this.moneyMarketDeal = moneyMarketDeal;
    this.moneyMarketDealFormService.resetForm(this.editForm, moneyMarketDeal);

    this.moneyMarketListsSharedCollection = this.moneyMarketListService.addMoneyMarketListToCollectionIfMissing<IMoneyMarketList>(
      this.moneyMarketListsSharedCollection,
      moneyMarketDeal.moneyMarketList,
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
            this.moneyMarketDeal?.moneyMarketList,
          ),
        ),
      )
      .subscribe((moneyMarketLists: IMoneyMarketList[]) => (this.moneyMarketListsSharedCollection = moneyMarketLists));
  }
}
