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
import { IClassroom } from 'app/entities/classroom/classroom.model';
import { ClassroomService } from 'app/entities/classroom/service/classroom.service';
import { IGroup } from 'app/entities/group/group.model';
import { GroupService } from 'app/entities/group/service/group.service';
import { IUserCustom } from 'app/entities/user-custom/user-custom.model';
import { UserCustomService } from 'app/entities/user-custom/service/user-custom.service';
import { ISessionLink } from 'app/entities/session-link/session-link.model';
import { SessionLinkService } from 'app/entities/session-link/service/session-link.service';
import { SessionMode } from 'app/entities/enumerations/session-mode.model';
import { SessionType } from 'app/entities/enumerations/session-type.model';
import { SessionJoinMode } from 'app/entities/enumerations/session-join-mode.model';
import { TargetedGender } from 'app/entities/enumerations/targeted-gender.model';
import { SessionService } from '../service/session.service';
import { ISession } from '../session.model';
import { SessionFormService, SessionFormGroup } from './session-form.service';

@Component({
  standalone: true,
  selector: 'jhi-session-update',
  templateUrl: './session-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SessionUpdateComponent implements OnInit {
  isSaving = false;
  session: ISession | null = null;
  sessionModeValues = Object.keys(SessionMode);
  sessionTypeValues = Object.keys(SessionType);
  sessionJoinModeValues = Object.keys(SessionJoinMode);
  targetedGenderValues = Object.keys(TargetedGender);

  classroomsSharedCollection: IClassroom[] = [];
  groupsSharedCollection: IGroup[] = [];
  userCustomsSharedCollection: IUserCustom[] = [];
  sessionLinksSharedCollection: ISessionLink[] = [];

  editForm: SessionFormGroup = this.sessionFormService.createSessionFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected sessionService: SessionService,
    protected sessionFormService: SessionFormService,
    protected classroomService: ClassroomService,
    protected groupService: GroupService,
    protected userCustomService: UserCustomService,
    protected sessionLinkService: SessionLinkService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareClassroom = (o1: IClassroom | null, o2: IClassroom | null): boolean => this.classroomService.compareClassroom(o1, o2);

  compareGroup = (o1: IGroup | null, o2: IGroup | null): boolean => this.groupService.compareGroup(o1, o2);

  compareUserCustom = (o1: IUserCustom | null, o2: IUserCustom | null): boolean => this.userCustomService.compareUserCustom(o1, o2);

  compareSessionLink = (o1: ISessionLink | null, o2: ISessionLink | null): boolean => this.sessionLinkService.compareSessionLink(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ session }) => {
      this.session = session;
      if (session) {
        this.updateForm(session);
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
    const session = this.sessionFormService.getSession(this.editForm);
    if (session.id !== null) {
      this.subscribeToSaveResponse(this.sessionService.update(session));
    } else {
      this.subscribeToSaveResponse(this.sessionService.create(session));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISession>>): void {
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

  protected updateForm(session: ISession): void {
    this.session = session;
    this.sessionFormService.resetForm(this.editForm, session);

    this.classroomsSharedCollection = this.classroomService.addClassroomToCollectionIfMissing<IClassroom>(
      this.classroomsSharedCollection,
      ...(session.classrooms ?? []),
    );
    this.groupsSharedCollection = this.groupService.addGroupToCollectionIfMissing<IGroup>(
      this.groupsSharedCollection,
      ...(session.groups ?? []),
    );
    this.userCustomsSharedCollection = this.userCustomService.addUserCustomToCollectionIfMissing<IUserCustom>(
      this.userCustomsSharedCollection,
      ...(session.professors ?? []),
      ...(session.employees ?? []),
    );
    this.sessionLinksSharedCollection = this.sessionLinkService.addSessionLinkToCollectionIfMissing<ISessionLink>(
      this.sessionLinksSharedCollection,
      ...(session.links ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.classroomService
      .query()
      .pipe(map((res: HttpResponse<IClassroom[]>) => res.body ?? []))
      .pipe(
        map((classrooms: IClassroom[]) =>
          this.classroomService.addClassroomToCollectionIfMissing<IClassroom>(classrooms, ...(this.session?.classrooms ?? [])),
        ),
      )
      .subscribe((classrooms: IClassroom[]) => (this.classroomsSharedCollection = classrooms));

    this.groupService
      .query()
      .pipe(map((res: HttpResponse<IGroup[]>) => res.body ?? []))
      .pipe(map((groups: IGroup[]) => this.groupService.addGroupToCollectionIfMissing<IGroup>(groups, ...(this.session?.groups ?? []))))
      .subscribe((groups: IGroup[]) => (this.groupsSharedCollection = groups));

    this.userCustomService
      .query()
      .pipe(map((res: HttpResponse<IUserCustom[]>) => res.body ?? []))
      .pipe(
        map((userCustoms: IUserCustom[]) =>
          this.userCustomService.addUserCustomToCollectionIfMissing<IUserCustom>(
            userCustoms,
            ...(this.session?.professors ?? []),
            ...(this.session?.employees ?? []),
          ),
        ),
      )
      .subscribe((userCustoms: IUserCustom[]) => (this.userCustomsSharedCollection = userCustoms));

    this.sessionLinkService
      .query()
      .pipe(map((res: HttpResponse<ISessionLink[]>) => res.body ?? []))
      .pipe(
        map((sessionLinks: ISessionLink[]) =>
          this.sessionLinkService.addSessionLinkToCollectionIfMissing<ISessionLink>(sessionLinks, ...(this.session?.links ?? [])),
        ),
      )
      .subscribe((sessionLinks: ISessionLink[]) => (this.sessionLinksSharedCollection = sessionLinks));
  }
}
