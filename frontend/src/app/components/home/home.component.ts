import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  template: `<h1>Welcome to CareerPath 👋</h1><p>Select an option from the menu.</p>`
})
export class HomeComponent {}
