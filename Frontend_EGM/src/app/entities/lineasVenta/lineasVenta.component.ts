import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ILineasVenta } from './lineasVenta.model';
import { LineasVentaService } from './lineasVenta.service';
import { CommonModule } from '@angular/common';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { filter, tap } from 'rxjs';
import { ITEM_DELETED_EVENT } from '../../config/navigation.constants';
import { LineasVentaDeleteComponent } from './lineasVenta-delete/lineasVenta-delete.component';
import { Router } from '@angular/router';

@Component({
  standalone: true,
  selector: 'lineas-venta',
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './lineasVenta.component.html',
  styleUrls: ['../entities.css'],
})
export class LineasVentaComponent implements OnInit {
  lineasVentaList: ILineasVenta[] = [];
  errorMessage = '';

  private lineasVentaService = inject(LineasVentaService);
  private modalService = inject(NgbModal);
  protected router = inject(Router);

  ngOnInit(): void {
    this.cargarLineasVenta();
  }

  cargarLineasVenta() {
    this.lineasVentaService.getAllLineasVentas().subscribe((res) => {
      this.lineasVentaList = res || [];
    });
  }

  editarLineaVenta(lineaVenta: any) {
    console.log('Editar Línea de Venta:', lineaVenta);
  }

  eliminarLineaVenta(lineaVenta: ILineasVenta) {
    const modalRef = this.modalService.open(LineasVentaDeleteComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.lineaVenta = lineaVenta;
    modalRef.componentInstance.errorSubject.subscribe({
      next: (errorMessage: string) => {
        this.errorMessage = errorMessage;  
      }
    });
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.cargarLineasVenta()),
      )
      .subscribe();
  }

  crearNuevo(): void {
    this.router.navigate(['/lineasVenta-create']);
  }
}
