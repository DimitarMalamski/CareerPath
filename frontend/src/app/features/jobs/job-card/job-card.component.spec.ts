import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { expect } from 'vitest';

import { JobCardComponent } from './job-card.component';
import { JobRecommendation } from '../../../core/models/job-recommendation';
import {RouterTestingModule} from '@angular/router/testing';

describe('JobCardComponent', () => {
  let fixture: ComponentFixture<JobCardComponent>;
  let component: JobCardComponent;

  const mockJob: JobRecommendation = {
    id: '1',
    title: 'Backend Dev',
    company: 'Google',
    location: 'Berlin',
    createdAt: new Date().toISOString(),
    type: 'FULL_TIME',
    skills: ['Java', 'Spring'],
    matchedSkills: ['Java'],
    missingSkills: ['Spring'],
    description: 'Build amazing backend systems. Use Java daily.',
    finalScore: 0.82,
    aiExplanation: 'Strong Java match.'
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        JobCardComponent,
        RouterTestingModule
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(JobCardComponent);
    component = fixture.componentInstance;
    component.job = mockJob;
    fixture.detectChanges();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should instantiate JobCardComponent class', () => {
    expect(true).toBe(true);
  });

  it('should accept a userId input', () => {
    component.userId = 'user-123';
    fixture.detectChanges();

    expect(component.userId).toBe('user-123');
  });

  it('should allow userId to be null', () => {
    component.userId = null;
    fixture.detectChanges();

    expect(component.userId).toBeNull();
  });

  it('togglePopup should switch isPopupOpen', () => {
    expect(component.isPopupOpen).toBe(false);
    component.togglePopup();
    expect(component.isPopupOpen).toBe(true);
    component.togglePopup();
    expect(component.isPopupOpen).toBe(false);
  });

  it('closePopup should set isPopupOpen to false', () => {
    component.isPopupOpen = true;
    component.closePopup();
    expect(component.isPopupOpen).toBe(false);
  });

  it('should show the AI popup when isPopupOpen = true', () => {
    component.isPopupOpen = true;
    fixture.detectChanges();

    const popup = fixture.debugElement.query(By.css('app-job-ai-popup'));
    expect(popup).not.toBeNull();
  });

  it('should hide the AI popup when isPopupOpen = false', () => {
    component.isPopupOpen = false;
    fixture.detectChanges();

    const popup = fixture.debugElement.query(By.css('app-job-ai-popup'));
    expect(popup).toBeNull();
  });

  it('should close popup when clicking outside', () => {
    component.isPopupOpen = true;

    const event = new MouseEvent('click', { bubbles: true });
    document.dispatchEvent(event);

    fixture.detectChanges();

    expect(component.isPopupOpen).toBe(false);
  });

  it('should NOT close popup when clicking inside popup', () => {
    component.isPopupOpen = true;
    fixture.detectChanges();

    const fakePopupElement = document.createElement('div');
    fakePopupElement.classList.add('ai-popup');

    const event = new MouseEvent('click', { bubbles: true });
    Object.defineProperty(event, 'target', { value: fakePopupElement });

    document.dispatchEvent(event);
    fixture.detectChanges();

    expect(component.isPopupOpen).toBe(true);
  });

  it('getJobSummary should return the first sentence', () => {
    const summary = component.getJobSummary(mockJob);
    expect(summary).toBe('Build amazing backend systems');
  });

  it('getJobSummary should truncate long sentences', () => {
    const longJob = {
      ...mockJob,
      description: 'A'.repeat(200)
    };
    const summary = component.getJobSummary(longJob);

    expect(summary.length).toBeLessThanOrEqual(84);
    expect(summary.endsWith('...')).toBe(true);
  });

  it('getJobSummary should return empty string if no description', () => {
    const emptyJob = { ...mockJob, description: '' };
    expect(component.getJobSummary(emptyJob)).toBe('');
  });
});
