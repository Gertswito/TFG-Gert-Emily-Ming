import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IProducto } from './producto.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ProductoService {
<<<<<<< Updated upstream
  private resourceUrl = '/productos';
=======
  private resourceUrl = `${environment.apiUrl}/productos`;
>>>>>>> Stashed changes
  protected http = inject(HttpClient);

  getAllProductos(): Observable<IProducto[]> {
    return this.http.get<IProducto[]>(`${this.resourceUrl}/all`);
  }
}
