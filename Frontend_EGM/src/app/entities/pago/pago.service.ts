import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IPago } from './pago.model';

@Injectable({
  providedIn: 'root',
})
export class PagoService {
  private resourceUrl = '/pagos';
  protected http = inject(HttpClient);

  getAllPagos(): Observable<IPago[]> {
    return this.http.get<IPago[]>(`${this.resourceUrl}/all`);
  }
}