import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ISession } from 'app/entities/session/session.model';
import { SessionService } from 'app/entities/session/service/session.service';
import { CommentType } from 'app/entities/enumerations/comment-type.model';
import { CommentsService } from '../service/comments.service';
import { IComments } from '../comments.model';
import { CommentsFormService, CommentsFormGroup } from './comments-form.service';

@Component({
  standalone: true,
  selector: 'jhi-comments-update',
  templateUrl: './comments-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CommentsUpdateComponent implements OnInit {
  isSaving = false;
  comments: IComments | null = null;
  commentTypeValues = Object.keys(CommentType);

  sessionsSharedCollection: ISession[] = [];

  editForm: CommentsFormGroup = this.commentsFormService.createCommentsFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected commentsService: CommentsService,
    protected commentsFormService: CommentsFormService,
    protected sessionService: SessionService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareSession = (o1: ISession | null, o2: ISession | null): boolean => this.sessionService.compareSession(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ comments }) => {
      this.comments = comments;
      if (comments) {
        this.updateForm(comments);
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

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const comments = this.commentsFormService.getComments(this.editForm);
    if (comments.id !== null) {
      this.subscribeToSaveResponse(this.commentsService.update(comments));
    } else {
      this.subscribeToSaveResponse(this.commentsService.create(comments));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IComments>>): void {
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

  protected updateForm(comments: IComments): void {
    this.comments = comments;
    this.commentsFormService.resetForm(this.editForm, comments);

    this.sessionsSharedCollection = this.sessionService.addSessionToCollectionIfMissing<ISession>(
      this.sessionsSharedCollection,
      ...(comments.sessions8s ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sessionService
      .query()
      .pipe(map((res: HttpResponse<ISession[]>) => res.body ?? []))
      .pipe(
        map((sessions: ISession[]) =>
          this.sessionService.addSessionToCollectionIfMissing<ISession>(sessions, ...(this.comments?.sessions8s ?? [])),
        ),
      )
      .subscribe((sessions: ISession[]) => (this.sessionsSharedCollection = sessions));
  }
}
