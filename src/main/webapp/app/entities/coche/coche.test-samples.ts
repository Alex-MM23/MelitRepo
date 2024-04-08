import { ICoche, NewCoche } from './coche.model';

export const sampleWithRequiredData: ICoche = {
  id: 19628,
};

export const sampleWithPartialData: ICoche = {
  id: 21338,
  color: 'Morado',
  precio: 14753.15,
};

export const sampleWithFullData: ICoche = {
  id: 13927,
  color: 'Gris',
  numeroSerie: 'big-hearted swaddle',
  precio: 1012.53,
  exposicion: true,
};

export const sampleWithNewData: NewCoche = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
