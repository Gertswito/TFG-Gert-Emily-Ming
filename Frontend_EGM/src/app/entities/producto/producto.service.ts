import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IProducto } from './producto.model';

@Injectable({
  providedIn: 'root',
})
export class ProductoService {
  private resourceUrl = '/productos';
  protected http = inject(HttpClient);

  getAllProductos(): Observable<IProducto[]> {
    return this.http.get<IProducto[]>(`${this.resourceUrl}/all`);
  }
}
