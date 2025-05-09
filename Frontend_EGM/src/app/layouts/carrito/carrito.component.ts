import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../auth/auth.service';
import { ClienteService } from '../../entities/cliente/cliente.service';
import { ICliente } from '../../entities/cliente/cliente.model';
import { IVenta } from '../../entities/venta/venta.model';
import { ILineasVenta } from '../../entities/lineasVenta/lineasVenta.model';
import { CarritoService } from './carrito.service';
import { faPlus, faMinus } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { RouterLink } from '@angular/router';

@Component({
  standalone: true,
  selector: 'carrito',
  imports: [FormsModule, ReactiveFormsModule, CommonModule, FontAwesomeModule, RouterLink],
  templateUrl: './carrito.component.html',
  styleUrls: ['./carrito.component.css'],
})
export class CarritoComponent implements OnInit {
  faPlus = faPlus;
  faMinus = faMinus;

  usuario: ICliente | null = null;
  venta: IVenta | null = null;
  lineasVenta: ILineasVenta[] = [];

  private authService = inject(AuthService);
  private clienteService = inject(ClienteService);
  private carritoService = inject(CarritoService);

  ngOnInit(): void {
    const usuarioNombre = this.authService.getUsuario();
    if (usuarioNombre) {
      this.clienteService.getCliente(usuarioNombre).subscribe((res) => {
        this.usuario = res || null;
        if (this.usuario) {
          const carritoKey = `carrito_${this.usuario.id}`;

          this.venta = JSON.parse(localStorage.getItem(carritoKey) ?? 'null');
          this.lineasVenta = JSON.parse(localStorage.getItem(`${carritoKey}_lineas`) ?? '[]');
          this.calcularPrecioFinal();
        }
      });
    } else {
      this.usuario = null;
    }
  }

  addUno(linea: ILineasVenta): void {
    if (!this.usuario) return;
    const carritoKey = `carrito_${this.usuario?.id}`;
    if (!carritoKey) return;
    if (linea.cantidadPedida == null || linea.precioUnitario == null) return; 

    linea.cantidadPedida += 1; 
    linea.precioTotal = linea.precioUnitario * linea.cantidadPedida; 

    localStorage.setItem(`${carritoKey}_lineas`, JSON.stringify(this.lineasVenta)); 
    this.calcularPrecioFinal();
    this.carritoService.actualizarCarritoCount(this.usuario.id!);
  }

  deleteUno(linea: ILineasVenta): void {
    if (!this.usuario) return;
    const carritoKey = `carrito_${this.usuario?.id}`;
    if (!carritoKey) return;
    if (linea.cantidadPedida == null || linea.precioUnitario == null) return; 

    if (linea.cantidadPedida > 1) {
      linea.cantidadPedida -= 1; 
      linea.precioTotal = linea.precioUnitario * linea.cantidadPedida; 
    } else {
      this.deleteLinea(linea); 
    }

    localStorage.setItem(`${carritoKey}_lineas`, JSON.stringify(this.lineasVenta)); 
    this.calcularPrecioFinal();
    this.carritoService.actualizarCarritoCount(this.usuario.id!);
  }

  deleteLinea(linea: ILineasVenta): void {
    if (!this.usuario) return;
    const carritoKey = `carrito_${this.usuario?.id}`;
    if (!carritoKey) return;

    this.lineasVenta = this.lineasVenta.filter(l => l !== linea);
    localStorage.setItem(`${carritoKey}_lineas`, JSON.stringify(this.lineasVenta));
    this.calcularPrecioFinal();
    this.carritoService.actualizarCarritoCount(this.usuario.id!);
  }

  calcularPrecioFinal(): void {
    if (!this.usuario) return;
  
    const carritoKey = `carrito_${this.usuario.id}`;
    const lineas: ILineasVenta[] = JSON.parse(localStorage.getItem(`${carritoKey}_lineas`) ?? '[]');
  
    const total = lineas.reduce((sum, linea) => {
      return sum + (linea.precioTotal ?? 0);
    }, 0);
  
    const venta: IVenta = JSON.parse(localStorage.getItem(carritoKey) ?? 'null');
    if (venta) {
      venta.precioFinal = total;
      localStorage.setItem(carritoKey, JSON.stringify(venta));
      this.venta = venta;
    }
  }
}