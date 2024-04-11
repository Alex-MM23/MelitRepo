import { IMarca } from 'app/entities/marca/marca.model';
import { IModelo } from 'app/entities/modelo/modelo.model';
import { motor } from 'app/entities/enumerations/motor.model';

export interface ICoche {
  id: number;
  color?: string | null;
  numeroSerie?: string | null;
  precio?: number | null;
  exposicion?: boolean | null;
  nPuertas?: number | null;
  motor?: keyof typeof motor | null;
  matricula?: string | null;
  anio?: number | null;
  pegatina?: string | null;
  marca?: IMarca | null;
  modelo?: IModelo | null;
}

export type NewCoche = Omit<ICoche, 'id'> & { id: null };
