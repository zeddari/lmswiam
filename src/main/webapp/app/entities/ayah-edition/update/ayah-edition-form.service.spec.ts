import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../ayah-edition.test-samples';

import { AyahEditionFormService } from './ayah-edition-form.service';

describe('AyahEdition Form Service', () => {
  let service: AyahEditionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AyahEditionFormService);
  });

  describe('Service methods', () => {
    describe('createAyahEditionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAyahEditionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ayahId: expect.any(Object),
            editionId: expect.any(Object),
            data: expect.any(Object),
            isAudio: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
          }),
        );
      });

      it('passing IAyahEdition should create a new form with FormGroup', () => {
        const formGroup = service.createAyahEditionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ayahId: expect.any(Object),
            editionId: expect.any(Object),
            data: expect.any(Object),
            isAudio: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
          }),
        );
      });
    });

    describe('getAyahEdition', () => {
      it('should return NewAyahEdition for default AyahEdition initial value', () => {
        const formGroup = service.createAyahEditionFormGroup(sampleWithNewData);

        const ayahEdition = service.getAyahEdition(formGroup) as any;

        expect(ayahEdition).toMatchObject(sampleWithNewData);
      });

      it('should return NewAyahEdition for empty AyahEdition initial value', () => {
        const formGroup = service.createAyahEditionFormGroup();

        const ayahEdition = service.getAyahEdition(formGroup) as any;

        expect(ayahEdition).toMatchObject({});
      });

      it('should return IAyahEdition', () => {
        const formGroup = service.createAyahEditionFormGroup(sampleWithRequiredData);

        const ayahEdition = service.getAyahEdition(formGroup) as any;

        expect(ayahEdition).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAyahEdition should not enable id FormControl', () => {
        const formGroup = service.createAyahEditionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAyahEdition should disable id FormControl', () => {
        const formGroup = service.createAyahEditionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
