import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISession, NewSession } from '../session.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISession for edit and NewSessionFormGroupInput for create.
 */
type SessionFormGroupInput = ISession | PartialWithRequiredKeyOf<NewSession>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISession | NewSession> = Omit<T, 'sessionStartTime' | 'sessionEndTime'> & {
  sessionStartTime?: string | null;
  sessionEndTime?: string | null;
};

type SessionFormRawValue = FormValueOf<ISession>;

type NewSessionFormRawValue = FormValueOf<NewSession>;

type SessionFormDefaults = Pick<
  NewSession,
  | 'id'
  | 'sessionStartTime'
  | 'sessionEndTime'
  | 'monday'
  | 'tuesday'
  | 'wednesday'
  | 'thursday'
  | 'friday'
  | 'saturday'
  | 'sunday'
  | 'isPeriodic'
  | 'isMinorAllowed'
  | 'isActive'
  | 'classrooms'
  | 'groups'
  | 'professors'
  | 'employees'
  | 'links'
>;

type SessionFormGroupContent = {
  id: FormControl<SessionFormRawValue['id'] | NewSession['id']>;
  sessionMode: FormControl<SessionFormRawValue['sessionMode']>;
  sessionType: FormControl<SessionFormRawValue['sessionType']>;
  sessionJoinMode: FormControl<SessionFormRawValue['sessionJoinMode']>;
  title: FormControl<SessionFormRawValue['title']>;
  description: FormControl<SessionFormRawValue['description']>;
  periodStartDate: FormControl<SessionFormRawValue['periodStartDate']>;
  periodeEndDate: FormControl<SessionFormRawValue['periodeEndDate']>;
  sessionStartTime: FormControl<SessionFormRawValue['sessionStartTime']>;
  sessionEndTime: FormControl<SessionFormRawValue['sessionEndTime']>;
  sessionSize: FormControl<SessionFormRawValue['sessionSize']>;
  targetedGender: FormControl<SessionFormRawValue['targetedGender']>;
  price: FormControl<SessionFormRawValue['price']>;
  thumbnail: FormControl<SessionFormRawValue['thumbnail']>;
  thumbnailContentType: FormControl<SessionFormRawValue['thumbnailContentType']>;
  monday: FormControl<SessionFormRawValue['monday']>;
  tuesday: FormControl<SessionFormRawValue['tuesday']>;
  wednesday: FormControl<SessionFormRawValue['wednesday']>;
  thursday: FormControl<SessionFormRawValue['thursday']>;
  friday: FormControl<SessionFormRawValue['friday']>;
  saturday: FormControl<SessionFormRawValue['saturday']>;
  sunday: FormControl<SessionFormRawValue['sunday']>;
  isPeriodic: FormControl<SessionFormRawValue['isPeriodic']>;
  isMinorAllowed: FormControl<SessionFormRawValue['isMinorAllowed']>;
  isActive: FormControl<SessionFormRawValue['isActive']>;
  classrooms: FormControl<SessionFormRawValue['classrooms']>;
  groups: FormControl<SessionFormRawValue['groups']>;
  professors: FormControl<SessionFormRawValue['professors']>;
  employees: FormControl<SessionFormRawValue['employees']>;
  links: FormControl<SessionFormRawValue['links']>;
};

