import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeroSectionComponent } from './hero-section/hero-section.component';
import { HowItWorksSectionComponent } from './how-it-works-section/how-it-works-section.component';
import { CarouselSectionComponent } from './carousel-section/carousel-section.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    HeroSectionComponent,
    HowItWorksSectionComponent,
    CarouselSectionComponent
  ],
  templateUrl: './home.component.html'
})
export class HomeComponent {}
