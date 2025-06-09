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
import { FiscalYearService } from '../service/fiscal-year.service';
import { IFiscalYear } from '../fiscal-year.model';
import { FiscalYearFormService } from './fiscal-year-form.service';

import { FiscalYearUpdateComponent } from './fiscal-year-update.component';

describe('FiscalYear Management Update Component', () => {
  let comp: FiscalYearUpdateComponent;
  let fixture: ComponentFixture<FiscalYearUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fiscalYearFormService: FiscalYearFormService;
  let fiscalYearService: FiscalYearService;
  let placeholderService: PlaceholderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FiscalYearUpdateComponent],
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
      .overrideTemplate(FiscalYearUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FiscalYearUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fiscalYearFormService = TestBed.inject(FiscalYearFormService);
    fiscalYearService = TestBed.inject(FiscalYearService);
    placeholderService = TestBed.inject(PlaceholderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Placeholder query and add missing value', () => {
      const fiscalYear: IFiscalYear = { id: 1005 };
      const placeholders: IPlaceholder[] = [{ id: 13408 }];
      fiscalYear.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 13408 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fiscalYear });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(
        placeholderCollection,
        ...additionalPlaceholders.map(expect.objectContaining),
      );
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const fiscalYear: IFiscalYear = { id: 1005 };
      const placeholder: IPlaceholder = { id: 13408 };
      fiscalYear.placeholders = [placeholder];

      activatedRoute.data = of({ fiscalYear });
      comp.ngOnInit();

      expect(comp.placeholdersSharedCollection).toContainEqual(placeholder);
      expect(comp.fiscalYear).toEqual(fiscalYear);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFiscalYear>>();
      const fiscalYear = { id: 1297 };
      jest.spyOn(fiscalYearFormService, 'getFiscalYear').mockReturnValue(fiscalYear);
      jest.spyOn(fiscalYearService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fiscalYear });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fiscalYear }));
      saveSubject.complete();

      // THEN
      expect(fiscalYearFormService.getFiscalYear).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fiscalYearService.update).toHaveBeenCalledWith(expect.objectContaining(fiscalYear));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFiscalYear>>();
      const fiscalYear = { id: 1297 };
      jest.spyOn(fiscalYearFormService, 'getFiscalYear').mockReturnValue({ id: null });
      jest.spyOn(fiscalYearService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fiscalYear: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fiscalYear }));
      saveSubject.complete();

      // THEN
      expect(fiscalYearFormService.getFiscalYear).toHaveBeenCalled();
      expect(fiscalYearService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFiscalYear>>();
      const fiscalYear = { id: 1297 };
      jest.spyOn(fiscalYearService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fiscalYear });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fiscalYearService.update).toHaveBeenCalled();
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
