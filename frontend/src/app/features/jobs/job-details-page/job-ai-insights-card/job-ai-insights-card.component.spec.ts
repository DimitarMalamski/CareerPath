import { ComponentFixture, TestBed } from '@angular/core/testing';
import { expect } from 'vitest';
import { AiInsightsCardComponent } from './job-ai-insights-card.component';

describe('AiInsightsCardComponent', () => {
  let fixture: ComponentFixture<AiInsightsCardComponent>;
  let component: AiInsightsCardComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AiInsightsCardComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(AiInsightsCardComponent);
    component = fixture.componentInstance;
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should display AI explanation when provided', () => {
    component.aiExplanation = 'This job matches your backend experience.';
    fixture.detectChanges();

    const text = fixture.nativeElement.textContent;
    expect(text).toContain('This job matches your backend experience.');
    expect(text).not.toContain('AI did not provide additional insights');
  });

  it('should show fallback text when aiExplanation is null', () => {
    component.aiExplanation = null;
    fixture.detectChanges();

    const text = fixture.nativeElement.textContent;
    expect(text).toContain('AI did not provide additional insights for this job.');
  });

  it('should show fallback text when aiExplanation is an empty string', () => {
    component.aiExplanation = '';
    fixture.detectChanges();

    const text = fixture.nativeElement.textContent;
    expect(text).toContain('AI did not provide additional insights for this job.');
  });
});
