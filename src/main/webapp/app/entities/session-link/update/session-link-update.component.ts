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
import { SessionProvider } from 'app/entities/enumerations/session-provider.model';
import { SessionLinkService } from '../service/session-link.service';
import { ISessionLink } from '../session-link.model';
import { SessionLinkFormService, SessionLinkFormGroup } from './session-link-form.service';

@Component({
  standalone: true,
  selector: 'jhi-session-link-update',
  templateUrl: './session-link-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SessionLinkUpdateComponent implements OnInit {
  isSaving = false;
  sessionLink: ISessionLink | null = null;
  sessionProviderValues = Object.keys(SessionProvider);

  editForm: SessionLinkFormGroup = this.sessionLinkFormService.createSessionLinkFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected sessionLinkService: SessionLinkService,
    protected sessionLinkFormService: SessionLinkFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sessionLink }) => {
      this.sessionLink = sessionLink;
      if (sessionLink) {
        this.updateForm(sessionLink);
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
    const sessionLink = this.sessionLinkFormService.getSessionLink(this.editForm);
    if (sessionLink.id !== null) {
      this.subscribeToSaveResponse(this.sessionLinkService.update(sessionLink));
    } else {
      this.subscribeToSaveResponse(this.sessionLinkService.create(sessionLink));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISessionLink>>): void {
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

  protected updateForm(sessionLink: ISessionLink): void {
    this.sessionLink = sessionLink;
    this.sessionLinkFormService.resetForm(this.editForm, sessionLink);
  }
}
