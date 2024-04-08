import { IMarca } from 'app/entities/marca/marca.model';
import { IModelo } from 'app/entities/modelo/modelo.model';

export interface ICoche {
  id: number;
  color?: string | null;
  numeroSerie?: string | null;
  precio?: number | null;
  exposicion?: boolean | null;
  npuertas?: number | null;
  marca?: IMarca | null;
  modelo?: IModelo | null;
}

export type NewCoche = Omit<ICoche, 'id'> & { id: null };
