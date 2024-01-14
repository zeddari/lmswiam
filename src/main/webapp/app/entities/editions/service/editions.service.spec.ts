import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEditions } from '../editions.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../editions.test-samples';

import { EditionsService, RestEditions } from './editions.service';

const requireRestSample: RestEditions = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
};

describe('Editions Service', () => {
  let service: EditionsService;
  let httpMock: HttpTestingController;
  let expectedResult: IEditions | IEditions[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EditionsService);
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

    it('should create a Editions', () => {
      const editions = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(editions).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Editions', () => {
      const editions = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(editions).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Editions', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Editions', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Editions', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Editions', () => {
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

    describe('addEditionsToCollectionIfMissing', () => {
      it('should add a Editions to an empty array', () => {
        const editions: IEditions = sampleWithRequiredData;
        expectedResult = service.addEditionsToCollectionIfMissing([], editions);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(editions);
      });

      it('should not add a Editions to an array that contains it', () => {
        const editions: IEditions = sampleWithRequiredData;
        const editionsCollection: IEditions[] = [
          {
            ...editions,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEditionsToCollectionIfMissing(editionsCollection, editions);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Editions to an array that doesn't contain it", () => {
        const editions: IEditions = sampleWithRequiredData;
        const editionsCollection: IEditions[] = [sampleWithPartialData];
        expectedResult = service.addEditionsToCollectionIfMissing(editionsCollection, editions);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(editions);
      });

      it('should add only unique Editions to an array', () => {
        const editionsArray: IEditions[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const editionsCollection: IEditions[] = [sampleWithRequiredData];
        expectedResult = service.addEditionsToCollectionIfMissing(editionsCollection, ...editionsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const editions: IEditions = sampleWithRequiredData;
        const editions2: IEditions = sampleWithPartialData;
        expectedResult = service.addEditionsToCollectionIfMissing([], editions, editions2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(editions);
        expect(expectedResult).toContain(editions2);
      });

      it('should accept null and undefined values', () => {
        const editions: IEditions = sampleWithRequiredData;
        expectedResult = service.addEditionsToCollectionIfMissing([], null, editions, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(editions);
      });

      it('should return initial array if no Editions is added', () => {
        const editionsCollection: IEditions[] = [sampleWithRequiredData];
        expectedResult = service.addEditionsToCollectionIfMissing(editionsCollection, undefined, null);
        expect(expectedResult).toEqual(editionsCollection);
      });
    });

    describe('compareEditions', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEditions(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEditions(entity1, entity2);
        const compareResult2 = service.compareEditions(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEditions(entity1, entity2);
        const compareResult2 = service.compareEditions(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEditions(entity1, entity2);
        const compareResult2 = service.compareEditions(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
