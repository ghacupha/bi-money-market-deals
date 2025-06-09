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
import { IReportBatch } from 'app/entities/moneyMarketBi/report-batch/report-batch.model';
import { ReportBatchService } from 'app/entities/moneyMarketBi/report-batch/service/report-batch.service';
import { IPlaceholder } from 'app/entities/maintenance/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/maintenance/placeholder/service/placeholder.service';
import { IMoneyMarketUploadNotification } from '../money-market-upload-notification.model';
import { MoneyMarketUploadNotificationService } from '../service/money-market-upload-notification.service';
import { MoneyMarketUploadNotificationFormService } from './money-market-upload-notification-form.service';

import { MoneyMarketUploadNotificationUpdateComponent } from './money-market-upload-notification-update.component';

describe('MoneyMarketUploadNotification Management Update Component', () => {
  let comp: MoneyMarketUploadNotificationUpdateComponent;
  let fixture: ComponentFixture<MoneyMarketUploadNotificationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let moneyMarketUploadNotificationFormService: MoneyMarketUploadNotificationFormService;
  let moneyMarketUploadNotificationService: MoneyMarketUploadNotificationService;
  let moneyMarketListService: MoneyMarketListService;
  let reportBatchService: ReportBatchService;
  let placeholderService: PlaceholderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MoneyMarketUploadNotificationUpdateComponent],
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
      .overrideTemplate(MoneyMarketUploadNotificationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MoneyMarketUploadNotificationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    moneyMarketUploadNotificationFormService = TestBed.inject(MoneyMarketUploadNotificationFormService);
    moneyMarketUploadNotificationService = TestBed.inject(MoneyMarketUploadNotificationService);
    moneyMarketListService = TestBed.inject(MoneyMarketListService);
    reportBatchService = TestBed.inject(ReportBatchService);
    placeholderService = TestBed.inject(PlaceholderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call MoneyMarketList query and add missing value', () => {
      const moneyMarketUploadNotification: IMoneyMarketUploadNotification = { id: 10273 };
      const moneyMarketList: IMoneyMarketList = { id: 29763 };
      moneyMarketUploadNotification.moneyMarketList = moneyMarketList;

      const moneyMarketListCollection: IMoneyMarketList[] = [{ id: 29763 }];
      jest.spyOn(moneyMarketListService, 'query').mockReturnValue(of(new HttpResponse({ body: moneyMarketListCollection })));
      const additionalMoneyMarketLists = [moneyMarketList];
      const expectedCollection: IMoneyMarketList[] = [...additionalMoneyMarketLists, ...moneyMarketListCollection];
      jest.spyOn(moneyMarketListService, 'addMoneyMarketListToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ moneyMarketUploadNotification });
      comp.ngOnInit();

      expect(moneyMarketListService.query).toHaveBeenCalled();
      expect(moneyMarketListService.addMoneyMarketListToCollectionIfMissing).toHaveBeenCalledWith(
        moneyMarketListCollection,
        ...additionalMoneyMarketLists.map(expect.objectContaining),
      );
      expect(comp.moneyMarketListsSharedCollection).toEqual(expectedCollection);
    });

    it('should call ReportBatch query and add missing value', () => {
      const moneyMarketUploadNotification: IMoneyMarketUploadNotification = { id: 10273 };
      const reportBatch: IReportBatch = { id: 28750 };
      moneyMarketUploadNotification.reportBatch = reportBatch;

      const reportBatchCollection: IReportBatch[] = [{ id: 28750 }];
      jest.spyOn(reportBatchService, 'query').mockReturnValue(of(new HttpResponse({ body: reportBatchCollection })));
      const additionalReportBatches = [reportBatch];
      const expectedCollection: IReportBatch[] = [...additionalReportBatches, ...reportBatchCollection];
      jest.spyOn(reportBatchService, 'addReportBatchToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ moneyMarketUploadNotification });
      comp.ngOnInit();

      expect(reportBatchService.query).toHaveBeenCalled();
      expect(reportBatchService.addReportBatchToCollectionIfMissing).toHaveBeenCalledWith(
        reportBatchCollection,
        ...additionalReportBatches.map(expect.objectContaining),
      );
      expect(comp.reportBatchesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Placeholder query and add missing value', () => {
      const moneyMarketUploadNotification: IMoneyMarketUploadNotification = { id: 10273 };
      const placeholders: IPlaceholder[] = [{ id: 13408 }];
      moneyMarketUploadNotification.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 13408 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ moneyMarketUploadNotification });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(
        placeholderCollection,
        ...additionalPlaceholders.map(expect.objectContaining),
      );
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const moneyMarketUploadNotification: IMoneyMarketUploadNotification = { id: 10273 };
      const moneyMarketList: IMoneyMarketList = { id: 29763 };
      moneyMarketUploadNotification.moneyMarketList = moneyMarketList;
      const reportBatch: IReportBatch = { id: 28750 };
      moneyMarketUploadNotification.reportBatch = reportBatch;
      const placeholder: IPlaceholder = { id: 13408 };
      moneyMarketUploadNotification.placeholders = [placeholder];

      activatedRoute.data = of({ moneyMarketUploadNotification });
      comp.ngOnInit();

      expect(comp.moneyMarketListsSharedCollection).toContainEqual(moneyMarketList);
      expect(comp.reportBatchesSharedCollection).toContainEqual(reportBatch);
      expect(comp.placeholdersSharedCollection).toContainEqual(placeholder);
      expect(comp.moneyMarketUploadNotification).toEqual(moneyMarketUploadNotification);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMoneyMarketUploadNotification>>();
      const moneyMarketUploadNotification = { id: 20382 };
      jest
        .spyOn(moneyMarketUploadNotificationFormService, 'getMoneyMarketUploadNotification')
        .mockReturnValue(moneyMarketUploadNotification);
      jest.spyOn(moneyMarketUploadNotificationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moneyMarketUploadNotification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: moneyMarketUploadNotification }));
      saveSubject.complete();

      // THEN
      expect(moneyMarketUploadNotificationFormService.getMoneyMarketUploadNotification).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(moneyMarketUploadNotificationService.update).toHaveBeenCalledWith(expect.objectContaining(moneyMarketUploadNotification));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMoneyMarketUploadNotification>>();
      const moneyMarketUploadNotification = { id: 20382 };
      jest.spyOn(moneyMarketUploadNotificationFormService, 'getMoneyMarketUploadNotification').mockReturnValue({ id: null });
      jest.spyOn(moneyMarketUploadNotificationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moneyMarketUploadNotification: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: moneyMarketUploadNotification }));
      saveSubject.complete();

      // THEN
      expect(moneyMarketUploadNotificationFormService.getMoneyMarketUploadNotification).toHaveBeenCalled();
      expect(moneyMarketUploadNotificationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMoneyMarketUploadNotification>>();
      const moneyMarketUploadNotification = { id: 20382 };
      jest.spyOn(moneyMarketUploadNotificationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moneyMarketUploadNotification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(moneyMarketUploadNotificationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMoneyMarketList', () => {
      it('should forward to moneyMarketListService', () => {
        const entity = { id: 29763 };
        const entity2 = { id: 21170 };
        jest.spyOn(moneyMarketListService, 'compareMoneyMarketList');
        comp.compareMoneyMarketList(entity, entity2);
        expect(moneyMarketListService.compareMoneyMarketList).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareReportBatch', () => {
      it('should forward to reportBatchService', () => {
        const entity = { id: 28750 };
        const entity2 = { id: 19041 };
        jest.spyOn(reportBatchService, 'compareReportBatch');
        comp.compareReportBatch(entity, entity2);
        expect(reportBatchService.compareReportBatch).toHaveBeenCalledWith(entity, entity2);
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
