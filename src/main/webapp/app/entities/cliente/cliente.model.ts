export interface ICliente {
  id: number;
  dni?: string | null;
  nombre?: string | null;
  numeroCompras?: number | null;
  tier?: number | null;
}

export type NewCliente = Omit<ICliente, 'id'> & { id: null };
