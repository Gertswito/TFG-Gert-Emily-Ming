import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IProducto } from '../producto.model';
import { RouterLink, RouterOutlet } from '@angular/router';
import { ProductoService } from '../producto.service';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../auth/auth.service';
import { ICliente } from '../../cliente/cliente.model';
import { ClienteService } from '../../cliente/cliente.service';
import { IVenta } from '../../venta/venta.model';
import { ILineasVenta } from '../../lineasVenta/lineasVenta.model';
import { CarritoService } from '../../../layouts/carrito/carrito.service';

@Component({
  standalone: true,
  selector: 'producto',
  imports: [FormsModule, ReactiveFormsModule, CommonModule, RouterLink, RouterOutlet],
  templateUrl: './producto-info.component.html',
  styleUrls: ['./producto-info.component.css'],
})
export class ProductoInfoComponent implements OnInit {
    productoSeleccionado: IProducto | null = null;
    usuario: ICliente | null = null;
    errorCargado = false;

    private productoService = inject(ProductoService);
    private clienteService = inject(ClienteService);
    private authService = inject(AuthService);
    private carritoService = inject(CarritoService);
    private route = inject(ActivatedRoute);

    ngOnInit(): void {
        window.scrollTo(0, 0);
        this.route.queryParams.subscribe(params => {
            const id = params['id'];

            if (id) {
                this.cargarProducto(id);
            } else {
                this.errorCargado = true;
            }
        });

        const usuarioNombre = this.authService.getUsuario();
        if (usuarioNombre) {
            this.clienteService.getCliente(usuarioNombre).subscribe((res) => {
                this.usuario = res || null;
            });
        } else {
            this.usuario = null;
        }
    }

    cargarProducto(id: number): void {
        this.productoService.getProducto(id).subscribe((res) => {
            this.productoSeleccionado = res || null;
            if (!this.productoSeleccionado) {
                this.errorCargado = true;
            } else {
                this.errorCargado = false;
            }
        });
    }

    alertaIniciarSesion() {
        alert('Debes iniciar sesión para añadir productos al carrito');
    }

    addCarrito(producto: IProducto): void {
        if (!this.usuario) return;
      
        const carritoKey = `carrito_${this.usuario.id}`;
        let venta = JSON.parse(localStorage.getItem(carritoKey) ?? 'null');
      
        if (!venta) {
          venta = {
            id: null,
            cliente: this.usuario,
            fechaHora: null,
            precioFinal: null,
            direccion: null,
            pago: null
          };
        }
      
        let lineas: ILineasVenta[] = JSON.parse(localStorage.getItem(`${carritoKey}_lineas`) ?? '[]');
      
        const lineaExistente = lineas.find(l => l.producto?.id === producto.id);
      
        if (lineaExistente && lineaExistente.cantidadPedida != null && lineaExistente.precioUnitario != null) {
            lineaExistente.cantidadPedida += 1;
            lineaExistente.precioTotal = lineaExistente.precioUnitario * lineaExistente.cantidadPedida;
        } else {
          const nuevaLinea: ILineasVenta = {
            id: null,
            venta: null,
            producto: producto,
            cantidadPedida: 1,
            precioUnitario: producto.precio,
            precioTotal: producto.precio
          };
          lineas.push(nuevaLinea);
        }
      
        localStorage.setItem(carritoKey, JSON.stringify(venta));
        localStorage.setItem(`${carritoKey}_lineas`, JSON.stringify(lineas));
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
        }
    }

    atras(): void {
        window.history.back();
    }
}