import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISessionInstance } from '../session-instance.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../session-instance.test-samples';

import { SessionInstanceService, RestSessionInstance } from './session-instance.service';

const requireRestSample: RestSessionInstance = {
  ...sampleWithRequiredData,
  sessionDate: sampleWithRequiredData.sessionDate?.format(DATE_FORMAT),
  startTime: sampleWithRequiredData.startTime?.toJSON(),
};

describe('SessionInstance Service', () => {
  let service: SessionInstanceService;
  let httpMock: HttpTestingController;
  let expectedResult: ISessionInstance | ISessionInstance[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SessionInstanceService);
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

    it('should create a SessionInstance', () => {
      const sessionInstance = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(sessionInstance).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SessionInstance', () => {
      const sessionInstance = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(sessionInstance).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SessionInstance', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SessionInstance', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SessionInstance', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a SessionInstance', () => {
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

    describe('addSessionInstanceToCollectionIfMissing', () => {
      it('should add a SessionInstance to an empty array', () => {
        const sessionInstance: ISessionInstance = sampleWithRequiredData;
        expectedResult = service.addSessionInstanceToCollectionIfMissing([], sessionInstance);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sessionInstance);
      });

      it('should not add a SessionInstance to an array that contains it', () => {
        const sessionInstance: ISessionInstance = sampleWithRequiredData;
        const sessionInstanceCollection: ISessionInstance[] = [
          {
            ...sessionInstance,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSessionInstanceToCollectionIfMissing(sessionInstanceCollection, sessionInstance);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SessionInstance to an array that doesn't contain it", () => {
        const sessionInstance: ISessionInstance = sampleWithRequiredData;
        const sessionInstanceCollection: ISessionInstance[] = [sampleWithPartialData];
        expectedResult = service.addSessionInstanceToCollectionIfMissing(sessionInstanceCollection, sessionInstance);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sessionInstance);
      });

      it('should add only unique SessionInstance to an array', () => {
        const sessionInstanceArray: ISessionInstance[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const sessionInstanceCollection: ISessionInstance[] = [sampleWithRequiredData];
        expectedResult = service.addSessionInstanceToCollectionIfMissing(sessionInstanceCollection, ...sessionInstanceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sessionInstance: ISessionInstance = sampleWithRequiredData;
        const sessionInstance2: ISessionInstance = sampleWithPartialData;
        expectedResult = service.addSessionInstanceToCollectionIfMissing([], sessionInstance, sessionInstance2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sessionInstance);
        expect(expectedResult).toContain(sessionInstance2);
      });

      it('should accept null and undefined values', () => {
        const sessionInstance: ISessionInstance = sampleWithRequiredData;
        expectedResult = service.addSessionInstanceToCollectionIfMissing([], null, sessionInstance, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sessionInstance);
      });

      it('should return initial array if no SessionInstance is added', () => {
        const sessionInstanceCollection: ISessionInstance[] = [sampleWithRequiredData];
        expectedResult = service.addSessionInstanceToCollectionIfMissing(sessionInstanceCollection, undefined, null);
        expect(expectedResult).toEqual(sessionInstanceCollection);
      });
    });

    describe('compareSessionInstance', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSessionInstance(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSessionInstance(entity1, entity2);
        const compareResult2 = service.compareSessionInstance(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSessionInstance(entity1, entity2);
        const compareResult2 = service.compareSessionInstance(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSessionInstance(entity1, entity2);
        const compareResult2 = service.compareSessionInstance(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
