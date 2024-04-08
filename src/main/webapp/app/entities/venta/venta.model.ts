import dayjs from 'dayjs/esm';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ICoche } from 'app/entities/coche/coche.model';

export interface IVenta {
  id: number;
  fecha?: dayjs.Dayjs | null;
  tipoPago?: string | null;
  total?: number | null;
  empleado?: IEmpleado | null;
  cliente?: ICliente | null;
  id_coche?: ICoche | null;
}

export type NewVenta = Omit<IVenta, 'id'> & { id: null };
