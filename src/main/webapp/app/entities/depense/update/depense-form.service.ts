import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDepense, NewDepense } from '../depense.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDepense for edit and NewDepenseFormGroupInput for create.
 */
type DepenseFormGroupInput = IDepense | PartialWithRequiredKeyOf<NewDepense>;

type DepenseFormDefaults = Pick<NewDepense, 'id'>;

type DepenseFormGroupContent = {
  id: FormControl<IDepense['id'] | NewDepense['id']>;
  type: FormControl<IDepense['type']>;
  target: FormControl<IDepense['target']>;
  frequency: FormControl<IDepense['frequency']>;
  amount: FormControl<IDepense['amount']>;
  ref: FormControl<IDepense['ref']>;
  message: FormControl<IDepense['message']>;
  resource: FormControl<IDepense['resource']>;
};

export type DepenseFormGroup = FormGroup<DepenseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DepenseFormService {
  createDepenseFormGroup(depense: DepenseFormGroupInput = { id: null }): DepenseFormGroup {
    const depenseRawValue = {
      ...this.getFormDefaults(),
      ...depense,
    };
    return new FormGroup<DepenseFormGroupContent>({
      id: new FormControl(
        { value: depenseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(depenseRawValue.type, {
        validators: [Validators.required],
      }),
      target: new FormControl(depenseRawValue.target, {
        validators: [Validators.required],
      }),
      frequency: new FormControl(depenseRawValue.frequency, {
        validators: [Validators.required],
      }),
      amount: new FormControl(depenseRawValue.amount, {
        validators: [Validators.required, Validators.min(0)],
      }),
      ref: new FormControl(depenseRawValue.ref, {
        validators: [Validators.required],
      }),
      message: new FormControl(depenseRawValue.message),
      resource: new FormControl(depenseRawValue.resource),
    });
  }

  getDepense(form: DepenseFormGroup): IDepense | NewDepense {
    return form.getRawValue() as IDepense | NewDepense;
  }

  resetForm(form: DepenseFormGroup, depense: DepenseFormGroupInput): void {
    const depenseRawValue = { ...this.getFormDefaults(), ...depense };
    form.reset(
      {
        ...depenseRawValue,
        id: { value: depenseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DepenseFormDefaults {
    return {
      id: null,
    };
  }
}
