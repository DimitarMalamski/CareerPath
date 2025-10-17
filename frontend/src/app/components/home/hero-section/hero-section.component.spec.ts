import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HeroSectionComponent } from './hero-section.component';
import { By } from '@angular/platform-browser';
import { RouterTestingModule } from '@angular/router/testing';

describe('HeroSectionComponent', () => {
  let component: HeroSectionComponent;
  let fixture: ComponentFixture<HeroSectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeroSectionComponent, RouterTestingModule],
    }).compileComponents();

    fixture = TestBed.createComponent(HeroSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should render correct heading text', () => {
    const heading = fixture.debugElement.query(By.css('h1')).nativeElement;
    expect(heading.textContent).toContain('Find Internships');
  });

  it('should render subtext', () => {
    const paragraph = fixture.debugElement.query(By.css('p')).nativeElement;
    expect(paragraph.textContent).toContain('AI-powered job matching based on your CV and preferences');
  });

  it('should have Explore Jobs with correct routerLink', () => {
    const exploreLink = fixture.debugElement
      .queryAll(By.css('a'))
      .find(link => link.nativeElement.textContent.includes('Explore Jobs'));

    expect(exploreLink).toBeTruthy();
    expect(exploreLink?.nativeElement.getAttribute('ng-reflect-router-link'))
      || exploreLink?.properties['routerLink']
      .toContain('/jobs');
  });

  it('should have Create Profile link with correct routerLink', () => {
    const profileLink = fixture.debugElement
      .queryAll(By.css('a'))
      .find(link => link.nativeElement.textContent.includes('Create Profile'));

    expect(profileLink).toBeTruthy();
    expect(profileLink?.nativeElement.getAttribute('ng-reflect-router-link'))
      || profileLink?.properties['routerLink']
      .toContain('/profile');
  });

  it('should render hero image with alt text', () => {
    const image = fixture.debugElement.query(By.css('img')).nativeElement;
    expect(image.alt).toBe('Hero Component Image');
  });
})
