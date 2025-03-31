import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ISubcategoria } from './subcategoria.model';
import { SubcategoriaService } from './subcategoria.service';
import { CommonModule } from '@angular/common';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { filter, tap } from 'rxjs';
import { ITEM_DELETED_EVENT } from '../../config/navigation.constants';
import { SubcategoriaDeleteComponent } from './subcategoria-delete/subcategoria-delete.component';

@Component({
  standalone: true,
  selector: 'subcategoria',
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './subcategoria.component.html',
  styleUrls: ['../entities.css'],
})
export class SubcategoriaComponent implements OnInit {
  subcategoriaList: ISubcategoria[] = [];
  errorMessage = '';

  private subcategoriaService = inject(SubcategoriaService);
  private modalService = inject(NgbModal);

  ngOnInit(): void {
    this.cargarSubcategorias();
  }

  cargarSubcategorias() {
    this.subcategoriaService.getAllSubcategorias().subscribe((res) => {
      this.subcategoriaList = res || [];
    });
  }

  editarSubcategoria(subcategoria: any) {
    console.log('Editar Subcategoría:', subcategoria);
  }

  eliminarSubcategoria(subcategoria: ISubcategoria) {
    const modalRef = this.modalService.open(SubcategoriaDeleteComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.subcategoria = subcategoria;
    modalRef.componentInstance.errorSubject.subscribe({
      next: (errorMessage: string) => {
        this.errorMessage = errorMessage;  
      }
    });
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.cargarSubcategorias()),
      )
      .subscribe();
  }
}
