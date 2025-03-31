import { Component, inject } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { ITEM_DELETED_EVENT } from "../../../config/navigation.constants";
import { ICategoria } from "../categoria.model";
import { CategoriaService } from "../categoria.service";
import { Subject } from "rxjs";

@Component({
  standalone: true,
  templateUrl: './categoria-delete.component.html',
  styleUrls: ['../../entities.css'],
  imports: [FormsModule],
})
export class CategoriaDeleteComponent {
  categoria?: ICategoria;
  errorSubject = new Subject<string>();

  protected categoriaService = inject(CategoriaService);
  protected activeModal = inject(NgbActiveModal);

  cancelar(): void {
    this.activeModal.dismiss();
  }

  confirmarDelete(id: number): void {
    this.categoriaService.deleteCategoria(id).subscribe({
      next: () => {
        this.activeModal.close(ITEM_DELETED_EVENT);
      },
      error: (err) => {
        const categoriaNombre = this.categoria?.nombre ?? id;
        if (err.status === 409) {
          this.errorSubject.next('No se puede eliminar la categoría "' + categoriaNombre + '" por culpa de otras dependencias, asegúrese de que no haya ninguna subcategoría o producto asociados a esta categoría.');
        } else if (err.status === 404) {
          this.errorSubject.next('La categoría "' + categoriaNombre + '" no existe.');
        } else {
          this.errorSubject.next('Error desconocido. Inténtalo de nuevo más tarde.');
        }
        this.activeModal.dismiss(); 
      }
    });
  }
}