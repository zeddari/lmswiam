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
import { IGroup } from 'app/entities/group/group.model';
import { GroupService } from 'app/entities/group/service/group.service';
import { ITopic } from 'app/entities/topic/topic.model';
import { TopicService } from 'app/entities/topic/service/topic.service';
import { ICertificate } from '../certificate.model';
import { CertificateService } from '../service/certificate.service';
import { CertificateFormService } from './certificate-form.service';

import { CertificateUpdateComponent } from './certificate-update.component';

describe('Certificate Management Update Component', () => {
  let comp: CertificateUpdateComponent;
  let fixture: ComponentFixture<CertificateUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let certificateFormService: CertificateFormService;
  let certificateService: CertificateService;
  let siteService: SiteService;
  let userCustomService: UserCustomService;
  let groupService: GroupService;
  let topicService: TopicService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CertificateUpdateComponent],
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
      .overrideTemplate(CertificateUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CertificateUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    certificateFormService = TestBed.inject(CertificateFormService);
    certificateService = TestBed.inject(CertificateService);
    siteService = TestBed.inject(SiteService);
    userCustomService = TestBed.inject(UserCustomService);
    groupService = TestBed.inject(GroupService);
    topicService = TestBed.inject(TopicService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Site query and add missing value', () => {
      const certificate: ICertificate = { id: 456 };
      const site19: ISite = { id: 23747 };
      certificate.site19 = site19;

      const siteCollection: ISite[] = [{ id: 4709 }];
      jest.spyOn(siteService, 'query').mockReturnValue(of(new HttpResponse({ body: siteCollection })));
      const additionalSites = [site19];
      const expectedCollection: ISite[] = [...additionalSites, ...siteCollection];
      jest.spyOn(siteService, 'addSiteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ certificate });
      comp.ngOnInit();

      expect(siteService.query).toHaveBeenCalled();
      expect(siteService.addSiteToCollectionIfMissing).toHaveBeenCalledWith(
        siteCollection,
        ...additionalSites.map(expect.objectContaining),
      );
      expect(comp.sitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UserCustom query and add missing value', () => {
      const certificate: ICertificate = { id: 456 };
      const userCustom6: IUserCustom = { id: 17635 };
      certificate.userCustom6 = userCustom6;

      const userCustomCollection: IUserCustom[] = [{ id: 18923 }];
      jest.spyOn(userCustomService, 'query').mockReturnValue(of(new HttpResponse({ body: userCustomCollection })));
      const additionalUserCustoms = [userCustom6];
      const expectedCollection: IUserCustom[] = [...additionalUserCustoms, ...userCustomCollection];
      jest.spyOn(userCustomService, 'addUserCustomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ certificate });
      comp.ngOnInit();

      expect(userCustomService.query).toHaveBeenCalled();
      expect(userCustomService.addUserCustomToCollectionIfMissing).toHaveBeenCalledWith(
        userCustomCollection,
        ...additionalUserCustoms.map(expect.objectContaining),
      );
      expect(comp.userCustomsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Group query and add missing value', () => {
      const certificate: ICertificate = { id: 456 };
      const comitte: IGroup = { id: 5269 };
      certificate.comitte = comitte;

      const groupCollection: IGroup[] = [{ id: 1332 }];
      jest.spyOn(groupService, 'query').mockReturnValue(of(new HttpResponse({ body: groupCollection })));
      const additionalGroups = [comitte];
      const expectedCollection: IGroup[] = [...additionalGroups, ...groupCollection];
      jest.spyOn(groupService, 'addGroupToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ certificate });
      comp.ngOnInit();

      expect(groupService.query).toHaveBeenCalled();
      expect(groupService.addGroupToCollectionIfMissing).toHaveBeenCalledWith(
        groupCollection,
        ...additionalGroups.map(expect.objectContaining),
      );
      expect(comp.groupsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Topic query and add missing value', () => {
      const certificate: ICertificate = { id: 456 };
      const topic4: ITopic = { id: 14450 };
      certificate.topic4 = topic4;

      const topicCollection: ITopic[] = [{ id: 17218 }];
      jest.spyOn(topicService, 'query').mockReturnValue(of(new HttpResponse({ body: topicCollection })));
      const additionalTopics = [topic4];
      const expectedCollection: ITopic[] = [...additionalTopics, ...topicCollection];
      jest.spyOn(topicService, 'addTopicToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ certificate });
      comp.ngOnInit();

      expect(topicService.query).toHaveBeenCalled();
      expect(topicService.addTopicToCollectionIfMissing).toHaveBeenCalledWith(
        topicCollection,
        ...additionalTopics.map(expect.objectContaining),
      );
      expect(comp.topicsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const certificate: ICertificate = { id: 456 };
      const site19: ISite = { id: 786 };
      certificate.site19 = site19;
      const userCustom6: IUserCustom = { id: 23430 };
      certificate.userCustom6 = userCustom6;
      const comitte: IGroup = { id: 27596 };
      certificate.comitte = comitte;
      const topic4: ITopic = { id: 6864 };
      certificate.topic4 = topic4;

      activatedRoute.data = of({ certificate });
      comp.ngOnInit();

      expect(comp.sitesSharedCollection).toContain(site19);
      expect(comp.userCustomsSharedCollection).toContain(userCustom6);
      expect(comp.groupsSharedCollection).toContain(comitte);
      expect(comp.topicsSharedCollection).toContain(topic4);
      expect(comp.certificate).toEqual(certificate);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICertificate>>();
      const certificate = { id: 123 };
      jest.spyOn(certificateFormService, 'getCertificate').mockReturnValue(certificate);
      jest.spyOn(certificateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ certificate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: certificate }));
      saveSubject.complete();

      // THEN
      expect(certificateFormService.getCertificate).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(certificateService.update).toHaveBeenCalledWith(expect.objectContaining(certificate));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICertificate>>();
      const certificate = { id: 123 };
      jest.spyOn(certificateFormService, 'getCertificate').mockReturnValue({ id: null });
      jest.spyOn(certificateService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ certificate: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: certificate }));
      saveSubject.complete();

      // THEN
      expect(certificateFormService.getCertificate).toHaveBeenCalled();
      expect(certificateService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICertificate>>();
      const certificate = { id: 123 };
      jest.spyOn(certificateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ certificate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(certificateService.update).toHaveBeenCalled();
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

    describe('compareGroup', () => {
      it('Should forward to groupService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(groupService, 'compareGroup');
        comp.compareGroup(entity, entity2);
        expect(groupService.compareGroup).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTopic', () => {
      it('Should forward to topicService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(topicService, 'compareTopic');
        comp.compareTopic(entity, entity2);
        expect(topicService.compareTopic).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
