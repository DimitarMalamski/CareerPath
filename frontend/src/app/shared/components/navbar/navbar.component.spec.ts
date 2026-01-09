import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NavbarComponent } from './navbar.component';
import { RouterTestingModule } from '@angular/router/testing';
import { By } from '@angular/platform-browser';
import {provideMockSupabase} from '../../../../testing/mock-supabase';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('NavbarComponent', () => {
  let component: NavbarComponent;
  let fixture: ComponentFixture<NavbarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NavbarComponent, RouterTestingModule, HttpClientTestingModule],
      providers: [provideMockSupabase()]
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

  it('should have routerLink for Jobs, and Profile', () => {
    const links = fixture.debugElement.queryAll(By.css('nav a'));

    const linkTexts = ['Jobs', 'Profile'];

    linkTexts.forEach(text => {
      const link = links.find(link => link.nativeElement.textContent.includes(text));
      expect(link?.attributes['routerLink']).toBe('/' + text.toLowerCase());
    });
  });

  it('should toggle mobile menu visibility in the DOM', () => {
    let mobileMenu = fixture.debugElement.query(By.css('.md\\:hidden.px-6.pb-4'));
    expect(mobileMenu).toBeNull();

    component.toggleMenu();
    fixture.detectChanges();

    mobileMenu = fixture.debugElement.query(By.css('.md\\:hidden.px-6.pb-4'));
    expect(mobileMenu).not.toBeNull();

    component.toggleMenu();
    fixture.detectChanges();

    mobileMenu = fixture.debugElement.query(By.css('.md\\:hidden.px-6.pb-4'));
    expect(mobileMenu).toBeNull();
  })
})
