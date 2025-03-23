import { Component, inject } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { ITEM_DELETED_EVENT } from "../../../config/navigation.constants";
import { IVenta } from "../venta.model";
import { VentaService } from "../venta.service";
import { Subject } from "rxjs";

@Component({
  standalone: true,
  templateUrl: './venta-delete.component.html',
  styleUrls: ['../../entities.css'],
  imports: [FormsModule],
})
export class VentaDeleteComponent {
  venta?: IVenta;
  errorSubject = new Subject<string>();

  protected ventaService = inject(VentaService);
  protected activeModal = inject(NgbActiveModal);

  cancelar(): void {
    this.activeModal.dismiss();
  }

  confirmarDelete(id: number): void {
    this.ventaService.deleteVenta(id).subscribe({
      next: () => {
        this.activeModal.close(ITEM_DELETED_EVENT);
      },
      error: (err) => {
        if (err.status === 409) {
          this.errorSubject.next('No se puede eliminar la venta "' + id + '" por culpa de otras dependencias, asegúrese de que no haya ninguna línea de venta asociada a esta venta.');
        } else if (err.status === 404) {
          this.errorSubject.next('La venta "' + id + '" no existe.');
        } else {
          this.errorSubject.next('Error desconocido. Inténtalo de nuevo más tarde.');
        }
        this.activeModal.dismiss(); 
      }
    });
  }
}