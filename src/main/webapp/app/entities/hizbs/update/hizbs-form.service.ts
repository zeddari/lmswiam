import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IHizbs, NewHizbs } from '../hizbs.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHizbs for edit and NewHizbsFormGroupInput for create.
 */
type HizbsFormGroupInput = IHizbs | PartialWithRequiredKeyOf<NewHizbs>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IHizbs | NewHizbs> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type HizbsFormRawValue = FormValueOf<IHizbs>;

type NewHizbsFormRawValue = FormValueOf<NewHizbs>;

type HizbsFormDefaults = Pick<NewHizbs, 'id' | 'createdAt' | 'updatedAt'>;

type HizbsFormGroupContent = {
  id: FormControl<HizbsFormRawValue['id'] | NewHizbs['id']>;
  createdAt: FormControl<HizbsFormRawValue['createdAt']>;
  updatedAt: FormControl<HizbsFormRawValue['updatedAt']>;
};

export type HizbsFormGroup = FormGroup<HizbsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HizbsFormService {
  createHizbsFormGroup(hizbs: HizbsFormGroupInput = { id: null }): HizbsFormGroup {
    const hizbsRawValue = this.convertHizbsToHizbsRawValue({
      ...this.getFormDefaults(),
      ...hizbs,
    });
    return new FormGroup<HizbsFormGroupContent>({
      id: new FormControl(
        { value: hizbsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      createdAt: new FormControl(hizbsRawValue.createdAt),
      updatedAt: new FormControl(hizbsRawValue.updatedAt),
    });
  }

  getHizbs(form: HizbsFormGroup): IHizbs | NewHizbs {
    return this.convertHizbsRawValueToHizbs(form.getRawValue() as HizbsFormRawValue | NewHizbsFormRawValue);
  }

  resetForm(form: HizbsFormGroup, hizbs: HizbsFormGroupInput): void {
    const hizbsRawValue = this.convertHizbsToHizbsRawValue({ ...this.getFormDefaults(), ...hizbs });
    form.reset(
      {
        ...hizbsRawValue,
        id: { value: hizbsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): HizbsFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertHizbsRawValueToHizbs(rawHizbs: HizbsFormRawValue | NewHizbsFormRawValue): IHizbs | NewHizbs {
    return {
      ...rawHizbs,
      createdAt: dayjs(rawHizbs.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawHizbs.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertHizbsToHizbsRawValue(
    hizbs: IHizbs | (Partial<NewHizbs> & HizbsFormDefaults),
  ): HizbsFormRawValue | PartialWithRequiredKeyOf<NewHizbsFormRawValue> {
    return {
      ...hizbs,
      createdAt: hizbs.createdAt ? hizbs.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: hizbs.updatedAt ? hizbs.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
