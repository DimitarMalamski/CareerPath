import { ComponentFixture, TestBed } from '@angular/core/testing';
import { expect, vi } from 'vitest';
import { ForgotPasswordComponent } from './forgot-password.component';
import { SupabaseService } from '../../../core/services/supabase.service';
import { By } from '@angular/platform-browser';
import {RouterTestingModule} from '@angular/router/testing';

describe('ForgotPasswordComponent', () => {
  let component: ForgotPasswordComponent;
  let fixture: ComponentFixture<ForgotPasswordComponent>;

  const mockResetPassword = vi.fn();
  const mockAuth = { resetPasswordForEmail: mockResetPassword };
  const mockClient = { auth: mockAuth };

  const mockSupabaseService = {
    getClient: () => mockClient
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        ForgotPasswordComponent,
        RouterTestingModule],
      providers: [
        { provide: SupabaseService, useValue: mockSupabaseService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ForgotPasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should render the form', () => {
    const title = fixture.debugElement.query(By.css('h2')).nativeElement;
    expect(title.textContent).toContain('Reset Password');
  });

  it('should bind email input via ngModel', async () => {
    const input: HTMLInputElement = fixture.debugElement.query(By.css('#email')).nativeElement;

    input.value = 'user@example.com';
    input.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    expect(component.email).toBe('user@example.com');
  });

  it('should disable submit button when form is invalid', () => {
    component.email = '';
    fixture.detectChanges();

    const button = fixture.debugElement.query(By.css('button')).nativeElement;
    expect(button.disabled).toBe(true);
  });

  it('should show success message on valid reset request', async () => {
    mockResetPassword.mockResolvedValueOnce({ error: null });

    component.email = 'user@example.com';
    await component.sendResetEmail();
    fixture.detectChanges();

    expect(component.message).toBe('A reset link has been sent to user@example.com');

    const msg = fixture.debugElement.query(By.css('.text-green-400'));
    expect(msg.nativeElement.textContent).toContain('A reset link has been sent');
  });

  it('should show error message when Supabase returns an error', async () => {
    mockResetPassword.mockResolvedValueOnce({
      error: { message: 'Something went wrong' }
    });

    component.email = 'fail@example.com';
    await component.sendResetEmail();
    fixture.detectChanges();

    expect(component.error).toBe('Something went wrong');

    const errorEl = fixture.debugElement.query(By.css('.text-red-400'));
    expect(errorEl.nativeElement.textContent).toContain('Something went wrong');
  });

  it('should call supabase.resetPasswordForEmail with correct email', async () => {
    mockResetPassword.mockResolvedValueOnce({ error: null });

    component.email = 'fail@example.com';
    await component.sendResetEmail();
    fixture.detectChanges();

    expect(mockResetPassword).toHaveBeenLastCalledWith(
      component.email,
      expect.objectContaining({
        redirectTo: 'https://careerpath-ip.com/auth/reset-password'
      })
    );
  });
});
