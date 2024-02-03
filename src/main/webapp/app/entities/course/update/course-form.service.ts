import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICourse, NewCourse } from '../course.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICourse for edit and NewCourseFormGroupInput for create.
 */
type CourseFormGroupInput = ICourse | PartialWithRequiredKeyOf<NewCourse>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICourse | NewCourse> = Omit<T, 'activateAt' | 'confirmedAt'> & {
  activateAt?: string | null;
  confirmedAt?: string | null;
};

type CourseFormRawValue = FormValueOf<ICourse>;

type NewCourseFormRawValue = FormValueOf<NewCourse>;

type CourseFormDefaults = Pick<NewCourse, 'id' | 'isActive' | 'activateAt' | 'isConfirmed' | 'confirmedAt' | 'professors'>;

type CourseFormGroupContent = {
  id: FormControl<CourseFormRawValue['id'] | NewCourse['id']>;
  level: FormControl<CourseFormRawValue['level']>;
  titleAr: FormControl<CourseFormRawValue['titleAr']>;
  titleLat: FormControl<CourseFormRawValue['titleLat']>;
  description: FormControl<CourseFormRawValue['description']>;
  subTitles: FormControl<CourseFormRawValue['subTitles']>;
  requirements: FormControl<CourseFormRawValue['requirements']>;
  options: FormControl<CourseFormRawValue['options']>;
  duration: FormControl<CourseFormRawValue['duration']>;
  imageLink: FormControl<CourseFormRawValue['imageLink']>;
  imageLinkContentType: FormControl<CourseFormRawValue['imageLinkContentType']>;
  videoLink: FormControl<CourseFormRawValue['videoLink']>;
  price: FormControl<CourseFormRawValue['price']>;
  isActive: FormControl<CourseFormRawValue['isActive']>;
  activateAt: FormControl<CourseFormRawValue['activateAt']>;
  isConfirmed: FormControl<CourseFormRawValue['isConfirmed']>;
  confirmedAt: FormControl<CourseFormRawValue['confirmedAt']>;
  professors: FormControl<CourseFormRawValue['professors']>;
  site1: FormControl<CourseFormRawValue['site1']>;
  topic3: FormControl<CourseFormRawValue['topic3']>;
};

export type CourseFormGroup = FormGroup<CourseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CourseFormService {
  createCourseFormGroup(course: CourseFormGroupInput = { id: null }): CourseFormGroup {
    const courseRawValue = this.convertCourseToCourseRawValue({
      ...this.getFormDefaults(),
      ...course,
    });
    return new FormGroup<CourseFormGroupContent>({
      id: new FormControl(
        { value: courseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      level: new FormControl(courseRawValue.level, {
        validators: [Validators.required],
      }),
      titleAr: new FormControl(courseRawValue.titleAr, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      titleLat: new FormControl(courseRawValue.titleLat, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      description: new FormControl(courseRawValue.description),
      subTitles: new FormControl(courseRawValue.subTitles),
      requirements: new FormControl(courseRawValue.requirements),
      options: new FormControl(courseRawValue.options),
      duration: new FormControl(courseRawValue.duration),
      imageLink: new FormControl(courseRawValue.imageLink),
      imageLinkContentType: new FormControl(courseRawValue.imageLinkContentType),
      videoLink: new FormControl(courseRawValue.videoLink),
      price: new FormControl(courseRawValue.price, {
        validators: [Validators.min(0)],
      }),
      isActive: new FormControl(courseRawValue.isActive),
      activateAt: new FormControl(courseRawValue.activateAt),
      isConfirmed: new FormControl(courseRawValue.isConfirmed),
      confirmedAt: new FormControl(courseRawValue.confirmedAt),
      professors: new FormControl(courseRawValue.professors ?? []),
      site1: new FormControl(courseRawValue.site1),
      topic3: new FormControl(courseRawValue.topic3),
    });
  }

  getCourse(form: CourseFormGroup): ICourse | NewCourse {
    return this.convertCourseRawValueToCourse(form.getRawValue() as CourseFormRawValue | NewCourseFormRawValue);
  }

  resetForm(form: CourseFormGroup, course: CourseFormGroupInput): void {
    const courseRawValue = this.convertCourseToCourseRawValue({ ...this.getFormDefaults(), ...course });
    form.reset(
      {
        ...courseRawValue,
        id: { value: courseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CourseFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isActive: false,
      activateAt: currentTime,
      isConfirmed: false,
      confirmedAt: currentTime,
      professors: [],
    };
  }

  private convertCourseRawValueToCourse(rawCourse: CourseFormRawValue | NewCourseFormRawValue): ICourse | NewCourse {
    return {
      ...rawCourse,
      activateAt: dayjs(rawCourse.activateAt, DATE_TIME_FORMAT),
      confirmedAt: dayjs(rawCourse.confirmedAt, DATE_TIME_FORMAT),
    };
  }

  private convertCourseToCourseRawValue(
    course: ICourse | (Partial<NewCourse> & CourseFormDefaults),
  ): CourseFormRawValue | PartialWithRequiredKeyOf<NewCourseFormRawValue> {
    return {
      ...course,
      activateAt: course.activateAt ? course.activateAt.format(DATE_TIME_FORMAT) : undefined,
      confirmedAt: course.confirmedAt ? course.confirmedAt.format(DATE_TIME_FORMAT) : undefined,
      professors: course.professors ?? [],
    };
  }
}
