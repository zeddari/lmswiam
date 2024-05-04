import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IComments, NewComments } from '../comments.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IComments for edit and NewCommentsFormGroupInput for create.
 */
type CommentsFormGroupInput = IComments | PartialWithRequiredKeyOf<NewComments>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IComments | NewComments> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type CommentsFormRawValue = FormValueOf<IComments>;

type NewCommentsFormRawValue = FormValueOf<NewComments>;

type CommentsFormDefaults = Pick<NewComments, 'id' | 'like' | 'disLike' | 'createdAt' | 'updatedAt' | 'sessions8s'>;

type CommentsFormGroupContent = {
  id: FormControl<CommentsFormRawValue['id'] | NewComments['id']>;
  pseudoName: FormControl<CommentsFormRawValue['pseudoName']>;
  type: FormControl<CommentsFormRawValue['type']>;
  title: FormControl<CommentsFormRawValue['title']>;
  message: FormControl<CommentsFormRawValue['message']>;
  like: FormControl<CommentsFormRawValue['like']>;
  disLike: FormControl<CommentsFormRawValue['disLike']>;
  createdAt: FormControl<CommentsFormRawValue['createdAt']>;
  updatedAt: FormControl<CommentsFormRawValue['updatedAt']>;
  sessions8s: FormControl<CommentsFormRawValue['sessions8s']>;
};

export type CommentsFormGroup = FormGroup<CommentsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CommentsFormService {
  createCommentsFormGroup(comments: CommentsFormGroupInput = { id: null }): CommentsFormGroup {
    const commentsRawValue = this.convertCommentsToCommentsRawValue({
      ...this.getFormDefaults(),
      ...comments,
    });
    return new FormGroup<CommentsFormGroupContent>({
      id: new FormControl(
        { value: commentsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      pseudoName: new FormControl(commentsRawValue.pseudoName, {
        validators: [Validators.required],
      }),
      type: new FormControl(commentsRawValue.type, {
        validators: [Validators.required],
      }),
      title: new FormControl(commentsRawValue.title, {
        validators: [Validators.required],
      }),
      message: new FormControl(commentsRawValue.message),
      like: new FormControl(commentsRawValue.like),
      disLike: new FormControl(commentsRawValue.disLike),
      createdAt: new FormControl(commentsRawValue.createdAt),
      updatedAt: new FormControl(commentsRawValue.updatedAt),
      sessions8s: new FormControl(commentsRawValue.sessions8s ?? []),
    });
  }

  getComments(form: CommentsFormGroup): IComments | NewComments {
    return this.convertCommentsRawValueToComments(form.getRawValue() as CommentsFormRawValue | NewCommentsFormRawValue);
  }

  resetForm(form: CommentsFormGroup, comments: CommentsFormGroupInput): void {
    const commentsRawValue = this.convertCommentsToCommentsRawValue({ ...this.getFormDefaults(), ...comments });
    form.reset(
      {
        ...commentsRawValue,
        id: { value: commentsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CommentsFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      like: false,
      disLike: false,
      createdAt: currentTime,
      updatedAt: currentTime,
      sessions8s: [],
    };
  }

  private convertCommentsRawValueToComments(rawComments: CommentsFormRawValue | NewCommentsFormRawValue): IComments | NewComments {
    return {
      ...rawComments,
      createdAt: dayjs(rawComments.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawComments.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertCommentsToCommentsRawValue(
    comments: IComments | (Partial<NewComments> & CommentsFormDefaults),
  ): CommentsFormRawValue | PartialWithRequiredKeyOf<NewCommentsFormRawValue> {
    return {
      ...comments,
      createdAt: comments.createdAt ? comments.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: comments.updatedAt ? comments.updatedAt.format(DATE_TIME_FORMAT) : undefined,
      sessions8s: comments.sessions8s ?? [],
    };
  }
}
