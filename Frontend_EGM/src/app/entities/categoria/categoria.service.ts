import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ICategoria } from './categoria.model';

@Injectable({
  providedIn: 'root',
})
export class CategoriaService {
  private resourceUrl = '/categorias';
  protected http = inject(HttpClient);

  getAllCategorias(): Observable<ICategoria[]> {
    return this.http.get<ICategoria[]>(`${this.resourceUrl}/all`);
  }
}
