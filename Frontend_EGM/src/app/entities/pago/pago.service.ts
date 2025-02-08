import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IPago } from './pago.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class PagoService {
<<<<<<< Updated upstream
  private resourceUrl = '/pagos';
=======
  private resourceUrl = `${environment.apiUrl}/pagos`;
  
>>>>>>> Stashed changes
  protected http = inject(HttpClient);

  getAllPagos(): Observable<IPago[]> {
    return this.http.get<IPago[]>(`${this.resourceUrl}/all`);
  }
}