import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  FormArray,
  ReactiveFormsModule
} from '@angular/forms';

import { ProfileService } from '../../../core/services/profile.service';
import { ProfileDto } from '../../../core/models/profile.dto';
import { ProfileExperience } from '../../../core/models/profile-experience';
import { ProfileSkill } from '../../../core/models/profile-skill';

@Component({
  selector: 'app-profile-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profile-page.component.html',
})
export class ProfilePageComponent implements OnInit {

  private readonly profileService = inject(ProfileService);
  private readonly fb = inject(FormBuilder);

  form!: FormGroup;
  loading = true;

  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile() {
    this.profileService.getMyProfile().subscribe(profile => {
      this.form = this.fb.group({
        fullName: [profile.fullName],
        headline: [profile.headline],
        about: [profile.about],
        location: [profile.location],
        aiOptIn: [profile.aiOptIn],

        skills: this.fb.array(
          profile.skills.map(s => this.createSkillGroup(s))
        ),

        experiences: this.fb.array(
          profile.experiences.map(e => this.createExperienceGroup(e))
        )
      });

      this.loading = false;
    });
  }

  createSkillGroup(skill: ProfileSkill) {
    return this.fb.group({
      id: [skill.id],
      name: [skill.name],
      level: [skill.level]
    });
  }

  createExperienceGroup(exp: ProfileExperience) {
    return this.fb.group({
      id: [exp.id],
      company: [exp.company],
      title: [exp.title],
      employmentType: [exp.employmentType],
      location: [exp.location],
      startDate: [exp.startDate],
      endDate: [exp.endDate],
      isCurrent: [exp.isCurrent],
      description: [exp.description]
    });
  }

  get skills() {
    return this.form.get('skills') as FormArray;
  }

  get experiences() {
    return this.form.get('experiences') as FormArray;
  }

  addSkill() {
    this.skills.push(this.createSkillGroup({
      id: null,
      name: '',
      level: ''
    }));
  }

  removeSkill(index: number) {
    this.skills.removeAt(index);
  }

  addExperience() {
    this.experiences.push(this.createExperienceGroup({
      id: null,
      company: '',
      title: '',
      employmentType: '',
      location: '',
      startDate: '',
      endDate: '',
      isCurrent: false,
      description: ''
    }));
  }

  removeExperience(index: number) {
    this.experiences.removeAt(index);
  }

  save() {
    const dto: ProfileDto = this.form.value;

    this.profileService.updateMyProfile(dto).subscribe({
      next: (updated) => {
        console.log("Profile saved:", updated);
      },
      error: (err) => {
        console.error("Save error:", err);
      }
    });
  }
}
