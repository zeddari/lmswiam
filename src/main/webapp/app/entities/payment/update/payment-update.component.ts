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
import { IEnrolement } from 'app/entities/enrolement/enrolement.model';
import { EnrolementService } from 'app/entities/enrolement/service/enrolement.service';
import { ISponsoring } from 'app/entities/sponsoring/sponsoring.model';
import { SponsoringService } from 'app/entities/sponsoring/service/sponsoring.service';
import { ISession } from 'app/entities/session/session.model';
import { SessionService } from 'app/entities/session/service/session.service';
import { ICurrency } from 'app/entities/currency/currency.model';
import { CurrencyService } from 'app/entities/currency/service/currency.service';
import { PaymentMode } from 'app/entities/enumerations/payment-mode.model';
import { PaymentType } from 'app/entities/enumerations/payment-type.model';
import { PaymentService } from '../service/payment.service';
import { IPayment } from '../payment.model';
import { PaymentFormService, PaymentFormGroup } from './payment-form.service';

@Component({
  standalone: true,
  selector: 'jhi-payment-update',
  templateUrl: './payment-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PaymentUpdateComponent implements OnInit {
  isSaving = false;
  payment: IPayment | null = null;
  paymentModeValues = Object.keys(PaymentMode);
  paymentTypeValues = Object.keys(PaymentType);

  sitesSharedCollection: ISite[] = [];
  enrolementsSharedCollection: IEnrolement[] = [];
  sponsoringsSharedCollection: ISponsoring[] = [];
  sessionsSharedCollection: ISession[] = [];
  currenciesSharedCollection: ICurrency[] = [];

  editForm: PaymentFormGroup = this.paymentFormService.createPaymentFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected paymentService: PaymentService,
    protected paymentFormService: PaymentFormService,
    protected siteService: SiteService,
    protected enrolementService: EnrolementService,
    protected sponsoringService: SponsoringService,
    protected sessionService: SessionService,
    protected currencyService: CurrencyService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareSite = (o1: ISite | null, o2: ISite | null): boolean => this.siteService.compareSite(o1, o2);

  compareEnrolement = (o1: IEnrolement | null, o2: IEnrolement | null): boolean => this.enrolementService.compareEnrolement(o1, o2);

  compareSponsoring = (o1: ISponsoring | null, o2: ISponsoring | null): boolean => this.sponsoringService.compareSponsoring(o1, o2);

  compareSession = (o1: ISession | null, o2: ISession | null): boolean => this.sessionService.compareSession(o1, o2);

  compareCurrency = (o1: ICurrency | null, o2: ICurrency | null): boolean => this.currencyService.compareCurrency(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ payment }) => {
      this.payment = payment;
      if (payment) {
        this.updateForm(payment);
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
    const payment = this.paymentFormService.getPayment(this.editForm);
    if (payment.id !== null) {
      this.subscribeToSaveResponse(this.paymentService.update(payment));
    } else {
      this.subscribeToSaveResponse(this.paymentService.create(payment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPayment>>): void {
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

  protected updateForm(payment: IPayment): void {
    this.payment = payment;
    this.paymentFormService.resetForm(this.editForm, payment);

    this.sitesSharedCollection = this.siteService.addSiteToCollectionIfMissing<ISite>(this.sitesSharedCollection, payment.site9);
    this.enrolementsSharedCollection = this.enrolementService.addEnrolementToCollectionIfMissing<IEnrolement>(
      this.enrolementsSharedCollection,
      payment.enrolment,
    );
    this.sponsoringsSharedCollection = this.sponsoringService.addSponsoringToCollectionIfMissing<ISponsoring>(
      this.sponsoringsSharedCollection,
      payment.sponsoring,
    );
    this.sessionsSharedCollection = this.sessionService.addSessionToCollectionIfMissing<ISession>(
      this.sessionsSharedCollection,
      payment.session,
    );
    this.currenciesSharedCollection = this.currencyService.addCurrencyToCollectionIfMissing<ICurrency>(
      this.currenciesSharedCollection,
      payment.currency,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.siteService
      .query()
      .pipe(map((res: HttpResponse<ISite[]>) => res.body ?? []))
      .pipe(map((sites: ISite[]) => this.siteService.addSiteToCollectionIfMissing<ISite>(sites, this.payment?.site9)))
      .subscribe((sites: ISite[]) => (this.sitesSharedCollection = sites));

    this.enrolementService
      .query()
      .pipe(map((res: HttpResponse<IEnrolement[]>) => res.body ?? []))
      .pipe(
        map((enrolements: IEnrolement[]) =>
          this.enrolementService.addEnrolementToCollectionIfMissing<IEnrolement>(enrolements, this.payment?.enrolment),
        ),
      )
      .subscribe((enrolements: IEnrolement[]) => (this.enrolementsSharedCollection = enrolements));

    this.sponsoringService
      .query()
      .pipe(map((res: HttpResponse<ISponsoring[]>) => res.body ?? []))
      .pipe(
        map((sponsorings: ISponsoring[]) =>
          this.sponsoringService.addSponsoringToCollectionIfMissing<ISponsoring>(sponsorings, this.payment?.sponsoring),
        ),
      )
      .subscribe((sponsorings: ISponsoring[]) => (this.sponsoringsSharedCollection = sponsorings));

    this.sessionService
      .query()
      .pipe(map((res: HttpResponse<ISession[]>) => res.body ?? []))
      .pipe(map((sessions: ISession[]) => this.sessionService.addSessionToCollectionIfMissing<ISession>(sessions, this.payment?.session)))
      .subscribe((sessions: ISession[]) => (this.sessionsSharedCollection = sessions));

    this.currencyService
      .query()
      .pipe(map((res: HttpResponse<ICurrency[]>) => res.body ?? []))
      .pipe(
        map((currencies: ICurrency[]) =>
          this.currencyService.addCurrencyToCollectionIfMissing<ICurrency>(currencies, this.payment?.currency),
        ),
      )
      .subscribe((currencies: ICurrency[]) => (this.currenciesSharedCollection = currencies));
  }
}
