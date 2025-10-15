import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeroSectionComponent } from './hero-section/hero-section.component';
import { HowItWorksSectionComponent } from './how-it-works-section/how-it-works-section.component';
import { CarouselSectionComponent } from './carousel-section/carousel-section.component';
import { CallToActionSectionComponent } from './call-to-action-section/call-to-action-section.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    HeroSectionComponent,
    HowItWorksSectionComponent,
    CarouselSectionComponent,
    CallToActionSectionComponent
  ],
  templateUrl: './home.component.html'
})
export class HomeComponent {}
