import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ISubcategoria } from '../subcategoria.model';
import { RouterLink, RouterOutlet } from '@angular/router';
import { SubcategoriaService } from '../subcategoria.service';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

@Component({
  standalone: true,
  selector: 'subcategoria',
  imports: [FormsModule, ReactiveFormsModule, CommonModule, RouterLink, RouterOutlet],
  templateUrl: './subcategoria-list.component.html',
  styleUrls: ['./subcategoria-list.component.css'],
})
export class SubcategoriaListComponent implements OnInit {
  subcategoriaList: ISubcategoria[] = [];
  nombreCategoria = '';

  private subcategoriaService = inject(SubcategoriaService);
  private route = inject(ActivatedRoute);

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const id = params['id'];

      if (id) {
        this.cargarSubcategoriasConId(id);
        this.nombreCategoria = params['nombre'];
      } else {
        this.cargarAllSubcategorias();
        this.nombreCategoria = 'Todas las subcategorias';
      }
    });
  }

  cargarSubcategoriasConId(id: number) {
    this.subcategoriaService.getSubcategoriasConIdCategoria(id).subscribe((res) => {
      this.subcategoriaList = res || [];
    });
  }

  cargarAllSubcategorias() {
    this.subcategoriaService.getAllSubcategorias().subscribe((res) => {
      this.subcategoriaList = res || [];
    });
  }
}