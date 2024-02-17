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
import { AyahsService } from '../service/ayahs.service';
import { IAyahs } from '../ayahs.model';
import { AyahsFormService, AyahsFormGroup } from './ayahs-form.service';

@Component({
  standalone: true,
  selector: 'jhi-ayahs-update',
  templateUrl: './ayahs-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AyahsUpdateComponent implements OnInit {
  isSaving = false;
  ayahs: IAyahs | null = null;

  editForm: AyahsFormGroup = this.ayahsFormService.createAyahsFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected ayahsService: AyahsService,
    protected ayahsFormService: AyahsFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ayahs }) => {
      this.ayahs = ayahs;
      if (ayahs) {
        this.updateForm(ayahs);
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
    const ayahs = this.ayahsFormService.getAyahs(this.editForm);
    if (ayahs.id !== null) {
      this.subscribeToSaveResponse(this.ayahsService.update(ayahs));
    } else {
      this.subscribeToSaveResponse(this.ayahsService.create(ayahs));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAyahs>>): void {
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

  protected updateForm(ayahs: IAyahs): void {
    this.ayahs = ayahs;
    this.ayahsFormService.resetForm(this.editForm, ayahs);
  }
}
