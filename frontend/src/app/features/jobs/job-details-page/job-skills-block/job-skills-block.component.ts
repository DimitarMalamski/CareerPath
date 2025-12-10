import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-job-skills-block',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './job-skills-block.component.html'
})
export class JobSkillsBlockComponent {
  @Input() skills: string[] | null | undefined = [];
}
