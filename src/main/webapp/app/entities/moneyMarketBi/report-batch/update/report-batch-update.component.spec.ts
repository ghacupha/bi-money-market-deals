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

import { IApplicationUser } from 'app/entities/maintenance/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/maintenance/application-user/service/application-user.service';
import { IPlaceholder } from 'app/entities/maintenance/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/maintenance/placeholder/service/placeholder.service';
import { IReportBatch } from '../report-batch.model';
import { ReportBatchService } from '../service/report-batch.service';
import { ReportBatchFormService } from './report-batch-form.service';

import { ReportBatchUpdateComponent } from './report-batch-update.component';

describe('ReportBatch Management Update Component', () => {
  let comp: ReportBatchUpdateComponent;
  let fixture: ComponentFixture<ReportBatchUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reportBatchFormService: ReportBatchFormService;
  let reportBatchService: ReportBatchService;
  let applicationUserService: ApplicationUserService;
  let placeholderService: PlaceholderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReportBatchUpdateComponent],
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
      .overrideTemplate(ReportBatchUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReportBatchUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reportBatchFormService = TestBed.inject(ReportBatchFormService);
    reportBatchService = TestBed.inject(ReportBatchService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    placeholderService = TestBed.inject(PlaceholderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ApplicationUser query and add missing value', () => {
      const reportBatch: IReportBatch = { id: 19041 };
      const uploadedBy: IApplicationUser = { id: 2107 };
      reportBatch.uploadedBy = uploadedBy;

      const applicationUserCollection: IApplicationUser[] = [{ id: 2107 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [uploadedBy];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportBatch });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining),
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('should call Placeholder query and add missing value', () => {
      const reportBatch: IReportBatch = { id: 19041 };
      const placeholders: IPlaceholder[] = [{ id: 13408 }];
      reportBatch.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 13408 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportBatch });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(
        placeholderCollection,
        ...additionalPlaceholders.map(expect.objectContaining),
      );
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const reportBatch: IReportBatch = { id: 19041 };
      const uploadedBy: IApplicationUser = { id: 2107 };
      reportBatch.uploadedBy = uploadedBy;
      const placeholder: IPlaceholder = { id: 13408 };
      reportBatch.placeholders = [placeholder];

      activatedRoute.data = of({ reportBatch });
      comp.ngOnInit();

      expect(comp.applicationUsersSharedCollection).toContainEqual(uploadedBy);
      expect(comp.placeholdersSharedCollection).toContainEqual(placeholder);
      expect(comp.reportBatch).toEqual(reportBatch);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportBatch>>();
      const reportBatch = { id: 28750 };
      jest.spyOn(reportBatchFormService, 'getReportBatch').mockReturnValue(reportBatch);
      jest.spyOn(reportBatchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportBatch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportBatch }));
      saveSubject.complete();

      // THEN
      expect(reportBatchFormService.getReportBatch).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reportBatchService.update).toHaveBeenCalledWith(expect.objectContaining(reportBatch));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportBatch>>();
      const reportBatch = { id: 28750 };
      jest.spyOn(reportBatchFormService, 'getReportBatch').mockReturnValue({ id: null });
      jest.spyOn(reportBatchService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportBatch: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportBatch }));
      saveSubject.complete();

      // THEN
      expect(reportBatchFormService.getReportBatch).toHaveBeenCalled();
      expect(reportBatchService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportBatch>>();
      const reportBatch = { id: 28750 };
      jest.spyOn(reportBatchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportBatch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reportBatchService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareApplicationUser', () => {
      it('should forward to applicationUserService', () => {
        const entity = { id: 2107 };
        const entity2 = { id: 4268 };
        jest.spyOn(applicationUserService, 'compareApplicationUser');
        comp.compareApplicationUser(entity, entity2);
        expect(applicationUserService.compareApplicationUser).toHaveBeenCalledWith(entity, entity2);
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
