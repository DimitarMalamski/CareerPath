import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HomeComponent } from './home.component';
import { HeroSectionComponent } from './hero-section/hero-section.component';
import { HowItWorksSectionComponent } from './how-it-works-section/how-it-works-section.component';
import { CarouselSectionComponent } from './carousel-section/carousel-section.component';
import { CallToActionSectionComponent } from './call-to-action-section/call-to-action-section.component';

describe('HomeComponent', () => {
  let component: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HomeComponent,
        HeroSectionComponent,
        HowItWorksSectionComponent,
        CarouselSectionComponent,
        CallToActionSectionComponent
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(HomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should render all homepage components', () => {
    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.querySelector('app-hero-section')).toBeTruthy();
    expect(compiled.querySelector('app-how-it-works-section')).toBeTruthy();
    expect(compiled.querySelector('app-carousel-section')).toBeTruthy();
    expect(compiled.querySelector('app-call-to-action-section')).toBeTruthy();
  });
});
