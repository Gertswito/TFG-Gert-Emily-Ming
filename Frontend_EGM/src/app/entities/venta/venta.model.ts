import { ICliente } from "../cliente/cliente.model";
import { IDireccion } from "../direccion/direccion.model";
import { IPago } from "../pago/pago.model";

export interface IVenta {
    id: number | null;
    cliente: ICliente | null;
    fecha: Date | null;
    hora: Date | null;
    precioFinal: number | null;
    direccion: IDireccion | null;
    pago: IPago | null;
}