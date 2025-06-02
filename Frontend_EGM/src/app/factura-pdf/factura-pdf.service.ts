import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ILineasVenta } from '../entities/lineasVenta/lineasVenta.model';
import { IVenta } from '../entities/venta/venta.model';

@Injectable({
    providedIn: 'root'
})
export class FacturaPDFService {
    private baseUrl = 'http://localhost:8080/api/facturas'; 
    protected http = inject(HttpClient);

    descargarFacturaPDF(venta: IVenta): Observable<Blob> {
        return this.http.post(`${this.baseUrl}/pdf`, venta, { responseType: 'blob' });
    }
}