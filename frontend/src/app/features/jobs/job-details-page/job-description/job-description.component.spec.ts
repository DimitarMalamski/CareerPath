import { ComponentFixture, TestBed } from '@angular/core/testing';
import { expect } from 'vitest';
import { JobDescriptionComponent } from './job-description.component';

describe('JobDescriptionComponent', () => {
  let fixture: ComponentFixture<JobDescriptionComponent>;
  let component: JobDescriptionComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JobDescriptionComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(JobDescriptionComponent);
    component = fixture.componentInstance;
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should render the description text when provided', () => {
    component.description = 'This is a backend engineering role.';
    fixture.detectChanges();

    const text = fixture.nativeElement.textContent;
    expect(text).toContain('This is a backend engineering role.');
  });

  it('should render nothing when description is null', () => {
    component.description = null;
    fixture.detectChanges();

    const text = fixture.nativeElement.textContent;
    expect(text).not.toContain('null');
  });

  it('should support multiline text (whitespace preserved)', () => {
    component.description = 'Line 1\nLine 2\nLine 3';
    fixture.detectChanges();

    const element = fixture.nativeElement.querySelector('p');
    expect(element.textContent).toContain('Line 1');
    expect(element.textContent).toContain('Line 2');
    expect(element.textContent).toContain('Line 3');
  });
});
