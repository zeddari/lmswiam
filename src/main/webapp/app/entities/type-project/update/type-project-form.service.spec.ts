import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../type-project.test-samples';

import { TypeProjectFormService } from './type-project-form.service';

describe('TypeProject Form Service', () => {
  let service: TypeProjectFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TypeProjectFormService);
  });

  describe('Service methods', () => {
    describe('createTypeProjectFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTypeProjectFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nameAr: expect.any(Object),
            nameLat: expect.any(Object),
          }),
        );
      });

      it('passing ITypeProject should create a new form with FormGroup', () => {
        const formGroup = service.createTypeProjectFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nameAr: expect.any(Object),
            nameLat: expect.any(Object),
          }),
        );
      });
    });

    describe('getTypeProject', () => {
      it('should return NewTypeProject for default TypeProject initial value', () => {
        const formGroup = service.createTypeProjectFormGroup(sampleWithNewData);

        const typeProject = service.getTypeProject(formGroup) as any;

        expect(typeProject).toMatchObject(sampleWithNewData);
      });

      it('should return NewTypeProject for empty TypeProject initial value', () => {
        const formGroup = service.createTypeProjectFormGroup();

        const typeProject = service.getTypeProject(formGroup) as any;

        expect(typeProject).toMatchObject({});
      });

      it('should return ITypeProject', () => {
        const formGroup = service.createTypeProjectFormGroup(sampleWithRequiredData);

        const typeProject = service.getTypeProject(formGroup) as any;

        expect(typeProject).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITypeProject should not enable id FormControl', () => {
        const formGroup = service.createTypeProjectFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTypeProject should disable id FormControl', () => {
        const formGroup = service.createTypeProjectFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
