import { IMarca } from 'app/entities/marca/marca.model';

export interface IModelo {
  id: number;
  nombre?: string | null;
  marca?: IMarca | null;
}

export type NewModelo = Omit<IModelo, 'id'> & { id: null };
