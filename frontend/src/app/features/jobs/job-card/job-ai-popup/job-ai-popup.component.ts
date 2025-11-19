import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-job-ai-popup',
  standalone: true,
  imports: [CommonModule],
  templateUrl: 'job-ai-popup.component.html'
})

export class JobAiPopupComponent {
  @Input() explanation!: string;
  @Output() close = new EventEmitter<void>();

  onClose() {
    this.close.emit();
  }
}
