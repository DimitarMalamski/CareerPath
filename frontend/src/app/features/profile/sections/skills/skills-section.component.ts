import { Component, Input } from '@angular/core';
import { FormArray, FormGroup, ReactiveFormsModule} from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-skills-section',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './skills-section.component.html'
})
export class SkillsSectionComponent {
  @Input({ required: true }) form!: FormGroup;
  @Input({ required: true }) createSkillFn!: () => FormGroup;

  get skills(): FormArray {
    return this.form.get('skills') as FormArray;
  }
}
