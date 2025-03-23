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

  deleteDireccion(id: number): Observable<any> {
    return this.http.delete<IDireccion>(`${this.resourceUrl}/delete/${id}`, { observe: 'response' });
  }
}