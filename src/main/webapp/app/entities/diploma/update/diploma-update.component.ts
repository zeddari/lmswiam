import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { DiplomaType } from 'app/entities/enumerations/diploma-type.model';
import { DiplomaService } from '../service/diploma.service';
import { IDiploma } from '../diploma.model';
import { DiplomaFormService, DiplomaFormGroup } from './diploma-form.service';

@Component({
  standalone: true,
  selector: 'jhi-diploma-update',
  templateUrl: './diploma-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DiplomaUpdateComponent implements OnInit {
  isSaving = false;
  diploma: IDiploma | null = null;
  diplomaTypeValues = Object.keys(DiplomaType);

  editForm: DiplomaFormGroup = this.diplomaFormService.createDiplomaFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected diplomaService: DiplomaService,
    protected diplomaFormService: DiplomaFormService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ diploma }) => {
      this.diploma = diploma;
      if (diploma) {
        this.updateForm(diploma);
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
    const diploma = this.diplomaFormService.getDiploma(this.editForm);
    if (diploma.id !== null) {
      this.subscribeToSaveResponse(this.diplomaService.update(diploma));
    } else {
      this.subscribeToSaveResponse(this.diplomaService.create(diploma));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDiploma>>): void {
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

  protected updateForm(diploma: IDiploma): void {
    this.diploma = diploma;
    this.diplomaFormService.resetForm(this.editForm, diploma);
  }
}
