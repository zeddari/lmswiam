import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IJuzs, NewJuzs } from '../juzs.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IJuzs for edit and NewJuzsFormGroupInput for create.
 */
type JuzsFormGroupInput = IJuzs | PartialWithRequiredKeyOf<NewJuzs>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IJuzs | NewJuzs> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type JuzsFormRawValue = FormValueOf<IJuzs>;

type NewJuzsFormRawValue = FormValueOf<NewJuzs>;

type JuzsFormDefaults = Pick<NewJuzs, 'id' | 'createdAt' | 'updatedAt'>;

type JuzsFormGroupContent = {
  id: FormControl<JuzsFormRawValue['id'] | NewJuzs['id']>;
  createdAt: FormControl<JuzsFormRawValue['createdAt']>;
  updatedAt: FormControl<JuzsFormRawValue['updatedAt']>;
};

export type JuzsFormGroup = FormGroup<JuzsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class JuzsFormService {
  createJuzsFormGroup(juzs: JuzsFormGroupInput = { id: null }): JuzsFormGroup {
    const juzsRawValue = this.convertJuzsToJuzsRawValue({
      ...this.getFormDefaults(),
      ...juzs,
    });
    return new FormGroup<JuzsFormGroupContent>({
      id: new FormControl(
        { value: juzsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      createdAt: new FormControl(juzsRawValue.createdAt),
      updatedAt: new FormControl(juzsRawValue.updatedAt),
    });
  }

  getJuzs(form: JuzsFormGroup): IJuzs | NewJuzs {
    return this.convertJuzsRawValueToJuzs(form.getRawValue() as JuzsFormRawValue | NewJuzsFormRawValue);
  }

  resetForm(form: JuzsFormGroup, juzs: JuzsFormGroupInput): void {
    const juzsRawValue = this.convertJuzsToJuzsRawValue({ ...this.getFormDefaults(), ...juzs });
    form.reset(
      {
        ...juzsRawValue,
        id: { value: juzsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): JuzsFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertJuzsRawValueToJuzs(rawJuzs: JuzsFormRawValue | NewJuzsFormRawValue): IJuzs | NewJuzs {
    return {
      ...rawJuzs,
      createdAt: dayjs(rawJuzs.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawJuzs.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertJuzsToJuzsRawValue(
    juzs: IJuzs | (Partial<NewJuzs> & JuzsFormDefaults),
  ): JuzsFormRawValue | PartialWithRequiredKeyOf<NewJuzsFormRawValue> {
    return {
      ...juzs,
      createdAt: juzs.createdAt ? juzs.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: juzs.updatedAt ? juzs.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
