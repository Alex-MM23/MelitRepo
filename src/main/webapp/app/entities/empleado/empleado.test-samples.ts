import { IEmpleado, NewEmpleado } from './empleado.model';

export const sampleWithRequiredData: IEmpleado = {
  id: 4426,
};

export const sampleWithPartialData: IEmpleado = {
  id: 25577,
  dni: 'homely',
  activo: true,
};

export const sampleWithFullData: IEmpleado = {
  id: 11311,
  dni: 'marketing class misspell',
  nombre: 'anenst',
  activo: true,
  numeroVentas: 7559,
};

export const sampleWithNewData: NewEmpleado = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
