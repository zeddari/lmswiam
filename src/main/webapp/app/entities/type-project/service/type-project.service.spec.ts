import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITypeProject } from '../type-project.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../type-project.test-samples';

import { TypeProjectService } from './type-project.service';

const requireRestSample: ITypeProject = {
  ...sampleWithRequiredData,
};

describe('TypeProject Service', () => {
  let service: TypeProjectService;
  let httpMock: HttpTestingController;
  let expectedResult: ITypeProject | ITypeProject[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TypeProjectService);
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

    it('should create a TypeProject', () => {
      const typeProject = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(typeProject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypeProject', () => {
      const typeProject = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(typeProject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TypeProject', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypeProject', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TypeProject', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a TypeProject', () => {
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

    describe('addTypeProjectToCollectionIfMissing', () => {
      it('should add a TypeProject to an empty array', () => {
        const typeProject: ITypeProject = sampleWithRequiredData;
        expectedResult = service.addTypeProjectToCollectionIfMissing([], typeProject);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeProject);
      });

      it('should not add a TypeProject to an array that contains it', () => {
        const typeProject: ITypeProject = sampleWithRequiredData;
        const typeProjectCollection: ITypeProject[] = [
          {
            ...typeProject,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTypeProjectToCollectionIfMissing(typeProjectCollection, typeProject);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypeProject to an array that doesn't contain it", () => {
        const typeProject: ITypeProject = sampleWithRequiredData;
        const typeProjectCollection: ITypeProject[] = [sampleWithPartialData];
        expectedResult = service.addTypeProjectToCollectionIfMissing(typeProjectCollection, typeProject);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeProject);
      });

      it('should add only unique TypeProject to an array', () => {
        const typeProjectArray: ITypeProject[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const typeProjectCollection: ITypeProject[] = [sampleWithRequiredData];
        expectedResult = service.addTypeProjectToCollectionIfMissing(typeProjectCollection, ...typeProjectArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typeProject: ITypeProject = sampleWithRequiredData;
        const typeProject2: ITypeProject = sampleWithPartialData;
        expectedResult = service.addTypeProjectToCollectionIfMissing([], typeProject, typeProject2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeProject);
        expect(expectedResult).toContain(typeProject2);
      });

      it('should accept null and undefined values', () => {
        const typeProject: ITypeProject = sampleWithRequiredData;
        expectedResult = service.addTypeProjectToCollectionIfMissing([], null, typeProject, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeProject);
      });

      it('should return initial array if no TypeProject is added', () => {
        const typeProjectCollection: ITypeProject[] = [sampleWithRequiredData];
        expectedResult = service.addTypeProjectToCollectionIfMissing(typeProjectCollection, undefined, null);
        expect(expectedResult).toEqual(typeProjectCollection);
      });
    });

    describe('compareTypeProject', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTypeProject(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTypeProject(entity1, entity2);
        const compareResult2 = service.compareTypeProject(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTypeProject(entity1, entity2);
        const compareResult2 = service.compareTypeProject(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTypeProject(entity1, entity2);
        const compareResult2 = service.compareTypeProject(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
