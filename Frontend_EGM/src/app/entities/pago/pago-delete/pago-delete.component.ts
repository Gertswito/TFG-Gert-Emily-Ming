import { Component, inject } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { ITEM_DELETED_EVENT } from "../../../config/navigation.constants";
import { IPago } from "../pago.model";
import { PagoService } from "../pago.service";
import { Subject } from "rxjs";

@Component({
  standalone: true,
  templateUrl: './pago-delete.component.html',
  styleUrls: ['../../entities.css'],
  imports: [FormsModule],
})
export class PagoDeleteComponent {
  pago?: IPago;
  errorSubject = new Subject<string>();

  protected pagoService = inject(PagoService);
  protected activeModal = inject(NgbActiveModal);

  cancelar(): void {
    this.activeModal.dismiss();
  }

  confirmarDelete(id: number): void {
    this.pagoService.deletePago(id).subscribe({
      next: () => {
        this.activeModal.close(ITEM_DELETED_EVENT);
      },
      error: (err) => {
        if (err.status === 409) {
          this.errorSubject.next('No se puede eliminar el pago "' + id + '" por culpa de otras dependencias.');
        } else if (err.status === 404) {
          this.errorSubject.next('El pago "' + id + '" no existe.');
        } else {
          this.errorSubject.next('Error desconocido. Inténtalo de nuevo más tarde.');
        }
        this.activeModal.dismiss(); 
      }
    });
  }
}