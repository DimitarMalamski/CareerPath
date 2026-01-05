import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CallToActionSectionComponent } from './call-to-action-section.component';
import { RouterTestingModule } from '@angular/router/testing';

describe('CallToActionSectionComponent', () => {
  let component: CallToActionSectionComponent;
  let fixture: ComponentFixture<CallToActionSectionComponent>;
  let element: HTMLElement;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CallToActionSectionComponent, RouterTestingModule]
    }).compileComponents();

    fixture = TestBed.createComponent(CallToActionSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    element = fixture.nativeElement;
  });

  it('it should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should render the title', () => {
    const heading = element.querySelector('h2');
    expect(heading?.textContent).toContain('Ready to Find Your Dream Job?');
  });

  it('should render the description', () => {
    const description = element.querySelector('p');
    expect(description?.textContent).toContain('Let AI guide your career');
  });

  it('should render the call to action button', () => {
    const button = element.querySelector('a[routerLink="/auth/register"]');
    expect(button).toBeTruthy();
    expect(button?.textContent).toContain('Get Started');
  });

  it('should render the background visual elements', () => {
    const visuals = element.querySelectorAll('.blur-3xl');
    expect(visuals.length).toBe(2);
  });
});
