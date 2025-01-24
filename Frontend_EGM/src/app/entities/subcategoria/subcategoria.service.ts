import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ISubcategoria } from './subcategoria.model';

@Injectable({
  providedIn: 'root',
})
export class SubcategoriaService {
  private resourceUrl = '/subcategorias';
  protected http = inject(HttpClient);

  getAllSubcategorias(): Observable<ISubcategoria[]> {
    return this.http.get<ISubcategoria[]>(`${this.resourceUrl}/all`);
  }
}
