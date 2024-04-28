import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../depense.test-samples';

import { DepenseFormService } from './depense-form.service';

describe('Depense Form Service', () => {
  let service: DepenseFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DepenseFormService);
  });

  describe('Service methods', () => {
    describe('createDepenseFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDepenseFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            target: expect.any(Object),
            frequency: expect.any(Object),
            amount: expect.any(Object),
            ref: expect.any(Object),
            message: expect.any(Object),
            resource: expect.any(Object),
          }),
        );
      });

      it('passing IDepense should create a new form with FormGroup', () => {
        const formGroup = service.createDepenseFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            target: expect.any(Object),
            frequency: expect.any(Object),
            amount: expect.any(Object),
            ref: expect.any(Object),
            message: expect.any(Object),
            resource: expect.any(Object),
          }),
        );
      });
    });

    describe('getDepense', () => {
      it('should return NewDepense for default Depense initial value', () => {
        const formGroup = service.createDepenseFormGroup(sampleWithNewData);

        const depense = service.getDepense(formGroup) as any;

        expect(depense).toMatchObject(sampleWithNewData);
      });

      it('should return NewDepense for empty Depense initial value', () => {
        const formGroup = service.createDepenseFormGroup();

        const depense = service.getDepense(formGroup) as any;

        expect(depense).toMatchObject({});
      });

      it('should return IDepense', () => {
        const formGroup = service.createDepenseFormGroup(sampleWithRequiredData);

        const depense = service.getDepense(formGroup) as any;

        expect(depense).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDepense should not enable id FormControl', () => {
        const formGroup = service.createDepenseFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDepense should disable id FormControl', () => {
        const formGroup = service.createDepenseFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
