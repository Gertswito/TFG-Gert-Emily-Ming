import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ICategoria } from './categoria.model';
import { CategoriaService } from './categoria.service';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'categoria',
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './categoria.component.html',
  styleUrls: ['../entities.css'],
})
export class CategoriaComponent implements OnInit {
  categoriaList: ICategoria[] = [];
  editarCategoria(Categoria: any) {
    // Lógica para editar la categoría
    console.log('Editar Categoría:', Categoria);
  }

  eliminarCategoria(Categoria: any) {
    // Lógica para eliminar la categoría
    console.log('Eliminar Categoría:', Categoria);
  }

  private categoriaService = inject(CategoriaService);

  ngOnInit(): void {
    this.categoriaService.getAllCategorias().subscribe((res) => {
      this.categoriaList = res || [];
    });
  }
}
