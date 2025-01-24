import { ICliente } from "../cliente/cliente.model";

export interface IVenta {
    id: number | null;
    cliente: ICliente | null;
    fecha: Date | null;
    hora: Date | null;
    precioFinal: number | null;
}