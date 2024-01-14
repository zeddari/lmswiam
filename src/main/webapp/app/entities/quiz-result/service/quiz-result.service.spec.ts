import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IQuizResult } from '../quiz-result.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../quiz-result.test-samples';

import { QuizResultService, RestQuizResult } from './quiz-result.service';

const requireRestSample: RestQuizResult = {
  ...sampleWithRequiredData,
  submittedAT: sampleWithRequiredData.submittedAT?.toJSON(),
};

describe('QuizResult Service', () => {
  let service: QuizResultService;
  let httpMock: HttpTestingController;
  let expectedResult: IQuizResult | IQuizResult[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(QuizResultService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a QuizResult', () => {
      const quizResult = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(quizResult).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a QuizResult', () => {
      const quizResult = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(quizResult).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a QuizResult', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of QuizResult', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a QuizResult', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a QuizResult', () => {
      const queryObject: any = {
        page: 0,
        size: 20,
        query: '',
        sort: [],
      };
      service.search(queryObject).subscribe(() => expectedResult);

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(null, { status: 500, statusText: 'Internal Server Error' });
      expect(expectedResult).toBe(null);
    });

    describe('addQuizResultToCollectionIfMissing', () => {
      it('should add a QuizResult to an empty array', () => {
        const quizResult: IQuizResult = sampleWithRequiredData;
        expectedResult = service.addQuizResultToCollectionIfMissing([], quizResult);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(quizResult);
      });

      it('should not add a QuizResult to an array that contains it', () => {
        const quizResult: IQuizResult = sampleWithRequiredData;
        const quizResultCollection: IQuizResult[] = [
          {
            ...quizResult,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addQuizResultToCollectionIfMissing(quizResultCollection, quizResult);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a QuizResult to an array that doesn't contain it", () => {
        const quizResult: IQuizResult = sampleWithRequiredData;
        const quizResultCollection: IQuizResult[] = [sampleWithPartialData];
        expectedResult = service.addQuizResultToCollectionIfMissing(quizResultCollection, quizResult);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(quizResult);
      });

      it('should add only unique QuizResult to an array', () => {
        const quizResultArray: IQuizResult[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const quizResultCollection: IQuizResult[] = [sampleWithRequiredData];
        expectedResult = service.addQuizResultToCollectionIfMissing(quizResultCollection, ...quizResultArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const quizResult: IQuizResult = sampleWithRequiredData;
        const quizResult2: IQuizResult = sampleWithPartialData;
        expectedResult = service.addQuizResultToCollectionIfMissing([], quizResult, quizResult2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(quizResult);
        expect(expectedResult).toContain(quizResult2);
      });

      it('should accept null and undefined values', () => {
        const quizResult: IQuizResult = sampleWithRequiredData;
        expectedResult = service.addQuizResultToCollectionIfMissing([], null, quizResult, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(quizResult);
      });

      it('should return initial array if no QuizResult is added', () => {
        const quizResultCollection: IQuizResult[] = [sampleWithRequiredData];
        expectedResult = service.addQuizResultToCollectionIfMissing(quizResultCollection, undefined, null);
        expect(expectedResult).toEqual(quizResultCollection);
      });
    });

    describe('compareQuizResult', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareQuizResult(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareQuizResult(entity1, entity2);
        const compareResult2 = service.compareQuizResult(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareQuizResult(entity1, entity2);
        const compareResult2 = service.compareQuizResult(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareQuizResult(entity1, entity2);
        const compareResult2 = service.compareQuizResult(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
