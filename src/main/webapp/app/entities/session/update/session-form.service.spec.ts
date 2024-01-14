import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../session.test-samples';

import { SessionFormService } from './session-form.service';

describe('Session Form Service', () => {
  let service: SessionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionFormService);
  });

  describe('Service methods', () => {
    describe('createSessionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSessionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sessionMode: expect.any(Object),
            sessionType: expect.any(Object),
            sessionJoinMode: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            periodStartDate: expect.any(Object),
            periodeEndDate: expect.any(Object),
            sessionStartTime: expect.any(Object),
            sessionEndTime: expect.any(Object),
            sessionSize: expect.any(Object),
            targetedGender: expect.any(Object),
            price: expect.any(Object),
            thumbnail: expect.any(Object),
            monday: expect.any(Object),
            tuesday: expect.any(Object),
            wednesday: expect.any(Object),
            thursday: expect.any(Object),
            friday: expect.any(Object),
            saturday: expect.any(Object),
            sunday: expect.any(Object),
            isPeriodic: expect.any(Object),
            isMinorAllowed: expect.any(Object),
            isActive: expect.any(Object),
            classrooms: expect.any(Object),
            groups: expect.any(Object),
            professors: expect.any(Object),
            employees: expect.any(Object),
            links: expect.any(Object),
          }),
        );
      });

      it('passing ISession should create a new form with FormGroup', () => {
        const formGroup = service.createSessionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sessionMode: expect.any(Object),
            sessionType: expect.any(Object),
            sessionJoinMode: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            periodStartDate: expect.any(Object),
            periodeEndDate: expect.any(Object),
            sessionStartTime: expect.any(Object),
            sessionEndTime: expect.any(Object),
            sessionSize: expect.any(Object),
            targetedGender: expect.any(Object),
            price: expect.any(Object),
            thumbnail: expect.any(Object),
            monday: expect.any(Object),
            tuesday: expect.any(Object),
            wednesday: expect.any(Object),
            thursday: expect.any(Object),
            friday: expect.any(Object),
            saturday: expect.any(Object),
            sunday: expect.any(Object),
            isPeriodic: expect.any(Object),
            isMinorAllowed: expect.any(Object),
            isActive: expect.any(Object),
            classrooms: expect.any(Object),
            groups: expect.any(Object),
            professors: expect.any(Object),
            employees: expect.any(Object),
            links: expect.any(Object),
          }),
        );
      });
    });

    describe('getSession', () => {
      it('should return NewSession for default Session initial value', () => {
        const formGroup = service.createSessionFormGroup(sampleWithNewData);

        const session = service.getSession(formGroup) as any;

        expect(session).toMatchObject(sampleWithNewData);
      });

      it('should return NewSession for empty Session initial value', () => {
        const formGroup = service.createSessionFormGroup();

        const session = service.getSession(formGroup) as any;

        expect(session).toMatchObject({});
      });

      it('should return ISession', () => {
        const formGroup = service.createSessionFormGroup(sampleWithRequiredData);

        const session = service.getSession(formGroup) as any;

        expect(session).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISession should not enable id FormControl', () => {
        const formGroup = service.createSessionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSession should disable id FormControl', () => {
        const formGroup = service.createSessionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
