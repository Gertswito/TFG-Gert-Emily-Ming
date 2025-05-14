import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { BuscadorComponent } from '../../buscador/buscador.component';
import { ICliente } from '../../../entities/cliente/cliente.model';
import { ILineasVenta } from '../../../entities/lineasVenta/lineasVenta.model';
import { VentaService } from '../../../entities/venta/venta.service';
import { ClienteService } from '../../../entities/cliente/cliente.service';
import { LineasVentaService } from '../../../entities/lineasVenta/lineasVenta.service';
import { AuthService } from '../../../auth/auth.service';
import { IVenta } from '../../../entities/venta/venta.model';

@Component({
  standalone: true,
  selector: 'lineas-venta',
  imports: [FormsModule, ReactiveFormsModule, CommonModule, BuscadorComponent, RouterLink, RouterOutlet],
  templateUrl: './historial-compra.component.html',
  styleUrls: ['./historial-compra.component.css'],
})
export class HistorialCompraComponent implements OnInit {
    cliente: ICliente | null = null;
    lineasVentaList: ILineasVenta[] = [];
    ventaList: IVenta[] = [];
    ventasDesplegadas = new Set<number>();

    protected clienteService = inject(ClienteService);
    protected lineasVentaService = inject(LineasVentaService);
    protected ventaService = inject(VentaService);
    protected authService = inject(AuthService);

    ngOnInit(): void {
        this.authService.loggedIn$.subscribe((status) => {
            if (status) {
                const usuario = this.authService.getUsuario();
                if (usuario) {
                    this.clienteService.getCliente(usuario).subscribe((res) => {
                        this.cliente = res;
                        this.cargarVentas();
                    });
                }
            }
        });
    }

    cargarVentas() {
        if (this.cliente && this.cliente.id) {
            this.ventaService.getVentasByCliente(this.cliente.id).subscribe((res) => {
                this.ventaList = res || [];
                this.ventaList.forEach((venta) => {
                    if (venta.id) {
                        this.lineasVentaService.getLineasVentasByVenta(venta.id).subscribe((lineas) => {
                            this.lineasVentaList.push(...lineas);
                        });
                    }
                });
            });
        }
    }

    getLineasPorVentaId(ventaId: number): ILineasVenta[] {
        return this.lineasVentaList.filter(linea => linea.venta?.id === ventaId);
    }

    formatearTarjeta(numero: string | number | null | undefined): string {
        if (!numero) return '';
        const str = numero.toString();
        return str.replace(/(.{4})/g, '$1 ').trim();
    }

    toggleLineas(ventaId: number): void {
        if (this.ventasDesplegadas.has(ventaId)) {
            this.ventasDesplegadas.delete(ventaId);
        } else {
            this.ventasDesplegadas.add(ventaId);
        }
    }
    
    isVentaDesplegada(ventaId: number): boolean {
        return this.ventasDesplegadas.has(ventaId);
    }
}