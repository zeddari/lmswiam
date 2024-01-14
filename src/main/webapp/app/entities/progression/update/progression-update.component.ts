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
import { ISessionInstance } from 'app/entities/session-instance/session-instance.model';
import { SessionInstanceService } from 'app/entities/session-instance/service/session-instance.service';
import { IUserCustom } from 'app/entities/user-custom/user-custom.model';
import { UserCustomService } from 'app/entities/user-custom/service/user-custom.service';
import { Attendance } from 'app/entities/enumerations/attendance.model';
import { ProgressionMode } from 'app/entities/enumerations/progression-mode.model';
import { ExamType } from 'app/entities/enumerations/exam-type.model';
import { Riwayats } from 'app/entities/enumerations/riwayats.model';
import { Sourate } from 'app/entities/enumerations/sourate.model';
import { Tilawa } from 'app/entities/enumerations/tilawa.model';
import { ProgressionService } from '../service/progression.service';
import { IProgression } from '../progression.model';
import { ProgressionFormService, ProgressionFormGroup } from './progression-form.service';

@Component({
  standalone: true,
  selector: 'jhi-progression-update',
  templateUrl: './progression-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProgressionUpdateComponent implements OnInit {
  isSaving = false;
  progression: IProgression | null = null;
  attendanceValues = Object.keys(Attendance);
  progressionModeValues = Object.keys(ProgressionMode);
  examTypeValues = Object.keys(ExamType);
  riwayatsValues = Object.keys(Riwayats);
  sourateValues = Object.keys(Sourate);
  tilawaValues = Object.keys(Tilawa);

  sessionInstancesSharedCollection: ISessionInstance[] = [];
  userCustomsSharedCollection: IUserCustom[] = [];

  editForm: ProgressionFormGroup = this.progressionFormService.createProgressionFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected progressionService: ProgressionService,
    protected progressionFormService: ProgressionFormService,
    protected sessionInstanceService: SessionInstanceService,
    protected userCustomService: UserCustomService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareSessionInstance = (o1: ISessionInstance | null, o2: ISessionInstance | null): boolean =>
    this.sessionInstanceService.compareSessionInstance(o1, o2);

  compareUserCustom = (o1: IUserCustom | null, o2: IUserCustom | null): boolean => this.userCustomService.compareUserCustom(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ progression }) => {
      this.progression = progression;
      if (progression) {
        this.updateForm(progression);
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
    const progression = this.progressionFormService.getProgression(this.editForm);
    if (progression.id !== null) {
      this.subscribeToSaveResponse(this.progressionService.update(progression));
    } else {
      this.subscribeToSaveResponse(this.progressionService.create(progression));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProgression>>): void {
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

  protected updateForm(progression: IProgression): void {
    this.progression = progression;
    this.progressionFormService.resetForm(this.editForm, progression);

    this.sessionInstancesSharedCollection = this.sessionInstanceService.addSessionInstanceToCollectionIfMissing<ISessionInstance>(
      this.sessionInstancesSharedCollection,
      progression.sessionInstance,
    );
    this.userCustomsSharedCollection = this.userCustomService.addUserCustomToCollectionIfMissing<IUserCustom>(
      this.userCustomsSharedCollection,
      progression.student,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sessionInstanceService
      .query()
      .pipe(map((res: HttpResponse<ISessionInstance[]>) => res.body ?? []))
      .pipe(
        map((sessionInstances: ISessionInstance[]) =>
          this.sessionInstanceService.addSessionInstanceToCollectionIfMissing<ISessionInstance>(
            sessionInstances,
            this.progression?.sessionInstance,
          ),
        ),
      )
      .subscribe((sessionInstances: ISessionInstance[]) => (this.sessionInstancesSharedCollection = sessionInstances));

    this.userCustomService
      .query()
      .pipe(map((res: HttpResponse<IUserCustom[]>) => res.body ?? []))
      .pipe(
        map((userCustoms: IUserCustom[]) =>
          this.userCustomService.addUserCustomToCollectionIfMissing<IUserCustom>(userCustoms, this.progression?.student),
        ),
      )
      .subscribe((userCustoms: IUserCustom[]) => (this.userCustomsSharedCollection = userCustoms));
  }
}
