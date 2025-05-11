import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ISubcategoria } from './subcategoria.model';

@Injectable({
  providedIn: 'root',
})
export class SubcategoriaService {
  private resourceUrl = 'http://localhost:8080/subcategorias';
  protected http = inject(HttpClient);

  getAllSubcategorias(): Observable<ISubcategoria[]> {
    return this.http.get<ISubcategoria[]>(`${this.resourceUrl}/all`);
  }

  getSubcategoria(id: number): Observable<ISubcategoria> {
    return this.http.get<ISubcategoria>(`${this.resourceUrl}/find/${id}`);
  }

  getSubcategoriasConIdCategoria(id: number): Observable<ISubcategoria[]> {
    return this.http.get<ISubcategoria[]>(`${this.resourceUrl}/categoria/${id}`);
  }

  getSubcategoriasConFiltro(texto: string): Observable<ISubcategoria[]> {
    return this.http.get<ISubcategoria[]>(`${this.resourceUrl}/busqueda/${texto}`);
  }

  getSubcategoriasConFiltroCategoria(id: number, texto: string): Observable<ISubcategoria[]> {
    return this.http.get<ISubcategoria[]>(`${this.resourceUrl}/busqueda/${texto}/categoria/${id}`);
  }

  getSubcategoriaConFiltroAdmin(texto: string): Observable<ISubcategoria[]> {
    return this.http.get<ISubcategoria[]>(`${this.resourceUrl}/admin-busqueda/${texto}`);
  }

  deleteSubcategoria(id: number): Observable<any> {
    return this.http.delete<ISubcategoria>(`${this.resourceUrl}/delete/${id}`, { observe: 'response' });
  }

  crearSubcategoria(subcategoria: ISubcategoria): Observable<HttpResponse<ISubcategoria>> {
    return this.http.post<ISubcategoria>(`${this.resourceUrl}/new`, subcategoria, { observe: 'response' });
  }

  editarSubcategoria(subcategoria: ISubcategoria): Observable<HttpResponse<ISubcategoria>> {
    return this.http.put<ISubcategoria>(`${this.resourceUrl}/update/${subcategoria.id}`, subcategoria, { observe: 'response' });
  }
}
