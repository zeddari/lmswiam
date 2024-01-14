import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITypeProject, NewTypeProject } from '../type-project.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITypeProject for edit and NewTypeProjectFormGroupInput for create.
 */
type TypeProjectFormGroupInput = ITypeProject | PartialWithRequiredKeyOf<NewTypeProject>;

type TypeProjectFormDefaults = Pick<NewTypeProject, 'id'>;

type TypeProjectFormGroupContent = {
  id: FormControl<ITypeProject['id'] | NewTypeProject['id']>;
  nameAr: FormControl<ITypeProject['nameAr']>;
  nameLat: FormControl<ITypeProject['nameLat']>;
};

export type TypeProjectFormGroup = FormGroup<TypeProjectFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TypeProjectFormService {
  createTypeProjectFormGroup(typeProject: TypeProjectFormGroupInput = { id: null }): TypeProjectFormGroup {
    const typeProjectRawValue = {
      ...this.getFormDefaults(),
      ...typeProject,
    };
    return new FormGroup<TypeProjectFormGroupContent>({
      id: new FormControl(
        { value: typeProjectRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nameAr: new FormControl(typeProjectRawValue.nameAr, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      nameLat: new FormControl(typeProjectRawValue.nameLat, {
        validators: [Validators.required, Validators.maxLength(500)],
      }),
    });
  }

  getTypeProject(form: TypeProjectFormGroup): ITypeProject | NewTypeProject {
    return form.getRawValue() as ITypeProject | NewTypeProject;
  }

  resetForm(form: TypeProjectFormGroup, typeProject: TypeProjectFormGroupInput): void {
    const typeProjectRawValue = { ...this.getFormDefaults(), ...typeProject };
    form.reset(
      {
        ...typeProjectRawValue,
        id: { value: typeProjectRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TypeProjectFormDefaults {
    return {
      id: null,
    };
  }
}
