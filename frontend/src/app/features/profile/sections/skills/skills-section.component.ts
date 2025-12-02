import { Component, Input } from '@angular/core';
import { FormArray, ReactiveFormsModule} from '@angular/forms';
import { CommonModule } from '@angular/common';
import {SkillFormModel} from '../../../../core/models/forms/profile/skill-form.model';

@Component({
  selector: 'app-skills-section',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './skills-section.component.html'
})
export class SkillsSectionComponent {
  @Input({ required: true }) form!: FormArray<SkillFormModel>;
  @Input({ required: true }) createSkillFn!: () => SkillFormModel;

  get skills(): FormArray<SkillFormModel> {
    return this.form;
  }
}
