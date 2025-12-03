import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormArray, FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { expect } from 'vitest';

import { SkillsSectionComponent } from './skills-section.component';
import { Component } from '@angular/core';

function createSkill(): FormGroup {
  return new FormGroup({
    name: new FormControl(''),
    level: new FormControl('')
  });
}

@Component({
  template: `
    <form [formGroup]="wrapper">
      <app-skills-section
        [form]="skillsArray"
        [createSkillFn]="createFn">
      </app-skills-section>
    </form>
  `,
  standalone: true,
  imports: [SkillsSectionComponent, ReactiveFormsModule]
})
class SkillsHostTestComponent {
  skillsArray = new FormArray([createSkill()]);
  wrapper = new FormGroup({ skills: this.skillsArray }); // wrapper needed for reactive forms

  createFn = () => createSkill();
}

describe('SkillsSectionComponent', () => {
  let fixture: ComponentFixture<SkillsHostTestComponent>;
  let component: SkillsSectionComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SkillsHostTestComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(SkillsHostTestComponent);

    const debug = fixture.debugElement.query(By.directive(SkillsSectionComponent));
    component = debug.componentInstance;

    fixture.detectChanges();
  });

  it('should render the Skills title', () => {
    const title = fixture.debugElement.query(By.css('h2')).nativeElement;
    expect(title.textContent).toContain('Skills');
  });

  it('should render one skill block initially', () => {
    const blocks = fixture.debugElement.queryAll(By.css('.relative'));
    expect(blocks.length).toBe(1);
  });

  it('should add a skill when + Add Skill is clicked', () => {
    const addBtn = fixture.debugElement.query(By.css('button')).nativeElement;
    addBtn.click();

    fixture.detectChanges();

    expect(component.skills.length).toBe(2);
  });

  it('should remove a skill when the ✕ button is clicked', () => {
    const removeBtn = fixture.debugElement.query(By.css('button.absolute')).nativeElement;
    removeBtn.click();

    fixture.detectChanges();

    expect(component.skills.length).toBe(0);
  });

  it('should bind skill name input to form control', () => {
    const input = fixture.debugElement.query(By.css('#skill-name-0')).nativeElement;

    input.value = 'Angular';
    input.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    expect(component.skills.at(0).get('name')?.value).toBe('Angular');
  });

  it('should bind skill level select to form control', () => {
    const select = fixture.debugElement.query(By.css('#skill-level-0')).nativeElement;

    select.value = 'Expert';
    select.dispatchEvent(new Event('change'));
    fixture.detectChanges();

    expect(component.skills.at(0).get('level')?.value).toBe('Expert');
  });

  it('should throw if form input is missing', () => {
    const createWithoutInput = () => {
      const comp = TestBed.createComponent(SkillsSectionComponent);
      comp.detectChanges();
    };

    expect(createWithoutInput).toThrow();
  });
});
