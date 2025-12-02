import {Component, OnInit, inject, DestroyRef} from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  ReactiveFormsModule
} from '@angular/forms';

import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ProfileService } from '../../../core/services/profile.service';
import { ProfileDto } from '../../../core/models/profile.dto';
import { ProfileExperience } from '../../../core/models/profile-experience';
import { ProfileSkill } from '../../../core/models/profile-skill';
import {BasicInfoComponent} from '../sections/basic-info/basic-info.component';
import {SkillsSectionComponent} from '../sections/skills/skills-section.component';
import {ExperienceSectionComponent} from '../sections/experience/experience-section.component';
import { ProfileFormModel } from '../../../core/models/forms/profile/profile-form.model';
import { SkillFormModel } from '../../../core/models/forms/profile/skill-form.model';
import { ExperienceFormModel } from '../../../core/models/forms/profile/experience-form.model';
import * as AOS from 'aos';

@Component({
  selector: 'app-profile-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, BasicInfoComponent, SkillsSectionComponent, ExperienceSectionComponent],
  templateUrl: './profile-page.component.html',
})
export class ProfilePageComponent implements OnInit {

  private readonly profileService = inject(ProfileService);
  private readonly fb = inject(FormBuilder);
  private readonly destroyRef = inject(DestroyRef);

  form!: ProfileFormModel;
  loading = true;
  saving = false;

  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile() {
    this.profileService.getMyProfile()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(profile => {
        this.form = this.buildForm(profile);
        this.loading = false;

        setTimeout(() => AOS.refresh(), 50);
      });
  }

  createEmptySkillGroup(): SkillFormModel {
    return this.createSkillGroup(this.EMPTY_SKILL);
  }

  createSkillGroup(skill: ProfileSkill) : SkillFormModel {
    return this.fb.group({
      id: this.fb.control(skill.id),
      name: this.fb.control(skill.name),
      level: this.fb.control(skill.level)
    });
  }

  createEmptyExperienceGroup(): ExperienceFormModel {
    return this.createExperienceGroup(this.EMPTY_EXPERIENCE);
  }

  createExperienceGroup(exp: ProfileExperience): ExperienceFormModel {
    return this.fb.group({
      id: this.fb.control(exp.id),
      company: this.fb.control(exp.company),
      title: this.fb.control(exp.title),
      employmentType: this.fb.control(exp.employmentType),
      location: this.fb.control(exp.location),
      startDate: this.fb.control(exp.startDate),
      endDate: this.fb.control(exp.endDate),
      isCurrent: this.fb.control(exp.isCurrent),
      description: this.fb.control(exp.description)
    });
  }

  save() {
    if(this.saving || this.form.invalid) return;
    this.saving = true;

    const dto: ProfileDto = this.form.getRawValue() as ProfileDto;

    this.profileService.updateMyProfile(dto).subscribe({
      next: () => {
        this.saving = false;
      },
      error: () => {
        this.saving = false;
      }
    });
  }

  private buildForm(profile: ProfileDto): ProfileFormModel {
    return this.fb.group({
      fullName: this.fb.control(profile.fullName),
      headline: this.fb.control(profile.headline),
      about: this.fb.control(profile.about),
      location: this.fb.control(profile.location),
      aiOptIn: this.fb.control(profile.aiOptIn),

      skills: this.fb.array(
        profile.skills.map(s => this.createSkillGroup(s))
      ),

      experiences: this.fb.array(
        profile.experiences.map(e => this.createExperienceGroup(e))
      )
    }) as ProfileFormModel;
  }

  private readonly EMPTY_SKILL: ProfileSkill = {
    id: null,
    name: '',
    level: ''
  };

  private readonly EMPTY_EXPERIENCE: ProfileExperience = {
    id: null,
    company: '',
    title: '',
    employmentType: '',
    location: '',
    startDate: '',
    endDate: '',
    isCurrent: false,
    description: ''
  };
}
