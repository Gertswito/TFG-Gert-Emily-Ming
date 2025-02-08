import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ICategoria } from './categoria.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class CategoriaService {
<<<<<<< Updated upstream
  private resourceUrl = '/categorias';
=======
  private resourceUrl = `${environment.apiUrl}/categorias`;
>>>>>>> Stashed changes
  protected http = inject(HttpClient);

  getAllCategorias(): Observable<ICategoria[]> {
    return this.http.get<ICategoria[]>(`${this.resourceUrl}/all`);
  }
}
