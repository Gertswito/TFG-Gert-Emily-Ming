import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IProducto } from '../producto.model';
import { RouterLink, RouterOutlet } from '@angular/router';
import { ProductoService } from '../producto.service';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

@Component({
  standalone: true,
  selector: 'producto',
  imports: [FormsModule, ReactiveFormsModule, CommonModule, RouterLink, RouterOutlet],
  templateUrl: './producto-info.component.html',
  styleUrls: ['./producto-info.component.css'],
})
export class ProductoInfoComponent implements OnInit {
    productoSeleccionado: IProducto | null = null;
    errorCargado = false;

    private productoService = inject(ProductoService);
    private route = inject(ActivatedRoute);

    ngOnInit(): void {
        this.route.queryParams.subscribe(params => {
        const id = params['id'];

        if (id) {
            this.cargarProducto(id);
        } else {
            this.errorCargado = true;
        }
        });
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

    atras(): void {
        window.history.back();
    }
}