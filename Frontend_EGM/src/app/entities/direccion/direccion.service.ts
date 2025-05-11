import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IDireccion } from './direccion.model';

@Injectable({
  providedIn: 'root',
})
export class DireccionService {
  private resourceUrl = 'http://localhost:8080/direcciones';
  protected http = inject(HttpClient);

  getAllDirecciones(): Observable<IDireccion[]> {
    return this.http.get<IDireccion[]>(`${this.resourceUrl}/all`);
  }

  getDireccion(id: number): Observable<IDireccion> {
    return this.http.get<IDireccion>(`${this.resourceUrl}/find/${id}`);
  }

  getDireccionesPorCliente(user: string): Observable<IDireccion[]> {
    return this.http.get<IDireccion[]>(`${this.resourceUrl}/cliente/${user}`);
  }

  getDireccionConFiltroAdmin(texto: string): Observable<IDireccion[]> {
    return this.http.get<IDireccion[]>(`${this.resourceUrl}/admin-busqueda/${texto}`);
  }

  createDireccion(user: string,direccion: IDireccion): Observable<IDireccion> {
    return this.http.post<IDireccion>(`${this.resourceUrl}/new/${user}`, direccion);
  }
  
  updateDireccion(id: number, direccion: IDireccion): Observable<IDireccion> {
    return this.http.put<IDireccion>(`${this.resourceUrl}/update/${id}`, direccion);
  }

  deleteDireccion(id: number): Observable<any> {
    return this.http.delete<IDireccion>(`${this.resourceUrl}/delete/${id}`, { observe: 'response' });
  }

  disableDireccion(id: number): Observable<any> {
    return this.http.delete<IDireccion>(`${this.resourceUrl}/disable/${id}`, { observe: 'response' });
  }

  enableDireccion(id: number): Observable<any> {
    return this.http.delete<IDireccion>(`${this.resourceUrl}/enable/${id}`, { observe: 'response' });
  }
}