import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICoche, NewCoche } from '../coche.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICoche for edit and NewCocheFormGroupInput for create.
 */
type CocheFormGroupInput = ICoche | PartialWithRequiredKeyOf<NewCoche>;

type CocheFormDefaults = Pick<NewCoche, 'id' | 'exposicion'>;

type CocheFormGroupContent = {
  id: FormControl<ICoche['id'] | NewCoche['id']>;
  color: FormControl<ICoche['color']>;
  numeroSerie: FormControl<ICoche['numeroSerie']>;
  precio: FormControl<ICoche['precio']>;
  exposicion: FormControl<ICoche['exposicion']>;
  nPuertas: FormControl<ICoche['nPuertas']>;
  motor: FormControl<ICoche['motor']>;
  matricula: FormControl<ICoche['matricula']>;
  fecha_llegada: FormControl<ICoche['fecha_llegada']>
  fecha_venta: FormControl<ICoche['fecha_venta']>
  marca: FormControl<ICoche['marca']>;
  modelo: FormControl<ICoche['modelo']>;
};

export type CocheFormGroup = FormGroup<CocheFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CocheFormService {
  createCocheFormGroup(coche: CocheFormGroupInput = { id: null }): CocheFormGroup {
    const cocheRawValue = {
      ...this.getFormDefaults(),
      ...coche,
    };
    return new FormGroup<CocheFormGroupContent>({
      id: new FormControl(
        { value: cocheRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      color: new FormControl(cocheRawValue.color),
      numeroSerie: new FormControl(cocheRawValue.numeroSerie),
      precio: new FormControl(cocheRawValue.precio),
      exposicion: new FormControl(cocheRawValue.exposicion),
      nPuertas: new FormControl(cocheRawValue.nPuertas),
      motor: new FormControl(cocheRawValue.motor),
      matricula: new FormControl(cocheRawValue.matricula),
      fecha_llegada: new FormControl(cocheRawValue.fecha_llegada),
      fecha_venta: new FormControl(cocheRawValue.fecha_venta),
      marca: new FormControl(cocheRawValue.marca),
      modelo: new FormControl(cocheRawValue.modelo),
    });
  }

  getCoche(form: CocheFormGroup): ICoche | NewCoche {
    return form.getRawValue() as ICoche | NewCoche;
  }

  resetForm(form: CocheFormGroup, coche: CocheFormGroupInput): void {
    const cocheRawValue = { ...this.getFormDefaults(), ...coche };
    form.reset(
      {
        ...cocheRawValue,
        id: { value: cocheRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CocheFormDefaults {
    return {
      id: null,
      exposicion: false,
    };
  }
}
