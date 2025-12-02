import { FormControl, FormGroup } from '@angular/forms';

export type BasicInfoFormModel = FormGroup<{
  fullName: FormControl<string | null>;
  headline: FormControl<string | null>;
  location: FormControl<string | null>;
  about: FormControl<string | null>;
}>;
