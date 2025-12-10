import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-job-description',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './job-description.component.html',
})
export class JobDescriptionComponent {
  @Input() description: string | null = null;
}
