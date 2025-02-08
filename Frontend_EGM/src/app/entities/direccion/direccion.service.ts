import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IDireccion } from './direccion.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class DireccionService {
<<<<<<< Updated upstream
  private resourceUrl = '/direcciones';
=======
  private resourceUrl = `${environment.apiUrl}/direcciones`;

>>>>>>> Stashed changes
  protected http = inject(HttpClient);

  getAllDirecciones(): Observable<IDireccion[]> {
    return this.http.get<IDireccion[]>(`${this.resourceUrl}/all`);
  }
}