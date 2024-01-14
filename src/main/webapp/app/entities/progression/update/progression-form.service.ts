import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProgression, NewProgression } from '../progression.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProgression for edit and NewProgressionFormGroupInput for create.
 */
type ProgressionFormGroupInput = IProgression | PartialWithRequiredKeyOf<NewProgression>;

type ProgressionFormDefaults = Pick<NewProgression, 'id' | 'lateArrival' | 'earlyDeparture' | 'taskDone'>;

type ProgressionFormGroupContent = {
  id: FormControl<IProgression['id'] | NewProgression['id']>;
  attendance: FormControl<IProgression['attendance']>;
  justifRef: FormControl<IProgression['justifRef']>;
  lateArrival: FormControl<IProgression['lateArrival']>;
  earlyDeparture: FormControl<IProgression['earlyDeparture']>;
  progressionMode: FormControl<IProgression['progressionMode']>;
  examType: FormControl<IProgression['examType']>;
  riwaya: FormControl<IProgression['riwaya']>;
  fromSourate: FormControl<IProgression['fromSourate']>;
  toSourate: FormControl<IProgression['toSourate']>;
  fromAyaNum: FormControl<IProgression['fromAyaNum']>;
  toAyaNum: FormControl<IProgression['toAyaNum']>;
  fromAyaVerset: FormControl<IProgression['fromAyaVerset']>;
  toAyaVerset: FormControl<IProgression['toAyaVerset']>;
  tilawaType: FormControl<IProgression['tilawaType']>;
  taskDone: FormControl<IProgression['taskDone']>;
  tajweedScore: FormControl<IProgression['tajweedScore']>;
  hifdScore: FormControl<IProgression['hifdScore']>;
  adaeScore: FormControl<IProgression['adaeScore']>;
  observation: FormControl<IProgression['observation']>;
  sessionInstance: FormControl<IProgression['sessionInstance']>;
  student: FormControl<IProgression['student']>;
};

export type ProgressionFormGroup = FormGroup<ProgressionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProgressionFormService {
  createProgressionFormGroup(progression: ProgressionFormGroupInput = { id: null }): ProgressionFormGroup {
    const progressionRawValue = {
      ...this.getFormDefaults(),
      ...progression,
    };
    return new FormGroup<ProgressionFormGroupContent>({
      id: new FormControl(
        { value: progressionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      attendance: new FormControl(progressionRawValue.attendance, {
        validators: [Validators.required],
      }),
      justifRef: new FormControl(progressionRawValue.justifRef),
      lateArrival: new FormControl(progressionRawValue.lateArrival, {
        validators: [Validators.required],
      }),
      earlyDeparture: new FormControl(progressionRawValue.earlyDeparture, {
        validators: [Validators.required],
      }),
      progressionMode: new FormControl(progressionRawValue.progressionMode),
      examType: new FormControl(progressionRawValue.examType),
      riwaya: new FormControl(progressionRawValue.riwaya),
      fromSourate: new FormControl(progressionRawValue.fromSourate),
      toSourate: new FormControl(progressionRawValue.toSourate),
      fromAyaNum: new FormControl(progressionRawValue.fromAyaNum),
      toAyaNum: new FormControl(progressionRawValue.toAyaNum),
      fromAyaVerset: new FormControl(progressionRawValue.fromAyaVerset),
      toAyaVerset: new FormControl(progressionRawValue.toAyaVerset),
      tilawaType: new FormControl(progressionRawValue.tilawaType),
      taskDone: new FormControl(progressionRawValue.taskDone, {
        validators: [Validators.required],
      }),
      tajweedScore: new FormControl(progressionRawValue.tajweedScore, {
        validators: [Validators.required, Validators.min(1), Validators.max(5)],
      }),
      hifdScore: new FormControl(progressionRawValue.hifdScore, {
        validators: [Validators.required, Validators.min(1), Validators.max(5)],
      }),
      adaeScore: new FormControl(progressionRawValue.adaeScore, {
        validators: [Validators.required, Validators.min(1), Validators.max(5)],
      }),
      observation: new FormControl(progressionRawValue.observation),
      sessionInstance: new FormControl(progressionRawValue.sessionInstance),
      student: new FormControl(progressionRawValue.student),
    });
  }

  getProgression(form: ProgressionFormGroup): IProgression | NewProgression {
    return form.getRawValue() as IProgression | NewProgression;
  }

  resetForm(form: ProgressionFormGroup, progression: ProgressionFormGroupInput): void {
    const progressionRawValue = { ...this.getFormDefaults(), ...progression };
    form.reset(
      {
        ...progressionRawValue,
        id: { value: progressionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProgressionFormDefaults {
    return {
      id: null,
      lateArrival: false,
      earlyDeparture: false,
      taskDone: false,
    };
  }
}
