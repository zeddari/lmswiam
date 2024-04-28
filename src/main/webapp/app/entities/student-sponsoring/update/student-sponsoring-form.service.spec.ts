import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../student-sponsoring.test-samples';

import { StudentSponsoringFormService } from './student-sponsoring-form.service';

describe('StudentSponsoring Form Service', () => {
  let service: StudentSponsoringFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StudentSponsoringFormService);
  });

  describe('Service methods', () => {
    describe('createStudentSponsoringFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createStudentSponsoringFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ref: expect.any(Object),
            message: expect.any(Object),
            amount: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            isAlways: expect.any(Object),
          }),
        );
      });

      it('passing IStudentSponsoring should create a new form with FormGroup', () => {
        const formGroup = service.createStudentSponsoringFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ref: expect.any(Object),
            message: expect.any(Object),
            amount: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            isAlways: expect.any(Object),
          }),
        );
      });
    });

    describe('getStudentSponsoring', () => {
      it('should return NewStudentSponsoring for default StudentSponsoring initial value', () => {
        const formGroup = service.createStudentSponsoringFormGroup(sampleWithNewData);

        const studentSponsoring = service.getStudentSponsoring(formGroup) as any;

        expect(studentSponsoring).toMatchObject(sampleWithNewData);
      });

      it('should return NewStudentSponsoring for empty StudentSponsoring initial value', () => {
        const formGroup = service.createStudentSponsoringFormGroup();

        const studentSponsoring = service.getStudentSponsoring(formGroup) as any;

        expect(studentSponsoring).toMatchObject({});
      });

      it('should return IStudentSponsoring', () => {
        const formGroup = service.createStudentSponsoringFormGroup(sampleWithRequiredData);

        const studentSponsoring = service.getStudentSponsoring(formGroup) as any;

        expect(studentSponsoring).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IStudentSponsoring should not enable id FormControl', () => {
        const formGroup = service.createStudentSponsoringFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewStudentSponsoring should disable id FormControl', () => {
        const formGroup = service.createStudentSponsoringFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
