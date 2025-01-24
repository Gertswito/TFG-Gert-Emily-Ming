import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ILineasVenta } from './lineasVenta.model';

@Injectable({
  providedIn: 'root',
})
export class LineasVentaService {
  private resourceUrl = '/lineas-ventas';
  protected http = inject(HttpClient);

  getAllLineasVentas(): Observable<ILineasVenta[]> {
    return this.http.get<ILineasVenta[]>(`${this.resourceUrl}/all`);
  }
}
