import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ISubcategoria } from '../subcategoria.model';
import { RouterLink, RouterOutlet } from '@angular/router';
import { SubcategoriaService } from '../subcategoria.service';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { BuscadorComponent } from '../../../layouts/buscador/buscador.component';

@Component({
  standalone: true,
  selector: 'subcategoria',
  imports: [FormsModule, ReactiveFormsModule, CommonModule, BuscadorComponent, RouterLink, RouterOutlet],
  templateUrl: './subcategoria-list.component.html',
  styleUrls: ['./subcategoria-list.component.css'],
})
export class SubcategoriaListComponent implements OnInit {
  subcategoriaList: ISubcategoria[] = [];
  nombreCategoria = '';
  imgCategoria = '';
  id: number | null = null;

  private subcategoriaService = inject(SubcategoriaService);
  private route = inject(ActivatedRoute);

  ngOnInit(): void {
    window.scrollTo(0, 0);
    this.route.queryParams.subscribe(params => {
      this.id = params['id'];

      if (this.id) {
        this.cargarSubcategoriasConId(this.id);
        this.nombreCategoria = params['nombre'];
      } else {
        this.cargarAllSubcategorias();
        this.nombreCategoria = 'Todas las subcategorías';
      }
    });
  }

  cargarSubcategoriasConId(id: number) {
    this.subcategoriaService.getSubcategoriasConIdCategoria(id).subscribe((res) => {
      this.subcategoriaList = res || [];
      this.imgCategoria = res.length > 0 ? res[0].categoria?.imagenCategoria || '' : '';
    });
  }

  cargarAllSubcategorias() {
    this.subcategoriaService.getAllSubcategorias().subscribe((res) => {
      this.subcategoriaList = res || [];
    });
  }

  onBuscar(texto: string): void {
    if (texto) {
      if (this.id) {
        this.subcategoriaService.getSubcategoriasConFiltroCategoria(this.id, texto).subscribe((data) => {
          this.subcategoriaList = data;
        });
      } else {
        this.subcategoriaService.getSubcategoriasConFiltro(texto).subscribe((data) => {
          this.subcategoriaList = data;
        });
      }
    } else {
      if (this.id) {
        this.cargarSubcategoriasConId(this.id);
      } else {
        this.cargarAllSubcategorias();
      }
    }
  }
}