import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IDireccion } from './direccion.model';
import { DireccionService } from './direccion.service';
import { CommonModule } from '@angular/common';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { filter, tap } from 'rxjs';
import { ITEM_DELETED_EVENT } from '../../config/navigation.constants';
import { DireccionDeleteComponent } from './direccion-delete/direccion-delete.component';
import { Router } from '@angular/router';

@Component({
  standalone: true,
  selector: 'direccion',
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './direccion.component.html',
  styleUrls: ['../entities.css'],
})
export class DireccionComponent implements OnInit {
  direccionList: IDireccion[] = [];
  errorMessage = '';

  private direccionService = inject(DireccionService);
  private modalService = inject(NgbModal);
  protected router = inject(Router);

  ngOnInit(): void {
    this.cargarDirecciones();
  }

  cargarDirecciones() {
    this.direccionService.getAllDirecciones().subscribe((res) => {
      this.direccionList = res || [];
    });
  }

  editarDireccion(direccion: any) {
    console.log('Editar dirección:', direccion);
  }

  eliminarDireccion(direccion: IDireccion) {
    const modalRef = this.modalService.open(DireccionDeleteComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.direccion = direccion;
    modalRef.componentInstance.errorSubject.subscribe({
      next: (errorMessage: string) => {
        this.errorMessage = errorMessage;  
      }
    });
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.cargarDirecciones()),
      )
      .subscribe();
  }

  crearNuevo(): void {
    this.router.navigate(['/direccion-create']);
  }

  cambiarEstado(direccion: IDireccion): void {
    if (direccion.id) {
      if (direccion.activo) {
        this.direccionService.disableDireccion(direccion.id).subscribe(() => {
          this.cargarDirecciones();
        });
      } else if (!direccion.activo) {
        this.direccionService.enableDireccion(direccion.id).subscribe(() => {
          this.cargarDirecciones();
        });
      }
      this.cargarDirecciones();
    }
  }
}
