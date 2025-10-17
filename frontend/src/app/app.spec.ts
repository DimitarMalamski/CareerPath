import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { App } from './app';

// eslint-disable-next-line @typescript-eslint/no-require-imports
const AOS = require('aos');
AOS.init = jasmine.createSpy('init');

describe('App', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [App],
      providers: [provideHttpClient()]
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(App);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should render a router outlet', () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('router-outlet')).toBeTruthy();
  });

  it('should call AOS.init on ngOnInit', () => {
    const fixture = TestBed.createComponent(App);
    const app = fixture.componentInstance;

    app.ngOnInit();

    expect(AOS.init).toHaveBeenCalledWith({
      duration: 700,
      once: true,
    });
  });
});
