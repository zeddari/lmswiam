import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUserCustom, NewUserCustom } from '../user-custom.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserCustom for edit and NewUserCustomFormGroupInput for create.
 */
type UserCustomFormGroupInput = IUserCustom | PartialWithRequiredKeyOf<NewUserCustom>;

type UserCustomFormDefaults = Pick<NewUserCustom, 'id' | 'diplomas' | 'languages'>;

type UserCustomFormGroupContent = {
  id: FormControl<IUserCustom['id'] | NewUserCustom['id']>;
  firstName: FormControl<IUserCustom['firstName']>;
  lastName: FormControl<IUserCustom['lastName']>;
  code: FormControl<IUserCustom['code']>;
  accountName: FormControl<IUserCustom['accountName']>;
  role: FormControl<IUserCustom['role']>;
  accountStatus: FormControl<IUserCustom['accountStatus']>;
  phoneNumber1: FormControl<IUserCustom['phoneNumber1']>;
  phoneNumver2: FormControl<IUserCustom['phoneNumver2']>;
  sex: FormControl<IUserCustom['sex']>;
  birthdate: FormControl<IUserCustom['birthdate']>;
  photo: FormControl<IUserCustom['photo']>;
  photoContentType: FormControl<IUserCustom['photoContentType']>;
  address: FormControl<IUserCustom['address']>;
  facebook: FormControl<IUserCustom['facebook']>;
  telegramUserCustomId: FormControl<IUserCustom['telegramUserCustomId']>;
  telegramUserCustomName: FormControl<IUserCustom['telegramUserCustomName']>;
  biography: FormControl<IUserCustom['biography']>;
  bankAccountDetails: FormControl<IUserCustom['bankAccountDetails']>;
  user: FormControl<IUserCustom['user']>;
  diplomas: FormControl<IUserCustom['diplomas']>;
  languages: FormControl<IUserCustom['languages']>;
  site13: FormControl<IUserCustom['site13']>;
  country: FormControl<IUserCustom['country']>;
  nationality: FormControl<IUserCustom['nationality']>;
  job: FormControl<IUserCustom['job']>;
  departement2: FormControl<IUserCustom['departement2']>;
};

export type UserCustomFormGroup = FormGroup<UserCustomFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserCustomFormService {
  createUserCustomFormGroup(userCustom: UserCustomFormGroupInput = { id: null }): UserCustomFormGroup {
    const userCustomRawValue = {
      ...this.getFormDefaults(),
      ...userCustom,
    };
    return new FormGroup<UserCustomFormGroupContent>({
      id: new FormControl(
        { value: userCustomRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      firstName: new FormControl(userCustomRawValue.firstName, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      lastName: new FormControl(userCustomRawValue.lastName, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      code: new FormControl(userCustomRawValue.code, {
        validators: [Validators.maxLength(100)],
      }),
      accountName: new FormControl(userCustomRawValue.accountName, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      role: new FormControl(userCustomRawValue.role, {
        validators: [Validators.required],
      }),
      accountStatus: new FormControl(userCustomRawValue.accountStatus, {
        validators: [Validators.required],
      }),
      phoneNumber1: new FormControl(userCustomRawValue.phoneNumber1, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      phoneNumver2: new FormControl(userCustomRawValue.phoneNumver2, {
        validators: [Validators.maxLength(50)],
      }),
      sex: new FormControl(userCustomRawValue.sex, {
        validators: [Validators.required],
      }),
      birthdate: new FormControl(userCustomRawValue.birthdate, {
        validators: [Validators.required],
      }),
      photo: new FormControl(userCustomRawValue.photo),
      photoContentType: new FormControl(userCustomRawValue.photoContentType),
      address: new FormControl(userCustomRawValue.address),
      facebook: new FormControl(userCustomRawValue.facebook),
      telegramUserCustomId: new FormControl(userCustomRawValue.telegramUserCustomId),
      telegramUserCustomName: new FormControl(userCustomRawValue.telegramUserCustomName),
      biography: new FormControl(userCustomRawValue.biography),
      bankAccountDetails: new FormControl(userCustomRawValue.bankAccountDetails),
      user: new FormControl(userCustomRawValue.user),
      diplomas: new FormControl(userCustomRawValue.diplomas ?? []),
      languages: new FormControl(userCustomRawValue.languages ?? []),
      site13: new FormControl(userCustomRawValue.site13),
      country: new FormControl(userCustomRawValue.country),
      nationality: new FormControl(userCustomRawValue.nationality),
      job: new FormControl(userCustomRawValue.job),
      departement2: new FormControl(userCustomRawValue.departement2),
    });
  }

  getUserCustom(form: UserCustomFormGroup): IUserCustom | NewUserCustom {
    return form.getRawValue() as IUserCustom | NewUserCustom;
  }

  resetForm(form: UserCustomFormGroup, userCustom: UserCustomFormGroupInput): void {
    const userCustomRawValue = { ...this.getFormDefaults(), ...userCustom };
    form.reset(
      {
        ...userCustomRawValue,
        id: { value: userCustomRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UserCustomFormDefaults {
    return {
      id: null,
      diplomas: [],
      languages: [],
    };
  }
}
