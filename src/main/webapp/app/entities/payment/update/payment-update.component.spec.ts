import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IEnrolement } from 'app/entities/enrolement/enrolement.model';
import { EnrolementService } from 'app/entities/enrolement/service/enrolement.service';
import { ISponsoring } from 'app/entities/sponsoring/sponsoring.model';
import { SponsoringService } from 'app/entities/sponsoring/service/sponsoring.service';
import { ISession } from 'app/entities/session/session.model';
import { SessionService } from 'app/entities/session/service/session.service';
import { ICurrency } from 'app/entities/currency/currency.model';
import { CurrencyService } from 'app/entities/currency/service/currency.service';
import { IPayment } from '../payment.model';
import { PaymentService } from '../service/payment.service';
import { PaymentFormService } from './payment-form.service';

import { PaymentUpdateComponent } from './payment-update.component';

describe('Payment Management Update Component', () => {
  let comp: PaymentUpdateComponent;
  let fixture: ComponentFixture<PaymentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let paymentFormService: PaymentFormService;
  let paymentService: PaymentService;
  let enrolementService: EnrolementService;
  let sponsoringService: SponsoringService;
  let sessionService: SessionService;
  let currencyService: CurrencyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), PaymentUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PaymentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaymentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paymentFormService = TestBed.inject(PaymentFormService);
    paymentService = TestBed.inject(PaymentService);
    enrolementService = TestBed.inject(EnrolementService);
    sponsoringService = TestBed.inject(SponsoringService);
    sessionService = TestBed.inject(SessionService);
    currencyService = TestBed.inject(CurrencyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Enrolement query and add missing value', () => {
      const payment: IPayment = { id: 456 };
      const enrolment: IEnrolement = { id: 5952 };
      payment.enrolment = enrolment;

      const enrolementCollection: IEnrolement[] = [{ id: 19755 }];
      jest.spyOn(enrolementService, 'query').mockReturnValue(of(new HttpResponse({ body: enrolementCollection })));
      const additionalEnrolements = [enrolment];
      const expectedCollection: IEnrolement[] = [...additionalEnrolements, ...enrolementCollection];
      jest.spyOn(enrolementService, 'addEnrolementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      expect(enrolementService.query).toHaveBeenCalled();
      expect(enrolementService.addEnrolementToCollectionIfMissing).toHaveBeenCalledWith(
        enrolementCollection,
        ...additionalEnrolements.map(expect.objectContaining),
      );
      expect(comp.enrolementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Sponsoring query and add missing value', () => {
      const payment: IPayment = { id: 456 };
      const sponsoring: ISponsoring = { id: 11141 };
      payment.sponsoring = sponsoring;

      const sponsoringCollection: ISponsoring[] = [{ id: 3806 }];
      jest.spyOn(sponsoringService, 'query').mockReturnValue(of(new HttpResponse({ body: sponsoringCollection })));
      const additionalSponsorings = [sponsoring];
      const expectedCollection: ISponsoring[] = [...additionalSponsorings, ...sponsoringCollection];
      jest.spyOn(sponsoringService, 'addSponsoringToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      expect(sponsoringService.query).toHaveBeenCalled();
      expect(sponsoringService.addSponsoringToCollectionIfMissing).toHaveBeenCalledWith(
        sponsoringCollection,
        ...additionalSponsorings.map(expect.objectContaining),
      );
      expect(comp.sponsoringsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Session query and add missing value', () => {
      const payment: IPayment = { id: 456 };
      const session: ISession = { id: 24380 };
      payment.session = session;

      const sessionCollection: ISession[] = [{ id: 3406 }];
      jest.spyOn(sessionService, 'query').mockReturnValue(of(new HttpResponse({ body: sessionCollection })));
      const additionalSessions = [session];
      const expectedCollection: ISession[] = [...additionalSessions, ...sessionCollection];
      jest.spyOn(sessionService, 'addSessionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      expect(sessionService.query).toHaveBeenCalled();
      expect(sessionService.addSessionToCollectionIfMissing).toHaveBeenCalledWith(
        sessionCollection,
        ...additionalSessions.map(expect.objectContaining),
      );
      expect(comp.sessionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Currency query and add missing value', () => {
      const payment: IPayment = { id: 456 };
      const currency: ICurrency = { id: 11148 };
      payment.currency = currency;

      const currencyCollection: ICurrency[] = [{ id: 23831 }];
      jest.spyOn(currencyService, 'query').mockReturnValue(of(new HttpResponse({ body: currencyCollection })));
      const additionalCurrencies = [currency];
      const expectedCollection: ICurrency[] = [...additionalCurrencies, ...currencyCollection];
      jest.spyOn(currencyService, 'addCurrencyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      expect(currencyService.query).toHaveBeenCalled();
      expect(currencyService.addCurrencyToCollectionIfMissing).toHaveBeenCalledWith(
        currencyCollection,
        ...additionalCurrencies.map(expect.objectContaining),
      );
      expect(comp.currenciesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const payment: IPayment = { id: 456 };
      const enrolment: IEnrolement = { id: 3049 };
      payment.enrolment = enrolment;
      const sponsoring: ISponsoring = { id: 6241 };
      payment.sponsoring = sponsoring;
      const session: ISession = { id: 24241 };
      payment.session = session;
      const currency: ICurrency = { id: 17416 };
      payment.currency = currency;

      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      expect(comp.enrolementsSharedCollection).toContain(enrolment);
      expect(comp.sponsoringsSharedCollection).toContain(sponsoring);
      expect(comp.sessionsSharedCollection).toContain(session);
      expect(comp.currenciesSharedCollection).toContain(currency);
      expect(comp.payment).toEqual(payment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPayment>>();
      const payment = { id: 123 };
      jest.spyOn(paymentFormService, 'getPayment').mockReturnValue(payment);
      jest.spyOn(paymentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: payment }));
      saveSubject.complete();

      // THEN
      expect(paymentFormService.getPayment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(paymentService.update).toHaveBeenCalledWith(expect.objectContaining(payment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPayment>>();
      const payment = { id: 123 };
      jest.spyOn(paymentFormService, 'getPayment').mockReturnValue({ id: null });
      jest.spyOn(paymentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ payment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: payment }));
      saveSubject.complete();

      // THEN
      expect(paymentFormService.getPayment).toHaveBeenCalled();
      expect(paymentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPayment>>();
      const payment = { id: 123 };
      jest.spyOn(paymentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paymentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEnrolement', () => {
      it('Should forward to enrolementService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(enrolementService, 'compareEnrolement');
        comp.compareEnrolement(entity, entity2);
        expect(enrolementService.compareEnrolement).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSponsoring', () => {
      it('Should forward to sponsoringService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(sponsoringService, 'compareSponsoring');
        comp.compareSponsoring(entity, entity2);
        expect(sponsoringService.compareSponsoring).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSession', () => {
      it('Should forward to sessionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(sessionService, 'compareSession');
        comp.compareSession(entity, entity2);
        expect(sessionService.compareSession).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCurrency', () => {
      it('Should forward to currencyService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(currencyService, 'compareCurrency');
        comp.compareCurrency(entity, entity2);
        expect(currencyService.compareCurrency).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
