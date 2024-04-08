import { IModelo, NewModelo } from './modelo.model';

export const sampleWithRequiredData: IModelo = {
  id: 4143,
};

export const sampleWithPartialData: IModelo = {
  id: 22194,
};

export const sampleWithFullData: IModelo = {
  id: 25597,
  nombre: 'phooey credential flickering',
};

export const sampleWithNewData: NewModelo = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
