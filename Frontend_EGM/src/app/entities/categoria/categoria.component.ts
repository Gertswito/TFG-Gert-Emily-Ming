import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ICategoria } from './categoria.model';
import { CategoriaService } from './categoria.service';
import { CommonModule } from '@angular/common';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEM_DELETED_EVENT } from '../../config/navigation.constants';
import { filter, tap } from 'rxjs';
import { CategoriaDeleteComponent } from './categoria-delete/categoria-delete.component';
import { Router } from '@angular/router';

@Component({
  standalone: true,
  selector: 'categoria',
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './categoria.component.html',
  styleUrls: ['../entities.css'],
})
export class CategoriaComponent implements OnInit {
  categoriaList: ICategoria[] = [];
  errorMessage = '';

  private categoriaService = inject(CategoriaService);
  private modalService = inject(NgbModal);
  protected router = inject(Router);

  ngOnInit(): void {
    this.cargarCategorias();
  }

  cargarCategorias() {
    this.categoriaService.getAllCategorias().subscribe((res) => {
      this.categoriaList = res || [];
    });
  }

  editarCategoria(Categoria: any) {
    console.log('Editar Categoría:', Categoria);
  }

  eliminarCategoria(categoria: ICategoria) {
    const modalRef = this.modalService.open(CategoriaDeleteComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.categoria = categoria;
    modalRef.componentInstance.errorSubject.subscribe({
      next: (errorMessage: string) => {
        this.errorMessage = errorMessage;  
      }
    });
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.cargarCategorias()),
      )
      .subscribe();
  }

  crearNuevo(): void {
    this.router.navigate(['/categoria-create']);
  }
}
