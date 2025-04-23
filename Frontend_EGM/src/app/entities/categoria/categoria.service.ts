import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ICategoria } from './categoria.model';

@Injectable({
  providedIn: 'root',
})
export class CategoriaService {
  private resourceUrl = 'http://localhost:8080/categorias';
  protected http = inject(HttpClient);

  getAllCategorias(): Observable<ICategoria[]> {
    return this.http.get<ICategoria[]>(`${this.resourceUrl}/all`);
  }

  getCategoria(id: number): Observable<ICategoria> {
    return this.http.get<ICategoria>(`${this.resourceUrl}/find/${id}`);
  }

  deleteCategoria(id: number): Observable<any> {
    return this.http.delete<ICategoria>(`${this.resourceUrl}/delete/${id}`, { observe: 'response' });
  }

  crearCategoria(categoria: ICategoria): Observable<HttpResponse<ICategoria>> {
    return this.http.post<ICategoria>(`${this.resourceUrl}/new`, categoria, { observe: 'response' });
  }

  editarCategoria(categoria: ICategoria): Observable<HttpResponse<ICategoria>> {
    return this.http.put<ICategoria>(`${this.resourceUrl}/update/${categoria.id}`, categoria, { observe: 'response' });
  }
}
