import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { expect } from 'vitest';

import { BasicInfoComponent } from './basic-info.component';

describe('BasicInfoComponent', () => {
  let component: BasicInfoComponent;
  let fixture: ComponentFixture<BasicInfoComponent>;

  let form: FormGroup;

  beforeEach(async () => {
    form = new FormGroup({
      fullName: new FormControl(''),
      headline: new FormControl(''),
      location: new FormControl(''),
      about: new FormControl('')
    });

    await TestBed.configureTestingModule({
      imports: [BasicInfoComponent, ReactiveFormsModule]
    }).compileComponents();

    fixture = TestBed.createComponent(BasicInfoComponent);
    component = fixture.componentInstance;

    component.form = form; // set required input
    fixture.detectChanges();
  });

  it('should render section title', () => {
    const title = fixture.debugElement.query(By.css('h2')).nativeElement;
    expect(title.textContent).toContain('Basic Information');
  });

  it('should bind form controls to inputs', () => {
    const fullNameInput = fixture.debugElement.query(By.css('#fullName')).nativeElement;
    const headlineInput = fixture.debugElement.query(By.css('#headline')).nativeElement;
    const locationInput = fixture.debugElement.query(By.css('#location')).nativeElement;
    const aboutInput = fixture.debugElement.query(By.css('#about')).nativeElement;

    fullNameInput.value = 'John Doe';
    fullNameInput.dispatchEvent(new Event('input'));

    headlineInput.value = 'Backend Developer';
    headlineInput.dispatchEvent(new Event('input'));

    locationInput.value = 'Eindhoven';
    locationInput.dispatchEvent(new Event('input'));

    aboutInput.value = 'I am a developer.';
    aboutInput.dispatchEvent(new Event('input'));

    fixture.detectChanges();

    expect(form.get('fullName')?.value).toBe('John Doe');
    expect(form.get('headline')?.value).toBe('Backend Developer');
    expect(form.get('location')?.value).toBe('Eindhoven');
    expect(form.get('about')?.value).toBe('I am a developer.');
  });

  it('should populate UI with initial form values', () => {
    form.patchValue({
      fullName: 'Alice',
      headline: 'Frontend Dev',
      location: 'Amsterdam',
      about: 'Loves Angular.'
    });

    fixture.detectChanges();

    const fullNameInput = fixture.debugElement.query(By.css('#fullName')).nativeElement;
    const headlineInput = fixture.debugElement.query(By.css('#headline')).nativeElement;
    const locationInput = fixture.debugElement.query(By.css('#location')).nativeElement;
    const aboutInput = fixture.debugElement.query(By.css('#about')).nativeElement;

    expect(fullNameInput.value).toBe('Alice');
    expect(headlineInput.value).toBe('Frontend Dev');
    expect(locationInput.value).toBe('Amsterdam');
    expect(aboutInput.value).toBe('Loves Angular.');
  });

  it('should throw an error if the required form Input is missing', () => {
    const createWithoutInput = () => {
      const noInputFixture = TestBed.createComponent(BasicInfoComponent);
      noInputFixture.detectChanges(); // should throw
    };

    expect(createWithoutInput).toThrow();
  });
});
