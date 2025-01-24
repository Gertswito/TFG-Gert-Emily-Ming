import { ICategoria } from "../categoria/categoria.model";
import { ISubcategoria } from "../subcategoria/subcategoria.model";

export interface IProducto {
    id: number | null;
    urlImagen: string | null;
    referencia: string | null;
    nombre: string | null;
    marca: string | null;
    categoria: ICategoria | null;
    subcategoria: ISubcategoria | null;
    descripcion: string | null;
    ingredientes: string | null;
    tipoIVA: number | null;
    cantidad: number | null;
    stock: number | null;
    precio: number | null;
}