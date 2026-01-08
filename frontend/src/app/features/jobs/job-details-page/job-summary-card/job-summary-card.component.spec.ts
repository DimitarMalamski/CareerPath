import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { expect } from 'vitest';
import { JobSummaryCardComponent } from './job-summary-card.component';

describe('JobSummaryCardComponent', () => {
  let fixture: ComponentFixture<JobSummaryCardComponent>;
  let component: JobSummaryCardComponent;

  const baseJob = {
    stackSummary: 'Java, Spring Boot, PostgreSQL',
    finalScore: 0.87456,
    matchedSkills: ['Java', 'Spring Boot'],
    missingSkills: ['AWS'],
    applyUrl: 'https://example.com/apply',
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JobSummaryCardComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(JobSummaryCardComponent);
    component = fixture.componentInstance;
  });

  it('renders role summary when provided', () => {
    component.job = { ...baseJob };
    fixture.detectChanges();

    const summary = fixture.debugElement.query(
      By.css('p')
    ).nativeElement.textContent;

    expect(summary).toContain('Java, Spring Boot, PostgreSQL');
  });

  it('renders fallback role summary when missing', () => {
    component.job = { ...baseJob, stackSummary: '' };
    fixture.detectChanges();

    const summary = fixture.debugElement.query(
      By.css('p')
    ).nativeElement.textContent;

    expect(summary).toContain('No stack summary provided.');
  });

  it('formats and displays the match score', () => {
    component.job = { ...baseJob };
    fixture.detectChanges();

    const score = fixture.debugElement.query(
      By.css('p.text-orange-400')
    ).nativeElement.textContent.trim();

    expect(score).toBe('87%');
  });

  it('renders matched skills when present', () => {
    component.job = { ...baseJob };
    fixture.detectChanges();

    const matched = fixture.debugElement.queryAll(
      By.css('.bg-green-700\\/40')
    );

    expect(matched.length).toBe(2);
  });

  it('shows fallback when no matched skills', () => {
    component.job = { ...baseJob, matchedSkills: [] };
    fixture.detectChanges();

    const fallback = fixture.debugElement.query(
      By.css('p.text-gray-500')
    );

    expect(fallback.nativeElement.textContent)
      .toContain('No matched skills.');
  });

  it('renders missing skills when present', () => {
    component.job = { ...baseJob };
    fixture.detectChanges();

    const missing = fixture.debugElement.queryAll(
      By.css('.bg-red-800\\/40')
    );

    expect(missing.length).toBe(1);
  });

  it('shows fallback when no missing skills', () => {
    component.job = { ...baseJob, missingSkills: [] };
    fixture.detectChanges();

    const fallback = fixture.debugElement.query(
      By.css('p.text-gray-500')
    );

    expect(fallback.nativeElement.textContent)
      .toContain('No missing skills.');
  });

  it('enables apply link when applyUrl exists', () => {
    component.job = { ...baseJob };
    fixture.detectChanges();

    const link: HTMLAnchorElement =
      fixture.nativeElement.querySelector('a');

    expect(link.textContent).toContain('Apply Now');
    expect(link.getAttribute('href')).toBe('https://example.com/apply');
    expect(link.classList.contains('pointer-events-none')).toBe(false);
  });

  it('disables apply link when applyUrl is missing', () => {
    component.job = { ...baseJob, applyUrl: null };
    fixture.detectChanges();

    const link: HTMLAnchorElement =
      fixture.nativeElement.querySelector('a');

    expect(link.textContent).toContain('Apply Link Unavailable');
    expect(link.getAttribute('href')).toBe('#');
    expect(link.classList.contains('pointer-events-none')).toBe(true);
  });
});
