import { IProducto } from "../producto/producto.model";
import { IVenta } from "../venta/venta.model";

export interface ILineasVenta {
    id: number | null;
    venta: IVenta | null;
    producto: IProducto | null;
    cantidadPedida: number | null;
    precioUnitario: number | null;
    precioTotal: number | null;
}