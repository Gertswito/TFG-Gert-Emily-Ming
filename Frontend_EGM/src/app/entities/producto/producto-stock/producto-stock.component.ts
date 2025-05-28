import { Component, inject } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { STOCK_UPDATED_EVENT } from "../../../config/navigation.constants";
import { Subject } from "rxjs";
import { IProducto } from "../producto.model";
import { ProductoService } from "../producto.service";

@Component({
  standalone: true,
  templateUrl: './producto-stock.component.html',
  styleUrls: ['../../entities.css'],
  imports: [FormsModule],
})
export class ProductoStockComponent {
  producto?: IProducto;
  stock: number = 0;
  errorSubject = new Subject<string>();
  errorMessage: string = '';

  protected productoService = inject(ProductoService);
  protected activeModal = inject(NgbActiveModal);

  cancelar(): void {
    this.activeModal.dismiss();
  }

  addStock(id: number): void {
    if (!this.stock || this.stock <= 0) {
      this.errorMessage = 'La cantidad debe ser mayor que 0';
      return;
    }

    this.productoService.addStockAlProducto(id, this.stock).subscribe({
      next: () => {
        this.activeModal.close(STOCK_UPDATED_EVENT);
      },
      error: () => {
        this.errorSubject.next('Error desconocido. Inténtalo de nuevo más tarde.');
        this.activeModal.dismiss(); 
      }
    });
  }
}