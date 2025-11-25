import { ComponentFixture, TestBed } from '@angular/core/testing';
import { expect, vi } from 'vitest';
import { JobScoreComponent } from './job-score.component';
import { By } from '@angular/platform-browser';

describe('JobScoreComponent', () => {
  let component: JobScoreComponent;
  let fixture: ComponentFixture<JobScoreComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JobScoreComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(JobScoreComponent);
    component = fixture.componentInstance;
  });

  it('should display the score as a percentage', () => {
    component.score = 0.85;
    fixture.detectChanges();

    const textEl: HTMLElement = fixture.debugElement.nativeElement;
    expect(textEl.textContent?.trim()).toContain('85%');
  });

  it('should apply green color when score >= 0.75', () => {
    component.score = 0.8;
    fixture.detectChanges();

    const scoreEl = fixture.debugElement.query(By.css('p'));
    expect(scoreEl.nativeElement.classList).toContain('text-green-400');
  });

  it('should apply yellow color when 0.4 <= score < 0.75', () => {
    component.score = 0.5;
    fixture.detectChanges();

    const el = fixture.debugElement.query(By.css('p'));
    expect(el.nativeElement.classList).toContain('text-yellow-400');
  });

  it('should apply red color when score < 0.4', () => {
    component.score = 0.2;
    fixture.detectChanges();

    const el = fixture.debugElement.query(By.css('p'));
    expect(el.nativeElement.classList).toContain('text-red-400');
  });

  it('should emit infoClick when AI icon is clicked', () => {
    component.score = 0.7;
    component.hasAi = true;

    const spy = vi.fn();
    component.infoClick.subscribe(spy);

    fixture.detectChanges();

    const button = fixture.debugElement.query(By.css('button'));
    expect(button).not.toBeNull();

    button.nativeElement.click();

    expect(spy).toHaveBeenCalledTimes(1);
  });
});
