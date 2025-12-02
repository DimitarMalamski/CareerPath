import {FormControl, FormGroup} from '@angular/forms';

export type ExperienceFormModel = FormGroup<{
  id: FormControl<number | null>;
  company: FormControl<string | null>;
  title: FormControl<string | null>;
  employmentType: FormControl<string | null>;
  location: FormControl<string | null>;
  startDate: FormControl<string | null>;
  endDate: FormControl<string | null>;
  isCurrent: FormControl<boolean | null>;
  description: FormControl<string | null>;
}>;
