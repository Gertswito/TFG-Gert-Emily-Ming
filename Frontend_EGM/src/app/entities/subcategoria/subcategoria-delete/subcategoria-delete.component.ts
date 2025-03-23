import { Component, inject } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { ITEM_DELETED_EVENT } from "../../../config/navigation.constants";
import { ISubcategoria } from "../subcategoria.model";
import { SubcategoriaService } from "../subcategoria.service";
import { Subject } from "rxjs";

@Component({
  standalone: true,
  templateUrl: './subcategoria-delete.component.html',
  styleUrls: ['../../entities.css'],
  imports: [FormsModule],
})
export class SubcategoriaDeleteComponent {
  subcategoria?: ISubcategoria;
  errorSubject = new Subject<string>();

  protected subcategoriaService = inject(SubcategoriaService);
  protected activeModal = inject(NgbActiveModal);

  cancelar(): void {
    this.activeModal.dismiss();
  }

  confirmarDelete(id: number): void {
    this.subcategoriaService.deleteSubcategoria(id).subscribe({
      next: () => {
        this.activeModal.close(ITEM_DELETED_EVENT);
      },
      error: (err) => {
        const subcategoriaNombre = this.subcategoria?.nombre ?? id;
        if (err.status === 409) {
          this.errorSubject.next('No se puede eliminar la subcategoría "' + subcategoriaNombre + '" por culpa de otras dependencias, asegúrese de que no haya ningun producto asociado a esta subcategoría.');
        } else if (err.status === 404) {
          this.errorSubject.next('La subcategoría "' + subcategoriaNombre + '" no existe.');
        } else {
          this.errorSubject.next('Error desconocido. Inténtalo de nuevo más tarde.');
        }
        this.activeModal.dismiss(); 
      }
    });
  }
}