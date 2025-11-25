import { ComponentFixture, TestBed } from '@angular/core/testing';
import { JobTagsComponent } from './job-tags.component';
import { By } from '@angular/platform-browser';
import { expect } from 'vitest';
import { JobRecommendation } from '../../../../core/models/job-recommendation';

describe('JobTagsComponent', () => {
  let component: JobTagsComponent;
  let fixture: ComponentFixture<JobTagsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JobTagsComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(JobTagsComponent);
    component = fixture.componentInstance;
  });

  const setJob = (partial: Partial<JobRecommendation>) => {
    component.job = {
      id: '1',
      title: 'Test Job',
      company: 'Company',
      location: 'Eindhoven',
      description: '',
      type: 'FULL_TIME',
      skills: [],
      matchedSkills: [],
      missingSkills: [],
      aiExplanation: '',
      createdAt: '',
      finalScore: 0.8,
      ...partial
    };
  };

  it('should display location', () => {
    setJob({ location: 'Berlin' });
    fixture.detectChanges();

    const locationEl = fixture.debugElement.query(By.css('.text-sm'));
    expect(locationEl.nativeElement.textContent.trim()).toContain('Berlin');
  });

  it('should show "Remote" tag when type is REMOTE', () => {
    setJob({ type: 'REMOTE' });
    fixture.detectChanges();

    const remoteEl = fixture.debugElement.query(By.css('.bg-green-500\\/20'));
    expect(remoteEl).not.toBeNull();
    expect(remoteEl.nativeElement.textContent).toContain('Remote');
  });

  it('should show "Hybrid" tag when type is HYBRID', () => {
    setJob({ type: 'HYBRID' });
    fixture.detectChanges();

    const hybridEl = fixture.debugElement.query(By.css('.bg-blue-500\\/20'));
    expect(hybridEl).not.toBeNull();
    expect(hybridEl.nativeElement.textContent).toContain('Hybrid');
  });

  it('should show formatted job type when not remote/hybrid', () => {
    setJob({ type: 'FULL_TIME' });
    fixture.detectChanges();

    const typeEl = fixture.debugElement.query(By.css('.bg-gray-700'));
    expect(typeEl).not.toBeNull();
    expect(typeEl.nativeElement.textContent.trim()).toBe('Full Time');
  });

  it('isRemote should correctly detect REMOTE', () => {
    expect(component.isRemote('REMOTE')).toBe(true);
    expect(component.isRemote('remote')).toBe(true);
    expect(component.isRemote('FULL_TIME')).toBe(false);
  });

  it('isHybrid should correctly detect HYBRID', () => {
    expect(component.isHybrid('HYBRID')).toBe(true);
    expect(component.isHybrid('hybrid')).toBe(true);
    expect(component.isHybrid('REMOTE')).toBe(false);
  });

  it('formatJobType should format strings correctly', () => {
    expect(component.formatJobType('FULL_TIME')).toBe('Full Time');
    expect(component.formatJobType('PART_TIME')).toBe('Part Time');
    expect(component.formatJobType('INTERNSHIP')).toBe('Internship');
  });

  it('formatJobType should return empty string when type is null/undefined', () => {
    expect(component.formatJobType('')).toBe('');
    expect(component.formatJobType(null as unknown as string)).toBe('');
    expect(component.formatJobType(undefined as unknown as string)).toBe('');
  });
});
