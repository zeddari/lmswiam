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
import { EditionsService } from '../service/editions.service';
import { IEditions } from '../editions.model';
import { EditionsFormService, EditionsFormGroup } from './editions-form.service';

@Component({
  standalone: true,
  selector: 'jhi-editions-update',
  templateUrl: './editions-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EditionsUpdateComponent implements OnInit {
  isSaving = false;
  editions: IEditions | null = null;

  editForm: EditionsFormGroup = this.editionsFormService.createEditionsFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected editionsService: EditionsService,
    protected editionsFormService: EditionsFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ editions }) => {
      this.editions = editions;
      if (editions) {
        this.updateForm(editions);
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
    const editions = this.editionsFormService.getEditions(this.editForm);
    if (editions.id !== null) {
      this.subscribeToSaveResponse(this.editionsService.update(editions));
    } else {
      this.subscribeToSaveResponse(this.editionsService.create(editions));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEditions>>): void {
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

  protected updateForm(editions: IEditions): void {
    this.editions = editions;
    this.editionsFormService.resetForm(this.editForm, editions);
  }
}
