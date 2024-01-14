import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IHizbs } from '../hizbs.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../hizbs.test-samples';

import { HizbsService, RestHizbs } from './hizbs.service';

const requireRestSample: RestHizbs = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
};

describe('Hizbs Service', () => {
  let service: HizbsService;
  let httpMock: HttpTestingController;
  let expectedResult: IHizbs | IHizbs[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HizbsService);
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

    it('should create a Hizbs', () => {
      const hizbs = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(hizbs).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Hizbs', () => {
      const hizbs = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(hizbs).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Hizbs', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Hizbs', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Hizbs', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Hizbs', () => {
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

    describe('addHizbsToCollectionIfMissing', () => {
      it('should add a Hizbs to an empty array', () => {
        const hizbs: IHizbs = sampleWithRequiredData;
        expectedResult = service.addHizbsToCollectionIfMissing([], hizbs);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hizbs);
      });

      it('should not add a Hizbs to an array that contains it', () => {
        const hizbs: IHizbs = sampleWithRequiredData;
        const hizbsCollection: IHizbs[] = [
          {
            ...hizbs,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHizbsToCollectionIfMissing(hizbsCollection, hizbs);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Hizbs to an array that doesn't contain it", () => {
        const hizbs: IHizbs = sampleWithRequiredData;
        const hizbsCollection: IHizbs[] = [sampleWithPartialData];
        expectedResult = service.addHizbsToCollectionIfMissing(hizbsCollection, hizbs);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hizbs);
      });

      it('should add only unique Hizbs to an array', () => {
        const hizbsArray: IHizbs[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const hizbsCollection: IHizbs[] = [sampleWithRequiredData];
        expectedResult = service.addHizbsToCollectionIfMissing(hizbsCollection, ...hizbsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const hizbs: IHizbs = sampleWithRequiredData;
        const hizbs2: IHizbs = sampleWithPartialData;
        expectedResult = service.addHizbsToCollectionIfMissing([], hizbs, hizbs2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hizbs);
        expect(expectedResult).toContain(hizbs2);
      });

      it('should accept null and undefined values', () => {
        const hizbs: IHizbs = sampleWithRequiredData;
        expectedResult = service.addHizbsToCollectionIfMissing([], null, hizbs, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hizbs);
      });

      it('should return initial array if no Hizbs is added', () => {
        const hizbsCollection: IHizbs[] = [sampleWithRequiredData];
        expectedResult = service.addHizbsToCollectionIfMissing(hizbsCollection, undefined, null);
        expect(expectedResult).toEqual(hizbsCollection);
      });
    });

    describe('compareHizbs', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHizbs(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareHizbs(entity1, entity2);
        const compareResult2 = service.compareHizbs(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareHizbs(entity1, entity2);
        const compareResult2 = service.compareHizbs(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareHizbs(entity1, entity2);
        const compareResult2 = service.compareHizbs(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
