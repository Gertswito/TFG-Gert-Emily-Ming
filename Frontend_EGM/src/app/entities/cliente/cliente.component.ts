import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ICliente } from './cliente.model';
import { ClienteService } from './cliente.service';
import { CommonModule } from '@angular/common';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { filter, tap } from 'rxjs';
import { ClienteDeleteComponent } from './cliente-delete/cliente-delete.component';
import { ITEM_DELETED_EVENT } from '../../config/navigation.constants';
import { Router, RouterLink, RouterOutlet } from '@angular/router';


@Component({
  standalone: true,
  selector: 'cliente',
  imports: [FormsModule, ReactiveFormsModule, CommonModule, RouterLink, RouterOutlet],
  templateUrl: './cliente.component.html',
  styleUrls: ['../entities.css'],
})
export class ClienteComponent implements OnInit {
  clienteList: ICliente[] = [];
  errorMessage = '';

  private clienteService = inject(ClienteService);
  private modalService = inject(NgbModal);
  protected router = inject(Router);

  ngOnInit(): void {
    this.cargarClientes();
  }

  cargarClientes() {
    this.clienteService.getAllClientes().subscribe((res) => {
      this.clienteList = res || [];
    });
  }

  editarCliente(Cliente: any) {
    console.log('Editar Cliente:', Cliente);
  }

  eliminarCliente(cliente: ICliente) {
    const modalRef = this.modalService.open(ClienteDeleteComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cliente = cliente;
    modalRef.componentInstance.errorSubject.subscribe({
      next: (errorMessage: string) => {
        this.errorMessage = errorMessage;  
      }
    });
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.cargarClientes()),
      )
      .subscribe();
  }

  crearNuevo(): void {
    this.router.navigate(['/cliente-create']);
  }
}
