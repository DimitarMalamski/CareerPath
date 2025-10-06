import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { LoginButtonComponent } from '../../login-button/login-button.component';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule, LoginButtonComponent],
  templateUrl: './navbar.component.html'
})

export class NavbarComponent {}
