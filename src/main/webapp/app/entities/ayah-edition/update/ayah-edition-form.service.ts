import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAyahEdition, NewAyahEdition } from '../ayah-edition.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAyahEdition for edit and NewAyahEditionFormGroupInput for create.
 */
type AyahEditionFormGroupInput = IAyahEdition | PartialWithRequiredKeyOf<NewAyahEdition>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAyahEdition | NewAyahEdition> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type AyahEditionFormRawValue = FormValueOf<IAyahEdition>;

type NewAyahEditionFormRawValue = FormValueOf<NewAyahEdition>;

type AyahEditionFormDefaults = Pick<NewAyahEdition, 'id' | 'isAudio' | 'createdAt' | 'updatedAt'>;

type AyahEditionFormGroupContent = {
  id: FormControl<AyahEditionFormRawValue['id'] | NewAyahEdition['id']>;
  ayahId: FormControl<AyahEditionFormRawValue['ayahId']>;
  editionId: FormControl<AyahEditionFormRawValue['editionId']>;
  data: FormControl<AyahEditionFormRawValue['data']>;
  isAudio: FormControl<AyahEditionFormRawValue['isAudio']>;
  createdAt: FormControl<AyahEditionFormRawValue['createdAt']>;
  updatedAt: FormControl<AyahEditionFormRawValue['updatedAt']>;
};

export type AyahEditionFormGroup = FormGroup<AyahEditionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AyahEditionFormService {
  createAyahEditionFormGroup(ayahEdition: AyahEditionFormGroupInput = { id: null }): AyahEditionFormGroup {
    const ayahEditionRawValue = this.convertAyahEditionToAyahEditionRawValue({
      ...this.getFormDefaults(),
      ...ayahEdition,
    });
    return new FormGroup<AyahEditionFormGroupContent>({
      id: new FormControl(
        { value: ayahEditionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      ayahId: new FormControl(ayahEditionRawValue.ayahId),
      editionId: new FormControl(ayahEditionRawValue.editionId),
      data: new FormControl(ayahEditionRawValue.data, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      isAudio: new FormControl(ayahEditionRawValue.isAudio),
      createdAt: new FormControl(ayahEditionRawValue.createdAt),
      updatedAt: new FormControl(ayahEditionRawValue.updatedAt),
    });
  }

  getAyahEdition(form: AyahEditionFormGroup): IAyahEdition | NewAyahEdition {
    return this.convertAyahEditionRawValueToAyahEdition(form.getRawValue() as AyahEditionFormRawValue | NewAyahEditionFormRawValue);
  }

  resetForm(form: AyahEditionFormGroup, ayahEdition: AyahEditionFormGroupInput): void {
    const ayahEditionRawValue = this.convertAyahEditionToAyahEditionRawValue({ ...this.getFormDefaults(), ...ayahEdition });
    form.reset(
      {
        ...ayahEditionRawValue,
        id: { value: ayahEditionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AyahEditionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isAudio: false,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertAyahEditionRawValueToAyahEdition(
    rawAyahEdition: AyahEditionFormRawValue | NewAyahEditionFormRawValue,
  ): IAyahEdition | NewAyahEdition {
    return {
      ...rawAyahEdition,
      createdAt: dayjs(rawAyahEdition.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawAyahEdition.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertAyahEditionToAyahEditionRawValue(
    ayahEdition: IAyahEdition | (Partial<NewAyahEdition> & AyahEditionFormDefaults),
  ): AyahEditionFormRawValue | PartialWithRequiredKeyOf<NewAyahEditionFormRawValue> {
    return {
      ...ayahEdition,
      createdAt: ayahEdition.createdAt ? ayahEdition.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: ayahEdition.updatedAt ? ayahEdition.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
