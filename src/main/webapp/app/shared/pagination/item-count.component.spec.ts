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

import { ComponentRef } from '@angular/core';
import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import ItemCountComponent from './item-count.component';

describe('ItemCountComponent test', () => {
  let comp: ItemCountComponent;
  let compRef: ComponentRef<ItemCountComponent>;
  let fixture: ComponentFixture<ItemCountComponent>;
  const inputParams = 'params';

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [ItemCountComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ItemCountComponent);
    comp = fixture.componentInstance;
    compRef = fixture.componentRef;
  });

  describe('UI logic tests', () => {
    it('should initialize with undefined', () => {
      expect(comp.first()).toBeUndefined();
      expect(comp.second()).toBeUndefined();
      expect(comp.total()).toBeUndefined();
    });

    it('should set calculated numbers to undefined if the page value is not yet defined', () => {
      // GIVEN
      compRef.setInput(inputParams, { page: undefined, totalItems: 0, itemsPerPage: 10 });

      // THEN
      expect(comp.first()).toBeUndefined();
      expect(comp.second()).toBeUndefined();
    });

    it('should change the content on page change', () => {
      // GIVEN
      compRef.setInput(inputParams, { page: 1, totalItems: 100, itemsPerPage: 10 });

      // THEN
      expect(comp.first()).toBe(1);
      expect(comp.second()).toBe(10);
      expect(comp.total()).toBe(100);

      // GIVEN
      compRef.setInput(inputParams, { page: 2, totalItems: 100, itemsPerPage: 10 });

      // THEN
      expect(comp.first()).toBe(11);
      expect(comp.second()).toBe(20);
      expect(comp.total()).toBe(100);
    });

    it('should set the second number to totalItems if this is the last page which contains less than itemsPerPage items', () => {
      // GIVEN
      compRef.setInput(inputParams, { page: 2, totalItems: 16, itemsPerPage: 10 });

      // THEN
      expect(comp.first()).toBe(11);
      expect(comp.second()).toBe(16);
      expect(comp.total()).toBe(16);
    });
  });
});
