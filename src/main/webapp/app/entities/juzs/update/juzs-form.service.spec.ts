import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../juzs.test-samples';

import { JuzsFormService } from './juzs-form.service';

describe('Juzs Form Service', () => {
  let service: JuzsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(JuzsFormService);
  });

  describe('Service methods', () => {
    describe('createJuzsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createJuzsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
          }),
        );
      });

      it('passing IJuzs should create a new form with FormGroup', () => {
        const formGroup = service.createJuzsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
          }),
        );
      });
    });

    describe('getJuzs', () => {
      it('should return NewJuzs for default Juzs initial value', () => {
        const formGroup = service.createJuzsFormGroup(sampleWithNewData);

        const juzs = service.getJuzs(formGroup) as any;

        expect(juzs).toMatchObject(sampleWithNewData);
      });

      it('should return NewJuzs for empty Juzs initial value', () => {
        const formGroup = service.createJuzsFormGroup();

        const juzs = service.getJuzs(formGroup) as any;

        expect(juzs).toMatchObject({});
      });

      it('should return IJuzs', () => {
        const formGroup = service.createJuzsFormGroup(sampleWithRequiredData);

        const juzs = service.getJuzs(formGroup) as any;

        expect(juzs).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IJuzs should not enable id FormControl', () => {
        const formGroup = service.createJuzsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewJuzs should disable id FormControl', () => {
        const formGroup = service.createJuzsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
