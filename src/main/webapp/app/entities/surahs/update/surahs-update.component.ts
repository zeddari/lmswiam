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
import { SurahsService } from '../service/surahs.service';
import { ISurahs } from '../surahs.model';
import { SurahsFormService, SurahsFormGroup } from './surahs-form.service';

@Component({
  standalone: true,
  selector: 'jhi-surahs-update',
  templateUrl: './surahs-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SurahsUpdateComponent implements OnInit {
  isSaving = false;
  surahs: ISurahs | null = null;

  editForm: SurahsFormGroup = this.surahsFormService.createSurahsFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected surahsService: SurahsService,
    protected surahsFormService: SurahsFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ surahs }) => {
      this.surahs = surahs;
      if (surahs) {
        this.updateForm(surahs);
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
    const surahs = this.surahsFormService.getSurahs(this.editForm);
    if (surahs.id !== null) {
      this.subscribeToSaveResponse(this.surahsService.update(surahs));
    } else {
      this.subscribeToSaveResponse(this.surahsService.create(surahs));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISurahs>>): void {
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

  protected updateForm(surahs: ISurahs): void {
    this.surahs = surahs;
    this.surahsFormService.resetForm(this.editForm, surahs);
  }
}
