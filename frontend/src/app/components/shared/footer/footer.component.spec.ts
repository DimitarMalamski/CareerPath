import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FooterComponent } from './footer.component';
import { By } from '@angular/platform-browser';
import { RouterTestingModule } from '@angular/router/testing';

describe('FooterComponent', () => {
  let component: FooterComponent;
  let fixture: ComponentFixture<FooterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FooterComponent, RouterTestingModule],
    }).compileComponents();

    fixture = TestBed.createComponent(FooterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should render current year correctly', () => {
    const yearText = fixture.debugElement.nativeElement.textContent;
    expect(yearText).toContain(new Date().getFullYear());
  });

  it('should render CareerPath brand section', () => {
    const brandHeader = fixture.debugElement.query(By.css('h3')).nativeElement;
    expect(brandHeader?.textContent).toContain('CareerPath');
  });

  it('should render contact information', () => {
    const contact = fixture.debugElement.nativeElement.textContent;
    expect(contact).toContain('Fontys University of Applied Sciences');
    expect(contact).toContain('Eindhoven');
  });

  it('should have routerLink to /jobs and /profile', () => {
    const links = fixture.debugElement.queryAll(By.css('a[routerLink]'));
    const paths = links.map(link => link.attributes['routerLink']);

    expect(paths).toContain('/jobs');
    expect(paths).toContain('/profile');
  });

  it('should scroll to top on logo button click', () => {
    const scrollSpy = spyOn(window as any, 'scrollTo').and.callFake(() => {});

    const button = fixture.debugElement.query(By.css('button'));
    button.triggerEventHandler('click');

    expect(scrollSpy).toHaveBeenCalledWith({
      top: 0,
      behavior: 'smooth'
    });
  });

  it('should render 3 social media icons with correct links', () => {
    const icons = fixture.debugElement.queryAll(By.css('a[aria-label]'));
    expect(icons.length).toBe(3);

    const labels = icons.map(icon => icon.attributes['aria-label']);
    expect(labels).toContain('Github');
    expect(labels).toContain('Instagram');
    expect(labels).toContain('LinkedIn');
  });
});
