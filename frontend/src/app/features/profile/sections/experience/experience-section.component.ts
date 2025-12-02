import { Component, Input } from '@angular/core';
import { FormArray, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-experience-section',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './experience-section.component.html'
})
export class ExperienceSectionComponent {
  @Input({ required: true }) form!: FormGroup;
  @Input({ required: true }) createExperienceFn!: () => FormGroup;

  get experiences(): FormArray {
    return this.form.get('experiences') as FormArray;
  }
}
