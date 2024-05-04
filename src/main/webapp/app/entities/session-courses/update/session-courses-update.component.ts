import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { SessionCoursesService } from '../service/session-courses.service';
import { ISessionCourses } from '../session-courses.model';
import { SessionCoursesFormService, SessionCoursesFormGroup } from './session-courses-form.service';

@Component({
  standalone: true,
  selector: 'jhi-session-courses-update',
  templateUrl: './session-courses-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SessionCoursesUpdateComponent implements OnInit {
  isSaving = false;
  sessionCourses: ISessionCourses | null = null;

  editForm: SessionCoursesFormGroup = this.sessionCoursesFormService.createSessionCoursesFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected sessionCoursesService: SessionCoursesService,
    protected sessionCoursesFormService: SessionCoursesFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sessionCourses }) => {
      this.sessionCourses = sessionCourses;
      if (sessionCourses) {
        this.updateForm(sessionCourses);
      }
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

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sessionCourses = this.sessionCoursesFormService.getSessionCourses(this.editForm);
    if (sessionCourses.id !== null) {
      this.subscribeToSaveResponse(this.sessionCoursesService.update(sessionCourses));
    } else {
      this.subscribeToSaveResponse(this.sessionCoursesService.create(sessionCourses));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISessionCourses>>): void {
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

  protected updateForm(sessionCourses: ISessionCourses): void {
    this.sessionCourses = sessionCourses;
    this.sessionCoursesFormService.resetForm(this.editForm, sessionCourses);
  }
}
