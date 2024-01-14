import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEditions, NewEditions } from '../editions.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEditions for edit and NewEditionsFormGroupInput for create.
 */
type EditionsFormGroupInput = IEditions | PartialWithRequiredKeyOf<NewEditions>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEditions | NewEditions> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type EditionsFormRawValue = FormValueOf<IEditions>;

type NewEditionsFormRawValue = FormValueOf<NewEditions>;

type EditionsFormDefaults = Pick<NewEditions, 'id' | 'createdAt' | 'updatedAt'>;

type EditionsFormGroupContent = {
  id: FormControl<EditionsFormRawValue['id'] | NewEditions['id']>;
  identifier: FormControl<EditionsFormRawValue['identifier']>;
  language: FormControl<EditionsFormRawValue['language']>;
  name: FormControl<EditionsFormRawValue['name']>;
  englishName: FormControl<EditionsFormRawValue['englishName']>;
  format: FormControl<EditionsFormRawValue['format']>;
  type: FormControl<EditionsFormRawValue['type']>;
  createdAt: FormControl<EditionsFormRawValue['createdAt']>;
  updatedAt: FormControl<EditionsFormRawValue['updatedAt']>;
};

export type EditionsFormGroup = FormGroup<EditionsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EditionsFormService {
  createEditionsFormGroup(editions: EditionsFormGroupInput = { id: null }): EditionsFormGroup {
    const editionsRawValue = this.convertEditionsToEditionsRawValue({
      ...this.getFormDefaults(),
      ...editions,
    });
    return new FormGroup<EditionsFormGroupContent>({
      id: new FormControl(
        { value: editionsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      identifier: new FormControl(editionsRawValue.identifier),
      language: new FormControl(editionsRawValue.language),
      name: new FormControl(editionsRawValue.name),
      englishName: new FormControl(editionsRawValue.englishName),
      format: new FormControl(editionsRawValue.format),
      type: new FormControl(editionsRawValue.type),
      createdAt: new FormControl(editionsRawValue.createdAt),
      updatedAt: new FormControl(editionsRawValue.updatedAt),
    });
  }

  getEditions(form: EditionsFormGroup): IEditions | NewEditions {
    return this.convertEditionsRawValueToEditions(form.getRawValue() as EditionsFormRawValue | NewEditionsFormRawValue);
  }

  resetForm(form: EditionsFormGroup, editions: EditionsFormGroupInput): void {
    const editionsRawValue = this.convertEditionsToEditionsRawValue({ ...this.getFormDefaults(), ...editions });
    form.reset(
      {
        ...editionsRawValue,
        id: { value: editionsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EditionsFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertEditionsRawValueToEditions(rawEditions: EditionsFormRawValue | NewEditionsFormRawValue): IEditions | NewEditions {
    return {
      ...rawEditions,
      createdAt: dayjs(rawEditions.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawEditions.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertEditionsToEditionsRawValue(
    editions: IEditions | (Partial<NewEditions> & EditionsFormDefaults),
  ): EditionsFormRawValue | PartialWithRequiredKeyOf<NewEditionsFormRawValue> {
    return {
      ...editions,
      createdAt: editions.createdAt ? editions.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: editions.updatedAt ? editions.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
