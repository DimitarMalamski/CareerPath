import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeroSectionComponent } from './hero-section/hero-section.component';
import { HowItWorksSectionComponent } from './how-it-works-section/how-it-works-section.component';
import { BeforeAfterSectionComponent } from './before-after-section/before-after-section.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    HeroSectionComponent,
    HowItWorksSectionComponent,
    BeforeAfterSectionComponent
  ],
  templateUrl: './home.component.html'
})
export class HomeComponent {}
