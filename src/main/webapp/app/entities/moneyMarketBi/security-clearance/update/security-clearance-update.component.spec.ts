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
import { SecurityClearanceService } from '../service/security-clearance.service';
import { ISecurityClearance } from '../security-clearance.model';
import { SecurityClearanceFormService } from './security-clearance-form.service';

import { SecurityClearanceUpdateComponent } from './security-clearance-update.component';

describe('SecurityClearance Management Update Component', () => {
  let comp: SecurityClearanceUpdateComponent;
  let fixture: ComponentFixture<SecurityClearanceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let securityClearanceFormService: SecurityClearanceFormService;
  let securityClearanceService: SecurityClearanceService;
  let placeholderService: PlaceholderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SecurityClearanceUpdateComponent],
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
      .overrideTemplate(SecurityClearanceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SecurityClearanceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    securityClearanceFormService = TestBed.inject(SecurityClearanceFormService);
    securityClearanceService = TestBed.inject(SecurityClearanceService);
    placeholderService = TestBed.inject(PlaceholderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Placeholder query and add missing value', () => {
      const securityClearance: ISecurityClearance = { id: 29421 };
      const placeholders: IPlaceholder[] = [{ id: 13408 }];
      securityClearance.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 13408 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ securityClearance });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(
        placeholderCollection,
        ...additionalPlaceholders.map(expect.objectContaining),
      );
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const securityClearance: ISecurityClearance = { id: 29421 };
      const placeholder: IPlaceholder = { id: 13408 };
      securityClearance.placeholders = [placeholder];

      activatedRoute.data = of({ securityClearance });
      comp.ngOnInit();

      expect(comp.placeholdersSharedCollection).toContainEqual(placeholder);
      expect(comp.securityClearance).toEqual(securityClearance);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISecurityClearance>>();
      const securityClearance = { id: 5365 };
      jest.spyOn(securityClearanceFormService, 'getSecurityClearance').mockReturnValue(securityClearance);
      jest.spyOn(securityClearanceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ securityClearance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: securityClearance }));
      saveSubject.complete();

      // THEN
      expect(securityClearanceFormService.getSecurityClearance).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(securityClearanceService.update).toHaveBeenCalledWith(expect.objectContaining(securityClearance));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISecurityClearance>>();
      const securityClearance = { id: 5365 };
      jest.spyOn(securityClearanceFormService, 'getSecurityClearance').mockReturnValue({ id: null });
      jest.spyOn(securityClearanceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ securityClearance: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: securityClearance }));
      saveSubject.complete();

      // THEN
      expect(securityClearanceFormService.getSecurityClearance).toHaveBeenCalled();
      expect(securityClearanceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISecurityClearance>>();
      const securityClearance = { id: 5365 };
      jest.spyOn(securityClearanceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ securityClearance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(securityClearanceService.update).toHaveBeenCalled();
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
