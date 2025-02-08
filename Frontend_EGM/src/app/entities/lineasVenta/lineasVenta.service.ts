import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ILineasVenta } from './lineasVenta.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class LineasVentaService {
<<<<<<< Updated upstream
  private resourceUrl = '/lineas-ventas';
=======
  private resourceUrl = `${environment.apiUrl}/lineas-ventas`;
>>>>>>> Stashed changes
  protected http = inject(HttpClient);

  getAllLineasVentas(): Observable<ILineasVenta[]> {
    return this.http.get<ILineasVenta[]>(`${this.resourceUrl}/all`);
  }
}
