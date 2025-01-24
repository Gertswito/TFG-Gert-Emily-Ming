import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ISubcategoria } from './subcategoria.model';
import { SubcategoriaService } from './subcategoria.service';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'subcategoria',
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './subcategoria.component.html',
  styleUrls: ['../entities.css'],
})
export class SubcategoriaComponent implements OnInit {
  subcategoriaList: ISubcategoria[] = [];

  private subcategoriaService = inject(SubcategoriaService);

  ngOnInit(): void {
    this.subcategoriaService.getAllSubcategorias().subscribe((res) => {
      this.subcategoriaList = res || [];
    });
  }
}