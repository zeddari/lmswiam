import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../hizbs.test-samples';

import { HizbsFormService } from './hizbs-form.service';

describe('Hizbs Form Service', () => {
  let service: HizbsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HizbsFormService);
  });

  describe('Service methods', () => {
    describe('createHizbsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHizbsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
          }),
        );
      });

      it('passing IHizbs should create a new form with FormGroup', () => {
        const formGroup = service.createHizbsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
          }),
        );
      });
    });

    describe('getHizbs', () => {
      it('should return NewHizbs for default Hizbs initial value', () => {
        const formGroup = service.createHizbsFormGroup(sampleWithNewData);

        const hizbs = service.getHizbs(formGroup) as any;

        expect(hizbs).toMatchObject(sampleWithNewData);
      });

      it('should return NewHizbs for empty Hizbs initial value', () => {
        const formGroup = service.createHizbsFormGroup();

        const hizbs = service.getHizbs(formGroup) as any;

        expect(hizbs).toMatchObject({});
      });

      it('should return IHizbs', () => {
        const formGroup = service.createHizbsFormGroup(sampleWithRequiredData);

        const hizbs = service.getHizbs(formGroup) as any;

        expect(hizbs).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHizbs should not enable id FormControl', () => {
        const formGroup = service.createHizbsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHizbs should disable id FormControl', () => {
        const formGroup = service.createHizbsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
