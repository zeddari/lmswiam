import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISessionLink, NewSessionLink } from '../session-link.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISessionLink for edit and NewSessionLinkFormGroupInput for create.
 */
type SessionLinkFormGroupInput = ISessionLink | PartialWithRequiredKeyOf<NewSessionLink>;

type SessionLinkFormDefaults = Pick<NewSessionLink, 'id'>;

type SessionLinkFormGroupContent = {
  id: FormControl<ISessionLink['id'] | NewSessionLink['id']>;
  provider: FormControl<ISessionLink['provider']>;
  title: FormControl<ISessionLink['title']>;
  description: FormControl<ISessionLink['description']>;
  link: FormControl<ISessionLink['link']>;
  site15: FormControl<ISessionLink['site15']>;
};

export type SessionLinkFormGroup = FormGroup<SessionLinkFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SessionLinkFormService {
  createSessionLinkFormGroup(sessionLink: SessionLinkFormGroupInput = { id: null }): SessionLinkFormGroup {
    const sessionLinkRawValue = {
      ...this.getFormDefaults(),
      ...sessionLink,
    };
    return new FormGroup<SessionLinkFormGroupContent>({
      id: new FormControl(
        { value: sessionLinkRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      provider: new FormControl(sessionLinkRawValue.provider),
      title: new FormControl(sessionLinkRawValue.title, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      description: new FormControl(sessionLinkRawValue.description),
      link: new FormControl(sessionLinkRawValue.link, {
        validators: [Validators.maxLength(500)],
      }),
      site15: new FormControl(sessionLinkRawValue.site15),
    });
  }

  getSessionLink(form: SessionLinkFormGroup): ISessionLink | NewSessionLink {
    return form.getRawValue() as ISessionLink | NewSessionLink;
  }

  resetForm(form: SessionLinkFormGroup, sessionLink: SessionLinkFormGroupInput): void {
    const sessionLinkRawValue = { ...this.getFormDefaults(), ...sessionLink };
    form.reset(
      {
        ...sessionLinkRawValue,
        id: { value: sessionLinkRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SessionLinkFormDefaults {
    return {
      id: null,
    };
  }
}
