import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import * as AOS from 'aos';

@Component({
  selector: 'app-hero-section',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './hero-section.component.html',
})

export class HeroSectionComponent implements OnInit {
  ngOnInit() {
    AOS.init({
      duration: 800,
      once: true
    });
  }
}
