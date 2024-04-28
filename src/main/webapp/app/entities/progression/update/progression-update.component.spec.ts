import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
import { ISessionInstance } from 'app/entities/session-instance/session-instance.model';
import { SessionInstanceService } from 'app/entities/session-instance/service/session-instance.service';
import { IUserCustom } from 'app/entities/user-custom/user-custom.model';
import { UserCustomService } from 'app/entities/user-custom/service/user-custom.service';
import { IProgression } from '../progression.model';
import { ProgressionService } from '../service/progression.service';
import { ProgressionFormService } from './progression-form.service';

import { ProgressionUpdateComponent } from './progression-update.component';

describe('Progression Management Update Component', () => {
  let comp: ProgressionUpdateComponent;
  let fixture: ComponentFixture<ProgressionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let progressionFormService: ProgressionFormService;
  let progressionService: ProgressionService;
  let siteService: SiteService;
  let sessionInstanceService: SessionInstanceService;
  let userCustomService: UserCustomService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ProgressionUpdateComponent],
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
      .overrideTemplate(ProgressionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProgressionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    progressionFormService = TestBed.inject(ProgressionFormService);
    progressionService = TestBed.inject(ProgressionService);
    siteService = TestBed.inject(SiteService);
    sessionInstanceService = TestBed.inject(SessionInstanceService);
    userCustomService = TestBed.inject(UserCustomService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Site query and add missing value', () => {
      const progression: IProgression = { id: 456 };
      const site17: ISite = { id: 6908 };
      progression.site17 = site17;

      const siteCollection: ISite[] = [{ id: 3345 }];
      jest.spyOn(siteService, 'query').mockReturnValue(of(new HttpResponse({ body: siteCollection })));
      const additionalSites = [site17];
      const expectedCollection: ISite[] = [...additionalSites, ...siteCollection];
      jest.spyOn(siteService, 'addSiteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ progression });
      comp.ngOnInit();

      expect(siteService.query).toHaveBeenCalled();
      expect(siteService.addSiteToCollectionIfMissing).toHaveBeenCalledWith(
        siteCollection,
        ...additionalSites.map(expect.objectContaining),
      );
      expect(comp.sitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SessionInstance query and add missing value', () => {
      const progression: IProgression = { id: 456 };
      const sessionInstance: ISessionInstance = { id: 28968 };
      progression.sessionInstance = sessionInstance;

      const sessionInstanceCollection: ISessionInstance[] = [{ id: 5337 }];
      jest.spyOn(sessionInstanceService, 'query').mockReturnValue(of(new HttpResponse({ body: sessionInstanceCollection })));
      const additionalSessionInstances = [sessionInstance];
      const expectedCollection: ISessionInstance[] = [...additionalSessionInstances, ...sessionInstanceCollection];
      jest.spyOn(sessionInstanceService, 'addSessionInstanceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ progression });
      comp.ngOnInit();

      expect(sessionInstanceService.query).toHaveBeenCalled();
      expect(sessionInstanceService.addSessionInstanceToCollectionIfMissing).toHaveBeenCalledWith(
        sessionInstanceCollection,
        ...additionalSessionInstances.map(expect.objectContaining),
      );
      expect(comp.sessionInstancesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UserCustom query and add missing value', () => {
      const progression: IProgression = { id: 456 };
      const student: IUserCustom = { id: 26848 };
      progression.student = student;

      const userCustomCollection: IUserCustom[] = [{ id: 4350 }];
      jest.spyOn(userCustomService, 'query').mockReturnValue(of(new HttpResponse({ body: userCustomCollection })));
      const additionalUserCustoms = [student];
      const expectedCollection: IUserCustom[] = [...additionalUserCustoms, ...userCustomCollection];
      jest.spyOn(userCustomService, 'addUserCustomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ progression });
      comp.ngOnInit();

      expect(userCustomService.query).toHaveBeenCalled();
      expect(userCustomService.addUserCustomToCollectionIfMissing).toHaveBeenCalledWith(
        userCustomCollection,
        ...additionalUserCustoms.map(expect.objectContaining),
      );
      expect(comp.userCustomsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const progression: IProgression = { id: 456 };
      const site17: ISite = { id: 13992 };
      progression.site17 = site17;
      const sessionInstance: ISessionInstance = { id: 2667 };
      progression.sessionInstance = sessionInstance;
      const student: IUserCustom = { id: 26351 };
      progression.student = student;

      activatedRoute.data = of({ progression });
      comp.ngOnInit();

      expect(comp.sitesSharedCollection).toContain(site17);
      expect(comp.sessionInstancesSharedCollection).toContain(sessionInstance);
      expect(comp.userCustomsSharedCollection).toContain(student);
      expect(comp.progression).toEqual(progression);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProgression>>();
      const progression = { id: 123 };
      jest.spyOn(progressionFormService, 'getProgression').mockReturnValue(progression);
      jest.spyOn(progressionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ progression });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: progression }));
      saveSubject.complete();

      // THEN
      expect(progressionFormService.getProgression).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(progressionService.update).toHaveBeenCalledWith(expect.objectContaining(progression));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProgression>>();
      const progression = { id: 123 };
      jest.spyOn(progressionFormService, 'getProgression').mockReturnValue({ id: null });
      jest.spyOn(progressionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ progression: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: progression }));
      saveSubject.complete();

      // THEN
      expect(progressionFormService.getProgression).toHaveBeenCalled();
      expect(progressionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProgression>>();
      const progression = { id: 123 };
      jest.spyOn(progressionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ progression });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(progressionService.update).toHaveBeenCalled();
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

    describe('compareSessionInstance', () => {
      it('Should forward to sessionInstanceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(sessionInstanceService, 'compareSessionInstance');
        comp.compareSessionInstance(entity, entity2);
        expect(sessionInstanceService.compareSessionInstance).toHaveBeenCalledWith(entity, entity2);
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
