import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CarritoService {
    private carritoCountSubject = new BehaviorSubject<number>(0);
    carritoCount$ = this.carritoCountSubject.asObservable();

    actualizarCarritoCount(usuarioId: number): void {
        const key = `carrito_${usuarioId}_lineas`;
        const lineas = JSON.parse(localStorage.getItem(key) ?? '[]');
        const count = Array.isArray(lineas) ? lineas.length : 0;
        this.carritoCountSubject.next(count);
    }
}