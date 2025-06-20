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

import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';

import { ProfileInfo } from 'app/layouts/profiles/profile-info.model';
import { ProfileService } from 'app/layouts/profiles/profile.service';

import PageRibbonComponent from './page-ribbon.component';

describe('Page Ribbon Component', () => {
  let comp: PageRibbonComponent;
  let fixture: ComponentFixture<PageRibbonComponent>;
  let profileService: ProfileService;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [PageRibbonComponent],
      providers: [provideHttpClient()],
    })
      .overrideTemplate(PageRibbonComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PageRibbonComponent);
    comp = fixture.componentInstance;
    profileService = TestBed.inject(ProfileService);
  });

  it('should call profileService.getProfileInfo on init', () => {
    // GIVEN
    jest.spyOn(profileService, 'getProfileInfo').mockReturnValue(of(new ProfileInfo()));

    // WHEN
    comp.ngOnInit();

    // THEN
    expect(profileService.getProfileInfo).toHaveBeenCalled();
  });
});
