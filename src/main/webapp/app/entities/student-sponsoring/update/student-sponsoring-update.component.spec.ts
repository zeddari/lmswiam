import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { StudentSponsoringService } from '../service/student-sponsoring.service';
import { IStudentSponsoring } from '../student-sponsoring.model';
import { StudentSponsoringFormService } from './student-sponsoring-form.service';

import { StudentSponsoringUpdateComponent } from './student-sponsoring-update.component';

describe('StudentSponsoring Management Update Component', () => {
  let comp: StudentSponsoringUpdateComponent;
  let fixture: ComponentFixture<StudentSponsoringUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let studentSponsoringFormService: StudentSponsoringFormService;
  let studentSponsoringService: StudentSponsoringService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), StudentSponsoringUpdateComponent],
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
      .overrideTemplate(StudentSponsoringUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StudentSponsoringUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    studentSponsoringFormService = TestBed.inject(StudentSponsoringFormService);
    studentSponsoringService = TestBed.inject(StudentSponsoringService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const studentSponsoring: IStudentSponsoring = { id: 456 };

      activatedRoute.data = of({ studentSponsoring });
      comp.ngOnInit();

      expect(comp.studentSponsoring).toEqual(studentSponsoring);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStudentSponsoring>>();
      const studentSponsoring = { id: 123 };
      jest.spyOn(studentSponsoringFormService, 'getStudentSponsoring').mockReturnValue(studentSponsoring);
      jest.spyOn(studentSponsoringService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ studentSponsoring });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: studentSponsoring }));
      saveSubject.complete();

      // THEN
      expect(studentSponsoringFormService.getStudentSponsoring).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(studentSponsoringService.update).toHaveBeenCalledWith(expect.objectContaining(studentSponsoring));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStudentSponsoring>>();
      const studentSponsoring = { id: 123 };
      jest.spyOn(studentSponsoringFormService, 'getStudentSponsoring').mockReturnValue({ id: null });
      jest.spyOn(studentSponsoringService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ studentSponsoring: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: studentSponsoring }));
      saveSubject.complete();

      // THEN
      expect(studentSponsoringFormService.getStudentSponsoring).toHaveBeenCalled();
      expect(studentSponsoringService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStudentSponsoring>>();
      const studentSponsoring = { id: 123 };
      jest.spyOn(studentSponsoringService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ studentSponsoring });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(studentSponsoringService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
