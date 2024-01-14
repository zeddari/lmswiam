import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAyahEdition } from '../ayah-edition.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../ayah-edition.test-samples';

import { AyahEditionService, RestAyahEdition } from './ayah-edition.service';

const requireRestSample: RestAyahEdition = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
};

describe('AyahEdition Service', () => {
  let service: AyahEditionService;
  let httpMock: HttpTestingController;
  let expectedResult: IAyahEdition | IAyahEdition[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AyahEditionService);
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

    it('should create a AyahEdition', () => {
      const ayahEdition = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ayahEdition).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AyahEdition', () => {
      const ayahEdition = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ayahEdition).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AyahEdition', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AyahEdition', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AyahEdition', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a AyahEdition', () => {
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

    describe('addAyahEditionToCollectionIfMissing', () => {
      it('should add a AyahEdition to an empty array', () => {
        const ayahEdition: IAyahEdition = sampleWithRequiredData;
        expectedResult = service.addAyahEditionToCollectionIfMissing([], ayahEdition);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ayahEdition);
      });

      it('should not add a AyahEdition to an array that contains it', () => {
        const ayahEdition: IAyahEdition = sampleWithRequiredData;
        const ayahEditionCollection: IAyahEdition[] = [
          {
            ...ayahEdition,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAyahEditionToCollectionIfMissing(ayahEditionCollection, ayahEdition);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AyahEdition to an array that doesn't contain it", () => {
        const ayahEdition: IAyahEdition = sampleWithRequiredData;
        const ayahEditionCollection: IAyahEdition[] = [sampleWithPartialData];
        expectedResult = service.addAyahEditionToCollectionIfMissing(ayahEditionCollection, ayahEdition);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ayahEdition);
      });

      it('should add only unique AyahEdition to an array', () => {
        const ayahEditionArray: IAyahEdition[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ayahEditionCollection: IAyahEdition[] = [sampleWithRequiredData];
        expectedResult = service.addAyahEditionToCollectionIfMissing(ayahEditionCollection, ...ayahEditionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ayahEdition: IAyahEdition = sampleWithRequiredData;
        const ayahEdition2: IAyahEdition = sampleWithPartialData;
        expectedResult = service.addAyahEditionToCollectionIfMissing([], ayahEdition, ayahEdition2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ayahEdition);
        expect(expectedResult).toContain(ayahEdition2);
      });

      it('should accept null and undefined values', () => {
        const ayahEdition: IAyahEdition = sampleWithRequiredData;
        expectedResult = service.addAyahEditionToCollectionIfMissing([], null, ayahEdition, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ayahEdition);
      });

      it('should return initial array if no AyahEdition is added', () => {
        const ayahEditionCollection: IAyahEdition[] = [sampleWithRequiredData];
        expectedResult = service.addAyahEditionToCollectionIfMissing(ayahEditionCollection, undefined, null);
        expect(expectedResult).toEqual(ayahEditionCollection);
      });
    });

    describe('compareAyahEdition', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAyahEdition(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAyahEdition(entity1, entity2);
        const compareResult2 = service.compareAyahEdition(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAyahEdition(entity1, entity2);
        const compareResult2 = service.compareAyahEdition(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAyahEdition(entity1, entity2);
        const compareResult2 = service.compareAyahEdition(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
