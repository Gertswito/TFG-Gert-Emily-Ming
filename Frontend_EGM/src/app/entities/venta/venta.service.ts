import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IVenta } from './venta.model';

@Injectable({
  providedIn: 'root',
})
export class VentaService {
  private resourceUrl = 'http://localhost:8080/ventas';
  protected http = inject(HttpClient);

  getAllVentas(): Observable<IVenta[]> {
    return this.http.get<IVenta[]>(`${this.resourceUrl}/all`);
  }

  deleteVenta(id: number): Observable<any> {
    return this.http.delete<IVenta>(`${this.resourceUrl}/delete/${id}`, { observe: 'response' });
  }
}
