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

import { IPlaceholder } from 'app/entities/maintenance/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/maintenance/placeholder/service/placeholder.service';
import { MoneyMarketListService } from '../service/money-market-list.service';
import { IMoneyMarketList } from '../money-market-list.model';
import { MoneyMarketListFormService } from './money-market-list-form.service';

import { MoneyMarketListUpdateComponent } from './money-market-list-update.component';

describe('MoneyMarketList Management Update Component', () => {
  let comp: MoneyMarketListUpdateComponent;
  let fixture: ComponentFixture<MoneyMarketListUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let moneyMarketListFormService: MoneyMarketListFormService;
  let moneyMarketListService: MoneyMarketListService;
  let placeholderService: PlaceholderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MoneyMarketListUpdateComponent],
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
      .overrideTemplate(MoneyMarketListUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MoneyMarketListUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    moneyMarketListFormService = TestBed.inject(MoneyMarketListFormService);
    moneyMarketListService = TestBed.inject(MoneyMarketListService);
    placeholderService = TestBed.inject(PlaceholderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Placeholder query and add missing value', () => {
      const moneyMarketList: IMoneyMarketList = { id: 31570 };
      const placeholders: IPlaceholder[] = [{ id: 13408 }];
      moneyMarketList.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 13408 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ moneyMarketList });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(
        placeholderCollection,
        ...additionalPlaceholders.map(expect.objectContaining),
      );
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const moneyMarketList: IMoneyMarketList = { id: 31570 };
      const placeholder: IPlaceholder = { id: 13408 };
      moneyMarketList.placeholders = [placeholder];

      activatedRoute.data = of({ moneyMarketList });
      comp.ngOnInit();

      expect(comp.placeholdersSharedCollection).toContainEqual(placeholder);
      expect(comp.moneyMarketList).toEqual(moneyMarketList);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMoneyMarketList>>();
      const moneyMarketList = { id: 21196 };
      jest.spyOn(moneyMarketListFormService, 'getMoneyMarketList').mockReturnValue(moneyMarketList);
      jest.spyOn(moneyMarketListService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moneyMarketList });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: moneyMarketList }));
      saveSubject.complete();

      // THEN
      expect(moneyMarketListFormService.getMoneyMarketList).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(moneyMarketListService.update).toHaveBeenCalledWith(expect.objectContaining(moneyMarketList));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMoneyMarketList>>();
      const moneyMarketList = { id: 21196 };
      jest.spyOn(moneyMarketListFormService, 'getMoneyMarketList').mockReturnValue({ id: null });
      jest.spyOn(moneyMarketListService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moneyMarketList: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: moneyMarketList }));
      saveSubject.complete();

      // THEN
      expect(moneyMarketListFormService.getMoneyMarketList).toHaveBeenCalled();
      expect(moneyMarketListService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMoneyMarketList>>();
      const moneyMarketList = { id: 21196 };
      jest.spyOn(moneyMarketListService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moneyMarketList });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(moneyMarketListService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePlaceholder', () => {
      it('should forward to placeholderService', () => {
        const entity = { id: 13408 };
        const entity2 = { id: 24257 };
        jest.spyOn(placeholderService, 'comparePlaceholder');
        comp.comparePlaceholder(entity, entity2);
        expect(placeholderService.comparePlaceholder).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
