import { IDireccion } from "../direccion/direccion.model";
import { IPago } from "../pago/pago.model";

export interface ICliente {
  id: number | null;
  rol: string | null;
  dni: string | null;
  nombre: string | null;
  apellidos: string | null;
  usuario: string | null;
  email: string | null;
  telefono: string | null;
  direccion: IDireccion | null;
  pago: IPago | null;
  contrasenha: string | null;
  fechaNacimiento: Date | null;
}
