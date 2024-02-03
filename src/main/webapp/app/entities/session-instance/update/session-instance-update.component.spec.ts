import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ISessionLink } from 'app/entities/session-link/session-link.model';
import { SessionLinkService } from 'app/entities/session-link/service/session-link.service';
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
import { ISession } from 'app/entities/session/session.model';
import { SessionService } from 'app/entities/session/service/session.service';
import { ISessionInstance } from '../session-instance.model';
import { SessionInstanceService } from '../service/session-instance.service';
import { SessionInstanceFormService } from './session-instance-form.service';

import { SessionInstanceUpdateComponent } from './session-instance-update.component';

describe('SessionInstance Management Update Component', () => {
  let comp: SessionInstanceUpdateComponent;
  let fixture: ComponentFixture<SessionInstanceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sessionInstanceFormService: SessionInstanceFormService;
  let sessionInstanceService: SessionInstanceService;
  let sessionLinkService: SessionLinkService;
  let siteService: SiteService;
  let sessionService: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), SessionInstanceUpdateComponent],
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
      .overrideTemplate(SessionInstanceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SessionInstanceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sessionInstanceFormService = TestBed.inject(SessionInstanceFormService);
    sessionInstanceService = TestBed.inject(SessionInstanceService);
    sessionLinkService = TestBed.inject(SessionLinkService);
    siteService = TestBed.inject(SiteService);
    sessionService = TestBed.inject(SessionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SessionLink query and add missing value', () => {
      const sessionInstance: ISessionInstance = { id: 456 };
      const links: ISessionLink[] = [{ id: 864 }];
      sessionInstance.links = links;

      const sessionLinkCollection: ISessionLink[] = [{ id: 11862 }];
      jest.spyOn(sessionLinkService, 'query').mockReturnValue(of(new HttpResponse({ body: sessionLinkCollection })));
      const additionalSessionLinks = [...links];
      const expectedCollection: ISessionLink[] = [...additionalSessionLinks, ...sessionLinkCollection];
      jest.spyOn(sessionLinkService, 'addSessionLinkToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sessionInstance });
      comp.ngOnInit();

      expect(sessionLinkService.query).toHaveBeenCalled();
      expect(sessionLinkService.addSessionLinkToCollectionIfMissing).toHaveBeenCalledWith(
        sessionLinkCollection,
        ...additionalSessionLinks.map(expect.objectContaining),
      );
      expect(comp.sessionLinksSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Site query and add missing value', () => {
      const sessionInstance: ISessionInstance = { id: 456 };
      const site16: ISite = { id: 18573 };
      sessionInstance.site16 = site16;

      const siteCollection: ISite[] = [{ id: 7368 }];
      jest.spyOn(siteService, 'query').mockReturnValue(of(new HttpResponse({ body: siteCollection })));
      const additionalSites = [site16];
      const expectedCollection: ISite[] = [...additionalSites, ...siteCollection];
      jest.spyOn(siteService, 'addSiteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sessionInstance });
      comp.ngOnInit();

      expect(siteService.query).toHaveBeenCalled();
      expect(siteService.addSiteToCollectionIfMissing).toHaveBeenCalledWith(
        siteCollection,
        ...additionalSites.map(expect.objectContaining),
      );
      expect(comp.sitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Session query and add missing value', () => {
      const sessionInstance: ISessionInstance = { id: 456 };
      const session1: ISession = { id: 7703 };
      sessionInstance.session1 = session1;

      const sessionCollection: ISession[] = [{ id: 3875 }];
      jest.spyOn(sessionService, 'query').mockReturnValue(of(new HttpResponse({ body: sessionCollection })));
      const additionalSessions = [session1];
      const expectedCollection: ISession[] = [...additionalSessions, ...sessionCollection];
      jest.spyOn(sessionService, 'addSessionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sessionInstance });
      comp.ngOnInit();

      expect(sessionService.query).toHaveBeenCalled();
      expect(sessionService.addSessionToCollectionIfMissing).toHaveBeenCalledWith(
        sessionCollection,
        ...additionalSessions.map(expect.objectContaining),
      );
      expect(comp.sessionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sessionInstance: ISessionInstance = { id: 456 };
      const links: ISessionLink = { id: 28667 };
      sessionInstance.links = [links];
      const site16: ISite = { id: 28851 };
      sessionInstance.site16 = site16;
      const session1: ISession = { id: 30878 };
      sessionInstance.session1 = session1;

      activatedRoute.data = of({ sessionInstance });
      comp.ngOnInit();

      expect(comp.sessionLinksSharedCollection).toContain(links);
      expect(comp.sitesSharedCollection).toContain(site16);
      expect(comp.sessionsSharedCollection).toContain(session1);
      expect(comp.sessionInstance).toEqual(sessionInstance);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISessionInstance>>();
      const sessionInstance = { id: 123 };
      jest.spyOn(sessionInstanceFormService, 'getSessionInstance').mockReturnValue(sessionInstance);
      jest.spyOn(sessionInstanceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sessionInstance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sessionInstance }));
      saveSubject.complete();

      // THEN
      expect(sessionInstanceFormService.getSessionInstance).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sessionInstanceService.update).toHaveBeenCalledWith(expect.objectContaining(sessionInstance));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISessionInstance>>();
      const sessionInstance = { id: 123 };
      jest.spyOn(sessionInstanceFormService, 'getSessionInstance').mockReturnValue({ id: null });
      jest.spyOn(sessionInstanceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sessionInstance: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sessionInstance }));
      saveSubject.complete();

      // THEN
      expect(sessionInstanceFormService.getSessionInstance).toHaveBeenCalled();
      expect(sessionInstanceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISessionInstance>>();
      const sessionInstance = { id: 123 };
      jest.spyOn(sessionInstanceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sessionInstance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sessionInstanceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSessionLink', () => {
      it('Should forward to sessionLinkService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(sessionLinkService, 'compareSessionLink');
        comp.compareSessionLink(entity, entity2);
        expect(sessionLinkService.compareSessionLink).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSite', () => {
      it('Should forward to siteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(siteService, 'compareSite');
        comp.compareSite(entity, entity2);
        expect(siteService.compareSite).toHaveBeenCalledWith(entity, entity2);
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
  });
});
