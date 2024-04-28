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
import { StudentSponsoringService } from '../service/student-sponsoring.service';
import { IStudentSponsoring } from '../student-sponsoring.model';
import { StudentSponsoringFormService, StudentSponsoringFormGroup } from './student-sponsoring-form.service';

@Component({
  standalone: true,
  selector: 'jhi-student-sponsoring-update',
  templateUrl: './student-sponsoring-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class StudentSponsoringUpdateComponent implements OnInit {
  isSaving = false;
  studentSponsoring: IStudentSponsoring | null = null;

  editForm: StudentSponsoringFormGroup = this.studentSponsoringFormService.createStudentSponsoringFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected studentSponsoringService: StudentSponsoringService,
    protected studentSponsoringFormService: StudentSponsoringFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ studentSponsoring }) => {
      this.studentSponsoring = studentSponsoring;
      if (studentSponsoring) {
        this.updateForm(studentSponsoring);
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
    const studentSponsoring = this.studentSponsoringFormService.getStudentSponsoring(this.editForm);
    if (studentSponsoring.id !== null) {
      this.subscribeToSaveResponse(this.studentSponsoringService.update(studentSponsoring));
    } else {
      this.subscribeToSaveResponse(this.studentSponsoringService.create(studentSponsoring));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudentSponsoring>>): void {
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

  protected updateForm(studentSponsoring: IStudentSponsoring): void {
    this.studentSponsoring = studentSponsoring;
    this.studentSponsoringFormService.resetForm(this.editForm, studentSponsoring);
  }
}
