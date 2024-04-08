import { IMarca, NewMarca } from './marca.model';

export const sampleWithRequiredData: IMarca = {
  id: 30818,
};

export const sampleWithPartialData: IMarca = {
  id: 8396,
  nombre: 'duh afore ugh',
};

export const sampleWithFullData: IMarca = {
  id: 7732,
  nombre: 'blanket ugh',
};

export const sampleWithNewData: NewMarca = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
