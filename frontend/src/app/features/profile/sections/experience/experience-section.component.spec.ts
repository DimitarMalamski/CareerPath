import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormArray, FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { expect } from 'vitest';

import { ExperienceSectionComponent } from './experience-section.component';
import {Component} from '@angular/core';

function createExperience(): FormGroup {
  return new FormGroup({
    company: new FormControl(''),
    title: new FormControl(''),
    employmentType: new FormControl(''),
    location: new FormControl(''),
    startDate: new FormControl(''),
    endDate: new FormControl(''),
    isCurrent: new FormControl(false),
    description: new FormControl('')
  });
}

describe('ExperienceSectionComponent', () => {
  let component: ExperienceSectionComponent;
  let fixture: ComponentFixture<ExperienceHostTestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExperienceHostTestComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(ExperienceHostTestComponent);

    const expDebug = fixture.debugElement.query(By.directive(ExperienceSectionComponent));
    component = expDebug.componentInstance;

    fixture.detectChanges();
  });

  it('should render the Experience title', () => {
    const title = fixture.debugElement.query(By.css('h2')).nativeElement;
    expect(title.textContent).toContain('Experience');
  });

  it('should render one experience block initially', () => {
    const blocks = fixture.debugElement.queryAll(By.css('.relative'));
    expect(blocks.length).toBe(1);
  });

  it('should add a new experience when + Add Experience is clicked', () => {
    const addBtn = fixture.debugElement.query(By.css('button')).nativeElement;
    addBtn.click();

    fixture.detectChanges();

    expect(component.experiences.length).toBe(2);
  });

  it('should remove an experience when ✕ button is clicked', () => {
    const removeBtn = fixture.debugElement.query(By.css('button.absolute')).nativeElement;
    removeBtn.click();

    fixture.detectChanges();

    expect(component.experiences.length).toBe(0);
  });

  it('should bind company input to form control', () => {
    const companyInput = fixture.debugElement.query(By.css('#company-0')).nativeElement;

    companyInput.value = 'BAS World';
    companyInput.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    expect(component.experiences.at(0).get('company')?.value).toBe('BAS World');
  });

  it('should throw if form input is missing', () => {
    const createWithoutInput = () => {
      const comp = TestBed.createComponent(ExperienceSectionComponent);
      comp.detectChanges();
    };

    expect(createWithoutInput).toThrow();
  });
});

@Component({
  template: `
    <form [formGroup]="wrapper">
      <app-experience-section
        [form]="formArray"
        [createExperienceFn]="createFn">
      </app-experience-section>
    </form>
  `,
  standalone: true,
  imports: [ExperienceSectionComponent, ReactiveFormsModule]
})
export class ExperienceHostTestComponent {
  formArray = new FormArray([createExperience()]);
  wrapper = new FormGroup({ experiences: this.formArray });

  createFn = () => createExperience();
}
