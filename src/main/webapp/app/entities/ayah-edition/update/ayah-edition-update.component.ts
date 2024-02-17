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
import { AyahEditionService } from '../service/ayah-edition.service';
import { IAyahEdition } from '../ayah-edition.model';
import { AyahEditionFormService, AyahEditionFormGroup } from './ayah-edition-form.service';

@Component({
  standalone: true,
  selector: 'jhi-ayah-edition-update',
  templateUrl: './ayah-edition-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AyahEditionUpdateComponent implements OnInit {
  isSaving = false;
  ayahEdition: IAyahEdition | null = null;

  editForm: AyahEditionFormGroup = this.ayahEditionFormService.createAyahEditionFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected ayahEditionService: AyahEditionService,
    protected ayahEditionFormService: AyahEditionFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ayahEdition }) => {
      this.ayahEdition = ayahEdition;
      if (ayahEdition) {
        this.updateForm(ayahEdition);
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
    const ayahEdition = this.ayahEditionFormService.getAyahEdition(this.editForm);
    if (ayahEdition.id !== null) {
      this.subscribeToSaveResponse(this.ayahEditionService.update(ayahEdition));
    } else {
      this.subscribeToSaveResponse(this.ayahEditionService.create(ayahEdition));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAyahEdition>>): void {
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

  protected updateForm(ayahEdition: IAyahEdition): void {
    this.ayahEdition = ayahEdition;
    this.ayahEditionFormService.resetForm(this.editForm, ayahEdition);
  }
}
