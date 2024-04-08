import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MarcaService } from '../service/marca.service';
import { IMarca } from '../marca.model';
import { MarcaFormService } from './marca-form.service';

import { MarcaUpdateComponent } from './marca-update.component';

describe('Marca Management Update Component', () => {
  let comp: MarcaUpdateComponent;
  let fixture: ComponentFixture<MarcaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let marcaFormService: MarcaFormService;
  let marcaService: MarcaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), MarcaUpdateComponent],
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
      .overrideTemplate(MarcaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MarcaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    marcaFormService = TestBed.inject(MarcaFormService);
    marcaService = TestBed.inject(MarcaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const marca: IMarca = { id: 456 };

      activatedRoute.data = of({ marca });
      comp.ngOnInit();

      expect(comp.marca).toEqual(marca);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMarca>>();
      const marca = { id: 123 };
      jest.spyOn(marcaFormService, 'getMarca').mockReturnValue(marca);
      jest.spyOn(marcaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ marca });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: marca }));
      saveSubject.complete();

      // THEN
      expect(marcaFormService.getMarca).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(marcaService.update).toHaveBeenCalledWith(expect.objectContaining(marca));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMarca>>();
      const marca = { id: 123 };
      jest.spyOn(marcaFormService, 'getMarca').mockReturnValue({ id: null });
      jest.spyOn(marcaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ marca: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: marca }));
      saveSubject.complete();

      // THEN
      expect(marcaFormService.getMarca).toHaveBeenCalled();
      expect(marcaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMarca>>();
      const marca = { id: 123 };
      jest.spyOn(marcaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ marca });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(marcaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
