import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AuthLayoutComponent } from './auth-layout.component';
import { RouterTestingModule } from '@angular/router/testing';

describe('AuthLayoutComponent', () => {
  let component: AuthLayoutComponent;
  let fixture: ComponentFixture<AuthLayoutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        AuthLayoutComponent,
        RouterTestingModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AuthLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the layout component', () => {
    expect(component).toBeTruthy();
  });

  it('should render the container div with correct classes', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    const container = compiled.querySelector('div');

    expect(container).toBeTruthy();
    expect(container?.classList).toContain('min-h-screen');
    expect(container?.classList).toContain('w-full');
    expect(container?.classList).toContain('bg-black');
    expect(container?.classList).toContain('flex');
  });

  it('should contain a router-outlet', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    const outlet = compiled.querySelector('router-outlet');

    expect(outlet).toBeTruthy();
  });
});
