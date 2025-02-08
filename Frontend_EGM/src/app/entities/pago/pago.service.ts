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
}