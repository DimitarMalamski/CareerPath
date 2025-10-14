import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeroSectionComponent } from './hero-section/hero-section.component';
import { SectionColorDirective } from '../../shared/section-color.directive';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, HeroSectionComponent, SectionColorDirective],
  templateUrl: './home.component.html'
})
export class HomeComponent {}
