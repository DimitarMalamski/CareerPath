import { Component } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { expect, vi } from 'vitest';
import { provideRouter, Router } from '@angular/router';

import { RegisterComponent } from './register.component';
import { SupabaseService } from '../../../core/services/supabase.service';

@Component({
  standalone: true,
  template: ''
})
class DummyComponent {}

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  const mockSignUp = vi.fn();

  const mockSupabaseService = {
    getClient: () => ({
      auth: {
        signUp: mockSignUp
      }
    })
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterComponent],
      providers: [
        provideRouter([
          { path: 'auth/login', component: DummyComponent },
          { path: 'check-email', component: DummyComponent }
        ]),
        { provide: SupabaseService, useValue: mockSupabaseService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should render the Create Account title', () => {
    const title = fixture.debugElement.query(By.css('h2')).nativeElement;
    expect(title.textContent).toContain('Create an Account');
  });

  it('should bind email, password, and confirmPassword inputs', () => {
    const emailInput: HTMLInputElement = fixture.debugElement.query(By.css('#email')).nativeElement;
    const passInput: HTMLInputElement = fixture.debugElement.query(By.css('#password')).nativeElement;
    const confirmInput: HTMLInputElement = fixture.debugElement.query(By.css('#confirmPassword')).nativeElement;

    emailInput.value = 'user@example.com';
    emailInput.dispatchEvent(new Event('input'));

    passInput.value = 'mypassword';
    passInput.dispatchEvent(new Event('input'));

    confirmInput.value = 'mypassword';
    confirmInput.dispatchEvent(new Event('input'));

    fixture.detectChanges();

    expect(component.email).toBe('user@example.com');
    expect(component.password).toBe('mypassword');
    expect(component.confirmPassword).toBe('mypassword');
  });

  it('should disable register button when form is invalid', () => {
    component.email = '';
    component.password = '';
    component.confirmPassword = '';
    fixture.detectChanges();

    const btn: HTMLButtonElement = fixture.debugElement.query(By.css('button')).nativeElement;
    expect(btn.disabled).toBe(true);
  });

  it('should show an error when passwords do not match', async () => {
    component.email = 'test@mail.com';
    component.password = '123456';
    component.confirmPassword = 'wrong';

    await component.register();
    fixture.detectChanges();

    expect(component.errorMessage).toBe('Passwords do not match.');

    const errorEl = fixture.debugElement.query(By.css('.text-red-400'));
    expect(errorEl.nativeElement.textContent).toContain('Passwords do not match.');
  });

  it('should show Supabase error if signUp fails', async () => {
    mockSignUp.mockResolvedValueOnce({
      error: { message: 'Email already registered' }
    });

    component.email = 'test@mail.com';
    component.password = '123456';
    component.confirmPassword = '123456';

    await component.register();
    fixture.detectChanges();

    expect(component.errorMessage).toBe('Email already registered');
  });

  it('should navigate to /check-email on successful registration', async () => {
    mockSignUp.mockResolvedValueOnce({ error: null });

    const router = TestBed.inject(Router);
    const spy = vi.spyOn(router, 'navigate');

    component.email = 'user@mail.com';
    component.password = '123456';
    component.confirmPassword = '123456';

    await component.register();
    fixture.detectChanges();

    expect(spy).toHaveBeenCalledWith(['/check-email']);
  });
});
