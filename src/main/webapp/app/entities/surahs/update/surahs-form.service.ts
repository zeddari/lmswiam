import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISurahs, NewSurahs } from '../surahs.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISurahs for edit and NewSurahsFormGroupInput for create.
 */
type SurahsFormGroupInput = ISurahs | PartialWithRequiredKeyOf<NewSurahs>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISurahs | NewSurahs> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type SurahsFormRawValue = FormValueOf<ISurahs>;

type NewSurahsFormRawValue = FormValueOf<NewSurahs>;

type SurahsFormDefaults = Pick<NewSurahs, 'id' | 'createdAt' | 'updatedAt'>;

type SurahsFormGroupContent = {
  id: FormControl<SurahsFormRawValue['id'] | NewSurahs['id']>;
  number: FormControl<SurahsFormRawValue['number']>;
  nameAr: FormControl<SurahsFormRawValue['nameAr']>;
  nameEn: FormControl<SurahsFormRawValue['nameEn']>;
  nameEnTranslation: FormControl<SurahsFormRawValue['nameEnTranslation']>;
  type: FormControl<SurahsFormRawValue['type']>;
  createdAt: FormControl<SurahsFormRawValue['createdAt']>;
  updatedAt: FormControl<SurahsFormRawValue['updatedAt']>;
};

export type SurahsFormGroup = FormGroup<SurahsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SurahsFormService {
  createSurahsFormGroup(surahs: SurahsFormGroupInput = { id: null }): SurahsFormGroup {
    const surahsRawValue = this.convertSurahsToSurahsRawValue({
      ...this.getFormDefaults(),
      ...surahs,
    });
    return new FormGroup<SurahsFormGroupContent>({
      id: new FormControl(
        { value: surahsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      number: new FormControl(surahsRawValue.number),
      nameAr: new FormControl(surahsRawValue.nameAr),
      nameEn: new FormControl(surahsRawValue.nameEn),
      nameEnTranslation: new FormControl(surahsRawValue.nameEnTranslation),
      type: new FormControl(surahsRawValue.type),
      createdAt: new FormControl(surahsRawValue.createdAt),
      updatedAt: new FormControl(surahsRawValue.updatedAt),
    });
  }

  getSurahs(form: SurahsFormGroup): ISurahs | NewSurahs {
    return this.convertSurahsRawValueToSurahs(form.getRawValue() as SurahsFormRawValue | NewSurahsFormRawValue);
  }

  resetForm(form: SurahsFormGroup, surahs: SurahsFormGroupInput): void {
    const surahsRawValue = this.convertSurahsToSurahsRawValue({ ...this.getFormDefaults(), ...surahs });
    form.reset(
      {
        ...surahsRawValue,
        id: { value: surahsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SurahsFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertSurahsRawValueToSurahs(rawSurahs: SurahsFormRawValue | NewSurahsFormRawValue): ISurahs | NewSurahs {
    return {
      ...rawSurahs,
      createdAt: dayjs(rawSurahs.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawSurahs.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertSurahsToSurahsRawValue(
    surahs: ISurahs | (Partial<NewSurahs> & SurahsFormDefaults),
  ): SurahsFormRawValue | PartialWithRequiredKeyOf<NewSurahsFormRawValue> {
    return {
      ...surahs,
      createdAt: surahs.createdAt ? surahs.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: surahs.updatedAt ? surahs.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
