import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../session-courses.test-samples';

import { SessionCoursesFormService } from './session-courses-form.service';

describe('SessionCourses Form Service', () => {
  let service: SessionCoursesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionCoursesFormService);
  });

  describe('Service methods', () => {
    describe('createSessionCoursesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSessionCoursesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            resourceLink: expect.any(Object),
            resourceFile: expect.any(Object),
          }),
        );
      });

      it('passing ISessionCourses should create a new form with FormGroup', () => {
        const formGroup = service.createSessionCoursesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            resourceLink: expect.any(Object),
            resourceFile: expect.any(Object),
          }),
        );
      });
    });

    describe('getSessionCourses', () => {
      it('should return NewSessionCourses for default SessionCourses initial value', () => {
        const formGroup = service.createSessionCoursesFormGroup(sampleWithNewData);

        const sessionCourses = service.getSessionCourses(formGroup) as any;

        expect(sessionCourses).toMatchObject(sampleWithNewData);
      });

      it('should return NewSessionCourses for empty SessionCourses initial value', () => {
        const formGroup = service.createSessionCoursesFormGroup();

        const sessionCourses = service.getSessionCourses(formGroup) as any;

        expect(sessionCourses).toMatchObject({});
      });

      it('should return ISessionCourses', () => {
        const formGroup = service.createSessionCoursesFormGroup(sampleWithRequiredData);

        const sessionCourses = service.getSessionCourses(formGroup) as any;

        expect(sessionCourses).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISessionCourses should not enable id FormControl', () => {
        const formGroup = service.createSessionCoursesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSessionCourses should disable id FormControl', () => {
        const formGroup = service.createSessionCoursesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
