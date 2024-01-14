import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDiploma, NewDiploma } from '../diploma.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDiploma for edit and NewDiplomaFormGroupInput for create.
 */
type DiplomaFormGroupInput = IDiploma | PartialWithRequiredKeyOf<NewDiploma>;

type DiplomaFormDefaults = Pick<NewDiploma, 'id'>;

type DiplomaFormGroupContent = {
  id: FormControl<IDiploma['id'] | NewDiploma['id']>;
  type: FormControl<IDiploma['type']>;
  title: FormControl<IDiploma['title']>;
  subject: FormControl<IDiploma['subject']>;
  detail: FormControl<IDiploma['detail']>;
  supervisor: FormControl<IDiploma['supervisor']>;
  grade: FormControl<IDiploma['grade']>;
  graduationDate: FormControl<IDiploma['graduationDate']>;
  school: FormControl<IDiploma['school']>;
  attachment: FormControl<IDiploma['attachment']>;
  attachmentContentType: FormControl<IDiploma['attachmentContentType']>;
};

export type DiplomaFormGroup = FormGroup<DiplomaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DiplomaFormService {
  createDiplomaFormGroup(diploma: DiplomaFormGroupInput = { id: null }): DiplomaFormGroup {
    const diplomaRawValue = {
      ...this.getFormDefaults(),
      ...diploma,
    };
    return new FormGroup<DiplomaFormGroupContent>({
      id: new FormControl(
        { value: diplomaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(diplomaRawValue.type, {
        validators: [Validators.required],
      }),
      title: new FormControl(diplomaRawValue.title, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      subject: new FormControl(diplomaRawValue.subject),
      detail: new FormControl(diplomaRawValue.detail),
      supervisor: new FormControl(diplomaRawValue.supervisor),
      grade: new FormControl(diplomaRawValue.grade),
      graduationDate: new FormControl(diplomaRawValue.graduationDate),
      school: new FormControl(diplomaRawValue.school),
      attachment: new FormControl(diplomaRawValue.attachment),
      attachmentContentType: new FormControl(diplomaRawValue.attachmentContentType),
    });
  }

  getDiploma(form: DiplomaFormGroup): IDiploma | NewDiploma {
    return form.getRawValue() as IDiploma | NewDiploma;
  }

  resetForm(form: DiplomaFormGroup, diploma: DiplomaFormGroupInput): void {
    const diplomaRawValue = { ...this.getFormDefaults(), ...diploma };
    form.reset(
      {
        ...diplomaRawValue,
        id: { value: diplomaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DiplomaFormDefaults {
    return {
      id: null,
    };
  }
}
