import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IMarca } from 'app/entities/marca/marca.model';
import { MarcaService } from 'app/entities/marca/service/marca.service';
import { IModelo } from 'app/entities/modelo/modelo.model';
import { ModeloService } from 'app/entities/modelo/service/modelo.service';
import { ICoche } from '../coche.model';
import { CocheService } from '../service/coche.service';
import { CocheFormService } from './coche-form.service';

import { CocheUpdateComponent } from './coche-update.component';

describe('Coche Management Update Component', () => {
  let comp: CocheUpdateComponent;
  let fixture: ComponentFixture<CocheUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cocheFormService: CocheFormService;
  let cocheService: CocheService;
  let marcaService: MarcaService;
  let modeloService: ModeloService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CocheUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CocheUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CocheUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cocheFormService = TestBed.inject(CocheFormService);
    cocheService = TestBed.inject(CocheService);
    marcaService = TestBed.inject(MarcaService);
    modeloService = TestBed.inject(ModeloService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Marca query and add missing value', () => {
      const coche: ICoche = { id: 456 };
      const marca: IMarca = { id: 13111 };
      coche.marca = marca;

      const marcaCollection: IMarca[] = [{ id: 1118 }];
      jest.spyOn(marcaService, 'query').mockReturnValue(of(new HttpResponse({ body: marcaCollection })));
      const additionalMarcas = [marca];
      const expectedCollection: IMarca[] = [...additionalMarcas, ...marcaCollection];
      jest.spyOn(marcaService, 'addMarcaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ coche });
      comp.ngOnInit();

      expect(marcaService.query).toHaveBeenCalled();
      expect(marcaService.addMarcaToCollectionIfMissing).toHaveBeenCalledWith(
        marcaCollection,
        ...additionalMarcas.map(expect.objectContaining),
      );
      expect(comp.marcasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Modelo query and add missing value', () => {
      const coche: ICoche = { id: 456 };
      const modelo: IModelo = { id: 28500 };
      coche.modelo = modelo;

      const modeloCollection: IModelo[] = [{ id: 2616 }];
      jest.spyOn(modeloService, 'query').mockReturnValue(of(new HttpResponse({ body: modeloCollection })));
      const additionalModelos = [modelo];
      const expectedCollection: IModelo[] = [...additionalModelos, ...modeloCollection];
      jest.spyOn(modeloService, 'addModeloToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ coche });
      comp.ngOnInit();

      expect(modeloService.query).toHaveBeenCalled();
      expect(modeloService.addModeloToCollectionIfMissing).toHaveBeenCalledWith(
        modeloCollection,
        ...additionalModelos.map(expect.objectContaining),
      );
      expect(comp.modelosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const coche: ICoche = { id: 456 };
      const marca: IMarca = { id: 4152 };
      coche.marca = marca;
      const modelo: IModelo = { id: 27753 };
      coche.modelo = modelo;

      activatedRoute.data = of({ coche });
      comp.ngOnInit();

      expect(comp.marcasSharedCollection).toContain(marca);
      expect(comp.modelosSharedCollection).toContain(modelo);
      expect(comp.coche).toEqual(coche);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICoche>>();
      const coche = { id: 123 };
      jest.spyOn(cocheFormService, 'getCoche').mockReturnValue(coche);
      jest.spyOn(cocheService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ coche });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: coche }));
      saveSubject.complete();

      // THEN
      expect(cocheFormService.getCoche).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cocheService.update).toHaveBeenCalledWith(expect.objectContaining(coche));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICoche>>();
      const coche = { id: 123 };
      jest.spyOn(cocheFormService, 'getCoche').mockReturnValue({ id: null });
      jest.spyOn(cocheService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ coche: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: coche }));
      saveSubject.complete();

      // THEN
      expect(cocheFormService.getCoche).toHaveBeenCalled();
      expect(cocheService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICoche>>();
      const coche = { id: 123 };
      jest.spyOn(cocheService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ coche });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cocheService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMarca', () => {
      it('Should forward to marcaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(marcaService, 'compareMarca');
        comp.compareMarca(entity, entity2);
        expect(marcaService.compareMarca).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareModelo', () => {
      it('Should forward to modeloService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(modeloService, 'compareModelo');
        comp.compareModelo(entity, entity2);
        expect(modeloService.compareModelo).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
