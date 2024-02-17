import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAyahs, NewAyahs } from '../ayahs.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAyahs for edit and NewAyahsFormGroupInput for create.
 */
type AyahsFormGroupInput = IAyahs | PartialWithRequiredKeyOf<NewAyahs>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAyahs | NewAyahs> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type AyahsFormRawValue = FormValueOf<IAyahs>;

type NewAyahsFormRawValue = FormValueOf<NewAyahs>;

type AyahsFormDefaults = Pick<NewAyahs, 'id' | 'sajda' | 'createdAt' | 'updatedAt'>;

type AyahsFormGroupContent = {
  id: FormControl<AyahsFormRawValue['id'] | NewAyahs['id']>;
  number: FormControl<AyahsFormRawValue['number']>;
  textdesc: FormControl<AyahsFormRawValue['textdesc']>;
  numberInSurah: FormControl<AyahsFormRawValue['numberInSurah']>;
  page: FormControl<AyahsFormRawValue['page']>;
  surahId: FormControl<AyahsFormRawValue['surahId']>;
  hizbId: FormControl<AyahsFormRawValue['hizbId']>;
  juzId: FormControl<AyahsFormRawValue['juzId']>;
  sajda: FormControl<AyahsFormRawValue['sajda']>;
  createdAt: FormControl<AyahsFormRawValue['createdAt']>;
  updatedAt: FormControl<AyahsFormRawValue['updatedAt']>;
};

export type AyahsFormGroup = FormGroup<AyahsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AyahsFormService {
  createAyahsFormGroup(ayahs: AyahsFormGroupInput = { id: null }): AyahsFormGroup {
    const ayahsRawValue = this.convertAyahsToAyahsRawValue({
      ...this.getFormDefaults(),
      ...ayahs,
    });
    return new FormGroup<AyahsFormGroupContent>({
      id: new FormControl(
        { value: ayahsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      number: new FormControl(ayahsRawValue.number),
      textdesc: new FormControl(ayahsRawValue.textdesc, {
        validators: [Validators.required],
      }),
      numberInSurah: new FormControl(ayahsRawValue.numberInSurah),
      page: new FormControl(ayahsRawValue.page),
      surahId: new FormControl(ayahsRawValue.surahId),
      hizbId: new FormControl(ayahsRawValue.hizbId),
      juzId: new FormControl(ayahsRawValue.juzId),
      sajda: new FormControl(ayahsRawValue.sajda),
      createdAt: new FormControl(ayahsRawValue.createdAt),
      updatedAt: new FormControl(ayahsRawValue.updatedAt),
    });
  }

  getAyahs(form: AyahsFormGroup): IAyahs | NewAyahs {
    return this.convertAyahsRawValueToAyahs(form.getRawValue() as AyahsFormRawValue | NewAyahsFormRawValue);
  }

  resetForm(form: AyahsFormGroup, ayahs: AyahsFormGroupInput): void {
    const ayahsRawValue = this.convertAyahsToAyahsRawValue({ ...this.getFormDefaults(), ...ayahs });
    form.reset(
      {
        ...ayahsRawValue,
        id: { value: ayahsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AyahsFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      sajda: false,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertAyahsRawValueToAyahs(rawAyahs: AyahsFormRawValue | NewAyahsFormRawValue): IAyahs | NewAyahs {
    return {
      ...rawAyahs,
      createdAt: dayjs(rawAyahs.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawAyahs.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertAyahsToAyahsRawValue(
    ayahs: IAyahs | (Partial<NewAyahs> & AyahsFormDefaults),
  ): AyahsFormRawValue | PartialWithRequiredKeyOf<NewAyahsFormRawValue> {
    return {
      ...ayahs,
      createdAt: ayahs.createdAt ? ayahs.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: ayahs.updatedAt ? ayahs.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
