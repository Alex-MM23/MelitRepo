import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEmpleado, NewEmpleado } from '../empleado.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmpleado for edit and NewEmpleadoFormGroupInput for create.
 */
type EmpleadoFormGroupInput = IEmpleado | PartialWithRequiredKeyOf<NewEmpleado>;

type EmpleadoFormDefaults = Pick<NewEmpleado, 'id' | 'activo'>;

type EmpleadoFormGroupContent = {
  id: FormControl<IEmpleado['id'] | NewEmpleado['id']>;
  dni: FormControl<IEmpleado['dni']>;
  nombre: FormControl<IEmpleado['nombre']>;
  activo: FormControl<IEmpleado['activo']>;
  numeroVentas: FormControl<IEmpleado['numeroVentas']>;
};

export type EmpleadoFormGroup = FormGroup<EmpleadoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmpleadoFormService {
  createEmpleadoFormGroup(empleado: EmpleadoFormGroupInput = { id: null }): EmpleadoFormGroup {
    const empleadoRawValue = {
      ...this.getFormDefaults(),
      ...empleado,
    };
    return new FormGroup<EmpleadoFormGroupContent>({
      id: new FormControl(
        { value: empleadoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dni: new FormControl(empleadoRawValue.dni),
      nombre: new FormControl(empleadoRawValue.nombre),
      activo: new FormControl(empleadoRawValue.activo),
      numeroVentas: new FormControl(empleadoRawValue.numeroVentas),
    });
  }

  getEmpleado(form: EmpleadoFormGroup): IEmpleado | NewEmpleado {
    return form.getRawValue() as IEmpleado | NewEmpleado;
  }

  resetForm(form: EmpleadoFormGroup, empleado: EmpleadoFormGroupInput): void {
    const empleadoRawValue = { ...this.getFormDefaults(), ...empleado };
    form.reset(
      {
        ...empleadoRawValue,
        id: { value: empleadoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EmpleadoFormDefaults {
    return {
      id: null,
      activo: false,
    };
  }
}
