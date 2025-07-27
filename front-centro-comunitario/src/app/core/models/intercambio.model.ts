import { RecursoTipo } from './recurso-tipo.model';

export interface Intercambio {
  id?: string;
  origemId: string;
  destinoId: string;
  recursos: {
    tipo: RecursoTipo;
    quantidade: number;
  }[];
  data: string;
}
