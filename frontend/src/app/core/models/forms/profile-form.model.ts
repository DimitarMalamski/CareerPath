import {FormArray, FormControl, FormGroup} from '@angular/forms';
import {SkillFormModel} from './skill-form.model';
import {ExperienceFormModel} from './experience-form.model';

export type ProfileFormModel = FormGroup<{
  fullName: FormControl<string | null>;
  headline: FormControl<string | null>;
  about: FormControl<string | null>;
  location: FormControl<string | null>;
  aiOptIn: FormControl<boolean | null>;

  skills: FormArray<SkillFormModel>;
  experiences: FormArray<ExperienceFormModel>;
}>;
