import { Component, inject } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { ITEM_DELETED_EVENT } from "../../../config/navigation.constants";
import { IDireccion } from "../direccion.model";
import { DireccionService } from "../direccion.service";
import { Subject } from "rxjs";

@Component({
  standalone: true,
  templateUrl: './direccion-delete.component.html',
  styleUrls: ['../../entities.css'],
  imports: [FormsModule],
})
export class DireccionDeleteComponent {
  direccion?: IDireccion;
  errorSubject = new Subject<string>();

  protected direccionService = inject(DireccionService);
  protected activeModal = inject(NgbActiveModal);

  cancelar(): void {
    this.activeModal.dismiss();
  }

  confirmarDelete(id: number): void {
    this.direccionService.deleteDireccion(id).subscribe({
      next: () => {
        this.activeModal.close(ITEM_DELETED_EVENT);
      },
      error: (err) => {
        if (err.status === 409) {
          this.errorSubject.next('No se puede eliminar la dirección "' + id + '" por culpa de otras dependencias, asegúrese de que no haya ninguna venta asociada a esta dirección.');
        } else if (err.status === 404) {
          this.errorSubject.next('La dirección "' + id + '" no existe.');
        } else {
          this.errorSubject.next('Error desconocido. Inténtalo de nuevo más tarde.');
        }
        this.activeModal.dismiss(); 
      }
    });
  }
}