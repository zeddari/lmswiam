import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
import { IUserCustom } from 'app/entities/user-custom/user-custom.model';
import { UserCustomService } from 'app/entities/user-custom/service/user-custom.service';
import { ITickets } from '../tickets.model';
import { TicketsService } from '../service/tickets.service';
import { TicketsFormService } from './tickets-form.service';

import { TicketsUpdateComponent } from './tickets-update.component';

describe('Tickets Management Update Component', () => {
  let comp: TicketsUpdateComponent;
  let fixture: ComponentFixture<TicketsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ticketsFormService: TicketsFormService;
  let ticketsService: TicketsService;
  let siteService: SiteService;
  let userCustomService: UserCustomService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TicketsUpdateComponent],
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
      .overrideTemplate(TicketsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TicketsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ticketsFormService = TestBed.inject(TicketsFormService);
    ticketsService = TestBed.inject(TicketsService);
    siteService = TestBed.inject(SiteService);
    userCustomService = TestBed.inject(UserCustomService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Site query and add missing value', () => {
      const tickets: ITickets = { id: 456 };
      const site18: ISite = { id: 4638 };
      tickets.site18 = site18;

      const siteCollection: ISite[] = [{ id: 10890 }];
      jest.spyOn(siteService, 'query').mockReturnValue(of(new HttpResponse({ body: siteCollection })));
      const additionalSites = [site18];
      const expectedCollection: ISite[] = [...additionalSites, ...siteCollection];
      jest.spyOn(siteService, 'addSiteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tickets });
      comp.ngOnInit();

      expect(siteService.query).toHaveBeenCalled();
      expect(siteService.addSiteToCollectionIfMissing).toHaveBeenCalledWith(
        siteCollection,
        ...additionalSites.map(expect.objectContaining),
      );
      expect(comp.sitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UserCustom query and add missing value', () => {
      const tickets: ITickets = { id: 456 };
      const userCustom5: IUserCustom = { id: 8168 };
      tickets.userCustom5 = userCustom5;

      const userCustomCollection: IUserCustom[] = [{ id: 24448 }];
      jest.spyOn(userCustomService, 'query').mockReturnValue(of(new HttpResponse({ body: userCustomCollection })));
      const additionalUserCustoms = [userCustom5];
      const expectedCollection: IUserCustom[] = [...additionalUserCustoms, ...userCustomCollection];
      jest.spyOn(userCustomService, 'addUserCustomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tickets });
      comp.ngOnInit();

      expect(userCustomService.query).toHaveBeenCalled();
      expect(userCustomService.addUserCustomToCollectionIfMissing).toHaveBeenCalledWith(
        userCustomCollection,
        ...additionalUserCustoms.map(expect.objectContaining),
      );
      expect(comp.userCustomsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const tickets: ITickets = { id: 456 };
      const site18: ISite = { id: 27867 };
      tickets.site18 = site18;
      const userCustom5: IUserCustom = { id: 29830 };
      tickets.userCustom5 = userCustom5;

      activatedRoute.data = of({ tickets });
      comp.ngOnInit();

      expect(comp.sitesSharedCollection).toContain(site18);
      expect(comp.userCustomsSharedCollection).toContain(userCustom5);
      expect(comp.tickets).toEqual(tickets);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITickets>>();
      const tickets = { id: 123 };
      jest.spyOn(ticketsFormService, 'getTickets').mockReturnValue(tickets);
      jest.spyOn(ticketsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tickets });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tickets }));
      saveSubject.complete();

      // THEN
      expect(ticketsFormService.getTickets).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ticketsService.update).toHaveBeenCalledWith(expect.objectContaining(tickets));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITickets>>();
      const tickets = { id: 123 };
      jest.spyOn(ticketsFormService, 'getTickets').mockReturnValue({ id: null });
      jest.spyOn(ticketsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tickets: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tickets }));
      saveSubject.complete();

      // THEN
      expect(ticketsFormService.getTickets).toHaveBeenCalled();
      expect(ticketsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITickets>>();
      const tickets = { id: 123 };
      jest.spyOn(ticketsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tickets });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ticketsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSite', () => {
      it('Should forward to siteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(siteService, 'compareSite');
        comp.compareSite(entity, entity2);
        expect(siteService.compareSite).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUserCustom', () => {
      it('Should forward to userCustomService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userCustomService, 'compareUserCustom');
        comp.compareUserCustom(entity, entity2);
        expect(userCustomService.compareUserCustom).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
