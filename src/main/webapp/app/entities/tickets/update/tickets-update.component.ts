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
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
import { IUserCustom } from 'app/entities/user-custom/user-custom.model';
import { UserCustomService } from 'app/entities/user-custom/service/user-custom.service';
import { TicketSubjects } from 'app/entities/enumerations/ticket-subjects.model';
import { TicketStatus } from 'app/entities/enumerations/ticket-status.model';
import { TicketsService } from '../service/tickets.service';
import { ITickets } from '../tickets.model';
import { TicketsFormService, TicketsFormGroup } from './tickets-form.service';

@Component({
  standalone: true,
  selector: 'jhi-tickets-update',
  templateUrl: './tickets-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TicketsUpdateComponent implements OnInit {
  isSaving = false;
  tickets: ITickets | null = null;
  ticketSubjectsValues = Object.keys(TicketSubjects);
  ticketStatusValues = Object.keys(TicketStatus);

  sitesSharedCollection: ISite[] = [];
  userCustomsSharedCollection: IUserCustom[] = [];

  editForm: TicketsFormGroup = this.ticketsFormService.createTicketsFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected ticketsService: TicketsService,
    protected ticketsFormService: TicketsFormService,
    protected siteService: SiteService,
    protected userCustomService: UserCustomService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareSite = (o1: ISite | null, o2: ISite | null): boolean => this.siteService.compareSite(o1, o2);

  compareUserCustom = (o1: IUserCustom | null, o2: IUserCustom | null): boolean => this.userCustomService.compareUserCustom(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tickets }) => {
      this.tickets = tickets;
      if (tickets) {
        this.updateForm(tickets);
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
    const tickets = this.ticketsFormService.getTickets(this.editForm);
    if (tickets.id !== null) {
      this.subscribeToSaveResponse(this.ticketsService.update(tickets));
    } else {
      this.subscribeToSaveResponse(this.ticketsService.create(tickets));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITickets>>): void {
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

  protected updateForm(tickets: ITickets): void {
    this.tickets = tickets;
    this.ticketsFormService.resetForm(this.editForm, tickets);

    this.sitesSharedCollection = this.siteService.addSiteToCollectionIfMissing<ISite>(this.sitesSharedCollection, tickets.site18);
    this.userCustomsSharedCollection = this.userCustomService.addUserCustomToCollectionIfMissing<IUserCustom>(
      this.userCustomsSharedCollection,
      tickets.userCustom5,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.siteService
      .query()
      .pipe(map((res: HttpResponse<ISite[]>) => res.body ?? []))
      .pipe(map((sites: ISite[]) => this.siteService.addSiteToCollectionIfMissing<ISite>(sites, this.tickets?.site18)))
      .subscribe((sites: ISite[]) => (this.sitesSharedCollection = sites));

    this.userCustomService
      .query()
      .pipe(map((res: HttpResponse<IUserCustom[]>) => res.body ?? []))
      .pipe(
        map((userCustoms: IUserCustom[]) =>
          this.userCustomService.addUserCustomToCollectionIfMissing<IUserCustom>(userCustoms, this.tickets?.userCustom5),
        ),
      )
      .subscribe((userCustoms: IUserCustom[]) => (this.userCustomsSharedCollection = userCustoms));
  }
}
