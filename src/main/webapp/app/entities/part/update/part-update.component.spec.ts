import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';
import { IPart } from '../part.model';
import { PartService } from '../service/part.service';
import { PartFormService } from './part-form.service';

import { PartUpdateComponent } from './part-update.component';

describe('Part Management Update Component', () => {
  let comp: PartUpdateComponent;
  let fixture: ComponentFixture<PartUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let partFormService: PartFormService;
  let partService: PartService;
  let siteService: SiteService;
  let courseService: CourseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), PartUpdateComponent],
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
      .overrideTemplate(PartUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PartUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    partFormService = TestBed.inject(PartFormService);
    partService = TestBed.inject(PartService);
    siteService = TestBed.inject(SiteService);
    courseService = TestBed.inject(CourseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Part query and add missing value', () => {
      const part: IPart = { id: 456 };
      const part1: IPart = { id: 13971 };
      part.part1 = part1;

      const partCollection: IPart[] = [{ id: 29616 }];
      jest.spyOn(partService, 'query').mockReturnValue(of(new HttpResponse({ body: partCollection })));
      const additionalParts = [part1];
      const expectedCollection: IPart[] = [...additionalParts, ...partCollection];
      jest.spyOn(partService, 'addPartToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ part });
      comp.ngOnInit();

      expect(partService.query).toHaveBeenCalled();
      expect(partService.addPartToCollectionIfMissing).toHaveBeenCalledWith(
        partCollection,
        ...additionalParts.map(expect.objectContaining),
      );
      expect(comp.partsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Site query and add missing value', () => {
      const part: IPart = { id: 456 };
      const site2: ISite = { id: 12204 };
      part.site2 = site2;

      const siteCollection: ISite[] = [{ id: 19307 }];
      jest.spyOn(siteService, 'query').mockReturnValue(of(new HttpResponse({ body: siteCollection })));
      const additionalSites = [site2];
      const expectedCollection: ISite[] = [...additionalSites, ...siteCollection];
      jest.spyOn(siteService, 'addSiteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ part });
      comp.ngOnInit();

      expect(siteService.query).toHaveBeenCalled();
      expect(siteService.addSiteToCollectionIfMissing).toHaveBeenCalledWith(
        siteCollection,
        ...additionalSites.map(expect.objectContaining),
      );
      expect(comp.sitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Course query and add missing value', () => {
      const part: IPart = { id: 456 };
      const course: ICourse = { id: 6560 };
      part.course = course;

      const courseCollection: ICourse[] = [{ id: 1712 }];
      jest.spyOn(courseService, 'query').mockReturnValue(of(new HttpResponse({ body: courseCollection })));
      const additionalCourses = [course];
      const expectedCollection: ICourse[] = [...additionalCourses, ...courseCollection];
      jest.spyOn(courseService, 'addCourseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ part });
      comp.ngOnInit();

      expect(courseService.query).toHaveBeenCalled();
      expect(courseService.addCourseToCollectionIfMissing).toHaveBeenCalledWith(
        courseCollection,
        ...additionalCourses.map(expect.objectContaining),
      );
      expect(comp.coursesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const part: IPart = { id: 456 };
      const part1: IPart = { id: 30221 };
      part.part1 = part1;
      const site2: ISite = { id: 218 };
      part.site2 = site2;
      const course: ICourse = { id: 4141 };
      part.course = course;

      activatedRoute.data = of({ part });
      comp.ngOnInit();

      expect(comp.partsSharedCollection).toContain(part1);
      expect(comp.sitesSharedCollection).toContain(site2);
      expect(comp.coursesSharedCollection).toContain(course);
      expect(comp.part).toEqual(part);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPart>>();
      const part = { id: 123 };
      jest.spyOn(partFormService, 'getPart').mockReturnValue(part);
      jest.spyOn(partService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ part });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: part }));
      saveSubject.complete();

      // THEN
      expect(partFormService.getPart).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(partService.update).toHaveBeenCalledWith(expect.objectContaining(part));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPart>>();
      const part = { id: 123 };
      jest.spyOn(partFormService, 'getPart').mockReturnValue({ id: null });
      jest.spyOn(partService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ part: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: part }));
      saveSubject.complete();

      // THEN
      expect(partFormService.getPart).toHaveBeenCalled();
      expect(partService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPart>>();
      const part = { id: 123 };
      jest.spyOn(partService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ part });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(partService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePart', () => {
      it('Should forward to partService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(partService, 'comparePart');
        comp.comparePart(entity, entity2);
        expect(partService.comparePart).toHaveBeenCalledWith(entity, entity2);
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
