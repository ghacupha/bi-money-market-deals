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

import { IFiscalYear } from 'app/entities/maintenance/fiscal-year/fiscal-year.model';
import { FiscalYearService } from 'app/entities/maintenance/fiscal-year/service/fiscal-year.service';
import { IPlaceholder } from 'app/entities/maintenance/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/maintenance/placeholder/service/placeholder.service';
import { IFiscalQuarter } from 'app/entities/maintenance/fiscal-quarter/fiscal-quarter.model';
import { FiscalQuarterService } from 'app/entities/maintenance/fiscal-quarter/service/fiscal-quarter.service';
import { IFiscalMonth } from '../fiscal-month.model';
import { FiscalMonthService } from '../service/fiscal-month.service';
import { FiscalMonthFormService } from './fiscal-month-form.service';

import { FiscalMonthUpdateComponent } from './fiscal-month-update.component';

describe('FiscalMonth Management Update Component', () => {
  let comp: FiscalMonthUpdateComponent;
  let fixture: ComponentFixture<FiscalMonthUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fiscalMonthFormService: FiscalMonthFormService;
  let fiscalMonthService: FiscalMonthService;
  let fiscalYearService: FiscalYearService;
  let placeholderService: PlaceholderService;
  let fiscalQuarterService: FiscalQuarterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FiscalMonthUpdateComponent],
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
      .overrideTemplate(FiscalMonthUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FiscalMonthUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fiscalMonthFormService = TestBed.inject(FiscalMonthFormService);
    fiscalMonthService = TestBed.inject(FiscalMonthService);
    fiscalYearService = TestBed.inject(FiscalYearService);
    placeholderService = TestBed.inject(PlaceholderService);
    fiscalQuarterService = TestBed.inject(FiscalQuarterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call FiscalYear query and add missing value', () => {
      const fiscalMonth: IFiscalMonth = { id: 17896 };
      const fiscalYear: IFiscalYear = { id: 1297 };
      fiscalMonth.fiscalYear = fiscalYear;

      const fiscalYearCollection: IFiscalYear[] = [{ id: 1297 }];
      jest.spyOn(fiscalYearService, 'query').mockReturnValue(of(new HttpResponse({ body: fiscalYearCollection })));
      const additionalFiscalYears = [fiscalYear];
      const expectedCollection: IFiscalYear[] = [...additionalFiscalYears, ...fiscalYearCollection];
      jest.spyOn(fiscalYearService, 'addFiscalYearToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fiscalMonth });
      comp.ngOnInit();

      expect(fiscalYearService.query).toHaveBeenCalled();
      expect(fiscalYearService.addFiscalYearToCollectionIfMissing).toHaveBeenCalledWith(
        fiscalYearCollection,
        ...additionalFiscalYears.map(expect.objectContaining),
      );
      expect(comp.fiscalYearsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Placeholder query and add missing value', () => {
      const fiscalMonth: IFiscalMonth = { id: 17896 };
      const placeholders: IPlaceholder[] = [{ id: 13408 }];
      fiscalMonth.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 13408 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fiscalMonth });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(
        placeholderCollection,
        ...additionalPlaceholders.map(expect.objectContaining),
      );
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('should call FiscalQuarter query and add missing value', () => {
      const fiscalMonth: IFiscalMonth = { id: 17896 };
      const fiscalQuarter: IFiscalQuarter = { id: 15832 };
      fiscalMonth.fiscalQuarter = fiscalQuarter;

      const fiscalQuarterCollection: IFiscalQuarter[] = [{ id: 15832 }];
      jest.spyOn(fiscalQuarterService, 'query').mockReturnValue(of(new HttpResponse({ body: fiscalQuarterCollection })));
      const additionalFiscalQuarters = [fiscalQuarter];
      const expectedCollection: IFiscalQuarter[] = [...additionalFiscalQuarters, ...fiscalQuarterCollection];
      jest.spyOn(fiscalQuarterService, 'addFiscalQuarterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fiscalMonth });
      comp.ngOnInit();

      expect(fiscalQuarterService.query).toHaveBeenCalled();
      expect(fiscalQuarterService.addFiscalQuarterToCollectionIfMissing).toHaveBeenCalledWith(
        fiscalQuarterCollection,
        ...additionalFiscalQuarters.map(expect.objectContaining),
      );
      expect(comp.fiscalQuartersSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const fiscalMonth: IFiscalMonth = { id: 17896 };
      const fiscalYear: IFiscalYear = { id: 1297 };
      fiscalMonth.fiscalYear = fiscalYear;
      const placeholder: IPlaceholder = { id: 13408 };
      fiscalMonth.placeholders = [placeholder];
      const fiscalQuarter: IFiscalQuarter = { id: 15832 };
      fiscalMonth.fiscalQuarter = fiscalQuarter;

      activatedRoute.data = of({ fiscalMonth });
      comp.ngOnInit();

      expect(comp.fiscalYearsSharedCollection).toContainEqual(fiscalYear);
      expect(comp.placeholdersSharedCollection).toContainEqual(placeholder);
      expect(comp.fiscalQuartersSharedCollection).toContainEqual(fiscalQuarter);
      expect(comp.fiscalMonth).toEqual(fiscalMonth);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFiscalMonth>>();
      const fiscalMonth = { id: 13140 };
      jest.spyOn(fiscalMonthFormService, 'getFiscalMonth').mockReturnValue(fiscalMonth);
      jest.spyOn(fiscalMonthService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fiscalMonth });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fiscalMonth }));
      saveSubject.complete();

      // THEN
      expect(fiscalMonthFormService.getFiscalMonth).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fiscalMonthService.update).toHaveBeenCalledWith(expect.objectContaining(fiscalMonth));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFiscalMonth>>();
      const fiscalMonth = { id: 13140 };
      jest.spyOn(fiscalMonthFormService, 'getFiscalMonth').mockReturnValue({ id: null });
      jest.spyOn(fiscalMonthService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fiscalMonth: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fiscalMonth }));
      saveSubject.complete();

      // THEN
      expect(fiscalMonthFormService.getFiscalMonth).toHaveBeenCalled();
      expect(fiscalMonthService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFiscalMonth>>();
      const fiscalMonth = { id: 13140 };
      jest.spyOn(fiscalMonthService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fiscalMonth });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fiscalMonthService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFiscalYear', () => {
      it('should forward to fiscalYearService', () => {
        const entity = { id: 1297 };
        const entity2 = { id: 1005 };
        jest.spyOn(fiscalYearService, 'compareFiscalYear');
        comp.compareFiscalYear(entity, entity2);
        expect(fiscalYearService.compareFiscalYear).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePlaceholder', () => {
      it('should forward to placeholderService', () => {
        const entity = { id: 13408 };
        const entity2 = { id: 24257 };
        jest.spyOn(placeholderService, 'comparePlaceholder');
        comp.comparePlaceholder(entity, entity2);
        expect(placeholderService.comparePlaceholder).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFiscalQuarter', () => {
      it('should forward to fiscalQuarterService', () => {
        const entity = { id: 15832 };
        const entity2 = { id: 12913 };
        jest.spyOn(fiscalQuarterService, 'compareFiscalQuarter');
        comp.compareFiscalQuarter(entity, entity2);
        expect(fiscalQuarterService.compareFiscalQuarter).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
