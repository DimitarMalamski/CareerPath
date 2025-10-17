import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CarouselSectionComponent } from './carousel-section.component';
import { By } from '@angular/platform-browser';

describe('CarouselSectionComponent', () => {
  let component: CarouselSectionComponent;
  let fixture: ComponentFixture<CarouselSectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CarouselSectionComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(CarouselSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should have 5 testmonials defined', () => {
    expect(component.testimonials.length).toBe(5);
  });

  it('should render all testimonials twice for seamless loop', () => {
    const renderedQuotes = fixture.debugElement.queryAll(By.css('p.italic'));
    expect(renderedQuotes.length).toBe(10);
  });

  it('should render testimonial name and role correctly', () => {
    const names = fixture.debugElement.queryAll(By.css('h4'));
    const roles = fixture.debugElement.queryAll(By.css('p.text-sm'));

    expect(names.length).toBe(10);
    expect(roles.length).toBe(10);

    expect(names[0].nativeElement.textContent).toContain('Anna Dimitrova');
    expect(roles[0].nativeElement.textContent).toContain('Frontend Intern');
  });
});
