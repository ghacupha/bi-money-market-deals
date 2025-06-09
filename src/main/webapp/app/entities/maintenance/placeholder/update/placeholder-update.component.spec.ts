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

import { IDealer } from 'app/entities/moneyMarketBi/dealer/dealer.model';
import { DealerService } from 'app/entities/moneyMarketBi/dealer/service/dealer.service';
import { ISecurityClearance } from 'app/entities/moneyMarketBi/security-clearance/security-clearance.model';
import { SecurityClearanceService } from 'app/entities/moneyMarketBi/security-clearance/service/security-clearance.service';
import { IApplicationUser } from 'app/entities/maintenance/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/maintenance/application-user/service/application-user.service';
import { IFiscalYear } from 'app/entities/maintenance/fiscal-year/fiscal-year.model';
import { FiscalYearService } from 'app/entities/maintenance/fiscal-year/service/fiscal-year.service';
import { IFiscalQuarter } from 'app/entities/maintenance/fiscal-quarter/fiscal-quarter.model';
import { FiscalQuarterService } from 'app/entities/maintenance/fiscal-quarter/service/fiscal-quarter.service';
import { IFiscalMonth } from 'app/entities/maintenance/fiscal-month/fiscal-month.model';
import { FiscalMonthService } from 'app/entities/maintenance/fiscal-month/service/fiscal-month.service';
import { IReportBatch } from 'app/entities/moneyMarketBi/report-batch/report-batch.model';
import { ReportBatchService } from 'app/entities/moneyMarketBi/report-batch/service/report-batch.service';
import { IMoneyMarketList } from 'app/entities/moneyMarketBi/money-market-list/money-market-list.model';
import { MoneyMarketListService } from 'app/entities/moneyMarketBi/money-market-list/service/money-market-list.service';
import { IMoneyMarketUploadNotification } from 'app/entities/moneyMarketBi/money-market-upload-notification/money-market-upload-notification.model';
import { MoneyMarketUploadNotificationService } from 'app/entities/moneyMarketBi/money-market-upload-notification/service/money-market-upload-notification.service';
import { IPlaceholder } from '../placeholder.model';
import { PlaceholderService } from '../service/placeholder.service';
import { PlaceholderFormService } from './placeholder-form.service';

import { PlaceholderUpdateComponent } from './placeholder-update.component';

