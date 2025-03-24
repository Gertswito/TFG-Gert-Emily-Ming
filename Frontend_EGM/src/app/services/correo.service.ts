import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CorreoService {
  private apiUrl = 'https://tu-backend.com/api/correo';

  constructor(private http: HttpClient) {}

  enviarCorreo(correo: string): Observable<any> {
    return this.http.post(this.apiUrl, { correo });
  }
}
