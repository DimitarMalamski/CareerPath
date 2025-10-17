import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HeroSectionComponent } from './hero-section.component';
import { By } from '@angular/platform-browser';
import { RouterTestingModule } from '@angular/router/testing';

const AOS = require('aos');
AOS.init = jasmine.createSpy('init');

describe('HeroSectionComponent', () => {
  let component: HeroSectionComponent;
  let fixture: ComponentFixture<HeroSectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HeroSectionComponent,
        RouterTestingModule.withRoutes([]),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(HeroSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should render correct heading text', () => {
    const heading = fixture.debugElement.query(By.css('h1')).nativeElement;
    expect(heading.textContent).toContain('Find Internships');
  });

  it('should render subtext', () => {
    const paragraph = fixture.debugElement.query(By.css('p')).nativeElement;
    expect(paragraph.textContent).toContain('AI-powered job matching based on your CV and preferences');
  });

  it('should have Explore Jobs with correct routerLink', () => {
    const link = fixture.nativeElement.querySelector('a[routerLink="/jobs"]');
    expect(link).toBeTruthy();
    expect(link?.textContent).toContain('Explore Jobs');
  });

  it('should have Create Profile link with correct routerLink', () => {
    const link = fixture.nativeElement.querySelector('a[routerLink="/profile"]');
    expect(link).toBeTruthy();
    expect(link?.textContent).toContain('Create Profile');
  });

  it('should render hero image with alt text', () => {
    const image = fixture.debugElement.query(By.css('img')).nativeElement;
    expect(image.alt).toBe('Hero Component Image');
  });

  it('should call AOS.init on ngOnInit', () => {
    const aosInitSpy = jasmine.createSpy('init');
    (AOS as any).init = aosInitSpy;

    component.ngOnInit();

    expect(aosInitSpy).toHaveBeenCalledWith({
      duration: 800,
      once: true
    });
  });
});
