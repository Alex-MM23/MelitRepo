export interface IMarca {
  id: number;
  nombre?: string | null;
}

export type NewMarca = Omit<IMarca, 'id'> & { id: null };
