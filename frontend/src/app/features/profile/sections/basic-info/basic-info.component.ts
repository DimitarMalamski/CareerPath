import { Component, Input } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import {BasicInfoFormModel} from '../../../../core/models/forms/profile/basic-info-form.model';

@Component({
  selector: 'app-basic-info-section',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './basic-info.component.html'
})
export class BasicInfoComponent {
  @Input({ required: true }) form!: BasicInfoFormModel;
}
