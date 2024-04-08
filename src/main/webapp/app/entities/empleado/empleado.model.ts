export interface IEmpleado {
  id: number;
  dni?: string | null;
  nombre?: string | null;
  activo?: boolean | null;
  numeroVentas?: number | null;
}

export type NewEmpleado = Omit<IEmpleado, 'id'> & { id: null };