describe('Placeholder Management Update Component', () => {
  let comp: PlaceholderUpdateComponent;
  let fixture: ComponentFixture<PlaceholderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let placeholderFormService: PlaceholderFormService;
  let placeholderService: PlaceholderService;
  let dealerService: DealerService;
  let securityClearanceService: SecurityClearanceService;
  let applicationUserService: ApplicationUserService;
  let fiscalYearService: FiscalYearService;
  let fiscalQuarterService: FiscalQuarterService;
  let fiscalMonthService: FiscalMonthService;
  let reportBatchService: ReportBatchService;
  let moneyMarketListService: MoneyMarketListService;
  let moneyMarketUploadNotificationService: MoneyMarketUploadNotificationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PlaceholderUpdateComponent],
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
      .overrideTemplate(PlaceholderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlaceholderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    placeholderFormService = TestBed.inject(PlaceholderFormService);
    placeholderService = TestBed.inject(PlaceholderService);
    dealerService = TestBed.inject(DealerService);
    securityClearanceService = TestBed.inject(SecurityClearanceService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    fiscalYearService = TestBed.inject(FiscalYearService);
    fiscalQuarterService = TestBed.inject(FiscalQuarterService);
    fiscalMonthService = TestBed.inject(FiscalMonthService);
    reportBatchService = TestBed.inject(ReportBatchService);
    moneyMarketListService = TestBed.inject(MoneyMarketListService);
    moneyMarketUploadNotificationService = TestBed.inject(MoneyMarketUploadNotificationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Placeholder query and add missing value', () => {
      const placeholder: IPlaceholder = { id: 24257 };
      const containingPlaceholder: IPlaceholder = { id: 13408 };
      placeholder.containingPlaceholder = containingPlaceholder;

      const placeholderCollection: IPlaceholder[] = [{ id: 13408 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [containingPlaceholder];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ placeholder });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(
        placeholderCollection,
        ...additionalPlaceholders.map(expect.objectContaining),
      );
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('should call Dealer query and add missing value', () => {
      const placeholder: IPlaceholder = { id: 24257 };
      const dealers: IDealer[] = [{ id: 332 }];
      placeholder.dealers = dealers;

      const dealerCollection: IDealer[] = [{ id: 332 }];
      jest.spyOn(dealerService, 'query').mockReturnValue(of(new HttpResponse({ body: dealerCollection })));
      const additionalDealers = [...dealers];
      const expectedCollection: IDealer[] = [...additionalDealers, ...dealerCollection];
      jest.spyOn(dealerService, 'addDealerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ placeholder });
      comp.ngOnInit();

      expect(dealerService.query).toHaveBeenCalled();
      expect(dealerService.addDealerToCollectionIfMissing).toHaveBeenCalledWith(
        dealerCollection,
        ...additionalDealers.map(expect.objectContaining),
      );
      expect(comp.dealersSharedCollection).toEqual(expectedCollection);
    });

    it('should call SecurityClearance query and add missing value', () => {
      const placeholder: IPlaceholder = { id: 24257 };
      const securityClearances: ISecurityClearance[] = [{ id: 5365 }];
      placeholder.securityClearances = securityClearances;

      const securityClearanceCollection: ISecurityClearance[] = [{ id: 5365 }];
      jest.spyOn(securityClearanceService, 'query').mockReturnValue(of(new HttpResponse({ body: securityClearanceCollection })));
      const additionalSecurityClearances = [...securityClearances];
      const expectedCollection: ISecurityClearance[] = [...additionalSecurityClearances, ...securityClearanceCollection];
      jest.spyOn(securityClearanceService, 'addSecurityClearanceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ placeholder });
      comp.ngOnInit();

      expect(securityClearanceService.query).toHaveBeenCalled();
      expect(securityClearanceService.addSecurityClearanceToCollectionIfMissing).toHaveBeenCalledWith(
        securityClearanceCollection,
        ...additionalSecurityClearances.map(expect.objectContaining),
      );
      expect(comp.securityClearancesSharedCollection).toEqual(expectedCollection);
    });

    it('should call ApplicationUser query and add missing value', () => {
      const placeholder: IPlaceholder = { id: 24257 };
      const applicationUsers: IApplicationUser[] = [{ id: 2107 }];
      placeholder.applicationUsers = applicationUsers;

      const applicationUserCollection: IApplicationUser[] = [{ id: 2107 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [...applicationUsers];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ placeholder });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining),
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('should call FiscalYear query and add missing value', () => {
      const placeholder: IPlaceholder = { id: 24257 };
      const fiscalYears: IFiscalYear[] = [{ id: 1297 }];
      placeholder.fiscalYears = fiscalYears;

      const fiscalYearCollection: IFiscalYear[] = [{ id: 1297 }];
      jest.spyOn(fiscalYearService, 'query').mockReturnValue(of(new HttpResponse({ body: fiscalYearCollection })));
      const additionalFiscalYears = [...fiscalYears];
      const expectedCollection: IFiscalYear[] = [...additionalFiscalYears, ...fiscalYearCollection];
      jest.spyOn(fiscalYearService, 'addFiscalYearToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ placeholder });
      comp.ngOnInit();

      expect(fiscalYearService.query).toHaveBeenCalled();
      expect(fiscalYearService.addFiscalYearToCollectionIfMissing).toHaveBeenCalledWith(
        fiscalYearCollection,
        ...additionalFiscalYears.map(expect.objectContaining),
      );
      expect(comp.fiscalYearsSharedCollection).toEqual(expectedCollection);
    });

    it('should call FiscalQuarter query and add missing value', () => {
      const placeholder: IPlaceholder = { id: 24257 };
      const fiscalQuarters: IFiscalQuarter[] = [{ id: 15832 }];
      placeholder.fiscalQuarters = fiscalQuarters;

      const fiscalQuarterCollection: IFiscalQuarter[] = [{ id: 15832 }];
      jest.spyOn(fiscalQuarterService, 'query').mockReturnValue(of(new HttpResponse({ body: fiscalQuarterCollection })));
      const additionalFiscalQuarters = [...fiscalQuarters];
      const expectedCollection: IFiscalQuarter[] = [...additionalFiscalQuarters, ...fiscalQuarterCollection];
      jest.spyOn(fiscalQuarterService, 'addFiscalQuarterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ placeholder });
      comp.ngOnInit();

      expect(fiscalQuarterService.query).toHaveBeenCalled();
      expect(fiscalQuarterService.addFiscalQuarterToCollectionIfMissing).toHaveBeenCalledWith(
        fiscalQuarterCollection,
        ...additionalFiscalQuarters.map(expect.objectContaining),
      );
      expect(comp.fiscalQuartersSharedCollection).toEqual(expectedCollection);
    });

    it('should call FiscalMonth query and add missing value', () => {
      const placeholder: IPlaceholder = { id: 24257 };
      const fiscalMonths: IFiscalMonth[] = [{ id: 13140 }];
      placeholder.fiscalMonths = fiscalMonths;

      const fiscalMonthCollection: IFiscalMonth[] = [{ id: 13140 }];
      jest.spyOn(fiscalMonthService, 'query').mockReturnValue(of(new HttpResponse({ body: fiscalMonthCollection })));
      const additionalFiscalMonths = [...fiscalMonths];
      const expectedCollection: IFiscalMonth[] = [...additionalFiscalMonths, ...fiscalMonthCollection];
      jest.spyOn(fiscalMonthService, 'addFiscalMonthToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ placeholder });
      comp.ngOnInit();

      expect(fiscalMonthService.query).toHaveBeenCalled();
      expect(fiscalMonthService.addFiscalMonthToCollectionIfMissing).toHaveBeenCalledWith(
        fiscalMonthCollection,
        ...additionalFiscalMonths.map(expect.objectContaining),
      );
      expect(comp.fiscalMonthsSharedCollection).toEqual(expectedCollection);
    });

    it('should call ReportBatch query and add missing value', () => {
      const placeholder: IPlaceholder = { id: 24257 };
      const reportBatches: IReportBatch[] = [{ id: 28750 }];
      placeholder.reportBatches = reportBatches;

      const reportBatchCollection: IReportBatch[] = [{ id: 28750 }];
      jest.spyOn(reportBatchService, 'query').mockReturnValue(of(new HttpResponse({ body: reportBatchCollection })));
      const additionalReportBatches = [...reportBatches];
      const expectedCollection: IReportBatch[] = [...additionalReportBatches, ...reportBatchCollection];
      jest.spyOn(reportBatchService, 'addReportBatchToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ placeholder });
      comp.ngOnInit();

      expect(reportBatchService.query).toHaveBeenCalled();
      expect(reportBatchService.addReportBatchToCollectionIfMissing).toHaveBeenCalledWith(
        reportBatchCollection,
        ...additionalReportBatches.map(expect.objectContaining),
      );
      expect(comp.reportBatchesSharedCollection).toEqual(expectedCollection);
    });

    it('should call MoneyMarketList query and add missing value', () => {
      const placeholder: IPlaceholder = { id: 24257 };
      const moneyMarketLists: IMoneyMarketList[] = [{ id: 29763 }];
      placeholder.moneyMarketLists = moneyMarketLists;

      const moneyMarketListCollection: IMoneyMarketList[] = [{ id: 29763 }];
      jest.spyOn(moneyMarketListService, 'query').mockReturnValue(of(new HttpResponse({ body: moneyMarketListCollection })));
      const additionalMoneyMarketLists = [...moneyMarketLists];
      const expectedCollection: IMoneyMarketList[] = [...additionalMoneyMarketLists, ...moneyMarketListCollection];
      jest.spyOn(moneyMarketListService, 'addMoneyMarketListToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ placeholder });
      comp.ngOnInit();

      expect(moneyMarketListService.query).toHaveBeenCalled();
      expect(moneyMarketListService.addMoneyMarketListToCollectionIfMissing).toHaveBeenCalledWith(
        moneyMarketListCollection,
        ...additionalMoneyMarketLists.map(expect.objectContaining),
      );
      expect(comp.moneyMarketListsSharedCollection).toEqual(expectedCollection);
    });

    it('should call MoneyMarketUploadNotification query and add missing value', () => {
      const placeholder: IPlaceholder = { id: 24257 };
      const moneyMarketUploadNotifications: IMoneyMarketUploadNotification[] = [{ id: 20382 }];
      placeholder.moneyMarketUploadNotifications = moneyMarketUploadNotifications;

      const moneyMarketUploadNotificationCollection: IMoneyMarketUploadNotification[] = [{ id: 20382 }];
      jest
        .spyOn(moneyMarketUploadNotificationService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: moneyMarketUploadNotificationCollection })));
      const additionalMoneyMarketUploadNotifications = [...moneyMarketUploadNotifications];
      const expectedCollection: IMoneyMarketUploadNotification[] = [
        ...additionalMoneyMarketUploadNotifications,
        ...moneyMarketUploadNotificationCollection,
      ];
      jest
        .spyOn(moneyMarketUploadNotificationService, 'addMoneyMarketUploadNotificationToCollectionIfMissing')
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ placeholder });
      comp.ngOnInit();

      expect(moneyMarketUploadNotificationService.query).toHaveBeenCalled();
      expect(moneyMarketUploadNotificationService.addMoneyMarketUploadNotificationToCollectionIfMissing).toHaveBeenCalledWith(
        moneyMarketUploadNotificationCollection,
        ...additionalMoneyMarketUploadNotifications.map(expect.objectContaining),
      );
      expect(comp.moneyMarketUploadNotificationsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const placeholder: IPlaceholder = { id: 24257 };
      const containingPlaceholder: IPlaceholder = { id: 13408 };
      placeholder.containingPlaceholder = containingPlaceholder;
      const dealer: IDealer = { id: 332 };
      placeholder.dealers = [dealer];
      const securityClearance: ISecurityClearance = { id: 5365 };
      placeholder.securityClearances = [securityClearance];
      const applicationUser: IApplicationUser = { id: 2107 };
      placeholder.applicationUsers = [applicationUser];
      const fiscalYear: IFiscalYear = { id: 1297 };
      placeholder.fiscalYears = [fiscalYear];
      const fiscalQuarter: IFiscalQuarter = { id: 15832 };
      placeholder.fiscalQuarters = [fiscalQuarter];
      const fiscalMonth: IFiscalMonth = { id: 13140 };
      placeholder.fiscalMonths = [fiscalMonth];
      const reportBatch: IReportBatch = { id: 28750 };
      placeholder.reportBatches = [reportBatch];
      const moneyMarketList: IMoneyMarketList = { id: 29763 };
      placeholder.moneyMarketLists = [moneyMarketList];
      const moneyMarketUploadNotification: IMoneyMarketUploadNotification = { id: 20382 };
      placeholder.moneyMarketUploadNotifications = [moneyMarketUploadNotification];

      activatedRoute.data = of({ placeholder });
      comp.ngOnInit();

      expect(comp.placeholdersSharedCollection).toContainEqual(containingPlaceholder);
      expect(comp.dealersSharedCollection).toContainEqual(dealer);
      expect(comp.securityClearancesSharedCollection).toContainEqual(securityClearance);
      expect(comp.applicationUsersSharedCollection).toContainEqual(applicationUser);
      expect(comp.fiscalYearsSharedCollection).toContainEqual(fiscalYear);
      expect(comp.fiscalQuartersSharedCollection).toContainEqual(fiscalQuarter);
      expect(comp.fiscalMonthsSharedCollection).toContainEqual(fiscalMonth);
      expect(comp.reportBatchesSharedCollection).toContainEqual(reportBatch);
      expect(comp.moneyMarketListsSharedCollection).toContainEqual(moneyMarketList);
      expect(comp.moneyMarketUploadNotificationsSharedCollection).toContainEqual(moneyMarketUploadNotification);
      expect(comp.placeholder).toEqual(placeholder);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlaceholder>>();
      const placeholder = { id: 13408 };
      jest.spyOn(placeholderFormService, 'getPlaceholder').mockReturnValue(placeholder);
      jest.spyOn(placeholderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ placeholder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: placeholder }));
      saveSubject.complete();

      // THEN
      expect(placeholderFormService.getPlaceholder).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(placeholderService.update).toHaveBeenCalledWith(expect.objectContaining(placeholder));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlaceholder>>();
      const placeholder = { id: 13408 };
      jest.spyOn(placeholderFormService, 'getPlaceholder').mockReturnValue({ id: null });
      jest.spyOn(placeholderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ placeholder: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: placeholder }));
      saveSubject.complete();

      // THEN
      expect(placeholderFormService.getPlaceholder).toHaveBeenCalled();
      expect(placeholderService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlaceholder>>();
      const placeholder = { id: 13408 };
      jest.spyOn(placeholderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ placeholder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(placeholderService.update).toHaveBeenCalled();
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

    describe('compareDealer', () => {
      it('should forward to dealerService', () => {
        const entity = { id: 332 };
        const entity2 = { id: 2737 };
        jest.spyOn(dealerService, 'compareDealer');
        comp.compareDealer(entity, entity2);
        expect(dealerService.compareDealer).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSecurityClearance', () => {
      it('should forward to securityClearanceService', () => {
        const entity = { id: 5365 };
        const entity2 = { id: 29421 };
        jest.spyOn(securityClearanceService, 'compareSecurityClearance');
        comp.compareSecurityClearance(entity, entity2);
        expect(securityClearanceService.compareSecurityClearance).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareApplicationUser', () => {
      it('should forward to applicationUserService', () => {
        const entity = { id: 2107 };
        const entity2 = { id: 4268 };
        jest.spyOn(applicationUserService, 'compareApplicationUser');
        comp.compareApplicationUser(entity, entity2);
        expect(applicationUserService.compareApplicationUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFiscalYear', () => {
      it('should forward to fiscalYearService', () => {
        const entity = { id: 1297 };
        const entity2 = { id: 1005 };
        jest.spyOn(fiscalYearService, 'compareFiscalYear');
        comp.compareFiscalYear(entity, entity2);
        expect(fiscalYearService.compareFiscalYear).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareFiscalMonth', () => {
      it('should forward to fiscalMonthService', () => {
        const entity = { id: 13140 };
        const entity2 = { id: 17896 };
        jest.spyOn(fiscalMonthService, 'compareFiscalMonth');
        comp.compareFiscalMonth(entity, entity2);
        expect(fiscalMonthService.compareFiscalMonth).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareMoneyMarketList', () => {
      it('should forward to moneyMarketListService', () => {
        const entity = { id: 29763 };
        const entity2 = { id: 21170 };
        jest.spyOn(moneyMarketListService, 'compareMoneyMarketList');
        comp.compareMoneyMarketList(entity, entity2);
        expect(moneyMarketListService.compareMoneyMarketList).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareMoneyMarketUploadNotification', () => {
      it('should forward to moneyMarketUploadNotificationService', () => {
        const entity = { id: 20382 };
        const entity2 = { id: 10273 };
        jest.spyOn(moneyMarketUploadNotificationService, 'compareMoneyMarketUploadNotification');
        comp.compareMoneyMarketUploadNotification(entity, entity2);
        expect(moneyMarketUploadNotificationService.compareMoneyMarketUploadNotification).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
