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
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MoneyMarketDealDetailComponent } from './money-market-deal-detail.component';

describe('MoneyMarketDeal Management Detail Component', () => {
  let comp: MoneyMarketDealDetailComponent;
  let fixture: ComponentFixture<MoneyMarketDealDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MoneyMarketDealDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./money-market-deal-detail.component').then(m => m.MoneyMarketDealDetailComponent),
              resolve: { moneyMarketDeal: () => of({ id: 21325 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MoneyMarketDealDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MoneyMarketDealDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load moneyMarketDeal on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MoneyMarketDealDetailComponent);

      // THEN
      expect(instance.moneyMarketDeal()).toEqual(expect.objectContaining({ id: 21325 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
