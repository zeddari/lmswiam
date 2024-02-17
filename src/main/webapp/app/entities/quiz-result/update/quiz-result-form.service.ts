import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IQuizResult, NewQuizResult } from '../quiz-result.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuizResult for edit and NewQuizResultFormGroupInput for create.
 */
type QuizResultFormGroupInput = IQuizResult | PartialWithRequiredKeyOf<NewQuizResult>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IQuizResult | NewQuizResult> = Omit<T, 'submittedAT'> & {
  submittedAT?: string | null;
};

type QuizResultFormRawValue = FormValueOf<IQuizResult>;

type NewQuizResultFormRawValue = FormValueOf<NewQuizResult>;

type QuizResultFormDefaults = Pick<NewQuizResult, 'id' | 'submittedAT'>;

type QuizResultFormGroupContent = {
  id: FormControl<QuizResultFormRawValue['id'] | NewQuizResult['id']>;
  result: FormControl<QuizResultFormRawValue['result']>;
  submittedAT: FormControl<QuizResultFormRawValue['submittedAT']>;
  site8: FormControl<QuizResultFormRawValue['site8']>;
  quiz: FormControl<QuizResultFormRawValue['quiz']>;
  userCustom2: FormControl<QuizResultFormRawValue['userCustom2']>;
};

export type QuizResultFormGroup = FormGroup<QuizResultFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QuizResultFormService {
  createQuizResultFormGroup(quizResult: QuizResultFormGroupInput = { id: null }): QuizResultFormGroup {
    const quizResultRawValue = this.convertQuizResultToQuizResultRawValue({
      ...this.getFormDefaults(),
      ...quizResult,
    });
    return new FormGroup<QuizResultFormGroupContent>({
      id: new FormControl(
        { value: quizResultRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      result: new FormControl(quizResultRawValue.result, {
        validators: [Validators.required],
      }),
      submittedAT: new FormControl(quizResultRawValue.submittedAT, {
        validators: [Validators.required],
      }),
      site8: new FormControl(quizResultRawValue.site8),
      quiz: new FormControl(quizResultRawValue.quiz),
      userCustom2: new FormControl(quizResultRawValue.userCustom2),
    });
  }

  getQuizResult(form: QuizResultFormGroup): IQuizResult | NewQuizResult {
    return this.convertQuizResultRawValueToQuizResult(form.getRawValue() as QuizResultFormRawValue | NewQuizResultFormRawValue);
  }

  resetForm(form: QuizResultFormGroup, quizResult: QuizResultFormGroupInput): void {
    const quizResultRawValue = this.convertQuizResultToQuizResultRawValue({ ...this.getFormDefaults(), ...quizResult });
    form.reset(
      {
        ...quizResultRawValue,
        id: { value: quizResultRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): QuizResultFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      submittedAT: currentTime,
    };
  }

  private convertQuizResultRawValueToQuizResult(
    rawQuizResult: QuizResultFormRawValue | NewQuizResultFormRawValue,
  ): IQuizResult | NewQuizResult {
    return {
      ...rawQuizResult,
      submittedAT: dayjs(rawQuizResult.submittedAT, DATE_TIME_FORMAT),
    };
  }

  private convertQuizResultToQuizResultRawValue(
    quizResult: IQuizResult | (Partial<NewQuizResult> & QuizResultFormDefaults),
  ): QuizResultFormRawValue | PartialWithRequiredKeyOf<NewQuizResultFormRawValue> {
    return {
      ...quizResult,
      submittedAT: quizResult.submittedAT ? quizResult.submittedAT.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
