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
import { CarritoService } from '../../../layouts/carrito/carrito.service';
import { BuscadorComponent } from '../../../layouts/buscador/buscador.component';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@Component({
  standalone: true,
  selector: 'producto',
  imports: [FormsModule, ReactiveFormsModule, CommonModule, BuscadorComponent, RouterLink, RouterOutlet, FontAwesomeModule],
  templateUrl: './producto-list.component.html',
  styleUrls: ['./producto-list.component.css'],
})
export class ProductoListComponent implements OnInit {
  faPlus = faPlus;

  productoList: IProducto[] = [];
  usuario: ICliente | null = null;
  nombreSubcategoria = '';
  imgSubcategoria = '';
  id: number | null = null;

  private productoService = inject(ProductoService);
  private clienteService = inject(ClienteService);
  private authService = inject(AuthService);
  private carritoService = inject(CarritoService);
  private route = inject(ActivatedRoute);

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.id = params['id'];

      if (this.id) {
        this.cargarProductosConId(this.id);
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
      this.imgSubcategoria = res.length > 0 ? res[0].subcategoria?.imagenSubcategoria || '' : '';
    });
  }

  cargarAllProductos() {
    this.productoService.getAllProductos().subscribe((res) => {
      this.productoList = res || [];
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

  onBuscar(texto: string): void {
    if (texto) {
      if (this.id) {
        this.productoService.getProductoConFiltroSubcategoria(this.id, texto).subscribe((res) => {
          this.productoList = res || [];
        });
      } else {
        this.productoService.getProductoConFiltro(texto).subscribe((res) => {
          this.productoList = res || [];
        });
      }
    } else {
      if (this.id) {
        this.cargarProductosConId(this.id);
      } else {
        this.cargarAllProductos();
      }
    }
  }
}

