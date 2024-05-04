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
import { ISessionLink } from 'app/entities/session-link/session-link.model';
import { SessionLinkService } from 'app/entities/session-link/service/session-link.service';
import { ISessionCourses } from 'app/entities/session-courses/session-courses.model';
import { SessionCoursesService } from 'app/entities/session-courses/service/session-courses.service';
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
import { ISession } from 'app/entities/session/session.model';
import { SessionService } from 'app/entities/session/service/session.service';
import { Attendance } from 'app/entities/enumerations/attendance.model';
import { SessionInstanceService } from '../service/session-instance.service';
import { ISessionInstance } from '../session-instance.model';
import { SessionInstanceFormService, SessionInstanceFormGroup } from './session-instance-form.service';

@Component({
  standalone: true,
  selector: 'jhi-session-instance-update',
  templateUrl: './session-instance-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SessionInstanceUpdateComponent implements OnInit {
  isSaving = false;
  sessionInstance: ISessionInstance | null = null;
  attendanceValues = Object.keys(Attendance);

  sessionLinksSharedCollection: ISessionLink[] = [];
  sessionCoursesSharedCollection: ISessionCourses[] = [];
  sitesSharedCollection: ISite[] = [];
  sessionsSharedCollection: ISession[] = [];

  editForm: SessionInstanceFormGroup = this.sessionInstanceFormService.createSessionInstanceFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected sessionInstanceService: SessionInstanceService,
    protected sessionInstanceFormService: SessionInstanceFormService,
    protected sessionLinkService: SessionLinkService,
    protected sessionCoursesService: SessionCoursesService,
    protected siteService: SiteService,
    protected sessionService: SessionService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareSessionLink = (o1: ISessionLink | null, o2: ISessionLink | null): boolean => this.sessionLinkService.compareSessionLink(o1, o2);

  compareSessionCourses = (o1: ISessionCourses | null, o2: ISessionCourses | null): boolean =>
    this.sessionCoursesService.compareSessionCourses(o1, o2);

  compareSite = (o1: ISite | null, o2: ISite | null): boolean => this.siteService.compareSite(o1, o2);

  compareSession = (o1: ISession | null, o2: ISession | null): boolean => this.sessionService.compareSession(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sessionInstance }) => {
      this.sessionInstance = sessionInstance;
      if (sessionInstance) {
        this.updateForm(sessionInstance);
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
    const sessionInstance = this.sessionInstanceFormService.getSessionInstance(this.editForm);
    if (sessionInstance.id !== null) {
      this.subscribeToSaveResponse(this.sessionInstanceService.update(sessionInstance));
    } else {
      this.subscribeToSaveResponse(this.sessionInstanceService.create(sessionInstance));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISessionInstance>>): void {
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

  protected updateForm(sessionInstance: ISessionInstance): void {
    this.sessionInstance = sessionInstance;
    this.sessionInstanceFormService.resetForm(this.editForm, sessionInstance);

    this.sessionLinksSharedCollection = this.sessionLinkService.addSessionLinkToCollectionIfMissing<ISessionLink>(
      this.sessionLinksSharedCollection,
      ...(sessionInstance.links ?? []),
    );
    this.sessionCoursesSharedCollection = this.sessionCoursesService.addSessionCoursesToCollectionIfMissing<ISessionCourses>(
      this.sessionCoursesSharedCollection,
      ...(sessionInstance.courses ?? []),
    );
    this.sitesSharedCollection = this.siteService.addSiteToCollectionIfMissing<ISite>(this.sitesSharedCollection, sessionInstance.site16);
    this.sessionsSharedCollection = this.sessionService.addSessionToCollectionIfMissing<ISession>(
      this.sessionsSharedCollection,
      sessionInstance.session1,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sessionLinkService
      .query()
      .pipe(map((res: HttpResponse<ISessionLink[]>) => res.body ?? []))
      .pipe(
        map((sessionLinks: ISessionLink[]) =>
          this.sessionLinkService.addSessionLinkToCollectionIfMissing<ISessionLink>(sessionLinks, ...(this.sessionInstance?.links ?? [])),
        ),
      )
      .subscribe((sessionLinks: ISessionLink[]) => (this.sessionLinksSharedCollection = sessionLinks));

    this.sessionCoursesService
      .query()
      .pipe(map((res: HttpResponse<ISessionCourses[]>) => res.body ?? []))
      .pipe(
        map((sessionCourses: ISessionCourses[]) =>
          this.sessionCoursesService.addSessionCoursesToCollectionIfMissing<ISessionCourses>(
            sessionCourses,
            ...(this.sessionInstance?.courses ?? []),
          ),
        ),
      )
      .subscribe((sessionCourses: ISessionCourses[]) => (this.sessionCoursesSharedCollection = sessionCourses));

    this.siteService
      .query()
      .pipe(map((res: HttpResponse<ISite[]>) => res.body ?? []))
      .pipe(map((sites: ISite[]) => this.siteService.addSiteToCollectionIfMissing<ISite>(sites, this.sessionInstance?.site16)))
      .subscribe((sites: ISite[]) => (this.sitesSharedCollection = sites));

    this.sessionService
      .query()
      .pipe(map((res: HttpResponse<ISession[]>) => res.body ?? []))
      .pipe(
        map((sessions: ISession[]) =>
          this.sessionService.addSessionToCollectionIfMissing<ISession>(sessions, this.sessionInstance?.session1),
        ),
      )
      .subscribe((sessions: ISession[]) => (this.sessionsSharedCollection = sessions));
  }
}
