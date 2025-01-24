import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IProducto } from './producto.model';
import { ProductoService } from './producto.service';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'producto',
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './producto.component.html',
  styleUrls: ['../entities.css'],
})
export class ProductoComponent implements OnInit {
  productoList: IProducto[] = [];

  private productoService = inject(ProductoService);

  ngOnInit(): void {
    this.productoService.getAllProductos().subscribe((res) => {
      this.productoList = res || [];
    });
  }
}
