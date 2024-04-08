import dayjs from 'dayjs/esm';

import { IVenta, NewVenta } from './venta.model';

export const sampleWithRequiredData: IVenta = {
  id: 17303,
};

export const sampleWithPartialData: IVenta = {
  id: 26099,
  fecha: dayjs('2024-04-03'),
  tipoPago: 'overproduce',
};

export const sampleWithFullData: IVenta = {
  id: 3108,
  fecha: dayjs('2024-04-03'),
  tipoPago: 'of ugh pessimistic',
  total: 29127.06,
};

export const sampleWithNewData: NewVenta = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
