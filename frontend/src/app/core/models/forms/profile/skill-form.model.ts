import {FormControl, FormGroup} from '@angular/forms';

export type SkillFormModel = FormGroup<{
  id: FormControl<number | null>;
  name: FormControl<string | null>;
  level: FormControl<string | null>;
}>;
