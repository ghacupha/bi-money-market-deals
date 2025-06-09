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

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IMoneyMarketList } from 'app/entities/moneyMarketBi/money-market-list/money-market-list.model';
import { MoneyMarketListService } from 'app/entities/moneyMarketBi/money-market-list/service/money-market-list.service';
import { MoneyMarketDealService } from '../service/money-market-deal.service';
import { IMoneyMarketDeal } from '../money-market-deal.model';
import { MoneyMarketDealFormService } from './money-market-deal-form.service';

import { MoneyMarketDealUpdateComponent } from './money-market-deal-update.component';

describe('MoneyMarketDeal Management Update Component', () => {
  let comp: MoneyMarketDealUpdateComponent;
  let fixture: ComponentFixture<MoneyMarketDealUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let moneyMarketDealFormService: MoneyMarketDealFormService;
  let moneyMarketDealService: MoneyMarketDealService;
  let moneyMarketListService: MoneyMarketListService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MoneyMarketDealUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MoneyMarketDealUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MoneyMarketDealUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    moneyMarketDealFormService = TestBed.inject(MoneyMarketDealFormService);
    moneyMarketDealService = TestBed.inject(MoneyMarketDealService);
    moneyMarketListService = TestBed.inject(MoneyMarketListService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call MoneyMarketList query and add missing value', () => {
      const moneyMarketDeal: IMoneyMarketDeal = { id: 4189 };
      const moneyMarketList: IMoneyMarketList = { id: 21196 };
      moneyMarketDeal.moneyMarketList = moneyMarketList;

      const moneyMarketListCollection: IMoneyMarketList[] = [{ id: 21196 }];
      jest.spyOn(moneyMarketListService, 'query').mockReturnValue(of(new HttpResponse({ body: moneyMarketListCollection })));
      const additionalMoneyMarketLists = [moneyMarketList];
      const expectedCollection: IMoneyMarketList[] = [...additionalMoneyMarketLists, ...moneyMarketListCollection];
      jest.spyOn(moneyMarketListService, 'addMoneyMarketListToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ moneyMarketDeal });
      comp.ngOnInit();

      expect(moneyMarketListService.query).toHaveBeenCalled();
      expect(moneyMarketListService.addMoneyMarketListToCollectionIfMissing).toHaveBeenCalledWith(
        moneyMarketListCollection,
        ...additionalMoneyMarketLists.map(expect.objectContaining),
      );
      expect(comp.moneyMarketListsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const moneyMarketDeal: IMoneyMarketDeal = { id: 4189 };
      const moneyMarketList: IMoneyMarketList = { id: 21196 };
      moneyMarketDeal.moneyMarketList = moneyMarketList;

      activatedRoute.data = of({ moneyMarketDeal });
      comp.ngOnInit();

      expect(comp.moneyMarketListsSharedCollection).toContainEqual(moneyMarketList);
      expect(comp.moneyMarketDeal).toEqual(moneyMarketDeal);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMoneyMarketDeal>>();
      const moneyMarketDeal = { id: 21325 };
      jest.spyOn(moneyMarketDealFormService, 'getMoneyMarketDeal').mockReturnValue(moneyMarketDeal);
      jest.spyOn(moneyMarketDealService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moneyMarketDeal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: moneyMarketDeal }));
      saveSubject.complete();

      // THEN
      expect(moneyMarketDealFormService.getMoneyMarketDeal).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(moneyMarketDealService.update).toHaveBeenCalledWith(expect.objectContaining(moneyMarketDeal));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMoneyMarketDeal>>();
      const moneyMarketDeal = { id: 21325 };
      jest.spyOn(moneyMarketDealFormService, 'getMoneyMarketDeal').mockReturnValue({ id: null });
      jest.spyOn(moneyMarketDealService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moneyMarketDeal: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: moneyMarketDeal }));
      saveSubject.complete();

      // THEN
      expect(moneyMarketDealFormService.getMoneyMarketDeal).toHaveBeenCalled();
      expect(moneyMarketDealService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMoneyMarketDeal>>();
      const moneyMarketDeal = { id: 21325 };
      jest.spyOn(moneyMarketDealService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moneyMarketDeal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(moneyMarketDealService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMoneyMarketList', () => {
      it('should forward to moneyMarketListService', () => {
        const entity = { id: 21196 };
        const entity2 = { id: 31570 };
        jest.spyOn(moneyMarketListService, 'compareMoneyMarketList');
        comp.compareMoneyMarketList(entity, entity2);
        expect(moneyMarketListService.compareMoneyMarketList).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
