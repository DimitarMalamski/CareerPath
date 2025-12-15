import { ComponentFixture, TestBed } from '@angular/core/testing';
import { expect } from 'vitest';
import { JobHeaderComponent } from './job-header.component';
import {RouterTestingModule} from '@angular/router/testing';

describe('JobHeaderComponent', () => {
  let fixture: ComponentFixture<JobHeaderComponent>;
  let component: JobHeaderComponent;

  const mockJob = {
    id: '123',
    title: 'Senior Backend Developer',
    company: 'Google',
    location: 'Amsterdam',
    type: 'FULL_TIME',
    createdAt: '2024-01-10T00:00:00Z',
    description: '',
    stackSummary: '',
    finalScore: 0,
    aiExplanation: '',
    updatedAt: null,
    expiresAt: null,
    skills: [],
    matchedSkills: [],
    missingSkills: [],
    applyUrl: null,
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        JobHeaderComponent,
        RouterTestingModule,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(JobHeaderComponent);
    component = fixture.componentInstance;
  });

  it('should link back to /jobs', () => {
    component.job = mockJob;
    fixture.detectChanges();

    const anchor: HTMLAnchorElement =
      fixture.nativeElement.querySelector('a');

    expect(anchor.getAttribute('href')).toBe('/jobs');
  });
});
