import { ComponentFixture, TestBed } from '@angular/core/testing';
import { expect, vi } from 'vitest';
import { LoginComponent } from './login.component';
import { SupabaseService } from '../../../core/services/supabase.service';
import { By } from '@angular/platform-browser';
import {provideRouter, Router} from '@angular/router';
import {DummyComponent} from '../../../shared/testing/dummy-component';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  const mockSignIn = vi.fn();
  const mockOAuth = vi.fn();

  const mockSupabaseService = {
    signIn: mockSignIn,
    getClient: () => ({
      auth: {
        signInWithOAuth: mockOAuth
      }
    })
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginComponent],
      providers: [
        provideRouter([
          { path: 'home', component: DummyComponent }
        ]),
        { provide: SupabaseService, useValue: mockSupabaseService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should render the login form title', () => {
    const title = fixture.debugElement.query(By.css('h2')).nativeElement;
    expect(title.textContent).toContain('Welcome Back');
  });

  it('should bind email and password inputs with ngModel', () => {
    const emailInput: HTMLInputElement = fixture.debugElement.query(By.css('#email')).nativeElement;
    const passwordInput: HTMLInputElement = fixture.debugElement.query(By.css('#password')).nativeElement;

    emailInput.value = 'user@example.com';
    emailInput.dispatchEvent(new Event('input'));

    passwordInput.value = 'mypassword';
    passwordInput.dispatchEvent(new Event('input'));

    fixture.detectChanges();

    expect(component.email).toBe('user@example.com');
    expect(component.password).toBe('mypassword');
  });

  it('should disable submit button when form is invalid', () => {
    component.email = '';
    component.password = '';
    fixture.detectChanges();

    const btn: HTMLButtonElement = fixture.debugElement.query(By.css('button')).nativeElement;
    expect(btn.disabled).toBe(true);
  });

  it('should show an error when login fails', async () => {
    mockSignIn.mockResolvedValueOnce({
      data: null,
      error: { message: 'Wrong credentials' }
    });

    component.email = 'wrong@test.com';
    component.password = '123';
    await component.login();
    fixture.detectChanges();

    expect(component.errorMessage).toBe('Wrong credentials');

    const errorEl = fixture.debugElement.query(By.css('.text-red-400'));
    expect(errorEl.nativeElement.textContent).toContain('Wrong credentials');
  });

  it('should show an error when no session is returned', async () => {
    mockSignIn.mockResolvedValueOnce({
      data: { session: null },
      error: null
    });

    component.email = 'ok@test.com';
    component.password = 'test123';
    await component.login();
    fixture.detectChanges();

    expect(component.errorMessage).toBe('Unexpected error: no session returned.');
  });

  it('should navigate to /home after successful login', async () => {
    mockSignIn.mockResolvedValueOnce({
      data: { session: { access_token: 'fake-token' } },
      error: null
    });

    const router = TestBed.inject(Router);
    const spy = vi.spyOn(router, 'navigate');

    component.email = 'user@test.com';
    component.password = 'pass';
    await component.login();
    fixture.detectChanges();

    expect(localStorage.getItem('jwt_token')).toBe('fake-token');
    expect(spy).toHaveBeenCalledWith(['/home']);
  });

  it('should call signInWithOAuth when Google login is clicked', async () => {
    await component.loginWithGoogle();

    expect(mockOAuth).toHaveBeenCalledWith({
      provider: 'google',
      options: { redirectTo: globalThis.location.origin }
    });
  });
});
