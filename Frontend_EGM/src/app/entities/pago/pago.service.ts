import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IPago } from './pago.model';

@Injectable({
  providedIn: 'root',
})
export class PagoService {
  private resourceUrl = 'http://localhost:8080/pagos';
  protected http = inject(HttpClient);

  getAllPagos(): Observable<IPago[]> {
    return this.http.get<IPago[]>(`${this.resourceUrl}/all`);
  }

  getPago(id: number): Observable<IPago> {
    return this.http.get<IPago>(`${this.resourceUrl}/find/${id}`);
  }

  getPagosByCliente(user: string): Observable<IPago[]> {
    return this.http.get<IPago[]>(`${this.resourceUrl}/cliente/${user}`);
  }

  createPago(user: string, pago: IPago): Observable<IPago> {
    return this.http.post<IPago>(`${this.resourceUrl}/new/${user}`, pago);
  }

  updatePago(id: number, pago: IPago): Observable<IPago> {
    return this.http.put<IPago>(`${this.resourceUrl}/update/${id}`, pago);
  }

  deletePago(id: number): Observable<any> {
    return this.http.delete<void>(`${this.resourceUrl}/delete/${id}`);
  }

  disablePago(id: number): Observable<any> {
    return this.http.delete<IPago>(`${this.resourceUrl}/disable/${id}`, { observe: 'response' });
  }

  enablePago(id: number): Observable<any> {
    return this.http.delete<IPago>(`${this.resourceUrl}/enable/${id}`, { observe: 'response' });
  }
}