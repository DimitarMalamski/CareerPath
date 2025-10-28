import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HeroSectionComponent } from './hero-section.component';
import { By } from '@angular/platform-browser';
import { RouterTestingModule } from '@angular/router/testing';
import {expect, vi} from 'vitest';

vi.mock('aos', () => ({
  init: vi.fn(),
}));

import * as AOS from 'aos';

describe('HeroSectionComponent', () => {
  let component: HeroSectionComponent;
  let fixture: ComponentFixture<HeroSectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HeroSectionComponent,
        RouterTestingModule.withRoutes([]),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(HeroSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
    const element = fixture.nativeElement as HTMLElement;
    expect(element.querySelector('section')).not.toBeNull();
  });

  it('should render correct heading text', () => {
    const heading = fixture.debugElement.query(By.css('h1')).nativeElement;
    expect(heading).toBeTruthy();
    expect(heading.textContent?.trim()).toContain('Find Internships');
  });

  it('should render subtext correctly', () => {
    const paragraph = fixture.debugElement.query(By.css('p')).nativeElement;
    expect(paragraph).toBeTruthy();
    expect(paragraph.textContent?.trim()).toContain('AI-powered job matching based on your CV and preferences');
  });

  it('should have "Explore Jobs" with correct routerLink', () => {
    const link = fixture.nativeElement.querySelector('a[routerLink="/jobs"]');
    expect(link).not.toBeNull();
    expect(link?.getAttribute('routerLink')).toBe('/jobs');
    expect(link?.textContent?.trim()).toContain('Explore Jobs');
  });

  it('should have "Create Profile" link with correct routerLink', () => {
    const link = fixture.nativeElement.querySelector('a[routerLink="/profile"]');
    expect(link).not.toBeNull();
    expect(link?.getAttribute('routerLink')).toBe('/profile');
    expect(link?.textContent?.trim()).toContain('Create Profile');
  });

  it('should render hero image with alt text and src', () => {
    const image = fixture.debugElement.query(By.css('img')).nativeElement;
    expect(image).toBeTruthy();
    expect(image.alt.trim().length).toBeGreaterThan(0);
    expect(image.src).toMatch(/(assets\/images\/.+\.(png|jpg|jpeg|svg)$)|(https?:\/\/.+\.(png|jpg|jpeg|svg)$)/);
  });

  it('should call AOS.init on ngOnInit', () => {
    const aosInitSpy = vi.fn();
    (AOS as unknown as { init: (config: { duration: number; once: boolean }) => void }).init = aosInitSpy;

    component.ngOnInit();

    expect(aosInitSpy).toHaveBeenCalledTimes(1);
    expect(aosInitSpy).toHaveBeenCalledWith({
      duration: 800,
      once: true
    });
  });
});
