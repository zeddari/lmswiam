import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUserCustom } from 'app/entities/user-custom/user-custom.model';
import { UserCustomService } from 'app/entities/user-custom/service/user-custom.service';
import { ITopic } from 'app/entities/topic/topic.model';
import { TopicService } from 'app/entities/topic/service/topic.service';
import { Level } from 'app/entities/enumerations/level.model';
import { CourseService } from '../service/course.service';
import { ICourse } from '../course.model';
import { CourseFormService, CourseFormGroup } from './course-form.service';

@Component({
  standalone: true,
  selector: 'jhi-course-update',
  templateUrl: './course-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CourseUpdateComponent implements OnInit {
  isSaving = false;
  course: ICourse | null = null;
  levelValues = Object.keys(Level);

  userCustomsSharedCollection: IUserCustom[] = [];
  topicsSharedCollection: ITopic[] = [];

  editForm: CourseFormGroup = this.courseFormService.createCourseFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected courseService: CourseService,
    protected courseFormService: CourseFormService,
    protected userCustomService: UserCustomService,
    protected topicService: TopicService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareUserCustom = (o1: IUserCustom | null, o2: IUserCustom | null): boolean => this.userCustomService.compareUserCustom(o1, o2);

  compareTopic = (o1: ITopic | null, o2: ITopic | null): boolean => this.topicService.compareTopic(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ course }) => {
      this.course = course;
      if (course) {
        this.updateForm(course);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('lmswiamApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const course = this.courseFormService.getCourse(this.editForm);
    if (course.id !== null) {
      this.subscribeToSaveResponse(this.courseService.update(course));
    } else {
      this.subscribeToSaveResponse(this.courseService.create(course));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourse>>): void {
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

  protected updateForm(course: ICourse): void {
    this.course = course;
    this.courseFormService.resetForm(this.editForm, course);

    this.userCustomsSharedCollection = this.userCustomService.addUserCustomToCollectionIfMissing<IUserCustom>(
      this.userCustomsSharedCollection,
      ...(course.professors ?? []),
    );
    this.topicsSharedCollection = this.topicService.addTopicToCollectionIfMissing<ITopic>(this.topicsSharedCollection, course.topic3);
  }

  protected loadRelationshipsOptions(): void {
    this.userCustomService
      .query()
      .pipe(map((res: HttpResponse<IUserCustom[]>) => res.body ?? []))
      .pipe(
        map((userCustoms: IUserCustom[]) =>
          this.userCustomService.addUserCustomToCollectionIfMissing<IUserCustom>(userCustoms, ...(this.course?.professors ?? [])),
        ),
      )
      .subscribe((userCustoms: IUserCustom[]) => (this.userCustomsSharedCollection = userCustoms));

    this.topicService
      .query()
      .pipe(map((res: HttpResponse<ITopic[]>) => res.body ?? []))
      .pipe(map((topics: ITopic[]) => this.topicService.addTopicToCollectionIfMissing<ITopic>(topics, this.course?.topic3)))
      .subscribe((topics: ITopic[]) => (this.topicsSharedCollection = topics));
  }
}
