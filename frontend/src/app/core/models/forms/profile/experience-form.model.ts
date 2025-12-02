import {FormControl, FormGroup} from '@angular/forms';

export type ExperienceFormModel = FormGroup<{
  id: FormControl<number | null>;
  company: FormControl<string>;
  title: FormControl<string>;
  employmentType: FormControl<string>;
  location: FormControl<string>;
  startDate: FormControl<string>;
  endDate: FormControl<string | null>;
  isCurrent: FormControl<boolean>;
  description: FormControl<string>;
}>;
