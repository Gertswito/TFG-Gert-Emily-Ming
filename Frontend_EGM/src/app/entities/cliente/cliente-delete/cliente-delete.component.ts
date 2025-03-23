import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEM_DELETED_EVENT } from '../../../config/navigation.constants';
import { ICliente } from '../cliente.model';
import { ClienteService } from '../cliente.service';
import { Subject } from 'rxjs';

@Component({
  standalone: true,
  templateUrl: './cliente-delete.component.html',
  styleUrls: ['../../entities.css'],
  imports: [FormsModule],
})
export class ClienteDeleteComponent {
  cliente?: ICliente;
  errorSubject = new Subject<string>();

  protected clienteService = inject(ClienteService);
  protected activeModal = inject(NgbActiveModal);

  cancelar(): void {
    this.activeModal.dismiss();
  }

  confirmarDelete(id: number): void {
    this.clienteService.deleteCliente(id).subscribe({
      next: () => {
        this.activeModal.close(ITEM_DELETED_EVENT);
      },
      error: (err) => {
        const clienteNombre = this.cliente?.usuario ?? id;
        if (err.status === 409) {
          this.errorSubject.next('No se puede eliminar el cliente "' + clienteNombre + '" por culpa de otras dependencias, asegúrese de que no haya ninguna venta, pago o dirección asociados a este cliente.');
        } else if (err.status === 404) {
          this.errorSubject.next('El cliente "' + clienteNombre + '" no existe.');
        } else {
          this.errorSubject.next('Error desconocido. Inténtalo de nuevo más tarde.');
        }
        this.activeModal.dismiss(); 
      }
    });
  }
}