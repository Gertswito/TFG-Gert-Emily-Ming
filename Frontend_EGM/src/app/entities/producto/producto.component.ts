import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IProducto } from './producto.model';
import { ProductoService } from './producto.service';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

@Component({
  standalone: true,
  selector: 'producto',
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './producto.component.html',
  styleUrls: ['../entities.css'],
})
export class ProductoComponent implements OnInit {
  productoList: IProducto[] = [];
  editarProducto(producto: any) {
    // Lógica para editar el producto
    console.log('Editar Producto:', producto);
  }

  eliminarProducto(producto: any) {
    // Lógica para eliminar el producto
    console.log('Eliminar Producto:', producto);
  }


  private productoService = inject(ProductoService);
  private route = inject(ActivatedRoute);

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const id = params['id'];

      if (id) {
        this.cargarProducttosConId(id);
      } else {
        this.cargarAllProductos();
      }
    });
  }

  cargarProducttosConId(id: number) {
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
