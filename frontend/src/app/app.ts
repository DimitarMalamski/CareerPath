import {Component, inject, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import * as AOS from 'aos';
import { JobWebsocketService } from './core/services/job-websocket.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet],
  templateUrl: `./app.html`,
})

export class App implements OnInit {
  private readonly jobWs = inject(JobWebsocketService);

  ngOnInit() {
    this.jobWs.connect();

    AOS.init({
      duration: 700,
      once: true
    });
  }
}
