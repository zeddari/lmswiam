import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IStudentSponsoring, NewStudentSponsoring } from '../student-sponsoring.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStudentSponsoring for edit and NewStudentSponsoringFormGroupInput for create.
 */
type StudentSponsoringFormGroupInput = IStudentSponsoring | PartialWithRequiredKeyOf<NewStudentSponsoring>;

type StudentSponsoringFormDefaults = Pick<NewStudentSponsoring, 'id' | 'isAlways'>;

type StudentSponsoringFormGroupContent = {
  id: FormControl<IStudentSponsoring['id'] | NewStudentSponsoring['id']>;
  ref: FormControl<IStudentSponsoring['ref']>;
  message: FormControl<IStudentSponsoring['message']>;
  amount: FormControl<IStudentSponsoring['amount']>;
  startDate: FormControl<IStudentSponsoring['startDate']>;
  endDate: FormControl<IStudentSponsoring['endDate']>;
  isAlways: FormControl<IStudentSponsoring['isAlways']>;
};

export type StudentSponsoringFormGroup = FormGroup<StudentSponsoringFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StudentSponsoringFormService {
  createStudentSponsoringFormGroup(studentSponsoring: StudentSponsoringFormGroupInput = { id: null }): StudentSponsoringFormGroup {
    const studentSponsoringRawValue = {
      ...this.getFormDefaults(),
      ...studentSponsoring,
    };
    return new FormGroup<StudentSponsoringFormGroupContent>({
      id: new FormControl(
        { value: studentSponsoringRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      ref: new FormControl(studentSponsoringRawValue.ref, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      message: new FormControl(studentSponsoringRawValue.message),
      amount: new FormControl(studentSponsoringRawValue.amount, {
        validators: [Validators.required, Validators.min(0)],
      }),
      startDate: new FormControl(studentSponsoringRawValue.startDate),
      endDate: new FormControl(studentSponsoringRawValue.endDate),
      isAlways: new FormControl(studentSponsoringRawValue.isAlways),
    });
  }

  getStudentSponsoring(form: StudentSponsoringFormGroup): IStudentSponsoring | NewStudentSponsoring {
    return form.getRawValue() as IStudentSponsoring | NewStudentSponsoring;
  }

  resetForm(form: StudentSponsoringFormGroup, studentSponsoring: StudentSponsoringFormGroupInput): void {
    const studentSponsoringRawValue = { ...this.getFormDefaults(), ...studentSponsoring };
    form.reset(
      {
        ...studentSponsoringRawValue,
        id: { value: studentSponsoringRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): StudentSponsoringFormDefaults {
    return {
      id: null,
      isAlways: false,
    };
  }
}
