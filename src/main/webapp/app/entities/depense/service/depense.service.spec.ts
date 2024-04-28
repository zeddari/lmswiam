import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDepense } from '../depense.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../depense.test-samples';

import { DepenseService } from './depense.service';

const requireRestSample: IDepense = {
  ...sampleWithRequiredData,
};

describe('Depense Service', () => {
  let service: DepenseService;
  let httpMock: HttpTestingController;
  let expectedResult: IDepense | IDepense[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DepenseService);
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

    it('should create a Depense', () => {
      const depense = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(depense).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Depense', () => {
      const depense = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(depense).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Depense', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Depense', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Depense', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Depense', () => {
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

    describe('addDepenseToCollectionIfMissing', () => {
      it('should add a Depense to an empty array', () => {
        const depense: IDepense = sampleWithRequiredData;
        expectedResult = service.addDepenseToCollectionIfMissing([], depense);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(depense);
      });

      it('should not add a Depense to an array that contains it', () => {
        const depense: IDepense = sampleWithRequiredData;
        const depenseCollection: IDepense[] = [
          {
            ...depense,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDepenseToCollectionIfMissing(depenseCollection, depense);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Depense to an array that doesn't contain it", () => {
        const depense: IDepense = sampleWithRequiredData;
        const depenseCollection: IDepense[] = [sampleWithPartialData];
        expectedResult = service.addDepenseToCollectionIfMissing(depenseCollection, depense);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(depense);
      });

      it('should add only unique Depense to an array', () => {
        const depenseArray: IDepense[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const depenseCollection: IDepense[] = [sampleWithRequiredData];
        expectedResult = service.addDepenseToCollectionIfMissing(depenseCollection, ...depenseArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const depense: IDepense = sampleWithRequiredData;
        const depense2: IDepense = sampleWithPartialData;
        expectedResult = service.addDepenseToCollectionIfMissing([], depense, depense2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(depense);
        expect(expectedResult).toContain(depense2);
      });

      it('should accept null and undefined values', () => {
        const depense: IDepense = sampleWithRequiredData;
        expectedResult = service.addDepenseToCollectionIfMissing([], null, depense, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(depense);
      });

      it('should return initial array if no Depense is added', () => {
        const depenseCollection: IDepense[] = [sampleWithRequiredData];
        expectedResult = service.addDepenseToCollectionIfMissing(depenseCollection, undefined, null);
        expect(expectedResult).toEqual(depenseCollection);
      });
    });

    describe('compareDepense', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDepense(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDepense(entity1, entity2);
        const compareResult2 = service.compareDepense(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDepense(entity1, entity2);
        const compareResult2 = service.compareDepense(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDepense(entity1, entity2);
        const compareResult2 = service.compareDepense(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
