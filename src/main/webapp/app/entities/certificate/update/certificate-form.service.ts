import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICertificate, NewCertificate } from '../certificate.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICertificate for edit and NewCertificateFormGroupInput for create.
 */
type CertificateFormGroupInput = ICertificate | PartialWithRequiredKeyOf<NewCertificate>;

type CertificateFormDefaults = Pick<NewCertificate, 'id'>;

type CertificateFormGroupContent = {
  id: FormControl<ICertificate['id'] | NewCertificate['id']>;
  certificateType: FormControl<ICertificate['certificateType']>;
  title: FormControl<ICertificate['title']>;
  riwaya: FormControl<ICertificate['riwaya']>;
  miqdar: FormControl<ICertificate['miqdar']>;
  observation: FormControl<ICertificate['observation']>;
  site19: FormControl<ICertificate['site19']>;
  userCustom6: FormControl<ICertificate['userCustom6']>;
  comitte: FormControl<ICertificate['comitte']>;
  topic4: FormControl<ICertificate['topic4']>;
};

export type CertificateFormGroup = FormGroup<CertificateFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CertificateFormService {
  createCertificateFormGroup(certificate: CertificateFormGroupInput = { id: null }): CertificateFormGroup {
    const certificateRawValue = {
      ...this.getFormDefaults(),
      ...certificate,
    };
    return new FormGroup<CertificateFormGroupContent>({
      id: new FormControl(
        { value: certificateRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      certificateType: new FormControl(certificateRawValue.certificateType),
      title: new FormControl(certificateRawValue.title, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      riwaya: new FormControl(certificateRawValue.riwaya),
      miqdar: new FormControl(certificateRawValue.miqdar, {
        validators: [Validators.required, Validators.min(1), Validators.max(60)],
      }),
      observation: new FormControl(certificateRawValue.observation, {
        validators: [Validators.required],
      }),
      site19: new FormControl(certificateRawValue.site19),
      userCustom6: new FormControl(certificateRawValue.userCustom6),
      comitte: new FormControl(certificateRawValue.comitte),
      topic4: new FormControl(certificateRawValue.topic4),
    });
  }

  getCertificate(form: CertificateFormGroup): ICertificate | NewCertificate {
    return form.getRawValue() as ICertificate | NewCertificate;
  }

  resetForm(form: CertificateFormGroup, certificate: CertificateFormGroupInput): void {
    const certificateRawValue = { ...this.getFormDefaults(), ...certificate };
    form.reset(
      {
        ...certificateRawValue,
        id: { value: certificateRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CertificateFormDefaults {
    return {
      id: null,
    };
  }
}
