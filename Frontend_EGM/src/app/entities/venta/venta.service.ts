import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { IVenta } from './venta.model';

@Injectable({
  providedIn: 'root',
})
export class VentaService {
  private resourceUrl = 'http://localhost:8080/ventas';
  protected http = inject(HttpClient);

  getAllVentasFormateadas(): Observable<IVenta[]> {
    return this.http.get<IVenta[]>(`${this.resourceUrl}/all`).pipe(
      map((res: any[]) => res.map(venta => ({
        ...venta,
      fechaHora: new Date(venta.fechaHora)})))
    );
  }

  getAllVentas(): Observable<IVenta[]> {
    return this.http.get<IVenta[]>(`${this.resourceUrl}/all`);
  }

  deleteVenta(id: number): Observable<any> {
    return this.http.delete<IVenta>(`${this.resourceUrl}/delete/${id}`, { observe: 'response' });
  }

  crearVenta(venta: IVenta): Observable<HttpResponse<IVenta>> {
    return this.http.post<IVenta>(`${this.resourceUrl}/new`, venta, { observe: 'response' });
  }
}
