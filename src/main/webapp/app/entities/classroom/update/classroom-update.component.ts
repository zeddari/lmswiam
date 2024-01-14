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
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
import { ClassroomService } from '../service/classroom.service';
import { IClassroom } from '../classroom.model';
import { ClassroomFormService, ClassroomFormGroup } from './classroom-form.service';

@Component({
  standalone: true,
  selector: 'jhi-classroom-update',
  templateUrl: './classroom-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ClassroomUpdateComponent implements OnInit {
  isSaving = false;
  classroom: IClassroom | null = null;

  sitesSharedCollection: ISite[] = [];

  editForm: ClassroomFormGroup = this.classroomFormService.createClassroomFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected classroomService: ClassroomService,
    protected classroomFormService: ClassroomFormService,
    protected siteService: SiteService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareSite = (o1: ISite | null, o2: ISite | null): boolean => this.siteService.compareSite(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classroom }) => {
      this.classroom = classroom;
      if (classroom) {
        this.updateForm(classroom);
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
    const classroom = this.classroomFormService.getClassroom(this.editForm);
    if (classroom.id !== null) {
      this.subscribeToSaveResponse(this.classroomService.update(classroom));
    } else {
      this.subscribeToSaveResponse(this.classroomService.create(classroom));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassroom>>): void {
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

  protected updateForm(classroom: IClassroom): void {
    this.classroom = classroom;
    this.classroomFormService.resetForm(this.editForm, classroom);

    this.sitesSharedCollection = this.siteService.addSiteToCollectionIfMissing<ISite>(this.sitesSharedCollection, classroom.site);
  }

  protected loadRelationshipsOptions(): void {
    this.siteService
      .query()
      .pipe(map((res: HttpResponse<ISite[]>) => res.body ?? []))
      .pipe(map((sites: ISite[]) => this.siteService.addSiteToCollectionIfMissing<ISite>(sites, this.classroom?.site)))
      .subscribe((sites: ISite[]) => (this.sitesSharedCollection = sites));
  }
}
