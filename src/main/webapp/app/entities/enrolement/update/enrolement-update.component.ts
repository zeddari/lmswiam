import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUserCustom } from 'app/entities/user-custom/user-custom.model';
import { UserCustomService } from 'app/entities/user-custom/service/user-custom.service';
import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';
import { EnrolementType } from 'app/entities/enumerations/enrolement-type.model';
import { EnrolementService } from '../service/enrolement.service';
import { IEnrolement } from '../enrolement.model';
import { EnrolementFormService, EnrolementFormGroup } from './enrolement-form.service';

@Component({
  standalone: true,
  selector: 'jhi-enrolement-update',
  templateUrl: './enrolement-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EnrolementUpdateComponent implements OnInit {
  isSaving = false;
  enrolement: IEnrolement | null = null;
  enrolementTypeValues = Object.keys(EnrolementType);

  userCustomsSharedCollection: IUserCustom[] = [];
  coursesSharedCollection: ICourse[] = [];

  editForm: EnrolementFormGroup = this.enrolementFormService.createEnrolementFormGroup();

  constructor(
    protected enrolementService: EnrolementService,
    protected enrolementFormService: EnrolementFormService,
    protected userCustomService: UserCustomService,
    protected courseService: CourseService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareUserCustom = (o1: IUserCustom | null, o2: IUserCustom | null): boolean => this.userCustomService.compareUserCustom(o1, o2);

  compareCourse = (o1: ICourse | null, o2: ICourse | null): boolean => this.courseService.compareCourse(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ enrolement }) => {
      this.enrolement = enrolement;
      if (enrolement) {
        this.updateForm(enrolement);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const enrolement = this.enrolementFormService.getEnrolement(this.editForm);
    if (enrolement.id !== null) {
      this.subscribeToSaveResponse(this.enrolementService.update(enrolement));
    } else {
      this.subscribeToSaveResponse(this.enrolementService.create(enrolement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEnrolement>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(enrolement: IEnrolement): void {
    this.enrolement = enrolement;
    this.enrolementFormService.resetForm(this.editForm, enrolement);

    this.userCustomsSharedCollection = this.userCustomService.addUserCustomToCollectionIfMissing<IUserCustom>(
      this.userCustomsSharedCollection,
      enrolement.userCustom4,
    );
    this.coursesSharedCollection = this.courseService.addCourseToCollectionIfMissing<ICourse>(
      this.coursesSharedCollection,
      enrolement.course,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userCustomService
      .query()
      .pipe(map((res: HttpResponse<IUserCustom[]>) => res.body ?? []))
      .pipe(
        map((userCustoms: IUserCustom[]) =>
          this.userCustomService.addUserCustomToCollectionIfMissing<IUserCustom>(userCustoms, this.enrolement?.userCustom4),
        ),
      )
      .subscribe((userCustoms: IUserCustom[]) => (this.userCustomsSharedCollection = userCustoms));

    this.courseService
      .query()
      .pipe(map((res: HttpResponse<ICourse[]>) => res.body ?? []))
      .pipe(map((courses: ICourse[]) => this.courseService.addCourseToCollectionIfMissing<ICourse>(courses, this.enrolement?.course)))
      .subscribe((courses: ICourse[]) => (this.coursesSharedCollection = courses));
  }
}
