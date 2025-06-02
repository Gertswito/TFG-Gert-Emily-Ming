import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ICliente } from './cliente.model';

export type EntityResponseType = HttpResponse<ICliente>;

@Injectable({
  providedIn: 'root',
})
export class ClienteService {
  private resourceUrl = 'http://localhost:8080/clientes';
  protected http = inject(HttpClient);

  getAllClientes(): Observable<ICliente[]> {
    return this.http.get<ICliente[]>(`${this.resourceUrl}/all`);
  }

  getCliente(usuario: string): Observable<ICliente> {
    return this.http.get<ICliente>(`${this.resourceUrl}/usuario/${usuario}`);
  }

  getClienteById(id: number): Observable<ICliente> {
    return this.http.get<ICliente>(`${this.resourceUrl}/find/${id}`);
  }

  getClienteConFiltroAdmin(texto: string): Observable<ICliente[]> {
    return this.http.get<ICliente[]>(`${this.resourceUrl}/admin-busqueda/${texto}`);
  }

  crearUsuario(cliente: ICliente): Observable<any> { 
    return this.http.post<any>(`${this.resourceUrl}/new`, cliente, { observe: 'response' });
  }

  registrar(cliente: ICliente): Observable<any> { 
    return this.http.post<any>(`${this.resourceUrl}/registrar`, cliente, { observe: 'response' });
  }

  login(cliente: ICliente): Observable<any> {
    return this.http.post<any>(`${this.resourceUrl}/login`, cliente, { observe: 'response' });
  }

  updateCliente(cliente: ICliente): Observable<any> {
    return this.http.put<ICliente>(`${this.resourceUrl}/update`, cliente, { observe: 'response' });
  }

  deleteCliente(id: number): Observable<any> {
    return this.http.delete<ICliente>(`${this.resourceUrl}/delete/${id}`, { observe: 'response' });
  }
}