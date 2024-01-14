import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IJuzs } from '../juzs.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../juzs.test-samples';

import { JuzsService, RestJuzs } from './juzs.service';

const requireRestSample: RestJuzs = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
};

describe('Juzs Service', () => {
  let service: JuzsService;
  let httpMock: HttpTestingController;
  let expectedResult: IJuzs | IJuzs[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(JuzsService);
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

    it('should create a Juzs', () => {
      const juzs = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(juzs).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Juzs', () => {
      const juzs = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(juzs).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Juzs', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Juzs', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Juzs', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Juzs', () => {
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

    describe('addJuzsToCollectionIfMissing', () => {
      it('should add a Juzs to an empty array', () => {
        const juzs: IJuzs = sampleWithRequiredData;
        expectedResult = service.addJuzsToCollectionIfMissing([], juzs);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(juzs);
      });

      it('should not add a Juzs to an array that contains it', () => {
        const juzs: IJuzs = sampleWithRequiredData;
        const juzsCollection: IJuzs[] = [
          {
            ...juzs,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addJuzsToCollectionIfMissing(juzsCollection, juzs);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Juzs to an array that doesn't contain it", () => {
        const juzs: IJuzs = sampleWithRequiredData;
        const juzsCollection: IJuzs[] = [sampleWithPartialData];
        expectedResult = service.addJuzsToCollectionIfMissing(juzsCollection, juzs);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(juzs);
      });

      it('should add only unique Juzs to an array', () => {
        const juzsArray: IJuzs[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const juzsCollection: IJuzs[] = [sampleWithRequiredData];
        expectedResult = service.addJuzsToCollectionIfMissing(juzsCollection, ...juzsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const juzs: IJuzs = sampleWithRequiredData;
        const juzs2: IJuzs = sampleWithPartialData;
        expectedResult = service.addJuzsToCollectionIfMissing([], juzs, juzs2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(juzs);
        expect(expectedResult).toContain(juzs2);
      });

      it('should accept null and undefined values', () => {
        const juzs: IJuzs = sampleWithRequiredData;
        expectedResult = service.addJuzsToCollectionIfMissing([], null, juzs, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(juzs);
      });

      it('should return initial array if no Juzs is added', () => {
        const juzsCollection: IJuzs[] = [sampleWithRequiredData];
        expectedResult = service.addJuzsToCollectionIfMissing(juzsCollection, undefined, null);
        expect(expectedResult).toEqual(juzsCollection);
      });
    });

    describe('compareJuzs', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareJuzs(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareJuzs(entity1, entity2);
        const compareResult2 = service.compareJuzs(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareJuzs(entity1, entity2);
        const compareResult2 = service.compareJuzs(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareJuzs(entity1, entity2);
        const compareResult2 = service.compareJuzs(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
