import { Component, inject } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { ITEM_DELETED_EVENT } from "../../../config/navigation.constants";
import { IProducto } from "../producto.model";
import { ProductoService } from "../producto.service";
import { Subject } from "rxjs";

@Component({
  standalone: true,
  templateUrl: './producto-delete.component.html',
  styleUrls: ['../../entities.css'],
  imports: [FormsModule],
})
export class ProductoDeleteComponent {
  producto?: IProducto;
  errorSubject = new Subject<string>();

  protected productoService = inject(ProductoService);
  protected activeModal = inject(NgbActiveModal);

  cancelar(): void {
    this.activeModal.dismiss();
  }

  confirmarDelete(id: number): void {
    this.productoService.deleteProducto(id).subscribe({
      next: () => {
        this.activeModal.close(ITEM_DELETED_EVENT);
      },
      error: (err) => {
        const productoNombre = this.producto?.nombre ?? id;
        if (err.status === 409) {
          this.errorSubject.next('No se puede eliminar el producto "' + productoNombre + '" por culpa de otras dependencias, asegúrese de que no haya ninguna linea de venta asociada a este producto.');
        } else if (err.status === 404) {
          this.errorSubject.next('El producto "' + productoNombre + '" no existe.');
        } else {
          this.errorSubject.next('Error desconocido. Inténtalo de nuevo más tarde.');
        }
        this.activeModal.dismiss(); 
      }
    });
  }
}