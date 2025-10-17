import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NavbarComponent } from './navbar.component';
import { RouterTestingModule } from '@angular/router/testing';
import { By } from '@angular/platform-browser';

describe('NavbarComponent', () => {
  let component: NavbarComponent;
  let fixture: ComponentFixture<NavbarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NavbarComponent, RouterTestingModule],
    }).compileComponents();

    fixture = TestBed.createComponent(NavbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should display the logo image', () => {
    const logo = fixture.debugElement.query(By.css('img')).nativeElement;
    expect(logo).toBeTruthy();
    expect(logo.getAttribute('alt')).toBe('CareerPath logo');
  });

  it('should have routerLink for Jobs, Dashboard, and Profile', () => {
    const links = fixture.debugElement.queryAll(By.css('nav a'));

    const linkTexts = ['Jobs', 'Dashboard', 'Profile'];

    linkTexts.forEach(text => {
      const link = links.find(link => link.nativeElement.textContent.includes(text));
      expect(link?.attributes['routerLink']).toBe('/' + text.toLowerCase());
    });
  });

  it('should have routerLink for Login and Sign Up', () => {
    const links = fixture.debugElement.queryAll(By.css('a'));
    const loginLink = links.find(link => link.nativeElement.textContent.includes('Login'));
    const signUpLink = links.find(link => link.nativeElement.textContent.includes('Sign Up'));

    expect(loginLink?.attributes['routerLink']).toBe('/login');
    expect(signUpLink?.attributes['routerLink']).toBe('/register');
  });

  it('should toggle mobile menu visibility', () => {
    expect(component.menuOpen).toBeFalse();

    component.toggleMenu();
    expect(component.menuOpen).toBeTrue();

    component.toggleMenu();
    expect(component.menuOpen).toBeFalse();
  })
})
