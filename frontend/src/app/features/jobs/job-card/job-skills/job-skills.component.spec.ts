import { ComponentFixture, TestBed } from '@angular/core/testing';
import { JobSkillsComponent } from './job-skills.component';
import { By } from '@angular/platform-browser';
import { expect } from 'vitest';

describe('JobSkillsComponent', () => {
  let component: JobSkillsComponent;
  let fixture: ComponentFixture<JobSkillsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JobSkillsComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(JobSkillsComponent);
    component = fixture.componentInstance;
  });

  it('should display matched skills', () => {
    component.matchedSkills = ['Java', 'Angular'];
    fixture.detectChanges();

    const matchedEls = fixture.debugElement.queryAll(
      By.css('.bg-green-500\\/20')
    );

    expect(matchedEls.length).toBe(2);
    expect(matchedEls[0].nativeElement.textContent.trim()).toBe('Java');
    expect(matchedEls[1].nativeElement.textContent.trim()).toBe('Angular');
  });

  it('should display missing skills', () => {
    component.missingSkills = ['React', 'Docker'];
    fixture.detectChanges();

    const missingEls = fixture.debugElement.queryAll(
      By.css('.bg-red-500\\/10')
    );

    expect(missingEls.length).toBe(2);
    expect(missingEls[0].nativeElement.textContent.trim()).toBe('React');
    expect(missingEls[1].nativeElement.textContent.trim()).toBe('Docker');
  });

  it('should show overflow text when skills > 4', () => {
    component.skills = ['A', 'B', 'C', 'D', 'E', 'F'];
    fixture.detectChanges();

    const overflow = fixture.debugElement.query(
      By.css('span.text-gray-400')
    );

    expect(overflow).not.toBeNull();
    expect(overflow.nativeElement.textContent.trim()).toBe('+2 more');
  });

  it('should NOT show overflow text when skills <= 4', () => {
    component.skills = ['A', 'B', 'C', 'D'];
    fixture.detectChanges();

    const overflow = fixture.debugElement.query(
      By.css('span.text-gray-400')
    );

    expect(overflow).toBeNull();
  });
});
