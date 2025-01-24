import { ICliente } from "../cliente/cliente.model";

export interface IPago {
    id: number | null;
    numeroTarjeta: string | null;
    fechaCaducidad: Date | null;
    cvv: string | null;
    cliente: ICliente | null;
}