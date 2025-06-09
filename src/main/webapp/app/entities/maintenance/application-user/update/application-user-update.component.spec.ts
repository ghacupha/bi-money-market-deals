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
import { IPlaceholder } from 'app/entities/maintenance/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/maintenance/placeholder/service/placeholder.service';
import { IApplicationUser } from '../application-user.model';
import { ApplicationUserService } from '../service/application-user.service';
import { ApplicationUserFormService } from './application-user-form.service';

import { ApplicationUserUpdateComponent } from './application-user-update.component';

describe('ApplicationUser Management Update Component', () => {
  let comp: ApplicationUserUpdateComponent;
  let fixture: ComponentFixture<ApplicationUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let applicationUserFormService: ApplicationUserFormService;
  let applicationUserService: ApplicationUserService;
  let dealerService: DealerService;
  let securityClearanceService: SecurityClearanceService;
  let placeholderService: PlaceholderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ApplicationUserUpdateComponent],
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
      .overrideTemplate(ApplicationUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApplicationUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    applicationUserFormService = TestBed.inject(ApplicationUserFormService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    dealerService = TestBed.inject(DealerService);
    securityClearanceService = TestBed.inject(SecurityClearanceService);
    placeholderService = TestBed.inject(PlaceholderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Dealer query and add missing value', () => {
      const applicationUser: IApplicationUser = { id: 4268 };
      const organization: IDealer = { id: 332 };
      applicationUser.organization = organization;
      const department: IDealer = { id: 332 };
      applicationUser.department = department;

      const dealerCollection: IDealer[] = [{ id: 332 }];
      jest.spyOn(dealerService, 'query').mockReturnValue(of(new HttpResponse({ body: dealerCollection })));
      const additionalDealers = [organization, department];
      const expectedCollection: IDealer[] = [...additionalDealers, ...dealerCollection];
      jest.spyOn(dealerService, 'addDealerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ applicationUser });
      comp.ngOnInit();

      expect(dealerService.query).toHaveBeenCalled();
      expect(dealerService.addDealerToCollectionIfMissing).toHaveBeenCalledWith(
        dealerCollection,
        ...additionalDealers.map(expect.objectContaining),
      );
      expect(comp.dealersSharedCollection).toEqual(expectedCollection);
    });

    it('should call dealerIdentity query and add missing value', () => {
      const applicationUser: IApplicationUser = { id: 4268 };
      const dealerIdentity: IDealer = { id: 332 };
      applicationUser.dealerIdentity = dealerIdentity;

      const dealerIdentityCollection: IDealer[] = [{ id: 332 }];
      jest.spyOn(dealerService, 'query').mockReturnValue(of(new HttpResponse({ body: dealerIdentityCollection })));
      const expectedCollection: IDealer[] = [dealerIdentity, ...dealerIdentityCollection];
      jest.spyOn(dealerService, 'addDealerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ applicationUser });
      comp.ngOnInit();

      expect(dealerService.query).toHaveBeenCalled();
      expect(dealerService.addDealerToCollectionIfMissing).toHaveBeenCalledWith(dealerIdentityCollection, dealerIdentity);
      expect(comp.dealerIdentitiesCollection).toEqual(expectedCollection);
    });

    it('should call SecurityClearance query and add missing value', () => {
      const applicationUser: IApplicationUser = { id: 4268 };
      const securityClearance: ISecurityClearance = { id: 5365 };
      applicationUser.securityClearance = securityClearance;

      const securityClearanceCollection: ISecurityClearance[] = [{ id: 5365 }];
      jest.spyOn(securityClearanceService, 'query').mockReturnValue(of(new HttpResponse({ body: securityClearanceCollection })));
      const additionalSecurityClearances = [securityClearance];
      const expectedCollection: ISecurityClearance[] = [...additionalSecurityClearances, ...securityClearanceCollection];
      jest.spyOn(securityClearanceService, 'addSecurityClearanceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ applicationUser });
      comp.ngOnInit();

      expect(securityClearanceService.query).toHaveBeenCalled();
      expect(securityClearanceService.addSecurityClearanceToCollectionIfMissing).toHaveBeenCalledWith(
        securityClearanceCollection,
        ...additionalSecurityClearances.map(expect.objectContaining),
      );
      expect(comp.securityClearancesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Placeholder query and add missing value', () => {
      const applicationUser: IApplicationUser = { id: 4268 };
      const placeholders: IPlaceholder[] = [{ id: 13408 }];
      applicationUser.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 13408 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ applicationUser });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(
        placeholderCollection,
        ...additionalPlaceholders.map(expect.objectContaining),
      );
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const applicationUser: IApplicationUser = { id: 4268 };
      const organization: IDealer = { id: 332 };
      applicationUser.organization = organization;
      const department: IDealer = { id: 332 };
      applicationUser.department = department;
      const dealerIdentity: IDealer = { id: 332 };
      applicationUser.dealerIdentity = dealerIdentity;
      const securityClearance: ISecurityClearance = { id: 5365 };
      applicationUser.securityClearance = securityClearance;
      const placeholder: IPlaceholder = { id: 13408 };
      applicationUser.placeholders = [placeholder];

      activatedRoute.data = of({ applicationUser });
      comp.ngOnInit();

      expect(comp.dealersSharedCollection).toContainEqual(organization);
      expect(comp.dealersSharedCollection).toContainEqual(department);
      expect(comp.dealerIdentitiesCollection).toContainEqual(dealerIdentity);
      expect(comp.securityClearancesSharedCollection).toContainEqual(securityClearance);
      expect(comp.placeholdersSharedCollection).toContainEqual(placeholder);
      expect(comp.applicationUser).toEqual(applicationUser);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApplicationUser>>();
      const applicationUser = { id: 2107 };
      jest.spyOn(applicationUserFormService, 'getApplicationUser').mockReturnValue(applicationUser);
      jest.spyOn(applicationUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ applicationUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: applicationUser }));
      saveSubject.complete();

      // THEN
      expect(applicationUserFormService.getApplicationUser).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(applicationUserService.update).toHaveBeenCalledWith(expect.objectContaining(applicationUser));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApplicationUser>>();
      const applicationUser = { id: 2107 };
      jest.spyOn(applicationUserFormService, 'getApplicationUser').mockReturnValue({ id: null });
      jest.spyOn(applicationUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ applicationUser: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: applicationUser }));
      saveSubject.complete();

      // THEN
      expect(applicationUserFormService.getApplicationUser).toHaveBeenCalled();
      expect(applicationUserService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApplicationUser>>();
      const applicationUser = { id: 2107 };
      jest.spyOn(applicationUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ applicationUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(applicationUserService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
