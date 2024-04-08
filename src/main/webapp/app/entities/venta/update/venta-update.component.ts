import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { EmpleadoService } from 'app/entities/empleado/service/empleado.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { ICoche } from 'app/entities/coche/coche.model';
import { CocheService } from 'app/entities/coche/service/coche.service';
import { VentaService } from '../service/venta.service';
import { IVenta } from '../venta.model';
import { VentaFormService, VentaFormGroup } from './venta-form.service';

@Component({
  standalone: true,
  selector: 'jhi-venta-update',
  templateUrl: './venta-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VentaUpdateComponent implements OnInit {
  isSaving = false;
  venta: IVenta | null = null;

  empleadosSharedCollection: IEmpleado[] = [];
  clientesSharedCollection: ICliente[] = [];
  id_cochesCollection: ICoche[] = [];

  protected ventaService = inject(VentaService);
  protected ventaFormService = inject(VentaFormService);
  protected empleadoService = inject(EmpleadoService);
  protected clienteService = inject(ClienteService);
  protected cocheService = inject(CocheService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VentaFormGroup = this.ventaFormService.createVentaFormGroup();

  compareEmpleado = (o1: IEmpleado | null, o2: IEmpleado | null): boolean => this.empleadoService.compareEmpleado(o1, o2);

  compareCliente = (o1: ICliente | null, o2: ICliente | null): boolean => this.clienteService.compareCliente(o1, o2);

  compareCoche = (o1: ICoche | null, o2: ICoche | null): boolean => this.cocheService.compareCoche(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ venta }) => {
      this.venta = venta;
      if (venta) {
        this.updateForm(venta);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const venta = this.ventaFormService.getVenta(this.editForm);
    if (venta.id !== null) {
      this.subscribeToSaveResponse(this.ventaService.update(venta));
    } else {
      this.subscribeToSaveResponse(this.ventaService.create(venta));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVenta>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(venta: IVenta): void {
    this.venta = venta;
    this.ventaFormService.resetForm(this.editForm, venta);

    this.empleadosSharedCollection = this.empleadoService.addEmpleadoToCollectionIfMissing<IEmpleado>(
      this.empleadosSharedCollection,
      venta.empleado,
    );
    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing<ICliente>(
      this.clientesSharedCollection,
      venta.cliente,
    );
    this.id_cochesCollection = this.cocheService.addCocheToCollectionIfMissing<ICoche>(this.id_cochesCollection, venta.id_coche);
  }

  protected loadRelationshipsOptions(): void {
    this.empleadoService
      .query()
      .pipe(map((res: HttpResponse<IEmpleado[]>) => res.body ?? []))
      .pipe(
        map((empleados: IEmpleado[]) => this.empleadoService.addEmpleadoToCollectionIfMissing<IEmpleado>(empleados, this.venta?.empleado)),
      )
      .subscribe((empleados: IEmpleado[]) => (this.empleadosSharedCollection = empleados));

    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing<ICliente>(clientes, this.venta?.cliente)))
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));

    this.cocheService
      .query({ filter: 'venta-is-null' })
      .pipe(map((res: HttpResponse<ICoche[]>) => res.body ?? []))
      .pipe(map((coches: ICoche[]) => this.cocheService.addCocheToCollectionIfMissing<ICoche>(coches, this.venta?.id_coche)))
      .subscribe((coches: ICoche[]) => (this.id_cochesCollection = coches));
  }
}
