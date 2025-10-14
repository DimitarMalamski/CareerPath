import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import * as AOS from 'aos';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet],
  templateUrl: `./app.html`,
})

export class App implements OnInit {
  ngOnInit() {
    AOS.init({
      duration: 700,
      once: true
    });
  }
}
