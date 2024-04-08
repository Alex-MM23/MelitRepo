import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IVenta, NewVenta } from '../venta.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVenta for edit and NewVentaFormGroupInput for create.
 */
type VentaFormGroupInput = IVenta | PartialWithRequiredKeyOf<NewVenta>;

type VentaFormDefaults = Pick<NewVenta, 'id'>;

type VentaFormGroupContent = {
  id: FormControl<IVenta['id'] | NewVenta['id']>;
  fecha: FormControl<IVenta['fecha']>;
  tipoPago: FormControl<IVenta['tipoPago']>;
  total: FormControl<IVenta['total']>;
  empleado: FormControl<IVenta['empleado']>;
  cliente: FormControl<IVenta['cliente']>;
  id_coche: FormControl<IVenta['id_coche']>;
};

export type VentaFormGroup = FormGroup<VentaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VentaFormService {
  createVentaFormGroup(venta: VentaFormGroupInput = { id: null }): VentaFormGroup {
    const ventaRawValue = {
      ...this.getFormDefaults(),
      ...venta,
    };
    return new FormGroup<VentaFormGroupContent>({
      id: new FormControl(
        { value: ventaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fecha: new FormControl(ventaRawValue.fecha),
      tipoPago: new FormControl(ventaRawValue.tipoPago),
      total: new FormControl(ventaRawValue.total),
      empleado: new FormControl(ventaRawValue.empleado),
      cliente: new FormControl(ventaRawValue.cliente),
      id_coche: new FormControl(ventaRawValue.id_coche),
    });
  }

  getVenta(form: VentaFormGroup): IVenta | NewVenta {
    return form.getRawValue() as IVenta | NewVenta;
  }

  resetForm(form: VentaFormGroup, venta: VentaFormGroupInput): void {
    const ventaRawValue = { ...this.getFormDefaults(), ...venta };
    form.reset(
      {
        ...ventaRawValue,
        id: { value: ventaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VentaFormDefaults {
    return {
      id: null,
    };
  }
}
