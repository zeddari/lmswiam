import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../quiz-result.test-samples';

import { QuizResultFormService } from './quiz-result-form.service';

describe('QuizResult Form Service', () => {
  let service: QuizResultFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QuizResultFormService);
  });

  describe('Service methods', () => {
    describe('createQuizResultFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createQuizResultFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            result: expect.any(Object),
            submittedAT: expect.any(Object),
            site8: expect.any(Object),
            quiz: expect.any(Object),
            userCustom2: expect.any(Object),
          }),
        );
      });

      it('passing IQuizResult should create a new form with FormGroup', () => {
        const formGroup = service.createQuizResultFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            result: expect.any(Object),
            submittedAT: expect.any(Object),
            site8: expect.any(Object),
            quiz: expect.any(Object),
            userCustom2: expect.any(Object),
          }),
        );
      });
    });

    describe('getQuizResult', () => {
      it('should return NewQuizResult for default QuizResult initial value', () => {
        const formGroup = service.createQuizResultFormGroup(sampleWithNewData);

        const quizResult = service.getQuizResult(formGroup) as any;

        expect(quizResult).toMatchObject(sampleWithNewData);
      });

      it('should return NewQuizResult for empty QuizResult initial value', () => {
        const formGroup = service.createQuizResultFormGroup();

        const quizResult = service.getQuizResult(formGroup) as any;

        expect(quizResult).toMatchObject({});
      });

      it('should return IQuizResult', () => {
        const formGroup = service.createQuizResultFormGroup(sampleWithRequiredData);

        const quizResult = service.getQuizResult(formGroup) as any;

        expect(quizResult).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IQuizResult should not enable id FormControl', () => {
        const formGroup = service.createQuizResultFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewQuizResult should disable id FormControl', () => {
        const formGroup = service.createQuizResultFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
