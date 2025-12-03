import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { expect, vi } from 'vitest';
import { of } from 'rxjs';

import { ProfilePageComponent } from './profile-page.component';
import { ProfileService } from '../../../core/services/profile.service';
import { SkillsSectionComponent } from '../sections/skills/skills-section.component';
import { ExperienceSectionComponent } from '../sections/experience/experience-section.component';

vi.mock('aos', () => ({
  refresh: vi.fn(),
  init: vi.fn()
}));

const mockProfile = {
  fullName: "Dimitar M",
  headline: "Backend Developer",
  about: "Some about text",
  location: "Eindhoven",
  aiOptIn: false,

  skills: [
    { id: 1, name: "Java", level: "Expert" }
  ],

  experiences: [
    {
      id: 1,
      company: "BAS World",
      title: "Developer",
      employmentType: "Internship",
      location: "Veghel",
      startDate: "2024-01",
      endDate: "2024-06",
      isCurrent: false,
      description: "Work on dashboards"
    }
  ]
};

describe('ProfilePageComponent', () => {
  let fixture: ComponentFixture<ProfilePageComponent>;
  let component: ProfilePageComponent;

  let profileServiceMock = {
    getMyProfile: vi.fn().mockReturnValue(of(mockProfile)),
    updateMyProfile: vi.fn().mockReturnValue(of(null))
  };

  beforeEach(async () => {
    vi.clearAllMocks();

    await TestBed.configureTestingModule({
      imports: [ProfilePageComponent],
      providers: [
        { provide: ProfileService, useValue: profileServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ProfilePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  function getSaveButton() {
    return fixture.debugElement
      .queryAll(By.css('button'))
      .map(btn => btn.nativeElement)
      .find(el => el.textContent.trim().startsWith('Save Changes'));
  }

  async function flushDOM() {
    await fixture.whenStable();
    fixture.detectChanges();
  }

  async function waitForProfileLoad() {
    await fixture.whenStable();
    fixture.detectChanges();
    await new Promise(r => setTimeout(r, 0));
    fixture.detectChanges();
  }

  it('should load the profile and build the form', () => {
    expect(component.loading).toBe(false);
    expect(component.form).toBeTruthy();

    expect(component.basicInfoForm.get('fullName')?.value).toBe("Dimitar M");
    expect(component.skillsForm.length).toBe(1);
    expect(component.experiencesForm.length).toBe(1);
  });

  it('should hide loading spinner after profile is loaded', () => {
    const loadingEl = fixture.debugElement.query(By.css('.animate-pulse'));
    expect(loadingEl).toBeNull();
  });

  it('should pass the correct skills FormArray to SkillsSectionComponent', async () => {
    await flushDOM();

    const skillsComp = fixture.debugElement
      .query(By.directive(SkillsSectionComponent))
      .componentInstance;

    expect(skillsComp.form.length).toBe(1);
    expect(skillsComp.form.at(0).get('name')?.value).toBe('Java');
  });

  it('should pass the correct experience FormArray to ExperienceSectionComponent', async () => {
    await waitForProfileLoad();

    const expComp = fixture.debugElement
      .query(By.directive(ExperienceSectionComponent))
      ?.componentInstance;

    expect(expComp).toBeTruthy();
    expect(expComp.form.length).toBe(1);
    expect(expComp.form.at(0).get('company')?.value).toBe('BAS World');
  });

  it('should call updateMyProfile() with correct DTO when save is clicked', async () => {
    await flushDOM();

    const saveBtn = getSaveButton();
    saveBtn.click();
    fixture.detectChanges();

    expect(profileServiceMock.updateMyProfile).toHaveBeenCalledTimes(1);

    const dto = profileServiceMock.updateMyProfile.mock.calls[0][0];
    expect(dto.fullName).toBe(mockProfile.fullName);
  });

  it('should NOT call updateMyProfile if form is invalid', async () => {
    await flushDOM();

    component.form.setErrors({ invalid: true });
    fixture.detectChanges();

    component.save();
    fixture.detectChanges();

    expect(profileServiceMock.updateMyProfile).toHaveBeenCalledTimes(0);
  });
});
