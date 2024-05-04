import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../session-instance.test-samples';

import { SessionInstanceFormService } from './session-instance-form.service';

describe('SessionInstance Form Service', () => {
  let service: SessionInstanceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionInstanceFormService);
  });

  describe('Service methods', () => {
    describe('createSessionInstanceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSessionInstanceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            sessionDate: expect.any(Object),
            startTime: expect.any(Object),
            duration: expect.any(Object),
            info: expect.any(Object),
            attendance: expect.any(Object),
            justifRef: expect.any(Object),
            isActive: expect.any(Object),
            links: expect.any(Object),
            courses: expect.any(Object),
            site16: expect.any(Object),
            session1: expect.any(Object),
          }),
        );
      });

      it('passing ISessionInstance should create a new form with FormGroup', () => {
        const formGroup = service.createSessionInstanceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            sessionDate: expect.any(Object),
            startTime: expect.any(Object),
            duration: expect.any(Object),
            info: expect.any(Object),
            attendance: expect.any(Object),
            justifRef: expect.any(Object),
            isActive: expect.any(Object),
            links: expect.any(Object),
            courses: expect.any(Object),
            site16: expect.any(Object),
            session1: expect.any(Object),
          }),
        );
      });
    });

    describe('getSessionInstance', () => {
      it('should return NewSessionInstance for default SessionInstance initial value', () => {
        const formGroup = service.createSessionInstanceFormGroup(sampleWithNewData);

        const sessionInstance = service.getSessionInstance(formGroup) as any;

        expect(sessionInstance).toMatchObject(sampleWithNewData);
      });

      it('should return NewSessionInstance for empty SessionInstance initial value', () => {
        const formGroup = service.createSessionInstanceFormGroup();

        const sessionInstance = service.getSessionInstance(formGroup) as any;

        expect(sessionInstance).toMatchObject({});
      });

      it('should return ISessionInstance', () => {
        const formGroup = service.createSessionInstanceFormGroup(sampleWithRequiredData);

        const sessionInstance = service.getSessionInstance(formGroup) as any;

        expect(sessionInstance).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISessionInstance should not enable id FormControl', () => {
        const formGroup = service.createSessionInstanceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSessionInstance should disable id FormControl', () => {
        const formGroup = service.createSessionInstanceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
