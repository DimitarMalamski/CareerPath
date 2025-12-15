import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { expect } from 'vitest';
import { RelatedJobsComponent } from './related-jobs.component';
import { RouterTestingModule } from '@angular/router/testing';

describe('RelatedJobsComponent', () => {
  let fixture: ComponentFixture<RelatedJobsComponent>;
  let component: RelatedJobsComponent;

  const mockJobs = [
    {
      id: '1',
      title: 'Frontend Developer',
      company: 'Google',
      skills: ['Angular', 'TypeScript', 'RxJS', 'Tailwind'],
    },
    {
      id: '2',
      title: 'Backend Developer',
      company: 'Amazon',
      skills: ['Java', 'Spring', 'PostgreSQL'],
    },
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RelatedJobsComponent,
        RouterTestingModule,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RelatedJobsComponent);
    component = fixture.componentInstance;
  });

  it('shows loading skeleton when loading is true', () => {
    component.loading = true;
    component.relatedJobs = [];
    fixture.detectChanges();

    const title = fixture.nativeElement.querySelector('h2');
    const skeletons = fixture.nativeElement.querySelectorAll(
      '.bg-gray-800\\/60'
    );

    expect(title.textContent).toContain('Related Jobs');
    expect(skeletons.length).toBe(3);
  });

  it('renders related job cards when loading is false and jobs exist', () => {
    component.loading = false;
    component.relatedJobs = mockJobs;
    fixture.detectChanges();

    const cards = fixture.debugElement.queryAll(By.css('a'));
    expect(cards.length).toBe(2);

    expect(cards[0].nativeElement.textContent)
      .toContain('Frontend Developer');
    expect(cards[0].nativeElement.textContent)
      .toContain('Google');
  });

  it('limits displayed skills to maximum of 3 per job', () => {
    component.loading = false;
    component.relatedJobs = mockJobs;
    fixture.detectChanges();

    const firstJobSkills = fixture.debugElement
      .queryAll(By.css('a'))[0]
      .queryAll(By.css('span'));

    expect(firstJobSkills.length).toBe(3);
  });

  it('generates correct job links', () => {
    component.loading = false;
    component.relatedJobs = mockJobs;
    fixture.detectChanges();

    const links: HTMLAnchorElement[] =
      fixture.nativeElement.querySelectorAll('a');

    expect(links[0].getAttribute('href')).toContain('/jobs/1');
    expect(links[1].getAttribute('href')).toContain('/jobs/2');
  });

  it('renders nothing when not loading and no related jobs exist', () => {
    component.loading = false;
    component.relatedJobs = [];
    fixture.detectChanges();

    const title = fixture.nativeElement.querySelector('h2');
    const cards = fixture.nativeElement.querySelectorAll('a');

    expect(title).toBeNull();
    expect(cards.length).toBe(0);
  });
});
