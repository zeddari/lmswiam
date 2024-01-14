import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISessionInstance, NewSessionInstance } from '../session-instance.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISessionInstance for edit and NewSessionInstanceFormGroupInput for create.
 */
type SessionInstanceFormGroupInput = ISessionInstance | PartialWithRequiredKeyOf<NewSessionInstance>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISessionInstance | NewSessionInstance> = Omit<T, 'startTime'> & {
  startTime?: string | null;
};

type SessionInstanceFormRawValue = FormValueOf<ISessionInstance>;

type NewSessionInstanceFormRawValue = FormValueOf<NewSessionInstance>;

type SessionInstanceFormDefaults = Pick<NewSessionInstance, 'id' | 'startTime' | 'isActive' | 'links'>;

type SessionInstanceFormGroupContent = {
  id: FormControl<SessionInstanceFormRawValue['id'] | NewSessionInstance['id']>;
  title: FormControl<SessionInstanceFormRawValue['title']>;
  sessionDate: FormControl<SessionInstanceFormRawValue['sessionDate']>;
  startTime: FormControl<SessionInstanceFormRawValue['startTime']>;
  duration: FormControl<SessionInstanceFormRawValue['duration']>;
  info: FormControl<SessionInstanceFormRawValue['info']>;
  attendance: FormControl<SessionInstanceFormRawValue['attendance']>;
  justifRef: FormControl<SessionInstanceFormRawValue['justifRef']>;
  isActive: FormControl<SessionInstanceFormRawValue['isActive']>;
  links: FormControl<SessionInstanceFormRawValue['links']>;
  session1: FormControl<SessionInstanceFormRawValue['session1']>;
};

export type SessionInstanceFormGroup = FormGroup<SessionInstanceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SessionInstanceFormService {
  createSessionInstanceFormGroup(sessionInstance: SessionInstanceFormGroupInput = { id: null }): SessionInstanceFormGroup {
    const sessionInstanceRawValue = this.convertSessionInstanceToSessionInstanceRawValue({
      ...this.getFormDefaults(),
      ...sessionInstance,
    });
    return new FormGroup<SessionInstanceFormGroupContent>({
      id: new FormControl(
        { value: sessionInstanceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(sessionInstanceRawValue.title, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      sessionDate: new FormControl(sessionInstanceRawValue.sessionDate, {
        validators: [Validators.required],
      }),
      startTime: new FormControl(sessionInstanceRawValue.startTime, {
        validators: [Validators.required],
      }),
      duration: new FormControl(sessionInstanceRawValue.duration, {
        validators: [Validators.required],
      }),
      info: new FormControl(sessionInstanceRawValue.info),
      attendance: new FormControl(sessionInstanceRawValue.attendance, {
        validators: [Validators.required],
      }),
      justifRef: new FormControl(sessionInstanceRawValue.justifRef),
      isActive: new FormControl(sessionInstanceRawValue.isActive, {
        validators: [Validators.required],
      }),
      links: new FormControl(sessionInstanceRawValue.links ?? []),
      session1: new FormControl(sessionInstanceRawValue.session1),
    });
  }

  getSessionInstance(form: SessionInstanceFormGroup): ISessionInstance | NewSessionInstance {
    return this.convertSessionInstanceRawValueToSessionInstance(
      form.getRawValue() as SessionInstanceFormRawValue | NewSessionInstanceFormRawValue,
    );
  }

  resetForm(form: SessionInstanceFormGroup, sessionInstance: SessionInstanceFormGroupInput): void {
    const sessionInstanceRawValue = this.convertSessionInstanceToSessionInstanceRawValue({ ...this.getFormDefaults(), ...sessionInstance });
    form.reset(
      {
        ...sessionInstanceRawValue,
        id: { value: sessionInstanceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SessionInstanceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startTime: currentTime,
      isActive: false,
      links: [],
    };
  }

  private convertSessionInstanceRawValueToSessionInstance(
    rawSessionInstance: SessionInstanceFormRawValue | NewSessionInstanceFormRawValue,
  ): ISessionInstance | NewSessionInstance {
    return {
      ...rawSessionInstance,
      startTime: dayjs(rawSessionInstance.startTime, DATE_TIME_FORMAT),
    };
  }

  private convertSessionInstanceToSessionInstanceRawValue(
    sessionInstance: ISessionInstance | (Partial<NewSessionInstance> & SessionInstanceFormDefaults),
  ): SessionInstanceFormRawValue | PartialWithRequiredKeyOf<NewSessionInstanceFormRawValue> {
    return {
      ...sessionInstance,
      startTime: sessionInstance.startTime ? sessionInstance.startTime.format(DATE_TIME_FORMAT) : undefined,
      links: sessionInstance.links ?? [],
    };
  }
}
