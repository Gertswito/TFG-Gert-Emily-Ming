import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../auth/auth.service';
import { ClienteService } from '../../entities/cliente/cliente.service';
import { ICliente } from '../../entities/cliente/cliente.model';
import { IVenta } from '../../entities/venta/venta.model';
import { ILineasVenta } from '../../entities/lineasVenta/lineasVenta.model';
import { CarritoService } from '../carrito/carrito.service';
import { faPlus, faMinus } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Router, RouterLink } from '@angular/router';
import { IDireccion } from '../../entities/direccion/direccion.model';
import { IPago } from '../../entities/pago/pago.model';
import { DireccionService } from '../../entities/direccion/direccion.service';
import { PagoService } from '../../entities/pago/pago.service';
import { DireccionAjusteComponent } from "../../entities/direccion/direccion-ajuste/direccion-ajuste.component";
import { PagoAjusteComponent } from "../../entities/pago/pago-ajuste/pago-ajuste.component";
import { VentaService } from '../../entities/venta/venta.service';

@Component({
  standalone: true,
  selector: 'compra',
  imports: [FormsModule, ReactiveFormsModule, CommonModule, FontAwesomeModule, RouterLink, DireccionAjusteComponent, PagoAjusteComponent],
  templateUrl: './compra.component.html',
  styleUrls: ['./compra.component.css'],
})
export class CompraComponent implements OnInit {
  usuario: ICliente | null = null;
  venta: IVenta | null = null;
  lineasVenta: ILineasVenta[] = [];
  direccionesCliente: IDireccion[] = [];
  pagosCliente: IPago[] = [];
  error = false;
  errorMessage = '';

  private authService = inject(AuthService);
  private clienteService = inject(ClienteService);
  private direccionService = inject(DireccionService);
  private pagoService = inject(PagoService);
  private ventaService = inject(VentaService);
  private carritoService = inject(CarritoService);
  protected router = inject(Router);

  ngOnInit(): void {
    const usuarioNombre = this.authService.getUsuario();
    if (usuarioNombre) {
      this.clienteService.getCliente(usuarioNombre).subscribe((res) => {
        this.usuario = res || null;
        if (this.usuario && this.usuario.usuario) {
          const carritoKey = `carrito_${this.usuario.id}`;

          this.venta = JSON.parse(localStorage.getItem(carritoKey) ?? 'null');
          this.venta!.direccion = null;
          this.venta!.pago = null;
          localStorage.setItem(carritoKey, JSON.stringify(this.venta));
          this.lineasVenta = JSON.parse(localStorage.getItem(`${carritoKey}_lineas`) ?? '[]');
          this.calcularPrecioFinal();
          this.loadDirecciones();
          this.loadPagos();
        }
      });
    } else {
      this.usuario = null;
    }
  }

  loadDirecciones(): void {
    if (this.usuario && this.usuario.usuario) {
      this.direccionService.getDireccionesPorCliente(this.usuario.usuario).subscribe((res) => {
        this.direccionesCliente = res || [];
      });
    }
  }

  loadPagos(): void {
    if (this.usuario && this.usuario.usuario) {
      this.pagoService.getPagosByCliente(this.usuario.usuario).subscribe((res) => {
        this.pagosCliente = res || [];
      });
    }
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

  getLastDayOfMonth(monthString: string): Date {
    const [year, month] = monthString.split("-").map(Number);
    return new Date(year, month, 0); 
  }

  finalizarCompra(): void {
    if(this.usuario == null || this.usuario.id == null) return;
    this.venta = JSON.parse(localStorage.getItem(`carrito_${this.usuario.id}`) ?? 'null');
    this.lineasVenta = JSON.parse(localStorage.getItem(`carrito_${this.usuario.id}_lineas`) ?? '[]');

    if (this.venta && this.venta.direccion == null && this.venta.pago == null) {
      this.error = true;
      this.errorMessage = 'Por favor, seleccione una dirección de envío y un metodo de pago.';
      return;
    } else if (this.venta && this.venta.direccion == null) {
      this.error = true;
      this.errorMessage = 'Por favor, seleccione una dirección de envío.';
      return;
    } else if (this.venta && this.venta.pago == null) {
      this.error = true;
      this.errorMessage = 'Por favor, seleccione un metodo de pago.';
      return;
    }

    this.error = false;
    this.errorMessage = '';
    if (this.venta && this.lineasVenta && this.venta.pago && this.venta.direccion) {
      const monthYear = this.venta.pago?.fechaCaducidad;
      if (typeof monthYear === 'string') {
        const lastDayOfMonth = this.getLastDayOfMonth(monthYear);
        this.venta.pago.fechaCaducidad = lastDayOfMonth;
      } 
      if (this.venta.pago.numeroTarjeta) {
        this.venta.pago.numeroTarjeta = this.venta.pago.numeroTarjeta.replace(/\s/g, "");
      }
      this.ventaService.finalizarCompra(this.venta, this.lineasVenta).subscribe((res) => {
        if (res.status === 201) {
          localStorage.removeItem(`carrito_${this.usuario!.id}`);
          localStorage.removeItem(`carrito_${this.usuario!.id}_lineas`);
          if (this.usuario) {
            this.carritoService.actualizarCarritoCount(this.usuario.id!);
          }
          this.venta = null;
          this.lineasVenta = [];
          this.error = false;
          this.errorMessage = '';
          
          this.router.navigate(['/compra-exito']).then(() => {});
        } else {
          this.error = true;
          this.errorMessage = 'Error al finalizar la compra. Por favor, inténtelo de nuevo.';
        }
      });
    }
  }
}