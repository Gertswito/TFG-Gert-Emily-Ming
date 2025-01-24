import { ICliente } from "../cliente/cliente.model";

export interface IDireccion {
    id: number | null;
    direccion: string | null;
    codigoPostal: number | null;
    localidad: string | null;
    comunidadAutonoma: string | null;
    cliente: ICliente | null;
}