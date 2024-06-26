import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IQuestion, NewQuestion } from '../question.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuestion for edit and NewQuestionFormGroupInput for create.
 */
type QuestionFormGroupInput = IQuestion | PartialWithRequiredKeyOf<NewQuestion>;

type QuestionFormDefaults = Pick<NewQuestion, 'id' | 'a1v' | 'a2v' | 'a3v' | 'a4v' | 'isActive'>;

type QuestionFormGroupContent = {
  id: FormControl<IQuestion['id'] | NewQuestion['id']>;
  question: FormControl<IQuestion['question']>;
  details: FormControl<IQuestion['details']>;
  a1: FormControl<IQuestion['a1']>;
  a1v: FormControl<IQuestion['a1v']>;
  a2: FormControl<IQuestion['a2']>;
  a2v: FormControl<IQuestion['a2v']>;
  a3: FormControl<IQuestion['a3']>;
  a3v: FormControl<IQuestion['a3v']>;
  a4: FormControl<IQuestion['a4']>;
  a4v: FormControl<IQuestion['a4v']>;
  isActive: FormControl<IQuestion['isActive']>;
  site5: FormControl<IQuestion['site5']>;
};

export type QuestionFormGroup = FormGroup<QuestionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QuestionFormService {
  createQuestionFormGroup(question: QuestionFormGroupInput = { id: null }): QuestionFormGroup {
    const questionRawValue = {
      ...this.getFormDefaults(),
      ...question,
    };
    return new FormGroup<QuestionFormGroupContent>({
      id: new FormControl(
        { value: questionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      question: new FormControl(questionRawValue.question, {
        validators: [Validators.required],
      }),
      details: new FormControl(questionRawValue.details),
      a1: new FormControl(questionRawValue.a1, {
        validators: [Validators.required],
      }),
      a1v: new FormControl(questionRawValue.a1v, {
        validators: [Validators.required],
      }),
      a2: new FormControl(questionRawValue.a2, {
        validators: [Validators.required],
      }),
      a2v: new FormControl(questionRawValue.a2v, {
        validators: [Validators.required],
      }),
      a3: new FormControl(questionRawValue.a3),
      a3v: new FormControl(questionRawValue.a3v),
      a4: new FormControl(questionRawValue.a4),
      a4v: new FormControl(questionRawValue.a4v),
      isActive: new FormControl(questionRawValue.isActive, {
        validators: [Validators.required],
      }),
      site5: new FormControl(questionRawValue.site5),
    });
  }

  getQuestion(form: QuestionFormGroup): IQuestion | NewQuestion {
    return form.getRawValue() as IQuestion | NewQuestion;
  }

  resetForm(form: QuestionFormGroup, question: QuestionFormGroupInput): void {
    const questionRawValue = { ...this.getFormDefaults(), ...question };
    form.reset(
      {
        ...questionRawValue,
        id: { value: questionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): QuestionFormDefaults {
    return {
      id: null,
      a1v: false,
      a2v: false,
      a3v: false,
      a4v: false,
      isActive: false,
    };
  }
}
