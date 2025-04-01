import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IVenta } from './venta.model';
import { VentaService } from './venta.service';
import { CommonModule } from '@angular/common';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { filter, tap } from 'rxjs';
import { ITEM_DELETED_EVENT } from '../../config/navigation.constants';
import { VentaDeleteComponent } from './venta-delete/venta-delete.component';
import { Router } from '@angular/router';

@Component({
  standalone: true,
  selector: 'venta',
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './venta.component.html',
  styleUrls: ['../entities.css'],
})
export class VentaComponent implements OnInit {
  ventaList: IVenta[] = [];
  errorMessage = '';

  private ventaService = inject(VentaService);
  private modalService = inject(NgbModal);  
  protected router = inject(Router);

  ngOnInit(): void {
    this.cargarVentas();
  }

  cargarVentas() {
    this.ventaService.getAllVentas().subscribe((res) => {
      this.ventaList = res || [];
    });
  }

  editarVenta(venta: any) {
    console.log('Editar Venta:', venta);
  }

  eliminarVenta(venta: IVenta) {
    const modalRef = this.modalService.open(VentaDeleteComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.venta = venta;
    modalRef.componentInstance.errorSubject.subscribe({
      next: (errorMessage: string) => {
        this.errorMessage = errorMessage;  
      }
    });
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.cargarVentas()),
      )
      .subscribe();
  }

  verLineasVenta(venta: any) {
    console.log('Ver Líneas de Venta:', venta);
  }

  crearNuevo(): void {
    this.router.navigate(['/venta-create']);
  }
}
