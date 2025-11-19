import { Component, Input, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { JobRecommendation } from '../../../core/models/job-recommendation';
import {JobScoreComponent} from './job-score/job-score.component';

@Component({
  selector: 'app-job-card',
  standalone: true,
  imports: [CommonModule, JobScoreComponent],
  templateUrl: 'job-card.component.html',
})

export class JobCardComponent {
  @Input() job!: JobRecommendation;

  isPopupOpen = false;

  togglePopup() {
    this.isPopupOpen = !this.isPopupOpen;
  }

  closePopup() {
    this.isPopupOpen = false;
  }

  @HostListener('document:click', ['$event'])
  onClick(event: Event) {
    const target = event.target as HTMLElement;
    if (!target.closest('.ai-popup') && !target.closest('.ai-info-btn')) {
      this.isPopupOpen = false;
    }
  }
}
