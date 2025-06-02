import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ILineasVenta } from '../entities/lineasVenta/lineasVenta.model';

@Injectable({
    providedIn: 'root'
})
export class FacturaPDFService {
    private baseUrl = 'http://localhost:8080/api/facturas'; 
    protected http = inject(HttpClient);

    descargarFacturaPDF(ventaId: number): Observable<Blob> {
        return this.http.get(`${this.baseUrl}/${ventaId}/pdf`, { responseType: 'blob' });
    }
}