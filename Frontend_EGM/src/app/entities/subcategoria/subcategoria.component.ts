import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ISubcategoria } from './subcategoria.model';
import { SubcategoriaService } from './subcategoria.service';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

@Component({
  standalone: true,
  selector: 'subcategoria',
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './subcategoria.component.html',
  styleUrls: ['../entities.css'],
})
export class SubcategoriaComponent implements OnInit {
  subcategoriaList: ISubcategoria[] = [];

  editarSubcategoria(subcategoria: any) {
    // Lógica para editar la subcategoría
    console.log('Editar Subcategoría:', subcategoria);
  }

  eliminarSubcategoria(subcategoria: any) {
    // Lógica para eliminar la subcategoría
    console.log('Eliminar Subcategoría:', subcategoria);
  }

  private subcategoriaService = inject(SubcategoriaService);
  private route = inject(ActivatedRoute);

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const id = params['id'];

      if (id) {
        this.cargarSubcategoriasConId(id);
      } else {
        this.cargarAllSubcategorias();
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
