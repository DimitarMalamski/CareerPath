import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HowItWorksSectionComponent } from './how-it-works-section.component';
import { By } from '@angular/platform-browser';

describe('HowItWorksSectionComponent', () => {
  let component: HowItWorksSectionComponent;
  let fixture: ComponentFixture<HowItWorksSectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HowItWorksSectionComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(HowItWorksSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should render 3 steps', () => {
    const steps = fixture.debugElement.queryAll(By.css('.step-wrapper'));
    expect(steps.length).toBe(3);
  });

  it('should render correct titles and descriptions for each step', () => {
    const titles = fixture.debugElement.queryAll(By.css('h3'));
    const descriptions = fixture.debugElement.queryAll(By.css('p'));

    expect(titles[0].nativeElement.textContent).toContain('Upload Your CV');
    expect(titles[1].nativeElement.textContent).toContain('Get Smart Matches');
    expect(titles[2].nativeElement.textContent).toContain('Apply Instantly');

    expect(descriptions[0].nativeElement.textContent).toContain('Let our AI understand');
    expect(descriptions[1].nativeElement.textContent).toContain('personalized jobs');
    expect(descriptions[2].nativeElement.textContent).toContain('with one click');
  });

  it('should inject save SVG into each step', () => {
    const icons = fixture.debugElement.queryAll(By.css('.step-icon'));
    expect(icons.length).toBe(3);
    icons.forEach(icon => {
      expect(icon.nativeElement.innerHTML.trim()).not.toBe('');
    });
  });

  it('should set data-delay attribute on each step', () => {
    const stepDivs = fixture.debugElement.queryAll(By.css('[data-aos-delay]'));
    expect(stepDivs.length).toBe(3);
    expect(stepDivs[0].attributes['data-aos-delay']).toBe('0');
    expect(stepDivs[1].attributes['data-aos-delay']).toBe('100');
    expect(stepDivs[2].attributes['data-aos-delay']).toBe('200');
  });

  it('should sanitize SVG string correctly', () => {
    const rawSvg = '<svg><path d="M0,0 L10,10"/></svg>';
    const result = component['sanitizeSvg'](rawSvg);

    expect(result).toBeTruthy();
  });
});
