import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-job-skills',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './job-skills.component.html',
})
export class JobSkillsComponent {
  @Input() skills: string[] = [];
}
