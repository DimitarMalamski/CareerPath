import { describe, it, expect, beforeEach, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { JobAiPopupComponent } from './job-ai-popup.component';
import { By } from '@angular/platform-browser';

describe('JobAiPopupComponent', () => {
  let component: JobAiPopupComponent;
  let fixture: ComponentFixture<JobAiPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JobAiPopupComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(JobAiPopupComponent);
    component = fixture.componentInstance;
  });

  it('should display the explanation text', () => {
    // Arrange
    component.explanation = 'AI thinks you match 80%!';

    // Act
    fixture.detectChanges();

    const textElement: HTMLElement = fixture.debugElement.query(
      By.css('p')
    ).nativeElement;

    // Assert
    expect(textElement.textContent).toContain('AI thinks you match 80%!');
  });

  it('should emit close event when clicking the button', () => {
    // Arrange
    component.explanation = 'test';
    const emitSpy = vi.spyOn(component.closeExplanation, 'emit');

    fixture.detectChanges();

    const button = fixture.debugElement.query(By.css('button'));

    // Act
    button.triggerEventHandler('click', null);

    // Assert
    expect(emitSpy).toHaveBeenCalled();
  });
});
