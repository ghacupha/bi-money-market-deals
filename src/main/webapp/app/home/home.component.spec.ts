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

jest.mock('app/core/auth/account.service');

import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { Router } from '@angular/router';
import { Subject, of } from 'rxjs';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';

import HomeComponent from './home.component';

describe('Home Component', () => {
  let comp: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;
  let mockAccountService: AccountService;
  let mockRouter: Router;
  const account: Account = {
    activated: true,
    authorities: [],
    email: '',
    firstName: null,
    langKey: '',
    lastName: null,
    login: 'login',
    imageUrl: null,
  };

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [HomeComponent],
      providers: [AccountService],
    })
      .overrideTemplate(HomeComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HomeComponent);
    comp = fixture.componentInstance;
    mockAccountService = TestBed.inject(AccountService);
    mockAccountService.identity = jest.fn(() => of(null));
    mockAccountService.getAuthenticationState = jest.fn(() => of(null));

    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
  });

  describe('ngOnInit', () => {
    it('should synchronize account variable with current account', () => {
      // GIVEN
      const authenticationState = new Subject<Account | null>();
      mockAccountService.getAuthenticationState = jest.fn(() => authenticationState.asObservable());

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.account()).toBeNull();

      // WHEN
      authenticationState.next(account);

      // THEN
      expect(comp.account()).toEqual(account);

      // WHEN
      authenticationState.next(null);

      // THEN
      expect(comp.account()).toBeNull();
    });
  });

  describe('login', () => {
    it('should navigate to /login on login', () => {
      // WHEN
      comp.login();

      // THEN
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
    });
  });

  describe('ngOnDestroy', () => {
    it('should destroy authentication state subscription on component destroy', () => {
      // GIVEN
      const authenticationState = new Subject<Account | null>();
      mockAccountService.getAuthenticationState = jest.fn(() => authenticationState.asObservable());

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.account()).toBeNull();

      // WHEN
      authenticationState.next(account);

      // THEN
      expect(comp.account()).toEqual(account);

      // WHEN
      comp.ngOnDestroy();
      authenticationState.next(null);

      // THEN
      expect(comp.account()).toEqual(account);
    });
  });
});
