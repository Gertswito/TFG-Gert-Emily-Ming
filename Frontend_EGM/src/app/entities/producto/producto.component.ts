import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IProducto } from './producto.model';
import { ProductoService } from './producto.service';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink, RouterOutlet } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEM_DELETED_EVENT } from '../../config/navigation.constants';
import { filter, tap } from 'rxjs';
import { ProductoDeleteComponent } from './producto-delete/producto-delete.component';
import { BuscadorComponent } from '../../layouts/buscador/buscador.component';

@Component({
  standalone: true,
  selector: 'producto',
  imports: [FormsModule, ReactiveFormsModule, CommonModule, BuscadorComponent, RouterLink, RouterOutlet],
  templateUrl: './producto.component.html',
  styleUrls: ['../entities.css'],
})
export class ProductoComponent implements OnInit {
  productoList: IProducto[] = [];
  errorMessage = '';

  private productoService = inject(ProductoService);
  private modalService = inject(NgbModal);
  protected router = inject(Router);

  ngOnInit(): void {
    window.scrollTo(0, 0);
    this.cargarProductos();
  }

  cargarProductos() {
    this.productoService.getAllProductos().subscribe((res) => {
      this.productoList = res || [];
    });
  }

  editarProducto(producto: any) {
    console.log('Editar Producto:', producto);
  }

  eliminarProducto(producto: IProducto) {
    const modalRef = this.modalService.open(ProductoDeleteComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.producto = producto;
    modalRef.componentInstance.errorSubject.subscribe({
      next: (errorMessage: string) => {
        this.errorMessage = errorMessage;  
      }
    });
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.cargarProductos()),
      )
      .subscribe();
  }

  crearNuevo(): void {
    this.router.navigate(['/producto-create']);
  }

  onBuscar(texto: string): void {
    if (texto) {
      this.productoService.getProductoConFiltroAdmin(texto).subscribe((data) => {
        this.productoList = data;
      });
    } else {
      this.cargarProductos();
    }
  }
}
