import { ICoche, NewCoche } from './coche.model';

export const sampleWithRequiredData: ICoche = {
  id: 16166,
};

export const sampleWithPartialData: ICoche = {
  id: 13716,
  nPuertas: 19706,
  motor: 'DIESEL',
  matricula: 'pfft',
};

export const sampleWithFullData: ICoche = {
  id: 15435,
  color: 'Blanco',
  numeroSerie: 'sans patio drat',
  precio: 13099.24,
  exposicion: true,
  nPuertas: 5435,
  motor: 'DIESEL',
  matricula: 'dote advocate during',
};

export const sampleWithNewData: NewCoche = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
