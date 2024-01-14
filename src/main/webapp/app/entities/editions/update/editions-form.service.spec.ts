import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../editions.test-samples';

import { EditionsFormService } from './editions-form.service';

describe('Editions Form Service', () => {
  let service: EditionsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EditionsFormService);
  });

  describe('Service methods', () => {
    describe('createEditionsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEditionsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            identifier: expect.any(Object),
            language: expect.any(Object),
            name: expect.any(Object),
            englishName: expect.any(Object),
            format: expect.any(Object),
            type: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
          }),
        );
      });

      it('passing IEditions should create a new form with FormGroup', () => {
        const formGroup = service.createEditionsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            identifier: expect.any(Object),
            language: expect.any(Object),
            name: expect.any(Object),
            englishName: expect.any(Object),
            format: expect.any(Object),
            type: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
          }),
        );
      });
    });

    describe('getEditions', () => {
      it('should return NewEditions for default Editions initial value', () => {
        const formGroup = service.createEditionsFormGroup(sampleWithNewData);

        const editions = service.getEditions(formGroup) as any;

        expect(editions).toMatchObject(sampleWithNewData);
      });

      it('should return NewEditions for empty Editions initial value', () => {
        const formGroup = service.createEditionsFormGroup();

        const editions = service.getEditions(formGroup) as any;

        expect(editions).toMatchObject({});
      });

      it('should return IEditions', () => {
        const formGroup = service.createEditionsFormGroup(sampleWithRequiredData);

        const editions = service.getEditions(formGroup) as any;

        expect(editions).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEditions should not enable id FormControl', () => {
        const formGroup = service.createEditionsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEditions should disable id FormControl', () => {
        const formGroup = service.createEditionsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
