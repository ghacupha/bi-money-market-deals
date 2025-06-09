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
import { IFiscalQuarter } from '../fiscal-quarter.model';
import { FiscalQuarterService } from '../service/fiscal-quarter.service';
import { FiscalQuarterFormService } from './fiscal-quarter-form.service';

import { FiscalQuarterUpdateComponent } from './fiscal-quarter-update.component';

describe('FiscalQuarter Management Update Component', () => {
  let comp: FiscalQuarterUpdateComponent;
  let fixture: ComponentFixture<FiscalQuarterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fiscalQuarterFormService: FiscalQuarterFormService;
  let fiscalQuarterService: FiscalQuarterService;
  let fiscalYearService: FiscalYearService;
  let placeholderService: PlaceholderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FiscalQuarterUpdateComponent],
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
      .overrideTemplate(FiscalQuarterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FiscalQuarterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fiscalQuarterFormService = TestBed.inject(FiscalQuarterFormService);
    fiscalQuarterService = TestBed.inject(FiscalQuarterService);
    fiscalYearService = TestBed.inject(FiscalYearService);
    placeholderService = TestBed.inject(PlaceholderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call FiscalYear query and add missing value', () => {
      const fiscalQuarter: IFiscalQuarter = { id: 12913 };
      const fiscalYear: IFiscalYear = { id: 1297 };
      fiscalQuarter.fiscalYear = fiscalYear;

      const fiscalYearCollection: IFiscalYear[] = [{ id: 1297 }];
      jest.spyOn(fiscalYearService, 'query').mockReturnValue(of(new HttpResponse({ body: fiscalYearCollection })));
      const additionalFiscalYears = [fiscalYear];
      const expectedCollection: IFiscalYear[] = [...additionalFiscalYears, ...fiscalYearCollection];
      jest.spyOn(fiscalYearService, 'addFiscalYearToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fiscalQuarter });
      comp.ngOnInit();

      expect(fiscalYearService.query).toHaveBeenCalled();
      expect(fiscalYearService.addFiscalYearToCollectionIfMissing).toHaveBeenCalledWith(
        fiscalYearCollection,
        ...additionalFiscalYears.map(expect.objectContaining),
      );
      expect(comp.fiscalYearsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Placeholder query and add missing value', () => {
      const fiscalQuarter: IFiscalQuarter = { id: 12913 };
      const placeholders: IPlaceholder[] = [{ id: 13408 }];
      fiscalQuarter.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 13408 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fiscalQuarter });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(
        placeholderCollection,
        ...additionalPlaceholders.map(expect.objectContaining),
      );
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const fiscalQuarter: IFiscalQuarter = { id: 12913 };
      const fiscalYear: IFiscalYear = { id: 1297 };
      fiscalQuarter.fiscalYear = fiscalYear;
      const placeholder: IPlaceholder = { id: 13408 };
      fiscalQuarter.placeholders = [placeholder];

      activatedRoute.data = of({ fiscalQuarter });
      comp.ngOnInit();

      expect(comp.fiscalYearsSharedCollection).toContainEqual(fiscalYear);
      expect(comp.placeholdersSharedCollection).toContainEqual(placeholder);
      expect(comp.fiscalQuarter).toEqual(fiscalQuarter);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFiscalQuarter>>();
      const fiscalQuarter = { id: 15832 };
      jest.spyOn(fiscalQuarterFormService, 'getFiscalQuarter').mockReturnValue(fiscalQuarter);
      jest.spyOn(fiscalQuarterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fiscalQuarter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fiscalQuarter }));
      saveSubject.complete();

      // THEN
      expect(fiscalQuarterFormService.getFiscalQuarter).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fiscalQuarterService.update).toHaveBeenCalledWith(expect.objectContaining(fiscalQuarter));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFiscalQuarter>>();
      const fiscalQuarter = { id: 15832 };
      jest.spyOn(fiscalQuarterFormService, 'getFiscalQuarter').mockReturnValue({ id: null });
      jest.spyOn(fiscalQuarterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fiscalQuarter: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fiscalQuarter }));
      saveSubject.complete();

      // THEN
      expect(fiscalQuarterFormService.getFiscalQuarter).toHaveBeenCalled();
      expect(fiscalQuarterService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFiscalQuarter>>();
      const fiscalQuarter = { id: 15832 };
      jest.spyOn(fiscalQuarterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fiscalQuarter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fiscalQuarterService.update).toHaveBeenCalled();
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
  });
});
