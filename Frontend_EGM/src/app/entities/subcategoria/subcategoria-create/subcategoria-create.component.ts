import { Component, inject, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink, RouterOutlet } from '@angular/router';
import { SubcategoriaService } from '../subcategoria.service';
import { ICategoria } from '../../categoria/categoria.model';
import { CategoriaService } from '../../categoria/categoria.service';

@Component({
  standalone: true,
  templateUrl: './subcategoria-create.component.html',
  styleUrls: ['../../entities.css'],
  imports: [FormsModule, ReactiveFormsModule, CommonModule, RouterOutlet, RouterLink,],
})
export class SubcategoriaCreateComponent implements OnInit {
    crearSubcategoriaFormulario!: FormGroup;
    categoriasCollection: ICategoria[] = [];
    booleanEditarExistente = false;

    protected router = inject(Router);
    protected subcategoriaService = inject(SubcategoriaService);
    protected categoriaService = inject(CategoriaService);
    private route = inject(ActivatedRoute);

    ngOnInit(): void {
      window.scrollTo(0, 0);
        this.loadCategorias();
        this.crearSubcategoriaFormulario = new FormGroup({
          id: new FormControl(null),
          nombre: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
          categoria: new FormControl(null, [Validators.required]),
          imagenSubcategoria: new FormControl(null)
        });

        this.route.queryParams.subscribe(params => {
          const id = params['id'];
          if (id) {
            this.booleanEditarExistente = true;
            this.subcategoriaService.getSubcategoria(id).subscribe({
              next: (res) => {
                setTimeout(() => {
                  const categoriaCorrespondiente = this.categoriasCollection.find(c => c.id === res.categoria?.id);
                  
                  this.crearSubcategoriaFormulario.patchValue({
                    id: res.id,
                    nombre: res.nombre,
                    categoria: categoriaCorrespondiente,
                    imagenSubcategoria: res.imagenSubcategoria
                  });
                }, 25);
              },
              error: (err) => {
                this.booleanEditarExistente = false;
              }
            });
          }
        });
    }

    loadCategorias() {
        this.categoriaService.getAllCategorias().subscribe((res) => {
            this.categoriasCollection = res || [];
        });
    }

    comprobarForm(): void {
      if (this.crearSubcategoriaFormulario.invalid) {
          this.crearSubcategoriaFormulario.markAllAsTouched();
          return;
      }
      if (this.booleanEditarExistente) {
        this.subcategoriaService.editarSubcategoria(this.crearSubcategoriaFormulario.value).subscribe({
          next: (response) => {
            this.router.navigate(['/subcategoria'], { queryParams: { editado: 'true' } });
          }
        });
      } else {
        this.subcategoriaService.crearSubcategoria(this.crearSubcategoriaFormulario?.value).subscribe({
            next: (response) => {
              this.router.navigate(['/subcategoria'], { queryParams: { creado: 'true' } });
            },
            error: (error) => {
              if (error.error && error.error.error) {
                if (error.error.error === 'nombreExiste') {
                    this.crearSubcategoriaFormulario.get('nombre')?.setErrors({ 'nombreExiste': true });
                }
              }
            }
        });
      }
    }

    volver(): void{
        window.history.back();
    }
}