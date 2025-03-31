import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
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

  deleteCategoria(id: number): Observable<any> {
    return this.http.delete<ICategoria>(`${this.resourceUrl}/delete/${id}`, { observe: 'response' });
  }
}
