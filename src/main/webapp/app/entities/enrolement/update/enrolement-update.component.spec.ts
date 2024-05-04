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
import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';
import { IEnrolement } from '../enrolement.model';
import { EnrolementService } from '../service/enrolement.service';
import { EnrolementFormService } from './enrolement-form.service';

import { EnrolementUpdateComponent } from './enrolement-update.component';

describe('Enrolement Management Update Component', () => {
  let comp: EnrolementUpdateComponent;
  let fixture: ComponentFixture<EnrolementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let enrolementFormService: EnrolementFormService;
  let enrolementService: EnrolementService;
  let siteService: SiteService;
  let userCustomService: UserCustomService;
  let courseService: CourseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), EnrolementUpdateComponent],
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
      .overrideTemplate(EnrolementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EnrolementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    enrolementFormService = TestBed.inject(EnrolementFormService);
    enrolementService = TestBed.inject(EnrolementService);
    siteService = TestBed.inject(SiteService);
    userCustomService = TestBed.inject(UserCustomService);
    courseService = TestBed.inject(CourseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Site query and add missing value', () => {
      const enrolement: IEnrolement = { id: 456 };
      const site4: ISite = { id: 26113 };
      enrolement.site4 = site4;

      const siteCollection: ISite[] = [{ id: 8474 }];
      jest.spyOn(siteService, 'query').mockReturnValue(of(new HttpResponse({ body: siteCollection })));
      const additionalSites = [site4];
      const expectedCollection: ISite[] = [...additionalSites, ...siteCollection];
      jest.spyOn(siteService, 'addSiteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ enrolement });
      comp.ngOnInit();

      expect(siteService.query).toHaveBeenCalled();
      expect(siteService.addSiteToCollectionIfMissing).toHaveBeenCalledWith(
        siteCollection,
        ...additionalSites.map(expect.objectContaining),
      );
      expect(comp.sitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UserCustom query and add missing value', () => {
      const enrolement: IEnrolement = { id: 456 };
      const userCustom4: IUserCustom = { id: 30255 };
      enrolement.userCustom4 = userCustom4;

      const userCustomCollection: IUserCustom[] = [{ id: 31298 }];
      jest.spyOn(userCustomService, 'query').mockReturnValue(of(new HttpResponse({ body: userCustomCollection })));
      const additionalUserCustoms = [userCustom4];
      const expectedCollection: IUserCustom[] = [...additionalUserCustoms, ...userCustomCollection];
      jest.spyOn(userCustomService, 'addUserCustomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ enrolement });
      comp.ngOnInit();

      expect(userCustomService.query).toHaveBeenCalled();
      expect(userCustomService.addUserCustomToCollectionIfMissing).toHaveBeenCalledWith(
        userCustomCollection,
        ...additionalUserCustoms.map(expect.objectContaining),
      );
      expect(comp.userCustomsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Course query and add missing value', () => {
      const enrolement: IEnrolement = { id: 456 };
      const course: ICourse = { id: 23406 };
      enrolement.course = course;

      const courseCollection: ICourse[] = [{ id: 27224 }];
      jest.spyOn(courseService, 'query').mockReturnValue(of(new HttpResponse({ body: courseCollection })));
      const additionalCourses = [course];
      const expectedCollection: ICourse[] = [...additionalCourses, ...courseCollection];
      jest.spyOn(courseService, 'addCourseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ enrolement });
      comp.ngOnInit();

      expect(courseService.query).toHaveBeenCalled();
      expect(courseService.addCourseToCollectionIfMissing).toHaveBeenCalledWith(
        courseCollection,
        ...additionalCourses.map(expect.objectContaining),
      );
      expect(comp.coursesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const enrolement: IEnrolement = { id: 456 };
      const site4: ISite = { id: 31378 };
      enrolement.site4 = site4;
      const userCustom4: IUserCustom = { id: 8498 };
      enrolement.userCustom4 = userCustom4;
      const course: ICourse = { id: 9732 };
      enrolement.course = course;

      activatedRoute.data = of({ enrolement });
      comp.ngOnInit();

      expect(comp.sitesSharedCollection).toContain(site4);
      expect(comp.userCustomsSharedCollection).toContain(userCustom4);
      expect(comp.coursesSharedCollection).toContain(course);
      expect(comp.enrolement).toEqual(enrolement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEnrolement>>();
      const enrolement = { id: 123 };
      jest.spyOn(enrolementFormService, 'getEnrolement').mockReturnValue(enrolement);
      jest.spyOn(enrolementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ enrolement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: enrolement }));
      saveSubject.complete();

      // THEN
      expect(enrolementFormService.getEnrolement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(enrolementService.update).toHaveBeenCalledWith(expect.objectContaining(enrolement));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEnrolement>>();
      const enrolement = { id: 123 };
      jest.spyOn(enrolementFormService, 'getEnrolement').mockReturnValue({ id: null });
      jest.spyOn(enrolementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ enrolement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: enrolement }));
      saveSubject.complete();

      // THEN
      expect(enrolementFormService.getEnrolement).toHaveBeenCalled();
      expect(enrolementService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEnrolement>>();
      const enrolement = { id: 123 };
      jest.spyOn(enrolementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ enrolement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(enrolementService.update).toHaveBeenCalled();
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

    describe('compareCourse', () => {
      it('Should forward to courseService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(courseService, 'compareCourse');
        comp.compareCourse(entity, entity2);
        expect(courseService.compareCourse).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
