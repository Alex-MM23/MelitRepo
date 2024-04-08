import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { EmpleadoService } from 'app/entities/empleado/service/empleado.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { ICoche } from 'app/entities/coche/coche.model';
import { CocheService } from 'app/entities/coche/service/coche.service';
import { IVenta } from '../venta.model';
import { VentaService } from '../service/venta.service';
import { VentaFormService } from './venta-form.service';

import { VentaUpdateComponent } from './venta-update.component';

describe('Venta Management Update Component', () => {
  let comp: VentaUpdateComponent;
  let fixture: ComponentFixture<VentaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ventaFormService: VentaFormService;
  let ventaService: VentaService;
  let empleadoService: EmpleadoService;
  let clienteService: ClienteService;
  let cocheService: CocheService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), VentaUpdateComponent],
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
      .overrideTemplate(VentaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VentaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ventaFormService = TestBed.inject(VentaFormService);
    ventaService = TestBed.inject(VentaService);
    empleadoService = TestBed.inject(EmpleadoService);
    clienteService = TestBed.inject(ClienteService);
    cocheService = TestBed.inject(CocheService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Empleado query and add missing value', () => {
      const venta: IVenta = { id: 456 };
      const empleado: IEmpleado = { id: 15142 };
      venta.empleado = empleado;

      const empleadoCollection: IEmpleado[] = [{ id: 3132 }];
      jest.spyOn(empleadoService, 'query').mockReturnValue(of(new HttpResponse({ body: empleadoCollection })));
      const additionalEmpleados = [empleado];
      const expectedCollection: IEmpleado[] = [...additionalEmpleados, ...empleadoCollection];
      jest.spyOn(empleadoService, 'addEmpleadoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ venta });
      comp.ngOnInit();

      expect(empleadoService.query).toHaveBeenCalled();
      expect(empleadoService.addEmpleadoToCollectionIfMissing).toHaveBeenCalledWith(
        empleadoCollection,
        ...additionalEmpleados.map(expect.objectContaining),
      );
      expect(comp.empleadosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Cliente query and add missing value', () => {
      const venta: IVenta = { id: 456 };
      const cliente: ICliente = { id: 15025 };
      venta.cliente = cliente;

      const clienteCollection: ICliente[] = [{ id: 10319 }];
      jest.spyOn(clienteService, 'query').mockReturnValue(of(new HttpResponse({ body: clienteCollection })));
      const additionalClientes = [cliente];
      const expectedCollection: ICliente[] = [...additionalClientes, ...clienteCollection];
      jest.spyOn(clienteService, 'addClienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ venta });
      comp.ngOnInit();

      expect(clienteService.query).toHaveBeenCalled();
      expect(clienteService.addClienteToCollectionIfMissing).toHaveBeenCalledWith(
        clienteCollection,
        ...additionalClientes.map(expect.objectContaining),
      );
      expect(comp.clientesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call id_coche query and add missing value', () => {
      const venta: IVenta = { id: 456 };
      const id_coche: ICoche = { id: 5472 };
      venta.id_coche = id_coche;

      const id_cocheCollection: ICoche[] = [{ id: 5195 }];
      jest.spyOn(cocheService, 'query').mockReturnValue(of(new HttpResponse({ body: id_cocheCollection })));
      const expectedCollection: ICoche[] = [id_coche, ...id_cocheCollection];
      jest.spyOn(cocheService, 'addCocheToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ venta });
      comp.ngOnInit();

      expect(cocheService.query).toHaveBeenCalled();
      expect(cocheService.addCocheToCollectionIfMissing).toHaveBeenCalledWith(id_cocheCollection, id_coche);
      expect(comp.id_cochesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const venta: IVenta = { id: 456 };
      const empleado: IEmpleado = { id: 22009 };
      venta.empleado = empleado;
      const cliente: ICliente = { id: 17566 };
      venta.cliente = cliente;
      const id_coche: ICoche = { id: 31395 };
      venta.id_coche = id_coche;

      activatedRoute.data = of({ venta });
      comp.ngOnInit();

      expect(comp.empleadosSharedCollection).toContain(empleado);
      expect(comp.clientesSharedCollection).toContain(cliente);
      expect(comp.id_cochesCollection).toContain(id_coche);
      expect(comp.venta).toEqual(venta);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVenta>>();
      const venta = { id: 123 };
      jest.spyOn(ventaFormService, 'getVenta').mockReturnValue(venta);
      jest.spyOn(ventaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ venta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: venta }));
      saveSubject.complete();

      // THEN
      expect(ventaFormService.getVenta).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ventaService.update).toHaveBeenCalledWith(expect.objectContaining(venta));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVenta>>();
      const venta = { id: 123 };
      jest.spyOn(ventaFormService, 'getVenta').mockReturnValue({ id: null });
      jest.spyOn(ventaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ venta: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: venta }));
      saveSubject.complete();

      // THEN
      expect(ventaFormService.getVenta).toHaveBeenCalled();
      expect(ventaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVenta>>();
      const venta = { id: 123 };
      jest.spyOn(ventaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ venta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ventaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEmpleado', () => {
      it('Should forward to empleadoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(empleadoService, 'compareEmpleado');
        comp.compareEmpleado(entity, entity2);
        expect(empleadoService.compareEmpleado).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCliente', () => {
      it('Should forward to clienteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(clienteService, 'compareCliente');
        comp.compareCliente(entity, entity2);
        expect(clienteService.compareCliente).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCoche', () => {
      it('Should forward to cocheService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(cocheService, 'compareCoche');
        comp.compareCoche(entity, entity2);
        expect(cocheService.compareCoche).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
