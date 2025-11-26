import { Component, EventEmitter, Input, Output } from '@angular/core';
import {DecimalPipe, NgClass, CommonModule} from '@angular/common';

@Component({
  selector: 'app-job-score',
  standalone: true,
  imports: [
    DecimalPipe,
    NgClass,
    CommonModule
  ],
  templateUrl: 'job-score.component.html'
})

export class JobScoreComponent {
  @Input() score!: number;
  @Input() hasAi = false;

  @Output() infoClick = new EventEmitter<void>();

  onInfoClick(event: MouseEvent) {
    event.stopPropagation();
    this.infoClick.emit();
  }

  get scoreColor(): string {
    if (this.score >= 0.75) return 'text-green-400';
    if (this.score >= 0.4) return 'text-yellow-400';
    return 'text-red-400';
  }
}
