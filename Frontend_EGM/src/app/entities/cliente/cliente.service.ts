import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ICliente } from './cliente.model';
import { environment } from '../../../environments/environment';

export type EntityResponseType = HttpResponse<ICliente>;

@Injectable({
  providedIn: 'root',
})
export class ClienteService {
<<<<<<< Updated upstream
  private resourceUrl = '/clientes';
=======
  private resourceUrl = `${environment.apiUrl}/clientes`;
>>>>>>> Stashed changes
  protected http = inject(HttpClient);

  getAllClientes(): Observable<ICliente[]> {
    return this.http.get<ICliente[]>(`${this.resourceUrl}/all`);
  }

  crearUsuario(cliente: ICliente): Observable<any> { 
    return this.http.post<any>(`${this.resourceUrl}/new`, cliente, { observe: 'response' });
  }

  login(cliente: ICliente): Observable<any> {
    return this.http.post<any>(`${this.resourceUrl}/login`, cliente, { observe: 'response' });
  }
}