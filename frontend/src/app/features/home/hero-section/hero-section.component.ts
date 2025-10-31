import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import * as AOS from 'aos';

@Component({
  selector: 'app-hero-section',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
  ],
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
