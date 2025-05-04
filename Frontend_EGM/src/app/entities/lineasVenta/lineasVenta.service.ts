import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ILineasVenta } from './lineasVenta.model';

@Injectable({
  providedIn: 'root',
})
export class LineasVentaService {
  private resourceUrl = 'http://localhost:8080/lineas-ventas';
  protected http = inject(HttpClient);

  getAllLineasVentas(): Observable<ILineasVenta[]> {
    return this.http.get<ILineasVenta[]>(`${this.resourceUrl}/all`);
  }

  getLineaVenta(id: number): Observable<ILineasVenta> {
    return this.http.get<ILineasVenta>(`${this.resourceUrl}/find/${id}`);
  }

  deleteLineasVenta(id: number): Observable<any> {
    return this.http.delete<ILineasVenta>(`${this.resourceUrl}/delete/${id}`, { observe: 'response' });
  }

  crearLineaVenta(lineaVenta: ILineasVenta): Observable<HttpResponse<ILineasVenta>> {
    return this.http.post<ILineasVenta>(`${this.resourceUrl}/new`, lineaVenta, { observe: 'response' });
  }

  editarLineaVenta(lineaVenta: ILineasVenta): Observable<HttpResponse<ILineasVenta>> {
    return this.http.put<ILineasVenta>(`${this.resourceUrl}/update/${lineaVenta.id}`, lineaVenta, { observe: 'response' });
  }
}
