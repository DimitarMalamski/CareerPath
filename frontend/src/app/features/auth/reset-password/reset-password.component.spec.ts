import { Component } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { expect, vi } from 'vitest';
import { provideRouter, Router } from '@angular/router';

import { ResetPasswordComponent } from './reset-password.component';
import { SupabaseService } from '../../../core/services/supabase.service';

@Component({
  standalone: true,
  template: ''
})
class DummyComponent {}

describe('ResetPasswordComponent', () => {
  let component: ResetPasswordComponent;
  let fixture: ComponentFixture<ResetPasswordComponent>;

  const mockUpdateUser = vi.fn();

  const mockSupabaseService = {
    getClient: () => ({
      auth: {
        updateUser: mockUpdateUser
      }
    })
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResetPasswordComponent],
      providers: [
        provideRouter([
          { path: 'login', component: DummyComponent }
        ]),
        { provide: SupabaseService, useValue: mockSupabaseService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ResetPasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should render the title', () => {
    const title = fixture.debugElement.query(By.css('h2')).nativeElement;
    expect(title.textContent).toContain('Choose a New Password');
  });

  it('should bind password and confirmPassword inputs', () => {
    const passInput: HTMLInputElement = fixture.debugElement.query(By.css('#newPassword')).nativeElement;
    const confirmInput: HTMLInputElement = fixture.debugElement.query(By.css('#confirmPassword')).nativeElement;

    passInput.value = 'secret123';
    passInput.dispatchEvent(new Event('input'));

    confirmInput.value = 'secret123';
    confirmInput.dispatchEvent(new Event('input'));

    fixture.detectChanges();

    expect(component.password).toBe('secret123');
    expect(component.confirmPassword).toBe('secret123');
  });

  it('should disable button when form is invalid', () => {
    component.password = '';
    component.confirmPassword = '';
    fixture.detectChanges();

    const btn: HTMLButtonElement = fixture.debugElement.query(By.css('button')).nativeElement;
    expect(btn.disabled).toBe(true);
  });

  it('should show an error when passwords do not match', async () => {
    component.password = 'abc123';
    component.confirmPassword = 'different';

    await component.updatePassword();
    fixture.detectChanges();

    expect(component.error).toBe('Passwords do not match.');

    const errorEl = fixture.debugElement.query(By.css('.text-red-400')).nativeElement;
    expect(errorEl.textContent).toContain('Passwords do not match.');
  });

  it('should show Supabase error when updateUser fails', async () => {
    mockUpdateUser.mockResolvedValueOnce({
      error: { message: 'Reset failed' }
    });

    component.password = 'secret123';
    component.confirmPassword = 'secret123';

    await component.updatePassword();
    fixture.detectChanges();

    expect(component.error).toBe('Reset failed');
  });

  it('should navigate to /login on successful password update', async () => {
    mockUpdateUser.mockResolvedValueOnce({ error: null });

    const router = TestBed.inject(Router);
    const spy = vi.spyOn(router, 'navigate');

    component.password = 'secret123';
    component.confirmPassword = 'secret123';

    await component.updatePassword();
    fixture.detectChanges();

    expect(spy).toHaveBeenCalledWith(['/login']);
  });
});
