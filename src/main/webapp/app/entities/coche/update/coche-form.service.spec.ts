import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../coche.test-samples';

import { CocheFormService } from './coche-form.service';

describe('Coche Form Service', () => {
  let service: CocheFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CocheFormService);
  });

  describe('Service methods', () => {
    describe('createCocheFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCocheFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            color: expect.any(Object),
            numeroSerie: expect.any(Object),
            precio: expect.any(Object),
            exposicion: expect.any(Object),
            marca: expect.any(Object),
            modelo: expect.any(Object),
          }),
        );
      });

      it('passing ICoche should create a new form with FormGroup', () => {
        const formGroup = service.createCocheFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            color: expect.any(Object),
            numeroSerie: expect.any(Object),
            precio: expect.any(Object),
            exposicion: expect.any(Object),
            marca: expect.any(Object),
            modelo: expect.any(Object),
          }),
        );
      });
    });

    describe('getCoche', () => {
      it('should return NewCoche for default Coche initial value', () => {
        const formGroup = service.createCocheFormGroup(sampleWithNewData);

        const coche = service.getCoche(formGroup) as any;

        expect(coche).toMatchObject(sampleWithNewData);
      });

      it('should return NewCoche for empty Coche initial value', () => {
        const formGroup = service.createCocheFormGroup();

        const coche = service.getCoche(formGroup) as any;

        expect(coche).toMatchObject({});
      });

      it('should return ICoche', () => {
        const formGroup = service.createCocheFormGroup(sampleWithRequiredData);

        const coche = service.getCoche(formGroup) as any;

        expect(coche).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICoche should not enable id FormControl', () => {
        const formGroup = service.createCocheFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCoche should disable id FormControl', () => {
        const formGroup = service.createCocheFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
