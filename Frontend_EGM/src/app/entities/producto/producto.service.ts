import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IProducto } from './producto.model';

@Injectable({
  providedIn: 'root',
})
export class ProductoService {
  private resourceUrl = 'http://localhost:8080/productos';
  protected http = inject(HttpClient);

  getAllProductos(): Observable<IProducto[]> {
    return this.http.get<IProducto[]>(`${this.resourceUrl}/all`);
  }

  getProductosConIdSubcategoria(id: number): Observable<IProducto[]> {
    return this.http.get<IProducto[]>(`${this.resourceUrl}/subcategoria/${id}`);
  }

  getProducto(id: number): Observable<IProducto> {
    return this.http.get<IProducto>(`${this.resourceUrl}/find/${id}`);
  }

  getProductoConFiltro(texto: string): Observable<IProducto[]> {
    return this.http.get<IProducto[]>(`${this.resourceUrl}/busqueda/${texto}`);
  }

  getProductoConFiltroSubcategoria(id: number, texto: string): Observable<IProducto[]> {
    return this.http.get<IProducto[]>(`${this.resourceUrl}/busqueda/${texto}/subcategoria/${id}`);
  }

  getProductoConFiltroAdmin(texto: string): Observable<IProducto[]> {
    return this.http.get<IProducto[]>(`${this.resourceUrl}/admin-busqueda/${texto}`);
  }

  getProductosConStockBajo(): Observable<IProducto[]> {
    return this.http.get<IProducto[]>(`${this.resourceUrl}/stock-bajo`);
  }

  deleteProducto(id: number): Observable<any> {
    return this.http.delete<IProducto>(`${this.resourceUrl}/delete/${id}`, { observe: 'response' });
  }

  crearProducto(producto: IProducto): Observable<HttpResponse<IProducto>> {
    return this.http.post<IProducto>(`${this.resourceUrl}/new`, producto, { observe: 'response' });
  }

  editarProducto(producto: IProducto): Observable<HttpResponse<IProducto>> {
    return this.http.put<IProducto>(`${this.resourceUrl}/update/${producto.id}`, producto, { observe: 'response' });
  }

  addStockAlProducto(id: number, stock: number): Observable<HttpResponse<IProducto>> {
    return this.http.post<IProducto>(`${this.resourceUrl}/add-stock/${id}/${stock}`, {}, { observe: 'response' });
  }
}
