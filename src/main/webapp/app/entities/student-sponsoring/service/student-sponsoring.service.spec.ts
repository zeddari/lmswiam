import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IStudentSponsoring } from '../student-sponsoring.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../student-sponsoring.test-samples';

import { StudentSponsoringService, RestStudentSponsoring } from './student-sponsoring.service';

const requireRestSample: RestStudentSponsoring = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
};

describe('StudentSponsoring Service', () => {
  let service: StudentSponsoringService;
  let httpMock: HttpTestingController;
  let expectedResult: IStudentSponsoring | IStudentSponsoring[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(StudentSponsoringService);
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

    it('should create a StudentSponsoring', () => {
      const studentSponsoring = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(studentSponsoring).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StudentSponsoring', () => {
      const studentSponsoring = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(studentSponsoring).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a StudentSponsoring', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StudentSponsoring', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a StudentSponsoring', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a StudentSponsoring', () => {
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

    describe('addStudentSponsoringToCollectionIfMissing', () => {
      it('should add a StudentSponsoring to an empty array', () => {
        const studentSponsoring: IStudentSponsoring = sampleWithRequiredData;
        expectedResult = service.addStudentSponsoringToCollectionIfMissing([], studentSponsoring);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(studentSponsoring);
      });

      it('should not add a StudentSponsoring to an array that contains it', () => {
        const studentSponsoring: IStudentSponsoring = sampleWithRequiredData;
        const studentSponsoringCollection: IStudentSponsoring[] = [
          {
            ...studentSponsoring,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addStudentSponsoringToCollectionIfMissing(studentSponsoringCollection, studentSponsoring);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StudentSponsoring to an array that doesn't contain it", () => {
        const studentSponsoring: IStudentSponsoring = sampleWithRequiredData;
        const studentSponsoringCollection: IStudentSponsoring[] = [sampleWithPartialData];
        expectedResult = service.addStudentSponsoringToCollectionIfMissing(studentSponsoringCollection, studentSponsoring);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(studentSponsoring);
      });

      it('should add only unique StudentSponsoring to an array', () => {
        const studentSponsoringArray: IStudentSponsoring[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const studentSponsoringCollection: IStudentSponsoring[] = [sampleWithRequiredData];
        expectedResult = service.addStudentSponsoringToCollectionIfMissing(studentSponsoringCollection, ...studentSponsoringArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const studentSponsoring: IStudentSponsoring = sampleWithRequiredData;
        const studentSponsoring2: IStudentSponsoring = sampleWithPartialData;
        expectedResult = service.addStudentSponsoringToCollectionIfMissing([], studentSponsoring, studentSponsoring2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(studentSponsoring);
        expect(expectedResult).toContain(studentSponsoring2);
      });

      it('should accept null and undefined values', () => {
        const studentSponsoring: IStudentSponsoring = sampleWithRequiredData;
        expectedResult = service.addStudentSponsoringToCollectionIfMissing([], null, studentSponsoring, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(studentSponsoring);
      });

      it('should return initial array if no StudentSponsoring is added', () => {
        const studentSponsoringCollection: IStudentSponsoring[] = [sampleWithRequiredData];
        expectedResult = service.addStudentSponsoringToCollectionIfMissing(studentSponsoringCollection, undefined, null);
        expect(expectedResult).toEqual(studentSponsoringCollection);
      });
    });

    describe('compareStudentSponsoring', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareStudentSponsoring(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareStudentSponsoring(entity1, entity2);
        const compareResult2 = service.compareStudentSponsoring(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareStudentSponsoring(entity1, entity2);
        const compareResult2 = service.compareStudentSponsoring(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareStudentSponsoring(entity1, entity2);
        const compareResult2 = service.compareStudentSponsoring(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
