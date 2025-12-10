import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-ai-insights-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: 'job-ai-insights-card.component.html',
})
export class AiInsightsCardComponent {
  @Input() aiExplanation: string | null = null;
}
