import { ISubcategoria } from "../subcategoria/subcategoria.model";

export interface ICategoria {
  id: number | null;
  nombre: string | null;
  subcategorias?: ISubcategoria[];
}
