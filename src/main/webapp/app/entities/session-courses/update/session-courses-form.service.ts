import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISessionCourses, NewSessionCourses } from '../session-courses.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISessionCourses for edit and NewSessionCoursesFormGroupInput for create.
 */
type SessionCoursesFormGroupInput = ISessionCourses | PartialWithRequiredKeyOf<NewSessionCourses>;

type SessionCoursesFormDefaults = Pick<NewSessionCourses, 'id'>;

type SessionCoursesFormGroupContent = {
  id: FormControl<ISessionCourses['id'] | NewSessionCourses['id']>;
  title: FormControl<ISessionCourses['title']>;
  description: FormControl<ISessionCourses['description']>;
  resourceLink: FormControl<ISessionCourses['resourceLink']>;
  resourceFile: FormControl<ISessionCourses['resourceFile']>;
  resourceFileContentType: FormControl<ISessionCourses['resourceFileContentType']>;
};

export type SessionCoursesFormGroup = FormGroup<SessionCoursesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SessionCoursesFormService {
  createSessionCoursesFormGroup(sessionCourses: SessionCoursesFormGroupInput = { id: null }): SessionCoursesFormGroup {
    const sessionCoursesRawValue = {
      ...this.getFormDefaults(),
      ...sessionCourses,
    };
    return new FormGroup<SessionCoursesFormGroupContent>({
      id: new FormControl(
        { value: sessionCoursesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(sessionCoursesRawValue.title, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      description: new FormControl(sessionCoursesRawValue.description),
      resourceLink: new FormControl(sessionCoursesRawValue.resourceLink, {
        validators: [Validators.maxLength(500)],
      }),
      resourceFile: new FormControl(sessionCoursesRawValue.resourceFile),
      resourceFileContentType: new FormControl(sessionCoursesRawValue.resourceFileContentType),
    });
  }

  getSessionCourses(form: SessionCoursesFormGroup): ISessionCourses | NewSessionCourses {
    return form.getRawValue() as ISessionCourses | NewSessionCourses;
  }

  resetForm(form: SessionCoursesFormGroup, sessionCourses: SessionCoursesFormGroupInput): void {
    const sessionCoursesRawValue = { ...this.getFormDefaults(), ...sessionCourses };
    form.reset(
      {
        ...sessionCoursesRawValue,
        id: { value: sessionCoursesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SessionCoursesFormDefaults {
    return {
      id: null,
    };
  }
}
