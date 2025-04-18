import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IProducto } from '../producto.model';
import { ProductoService } from '../producto.service';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink, RouterOutlet } from '@angular/router';
import { ILineasVenta } from '../../lineasVenta/lineasVenta.model';
import { IVenta } from '../../venta/venta.model';
import { ClienteService } from '../../cliente/cliente.service';
import { AuthService } from '../../../auth/auth.service';
import { ICliente } from '../../cliente/cliente.model';

@Component({
  standalone: true,
  selector: 'producto',
  imports: [FormsModule, ReactiveFormsModule, CommonModule, RouterLink, RouterOutlet],
  templateUrl: './producto-list.component.html',
  styleUrls: ['./producto-list.component.css'],
})
export class ProductoListComponent implements OnInit {
  productoList: IProducto[] = [];
  usuario: ICliente | null = null;
  nombreSubcategoria = '';

  private productoService = inject(ProductoService);
  private clienteService = inject(ClienteService);
  private authService = inject(AuthService);
  private route = inject(ActivatedRoute);

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const id = params['id'];

      if (id) {
        this.cargarProductosConId(id);
        this.nombreSubcategoria = params['nombre'];
      } else {
        this.cargarAllProductos();
        this.nombreSubcategoria = 'Todos los productos';
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

  cargarProductosConId(id: number) {
    this.productoService.getProductosConIdSubcategoria(id).subscribe((res) => {
      this.productoList = res || [];
    });
  }

  cargarAllProductos() {
    this.productoService.getAllProductos().subscribe((res) => {
      this.productoList = res || [];
    });
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
}

