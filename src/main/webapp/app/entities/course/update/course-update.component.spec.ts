import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IUserCustom } from 'app/entities/user-custom/user-custom.model';
import { UserCustomService } from 'app/entities/user-custom/service/user-custom.service';
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
import { ITopic } from 'app/entities/topic/topic.model';
import { TopicService } from 'app/entities/topic/service/topic.service';
import { ICourse } from '../course.model';
import { CourseService } from '../service/course.service';
import { CourseFormService } from './course-form.service';

import { CourseUpdateComponent } from './course-update.component';

describe('Course Management Update Component', () => {
  let comp: CourseUpdateComponent;
  let fixture: ComponentFixture<CourseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let courseFormService: CourseFormService;
  let courseService: CourseService;
  let userCustomService: UserCustomService;
  let siteService: SiteService;
  let topicService: TopicService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CourseUpdateComponent],
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
      .overrideTemplate(CourseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CourseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    courseFormService = TestBed.inject(CourseFormService);
    courseService = TestBed.inject(CourseService);
    userCustomService = TestBed.inject(UserCustomService);
    siteService = TestBed.inject(SiteService);
    topicService = TestBed.inject(TopicService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call UserCustom query and add missing value', () => {
      const course: ICourse = { id: 456 };
      const professors: IUserCustom[] = [{ id: 12805 }];
      course.professors = professors;

      const userCustomCollection: IUserCustom[] = [{ id: 6504 }];
      jest.spyOn(userCustomService, 'query').mockReturnValue(of(new HttpResponse({ body: userCustomCollection })));
      const additionalUserCustoms = [...professors];
      const expectedCollection: IUserCustom[] = [...additionalUserCustoms, ...userCustomCollection];
      jest.spyOn(userCustomService, 'addUserCustomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ course });
      comp.ngOnInit();

      expect(userCustomService.query).toHaveBeenCalled();
      expect(userCustomService.addUserCustomToCollectionIfMissing).toHaveBeenCalledWith(
        userCustomCollection,
        ...additionalUserCustoms.map(expect.objectContaining),
      );
      expect(comp.userCustomsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Site query and add missing value', () => {
      const course: ICourse = { id: 456 };
      const site1: ISite = { id: 19319 };
      course.site1 = site1;

      const siteCollection: ISite[] = [{ id: 29680 }];
      jest.spyOn(siteService, 'query').mockReturnValue(of(new HttpResponse({ body: siteCollection })));
      const additionalSites = [site1];
      const expectedCollection: ISite[] = [...additionalSites, ...siteCollection];
      jest.spyOn(siteService, 'addSiteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ course });
      comp.ngOnInit();

      expect(siteService.query).toHaveBeenCalled();
      expect(siteService.addSiteToCollectionIfMissing).toHaveBeenCalledWith(
        siteCollection,
        ...additionalSites.map(expect.objectContaining),
      );
      expect(comp.sitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Topic query and add missing value', () => {
      const course: ICourse = { id: 456 };
      const topic3: ITopic = { id: 2624 };
      course.topic3 = topic3;

      const topicCollection: ITopic[] = [{ id: 1892 }];
      jest.spyOn(topicService, 'query').mockReturnValue(of(new HttpResponse({ body: topicCollection })));
      const additionalTopics = [topic3];
      const expectedCollection: ITopic[] = [...additionalTopics, ...topicCollection];
      jest.spyOn(topicService, 'addTopicToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ course });
      comp.ngOnInit();

      expect(topicService.query).toHaveBeenCalled();
      expect(topicService.addTopicToCollectionIfMissing).toHaveBeenCalledWith(
        topicCollection,
        ...additionalTopics.map(expect.objectContaining),
      );
      expect(comp.topicsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const course: ICourse = { id: 456 };
      const professors: IUserCustom = { id: 17879 };
      course.professors = [professors];
      const site1: ISite = { id: 5733 };
      course.site1 = site1;
      const topic3: ITopic = { id: 13571 };
      course.topic3 = topic3;

      activatedRoute.data = of({ course });
      comp.ngOnInit();

      expect(comp.userCustomsSharedCollection).toContain(professors);
      expect(comp.sitesSharedCollection).toContain(site1);
      expect(comp.topicsSharedCollection).toContain(topic3);
      expect(comp.course).toEqual(course);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICourse>>();
      const course = { id: 123 };
      jest.spyOn(courseFormService, 'getCourse').mockReturnValue(course);
      jest.spyOn(courseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ course });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: course }));
      saveSubject.complete();

      // THEN
      expect(courseFormService.getCourse).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(courseService.update).toHaveBeenCalledWith(expect.objectContaining(course));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICourse>>();
      const course = { id: 123 };
      jest.spyOn(courseFormService, 'getCourse').mockReturnValue({ id: null });
      jest.spyOn(courseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ course: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: course }));
      saveSubject.complete();

      // THEN
      expect(courseFormService.getCourse).toHaveBeenCalled();
      expect(courseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICourse>>();
      const course = { id: 123 };
      jest.spyOn(courseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ course });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(courseService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUserCustom', () => {
      it('Should forward to userCustomService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userCustomService, 'compareUserCustom');
        comp.compareUserCustom(entity, entity2);
        expect(userCustomService.compareUserCustom).toHaveBeenCalledWith(entity, entity2);
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
