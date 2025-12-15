import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { expect } from 'vitest';
import { JobSkillsBlockComponent } from './job-skills-block.component';

describe('JobSkillsBlockComponent', () => {
  let fixture: ComponentFixture<JobSkillsBlockComponent>;
  let component: JobSkillsBlockComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JobSkillsBlockComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(JobSkillsBlockComponent);
    component = fixture.componentInstance;
  });

  it('renders all provided skills', () => {
    component.skills = ['Java', 'Spring Boot', 'PostgreSQL'];
    fixture.detectChanges();

    const skillElements = fixture.debugElement.queryAll(
      By.css('span')
    );

    expect(skillElements.length).toBe(3);
    expect(skillElements[0].nativeElement.textContent).toContain('Java');
    expect(skillElements[1].nativeElement.textContent).toContain('Spring Boot');
    expect(skillElements[2].nativeElement.textContent).toContain('PostgreSQL');
  });

  it('shows fallback text when skills list is empty', () => {
    component.skills = [];
    fixture.detectChanges();

    const fallback = fixture.debugElement.query(
      By.css('p')
    );

    expect(fallback).toBeTruthy();
    expect(fallback.nativeElement.textContent)
      .toContain('No specific skills listed.');
  });

  it('shows fallback text when skills is null', () => {
    component.skills = null;
    fixture.detectChanges();

    const fallback = fixture.debugElement.query(
      By.css('p')
    );

    expect(fallback).toBeTruthy();
  });

  it('shows fallback text when skills is undefined', () => {
    component.skills = undefined;
    fixture.detectChanges();

    const fallback = fixture.debugElement.query(
      By.css('p')
    );

    expect(fallback).toBeTruthy();
  });
});
