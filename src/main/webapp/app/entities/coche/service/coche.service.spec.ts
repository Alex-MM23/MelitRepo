import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICoche } from '../coche.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../coche.test-samples';

import { CocheService } from './coche.service';

const requireRestSample: ICoche = {
  ...sampleWithRequiredData,
};

describe('Coche Service', () => {
  let service: CocheService;
  let httpMock: HttpTestingController;
  let expectedResult: ICoche | ICoche[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CocheService);
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

    it('should create a Coche', () => {
      const coche = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(coche).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Coche', () => {
      const coche = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(coche).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Coche', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Coche', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Coche', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCocheToCollectionIfMissing', () => {
      it('should add a Coche to an empty array', () => {
        const coche: ICoche = sampleWithRequiredData;
        expectedResult = service.addCocheToCollectionIfMissing([], coche);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(coche);
      });

      it('should not add a Coche to an array that contains it', () => {
        const coche: ICoche = sampleWithRequiredData;
        const cocheCollection: ICoche[] = [
          {
            ...coche,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCocheToCollectionIfMissing(cocheCollection, coche);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Coche to an array that doesn't contain it", () => {
        const coche: ICoche = sampleWithRequiredData;
        const cocheCollection: ICoche[] = [sampleWithPartialData];
        expectedResult = service.addCocheToCollectionIfMissing(cocheCollection, coche);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(coche);
      });

      it('should add only unique Coche to an array', () => {
        const cocheArray: ICoche[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const cocheCollection: ICoche[] = [sampleWithRequiredData];
        expectedResult = service.addCocheToCollectionIfMissing(cocheCollection, ...cocheArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const coche: ICoche = sampleWithRequiredData;
        const coche2: ICoche = sampleWithPartialData;
        expectedResult = service.addCocheToCollectionIfMissing([], coche, coche2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(coche);
        expect(expectedResult).toContain(coche2);
      });

      it('should accept null and undefined values', () => {
        const coche: ICoche = sampleWithRequiredData;
        expectedResult = service.addCocheToCollectionIfMissing([], null, coche, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(coche);
      });

      it('should return initial array if no Coche is added', () => {
        const cocheCollection: ICoche[] = [sampleWithRequiredData];
        expectedResult = service.addCocheToCollectionIfMissing(cocheCollection, undefined, null);
        expect(expectedResult).toEqual(cocheCollection);
      });
    });

    describe('compareCoche', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCoche(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCoche(entity1, entity2);
        const compareResult2 = service.compareCoche(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCoche(entity1, entity2);
        const compareResult2 = service.compareCoche(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCoche(entity1, entity2);
        const compareResult2 = service.compareCoche(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
