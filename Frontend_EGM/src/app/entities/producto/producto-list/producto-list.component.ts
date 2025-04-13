import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IProducto } from '../producto.model';
import { ProductoService } from '../producto.service';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink, RouterOutlet } from '@angular/router';

@Component({
  standalone: true,
  selector: 'producto',
  imports: [FormsModule, ReactiveFormsModule, CommonModule, RouterLink, RouterOutlet],
  templateUrl: './producto-list.component.html',
  styleUrls: ['./producto-list.component.css'],
})
export class ProductoListComponent implements OnInit {
  productoList: IProducto[] = [];
  nombreSubcategoria = '';

  private productoService = inject(ProductoService);
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
}

