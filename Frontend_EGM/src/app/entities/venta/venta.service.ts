import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IVenta } from './venta.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class VentaService {
<<<<<<< Updated upstream
  private resourceUrl = '/ventas';
=======
  private resourceUrl = `${environment.apiUrl}/ventas`;
>>>>>>> Stashed changes
  protected http = inject(HttpClient);

  getAllVentas(): Observable<IVenta[]> {
    return this.http.get<IVenta[]>(`${this.resourceUrl}/all`);
  }
}
