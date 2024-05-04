import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SessionCoursesService } from '../service/session-courses.service';
import { ISessionCourses } from '../session-courses.model';
import { SessionCoursesFormService } from './session-courses-form.service';

import { SessionCoursesUpdateComponent } from './session-courses-update.component';

describe('SessionCourses Management Update Component', () => {
  let comp: SessionCoursesUpdateComponent;
  let fixture: ComponentFixture<SessionCoursesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sessionCoursesFormService: SessionCoursesFormService;
  let sessionCoursesService: SessionCoursesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), SessionCoursesUpdateComponent],
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
      .overrideTemplate(SessionCoursesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SessionCoursesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sessionCoursesFormService = TestBed.inject(SessionCoursesFormService);
    sessionCoursesService = TestBed.inject(SessionCoursesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const sessionCourses: ISessionCourses = { id: 456 };

      activatedRoute.data = of({ sessionCourses });
      comp.ngOnInit();

      expect(comp.sessionCourses).toEqual(sessionCourses);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISessionCourses>>();
      const sessionCourses = { id: 123 };
      jest.spyOn(sessionCoursesFormService, 'getSessionCourses').mockReturnValue(sessionCourses);
      jest.spyOn(sessionCoursesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sessionCourses });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sessionCourses }));
      saveSubject.complete();

      // THEN
      expect(sessionCoursesFormService.getSessionCourses).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sessionCoursesService.update).toHaveBeenCalledWith(expect.objectContaining(sessionCourses));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISessionCourses>>();
      const sessionCourses = { id: 123 };
      jest.spyOn(sessionCoursesFormService, 'getSessionCourses').mockReturnValue({ id: null });
      jest.spyOn(sessionCoursesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sessionCourses: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sessionCourses }));
      saveSubject.complete();

      // THEN
      expect(sessionCoursesFormService.getSessionCourses).toHaveBeenCalled();
      expect(sessionCoursesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISessionCourses>>();
      const sessionCourses = { id: 123 };
      jest.spyOn(sessionCoursesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sessionCourses });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sessionCoursesService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
