import {FormArray, FormControl, FormGroup} from '@angular/forms';
import {SkillFormModel} from './skill-form.model';
import {ExperienceFormModel} from './experience-form.model';
import {BasicInfoFormModel} from './basic-info-form.model';

export type ProfileFormModel = FormGroup<{
  basicInfo: BasicInfoFormModel;
  aiOptIn: FormControl<boolean | null>;
  skills: FormArray<SkillFormModel>;
  experiences: FormArray<ExperienceFormModel>;
}>;
