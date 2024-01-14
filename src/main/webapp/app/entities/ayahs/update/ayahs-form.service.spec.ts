import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../ayahs.test-samples';

import { AyahsFormService } from './ayahs-form.service';

describe('Ayahs Form Service', () => {
  let service: AyahsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AyahsFormService);
  });

  describe('Service methods', () => {
    describe('createAyahsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAyahsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            number: expect.any(Object),
            textdesc: expect.any(Object),
            numberInSurah: expect.any(Object),
            page: expect.any(Object),
            surahId: expect.any(Object),
            hizbId: expect.any(Object),
            juzId: expect.any(Object),
            sajda: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
          }),
        );
      });

      it('passing IAyahs should create a new form with FormGroup', () => {
        const formGroup = service.createAyahsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            number: expect.any(Object),
            textdesc: expect.any(Object),
            numberInSurah: expect.any(Object),
            page: expect.any(Object),
            surahId: expect.any(Object),
            hizbId: expect.any(Object),
            juzId: expect.any(Object),
            sajda: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
          }),
        );
      });
    });

    describe('getAyahs', () => {
      it('should return NewAyahs for default Ayahs initial value', () => {
        const formGroup = service.createAyahsFormGroup(sampleWithNewData);

        const ayahs = service.getAyahs(formGroup) as any;

        expect(ayahs).toMatchObject(sampleWithNewData);
      });

      it('should return NewAyahs for empty Ayahs initial value', () => {
        const formGroup = service.createAyahsFormGroup();

        const ayahs = service.getAyahs(formGroup) as any;

        expect(ayahs).toMatchObject({});
      });

      it('should return IAyahs', () => {
        const formGroup = service.createAyahsFormGroup(sampleWithRequiredData);

        const ayahs = service.getAyahs(formGroup) as any;

        expect(ayahs).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAyahs should not enable id FormControl', () => {
        const formGroup = service.createAyahsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAyahs should disable id FormControl', () => {
        const formGroup = service.createAyahsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
