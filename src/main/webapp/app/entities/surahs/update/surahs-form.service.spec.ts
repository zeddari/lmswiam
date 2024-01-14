import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../surahs.test-samples';

import { SurahsFormService } from './surahs-form.service';

describe('Surahs Form Service', () => {
  let service: SurahsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SurahsFormService);
  });

  describe('Service methods', () => {
    describe('createSurahsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSurahsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            number: expect.any(Object),
            nameAr: expect.any(Object),
            nameEn: expect.any(Object),
            nameEnTranslation: expect.any(Object),
            type: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
          }),
        );
      });

      it('passing ISurahs should create a new form with FormGroup', () => {
        const formGroup = service.createSurahsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            number: expect.any(Object),
            nameAr: expect.any(Object),
            nameEn: expect.any(Object),
            nameEnTranslation: expect.any(Object),
            type: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
          }),
        );
      });
    });

    describe('getSurahs', () => {
      it('should return NewSurahs for default Surahs initial value', () => {
        const formGroup = service.createSurahsFormGroup(sampleWithNewData);

        const surahs = service.getSurahs(formGroup) as any;

        expect(surahs).toMatchObject(sampleWithNewData);
      });

      it('should return NewSurahs for empty Surahs initial value', () => {
        const formGroup = service.createSurahsFormGroup();

        const surahs = service.getSurahs(formGroup) as any;

        expect(surahs).toMatchObject({});
      });

      it('should return ISurahs', () => {
        const formGroup = service.createSurahsFormGroup(sampleWithRequiredData);

        const surahs = service.getSurahs(formGroup) as any;

        expect(surahs).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISurahs should not enable id FormControl', () => {
        const formGroup = service.createSurahsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSurahs should disable id FormControl', () => {
        const formGroup = service.createSurahsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