export type SessionFormGroup = FormGroup<SessionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SessionFormService {
  createSessionFormGroup(session: SessionFormGroupInput = { id: null }): SessionFormGroup {
    const sessionRawValue = this.convertSessionToSessionRawValue({
      ...this.getFormDefaults(),
      ...session,
    });
    return new FormGroup<SessionFormGroupContent>({
      id: new FormControl(
        { value: sessionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sessionMode: new FormControl(sessionRawValue.sessionMode, {
        validators: [Validators.required],
      }),
      sessionType: new FormControl(sessionRawValue.sessionType, {
        validators: [Validators.required],
      }),
      sessionJoinMode: new FormControl(sessionRawValue.sessionJoinMode, {
        validators: [Validators.required],
      }),
      title: new FormControl(sessionRawValue.title, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      description: new FormControl(sessionRawValue.description),
      periodStartDate: new FormControl(sessionRawValue.periodStartDate),
      periodeEndDate: new FormControl(sessionRawValue.periodeEndDate),
      sessionStartTime: new FormControl(sessionRawValue.sessionStartTime, {
        validators: [Validators.required],
      }),
      sessionEndTime: new FormControl(sessionRawValue.sessionEndTime, {
        validators: [Validators.required],
      }),
      sessionSize: new FormControl(sessionRawValue.sessionSize, {
        validators: [Validators.required, Validators.min(0), Validators.max(100)],
      }),
      targetedGender: new FormControl(sessionRawValue.targetedGender, {
        validators: [Validators.required],
      }),
      price: new FormControl(sessionRawValue.price, {
        validators: [Validators.min(0)],
      }),
      thumbnail: new FormControl(sessionRawValue.thumbnail),
      thumbnailContentType: new FormControl(sessionRawValue.thumbnailContentType),
      monday: new FormControl(sessionRawValue.monday),
      tuesday: new FormControl(sessionRawValue.tuesday),
      wednesday: new FormControl(sessionRawValue.wednesday),
      thursday: new FormControl(sessionRawValue.thursday),
      friday: new FormControl(sessionRawValue.friday),
      saturday: new FormControl(sessionRawValue.saturday),
      sunday: new FormControl(sessionRawValue.sunday),
      isPeriodic: new FormControl(sessionRawValue.isPeriodic, {
        validators: [Validators.required],
      }),
      isMinorAllowed: new FormControl(sessionRawValue.isMinorAllowed, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(sessionRawValue.isActive, {
        validators: [Validators.required],
      }),
      classrooms: new FormControl(sessionRawValue.classrooms ?? []),
      groups: new FormControl(sessionRawValue.groups ?? []),
      professors: new FormControl(sessionRawValue.professors ?? []),
      employees: new FormControl(sessionRawValue.employees ?? []),
      links: new FormControl(sessionRawValue.links ?? []),
    });
  }

  getSession(form: SessionFormGroup): ISession | NewSession {
    return this.convertSessionRawValueToSession(form.getRawValue() as SessionFormRawValue | NewSessionFormRawValue);
  }

  resetForm(form: SessionFormGroup, session: SessionFormGroupInput): void {
    const sessionRawValue = this.convertSessionToSessionRawValue({ ...this.getFormDefaults(), ...session });
    form.reset(
      {
        ...sessionRawValue,
        id: { value: sessionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SessionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      sessionStartTime: currentTime,
      sessionEndTime: currentTime,
      monday: false,
      tuesday: false,
      wednesday: false,
      thursday: false,
      friday: false,
      saturday: false,
      sunday: false,
      isPeriodic: false,
      isMinorAllowed: false,
      isActive: false,
      classrooms: [],
      groups: [],
      professors: [],
      employees: [],
      links: [],
    };
  }

  private convertSessionRawValueToSession(rawSession: SessionFormRawValue | NewSessionFormRawValue): ISession | NewSession {
    return {
      ...rawSession,
      sessionStartTime: dayjs(rawSession.sessionStartTime, DATE_TIME_FORMAT),
      sessionEndTime: dayjs(rawSession.sessionEndTime, DATE_TIME_FORMAT),
    };
  }

  private convertSessionToSessionRawValue(
    session: ISession | (Partial<NewSession> & SessionFormDefaults),
  ): SessionFormRawValue | PartialWithRequiredKeyOf<NewSessionFormRawValue> {
    return {
      ...session,
      sessionStartTime: session.sessionStartTime ? session.sessionStartTime.format(DATE_TIME_FORMAT) : undefined,
      sessionEndTime: session.sessionEndTime ? session.sessionEndTime.format(DATE_TIME_FORMAT) : undefined,
      classrooms: session.classrooms ?? [],
      groups: session.groups ?? [],
      professors: session.professors ?? [],
      employees: session.employees ?? [],
      links: session.links ?? [],
    };
  }
}
