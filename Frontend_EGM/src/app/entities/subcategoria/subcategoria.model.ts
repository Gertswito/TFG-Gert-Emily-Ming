import { ICategoria } from '../categoria/categoria.model';

export interface ISubcategoria {
  id: number | null;
  nombre: string | null;
  categoria: ICategoria | null;
}
