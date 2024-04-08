import { ICliente, NewCliente } from './cliente.model';

export const sampleWithRequiredData: ICliente = {
  id: 8006,
};

export const sampleWithPartialData: ICliente = {
  id: 19533,
  nombre: 'accidentally',
};

export const sampleWithFullData: ICliente = {
  id: 21972,
  dni: 'soup',
  nombre: 'eventually',
  numeroCompras: 9329,
  tier: 24746,
};

export const sampleWithNewData: NewCliente = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
