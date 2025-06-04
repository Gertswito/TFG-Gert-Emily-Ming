import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IPago } from './pago.model';
import { PagoService } from './pago.service';
import { CommonModule } from '@angular/common';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { filter, tap } from 'rxjs';
import { ITEM_DELETED_EVENT } from '../../config/navigation.constants';
import { PagoDeleteComponent } from './pago-delete/pago-delete.component';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { BuscadorComponent } from '../../layouts/buscador/buscador.component';

@Component({
  standalone: true,
  selector: 'pago',
  imports: [FormsModule, ReactiveFormsModule, CommonModule, BuscadorComponent, RouterLink, RouterOutlet],
  templateUrl: './pago.component.html',
  styleUrls: ['../entities.css'],
})
export class PagoComponent implements OnInit {
  pagoList: IPago[] = [];
  errorMessage = '';

  private pagoService = inject(PagoService);
  private modalService = inject(NgbModal);
  protected router = inject(Router);

  ngOnInit(): void {
    window.scrollTo(0, 0);
    this.cargarPagos();
  }

  cargarPagos() {
    this.pagoService.getAllPagos().subscribe((res) => {
      this.pagoList = res || [];
    });
  }

  editarPago(pago: any) {
    console.log('Editar Pago:', pago);
  }

  eliminarPago(pago: any) {
    const modalRef = this.modalService.open(PagoDeleteComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pago = pago;
    modalRef.componentInstance.errorSubject.subscribe({
      next: (errorMessage: string) => {
        this.errorMessage = errorMessage;  
      }
    });
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.cargarPagos()),
      )
      .subscribe();
  }

  crearNuevo(): void {
    this.router.navigate(['/pago-create']);
  }

  cambiarEstado(pago: IPago): void {
      if (pago.id) {
        if (pago.activo) {
          this.pagoService.disablePago(pago.id).subscribe(() => {
            this.cargarPagos();
          });
        } else if (!pago.activo) {
          this.pagoService.enablePago(pago.id).subscribe(() => {
            this.cargarPagos();
          });
        }
      }
    }

  onBuscar(texto: string): void {
    if (texto) {
      this.pagoService.getPagoFiltroAdmin(texto).subscribe((data) => {
        this.pagoList = data;
      });
    } else {
      this.cargarPagos();
    }
  }
}