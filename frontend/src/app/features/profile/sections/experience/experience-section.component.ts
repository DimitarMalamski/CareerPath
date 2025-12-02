import { Component, Input } from '@angular/core';
import { FormArray, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import {ExperienceFormModel} from '../../../../core/models/forms/profile/experience-form.model';

@Component({
  selector: 'app-experience-section',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './experience-section.component.html'
})
export class ExperienceSectionComponent {
  @Input({ required: true }) form!: FormArray<ExperienceFormModel>;
  @Input({ required: true }) createExperienceFn!: () => ExperienceFormModel;

  get experiences(): FormArray<ExperienceFormModel> {
    return this.form;
  }
}
