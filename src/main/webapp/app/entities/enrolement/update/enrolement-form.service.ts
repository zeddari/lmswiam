import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEnrolement, NewEnrolement } from '../enrolement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEnrolement for edit and NewEnrolementFormGroupInput for create.
 */
type EnrolementFormGroupInput = IEnrolement | PartialWithRequiredKeyOf<NewEnrolement>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEnrolement | NewEnrolement> = Omit<T, 'enrolmentStartTime' | 'enrolemntEndTime' | 'activatedAt'> & {
  enrolmentStartTime?: string | null;
  enrolemntEndTime?: string | null;
  activatedAt?: string | null;
};

type EnrolementFormRawValue = FormValueOf<IEnrolement>;

type NewEnrolementFormRawValue = FormValueOf<NewEnrolement>;

type EnrolementFormDefaults = Pick<NewEnrolement, 'id' | 'enrolmentStartTime' | 'enrolemntEndTime' | 'isActive' | 'activatedAt'>;

type EnrolementFormGroupContent = {
  id: FormControl<EnrolementFormRawValue['id'] | NewEnrolement['id']>;
  ref: FormControl<EnrolementFormRawValue['ref']>;
  enrolementType: FormControl<EnrolementFormRawValue['enrolementType']>;
  enrolmentStartTime: FormControl<EnrolementFormRawValue['enrolmentStartTime']>;
  enrolemntEndTime: FormControl<EnrolementFormRawValue['enrolemntEndTime']>;
  isActive: FormControl<EnrolementFormRawValue['isActive']>;
  activatedAt: FormControl<EnrolementFormRawValue['activatedAt']>;
  userCustom4: FormControl<EnrolementFormRawValue['userCustom4']>;
  course: FormControl<EnrolementFormRawValue['course']>;
};

export type EnrolementFormGroup = FormGroup<EnrolementFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EnrolementFormService {
  createEnrolementFormGroup(enrolement: EnrolementFormGroupInput = { id: null }): EnrolementFormGroup {
    const enrolementRawValue = this.convertEnrolementToEnrolementRawValue({
      ...this.getFormDefaults(),
      ...enrolement,
    });
    return new FormGroup<EnrolementFormGroupContent>({
      id: new FormControl(
        { value: enrolementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      ref: new FormControl(enrolementRawValue.ref, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      enrolementType: new FormControl(enrolementRawValue.enrolementType),
      enrolmentStartTime: new FormControl(enrolementRawValue.enrolmentStartTime, {
        validators: [Validators.required],
      }),
      enrolemntEndTime: new FormControl(enrolementRawValue.enrolemntEndTime, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(enrolementRawValue.isActive, {
        validators: [Validators.required],
      }),
      activatedAt: new FormControl(enrolementRawValue.activatedAt),
      userCustom4: new FormControl(enrolementRawValue.userCustom4),
      course: new FormControl(enrolementRawValue.course),
    });
  }

  getEnrolement(form: EnrolementFormGroup): IEnrolement | NewEnrolement {
    return this.convertEnrolementRawValueToEnrolement(form.getRawValue() as EnrolementFormRawValue | NewEnrolementFormRawValue);
  }

  resetForm(form: EnrolementFormGroup, enrolement: EnrolementFormGroupInput): void {
    const enrolementRawValue = this.convertEnrolementToEnrolementRawValue({ ...this.getFormDefaults(), ...enrolement });
    form.reset(
      {
        ...enrolementRawValue,
        id: { value: enrolementRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EnrolementFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      enrolmentStartTime: currentTime,
      enrolemntEndTime: currentTime,
      isActive: false,
      activatedAt: currentTime,
    };
  }

  private convertEnrolementRawValueToEnrolement(
    rawEnrolement: EnrolementFormRawValue | NewEnrolementFormRawValue,
  ): IEnrolement | NewEnrolement {
    return {
      ...rawEnrolement,
      enrolmentStartTime: dayjs(rawEnrolement.enrolmentStartTime, DATE_TIME_FORMAT),
      enrolemntEndTime: dayjs(rawEnrolement.enrolemntEndTime, DATE_TIME_FORMAT),
      activatedAt: dayjs(rawEnrolement.activatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertEnrolementToEnrolementRawValue(
    enrolement: IEnrolement | (Partial<NewEnrolement> & EnrolementFormDefaults),
  ): EnrolementFormRawValue | PartialWithRequiredKeyOf<NewEnrolementFormRawValue> {
    return {
      ...enrolement,
      enrolmentStartTime: enrolement.enrolmentStartTime ? enrolement.enrolmentStartTime.format(DATE_TIME_FORMAT) : undefined,
      enrolemntEndTime: enrolement.enrolemntEndTime ? enrolement.enrolemntEndTime.format(DATE_TIME_FORMAT) : undefined,
      activatedAt: enrolement.activatedAt ? enrolement.activatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
