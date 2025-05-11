import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ISubcategoria } from './subcategoria.model';
import { SubcategoriaService } from './subcategoria.service';
import { CommonModule } from '@angular/common';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { filter, tap } from 'rxjs';
import { ITEM_DELETED_EVENT } from '../../config/navigation.constants';
import { SubcategoriaDeleteComponent } from './subcategoria-delete/subcategoria-delete.component';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { BuscadorComponent } from '../../layouts/buscador/buscador.component';

@Component({
  standalone: true,
  selector: 'subcategoria',
  imports: [FormsModule, ReactiveFormsModule, CommonModule, BuscadorComponent, RouterLink, RouterOutlet],
  templateUrl: './subcategoria.component.html',
  styleUrls: ['../entities.css'],
})
export class SubcategoriaComponent implements OnInit {
  subcategoriaList: ISubcategoria[] = [];
  errorMessage = '';

  private subcategoriaService = inject(SubcategoriaService);
  private modalService = inject(NgbModal);
  protected router = inject(Router);

  ngOnInit(): void {
    this.cargarSubcategorias();
  }

  cargarSubcategorias() {
    this.subcategoriaService.getAllSubcategorias().subscribe((res) => {
      this.subcategoriaList = res || [];
    });
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

  crearNuevo(): void {
    this.router.navigate(['/subcategoria-create']);
  }

  onBuscar(texto: string): void {
    if (texto) {
      this.subcategoriaService.getSubcategoriaConFiltroAdmin(texto).subscribe((data) => {
        this.subcategoriaList = data;
      });
    } else {
      this.cargarSubcategorias();
    }
  }
}
