import { Component, inject } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { ITEM_DELETED_EVENT } from "../../../config/navigation.constants";
import { ILineasVenta } from "../lineasVenta.model";
import { LineasVentaService } from "../lineasVenta.service";
import { Subject } from "rxjs";

@Component({
  standalone: true,
  templateUrl: './lineasVenta-delete.component.html',
  styleUrls: ['../../entities.css'],
  imports: [FormsModule],
})
export class LineasVentaDeleteComponent {
  lineaVenta?: ILineasVenta;
  errorSubject = new Subject<string>();

  protected lineasVentaService = inject(LineasVentaService);
  protected activeModal = inject(NgbActiveModal);

  cancelar(): void {
    this.activeModal.dismiss();
  }

  confirmarDelete(id: number): void {
    this.lineasVentaService.deleteLineasVenta(id).subscribe({
      next: () => {
        this.activeModal.close(ITEM_DELETED_EVENT);
      },
      error: (err) => {
        if (err.status === 409) {
          this.errorSubject.next('No se puede eliminar la línea de venta "' + id + '" por culpa de otras dependencias.');
        } else if (err.status === 404) {
          this.errorSubject.next('La línea de venta "' + id + '" no existe.');
        } else {
          this.errorSubject.next('Error desconocido. Inténtalo de nuevo más tarde.');
        }
        this.activeModal.dismiss(); 
      }
    });
  }
}