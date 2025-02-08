import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ISubcategoria } from './subcategoria.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class SubcategoriaService {
<<<<<<< Updated upstream
  private resourceUrl = '/subcategorias';
=======
  private resourceUrl = `${environment.apiUrl}/subcategorias`;

>>>>>>> Stashed changes
  protected http = inject(HttpClient);

  getAllSubcategorias(): Observable<ISubcategoria[]> {
    return this.http.get<ISubcategoria[]>(`${this.resourceUrl}/all`);
  }
}
