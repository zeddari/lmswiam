import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IJob, NewJob } from '../job.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IJob for edit and NewJobFormGroupInput for create.
 */
type JobFormGroupInput = IJob | PartialWithRequiredKeyOf<NewJob>;

type JobFormDefaults = Pick<NewJob, 'id'>;

type JobFormGroupContent = {
  id: FormControl<IJob['id'] | NewJob['id']>;
  titleAr: FormControl<IJob['titleAr']>;
  titleLat: FormControl<IJob['titleLat']>;
  description: FormControl<IJob['description']>;
};

export type JobFormGroup = FormGroup<JobFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class JobFormService {
  createJobFormGroup(job: JobFormGroupInput = { id: null }): JobFormGroup {
    const jobRawValue = {
      ...this.getFormDefaults(),
      ...job,
    };
    return new FormGroup<JobFormGroupContent>({
      id: new FormControl(
        { value: jobRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      titleAr: new FormControl(jobRawValue.titleAr, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      titleLat: new FormControl(jobRawValue.titleLat, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      description: new FormControl(jobRawValue.description),
    });
  }

  getJob(form: JobFormGroup): IJob | NewJob {
    return form.getRawValue() as IJob | NewJob;
  }

  resetForm(form: JobFormGroup, job: JobFormGroupInput): void {
    const jobRawValue = { ...this.getFormDefaults(), ...job };
    form.reset(
      {
        ...jobRawValue,
        id: { value: jobRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): JobFormDefaults {
    return {
      id: null,
    };
  }
}
