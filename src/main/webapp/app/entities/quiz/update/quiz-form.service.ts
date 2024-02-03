import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IQuiz, NewQuiz } from '../quiz.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuiz for edit and NewQuizFormGroupInput for create.
 */
type QuizFormGroupInput = IQuiz | PartialWithRequiredKeyOf<NewQuiz>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IQuiz | NewQuiz> = Omit<T, 'deadline'> & {
  deadline?: string | null;
};

type QuizFormRawValue = FormValueOf<IQuiz>;

type NewQuizFormRawValue = FormValueOf<NewQuiz>;

type QuizFormDefaults = Pick<NewQuiz, 'id' | 'deadline' | 'isActive' | 'groups' | 'questions'>;

type QuizFormGroupContent = {
  id: FormControl<QuizFormRawValue['id'] | NewQuiz['id']>;
  quizType: FormControl<QuizFormRawValue['quizType']>;
  quizTitle: FormControl<QuizFormRawValue['quizTitle']>;
  quizDescription: FormControl<QuizFormRawValue['quizDescription']>;
  deadline: FormControl<QuizFormRawValue['deadline']>;
  isActive: FormControl<QuizFormRawValue['isActive']>;
  groups: FormControl<QuizFormRawValue['groups']>;
  questions: FormControl<QuizFormRawValue['questions']>;
  site7: FormControl<QuizFormRawValue['site7']>;
  topic1: FormControl<QuizFormRawValue['topic1']>;
};

export type QuizFormGroup = FormGroup<QuizFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QuizFormService {
  createQuizFormGroup(quiz: QuizFormGroupInput = { id: null }): QuizFormGroup {
    const quizRawValue = this.convertQuizToQuizRawValue({
      ...this.getFormDefaults(),
      ...quiz,
    });
    return new FormGroup<QuizFormGroupContent>({
      id: new FormControl(
        { value: quizRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      quizType: new FormControl(quizRawValue.quizType),
      quizTitle: new FormControl(quizRawValue.quizTitle, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      quizDescription: new FormControl(quizRawValue.quizDescription),
      deadline: new FormControl(quizRawValue.deadline, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(quizRawValue.isActive, {
        validators: [Validators.required],
      }),
      groups: new FormControl(quizRawValue.groups ?? []),
      questions: new FormControl(quizRawValue.questions ?? []),
      site7: new FormControl(quizRawValue.site7),
      topic1: new FormControl(quizRawValue.topic1),
    });
  }

  getQuiz(form: QuizFormGroup): IQuiz | NewQuiz {
    return this.convertQuizRawValueToQuiz(form.getRawValue() as QuizFormRawValue | NewQuizFormRawValue);
  }

  resetForm(form: QuizFormGroup, quiz: QuizFormGroupInput): void {
    const quizRawValue = this.convertQuizToQuizRawValue({ ...this.getFormDefaults(), ...quiz });
    form.reset(
      {
        ...quizRawValue,
        id: { value: quizRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): QuizFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      deadline: currentTime,
      isActive: false,
      groups: [],
      questions: [],
    };
  }

  private convertQuizRawValueToQuiz(rawQuiz: QuizFormRawValue | NewQuizFormRawValue): IQuiz | NewQuiz {
    return {
      ...rawQuiz,
      deadline: dayjs(rawQuiz.deadline, DATE_TIME_FORMAT),
    };
  }

  private convertQuizToQuizRawValue(
    quiz: IQuiz | (Partial<NewQuiz> & QuizFormDefaults),
  ): QuizFormRawValue | PartialWithRequiredKeyOf<NewQuizFormRawValue> {
    return {
      ...quiz,
      deadline: quiz.deadline ? quiz.deadline.format(DATE_TIME_FORMAT) : undefined,
      groups: quiz.groups ?? [],
      questions: quiz.questions ?? [],
    };
  }
}
