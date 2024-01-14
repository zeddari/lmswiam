import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProject, NewProject } from '../project.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProject for edit and NewProjectFormGroupInput for create.
 */
type ProjectFormGroupInput = IProject | PartialWithRequiredKeyOf<NewProject>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProject | NewProject> = Omit<T, 'activateAt'> & {
  activateAt?: string | null;
};

type ProjectFormRawValue = FormValueOf<IProject>;

type NewProjectFormRawValue = FormValueOf<NewProject>;

type ProjectFormDefaults = Pick<NewProject, 'id' | 'isActive' | 'activateAt'>;

type ProjectFormGroupContent = {
  id: FormControl<ProjectFormRawValue['id'] | NewProject['id']>;
  titleAr: FormControl<ProjectFormRawValue['titleAr']>;
  titleLat: FormControl<ProjectFormRawValue['titleLat']>;
  description: FormControl<ProjectFormRawValue['description']>;
  goals: FormControl<ProjectFormRawValue['goals']>;
  requirements: FormControl<ProjectFormRawValue['requirements']>;
  imageLink: FormControl<ProjectFormRawValue['imageLink']>;
  imageLinkContentType: FormControl<ProjectFormRawValue['imageLinkContentType']>;
  videoLink: FormControl<ProjectFormRawValue['videoLink']>;
  budget: FormControl<ProjectFormRawValue['budget']>;
  isActive: FormControl<ProjectFormRawValue['isActive']>;
  activateAt: FormControl<ProjectFormRawValue['activateAt']>;
  startDate: FormControl<ProjectFormRawValue['startDate']>;
  endDate: FormControl<ProjectFormRawValue['endDate']>;
  typeProject: FormControl<ProjectFormRawValue['typeProject']>;
};

export type ProjectFormGroup = FormGroup<ProjectFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProjectFormService {
  createProjectFormGroup(project: ProjectFormGroupInput = { id: null }): ProjectFormGroup {
    const projectRawValue = this.convertProjectToProjectRawValue({
      ...this.getFormDefaults(),
      ...project,
    });
    return new FormGroup<ProjectFormGroupContent>({
      id: new FormControl(
        { value: projectRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      titleAr: new FormControl(projectRawValue.titleAr, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      titleLat: new FormControl(projectRawValue.titleLat, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      description: new FormControl(projectRawValue.description),
      goals: new FormControl(projectRawValue.goals),
      requirements: new FormControl(projectRawValue.requirements),
      imageLink: new FormControl(projectRawValue.imageLink),
      imageLinkContentType: new FormControl(projectRawValue.imageLinkContentType),
      videoLink: new FormControl(projectRawValue.videoLink),
      budget: new FormControl(projectRawValue.budget, {
        validators: [Validators.required, Validators.min(0)],
      }),
      isActive: new FormControl(projectRawValue.isActive),
      activateAt: new FormControl(projectRawValue.activateAt),
      startDate: new FormControl(projectRawValue.startDate),
      endDate: new FormControl(projectRawValue.endDate),
      typeProject: new FormControl(projectRawValue.typeProject),
    });
  }

  getProject(form: ProjectFormGroup): IProject | NewProject {
    return this.convertProjectRawValueToProject(form.getRawValue() as ProjectFormRawValue | NewProjectFormRawValue);
  }

  resetForm(form: ProjectFormGroup, project: ProjectFormGroupInput): void {
    const projectRawValue = this.convertProjectToProjectRawValue({ ...this.getFormDefaults(), ...project });
    form.reset(
      {
        ...projectRawValue,
        id: { value: projectRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProjectFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isActive: false,
      activateAt: currentTime,
    };
  }

  private convertProjectRawValueToProject(rawProject: ProjectFormRawValue | NewProjectFormRawValue): IProject | NewProject {
    return {
      ...rawProject,
      activateAt: dayjs(rawProject.activateAt, DATE_TIME_FORMAT),
    };
  }

  private convertProjectToProjectRawValue(
    project: IProject | (Partial<NewProject> & ProjectFormDefaults),
  ): ProjectFormRawValue | PartialWithRequiredKeyOf<NewProjectFormRawValue> {
    return {
      ...project,
      activateAt: project.activateAt ? project.activateAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
