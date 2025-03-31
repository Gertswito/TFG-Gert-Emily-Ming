import { Component, inject, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { SubcategoriaService } from '../subcategoria.service';
import { ISubcategoria } from '../subcategoria.model';
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

    protected router = inject(Router);
    protected subcategoriaService = inject(SubcategoriaService);
    protected categoriaService = inject(CategoriaService);

    ngOnInit(): void {
        this.categoriaService.getAllCategorias().subscribe((res) => {
            this.categoriasCollection = res || [];
        });
        this.crearSubcategoriaFormulario = new FormGroup({
          nombre: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
          categoria: new FormControl(null, [Validators.required]),
          imagenSubcategoria: new FormControl(null)
        });
    }

    comprobarForm(): void {
        if (this.crearSubcategoriaFormulario.invalid) {
            this.crearSubcategoriaFormulario.markAllAsTouched();
            return;
        }  
        const nuevaSubcategoria: ISubcategoria = {
            id: null,
            nombre: this.crearSubcategoriaFormulario.get('nombre')?.value,
            categoria: this.crearSubcategoriaFormulario.get('categoria')?.value,
            imagenSubcategoria: this.crearSubcategoriaFormulario.get('imagenSubcategoria')?.value
        }
        this.subcategoriaService.crearSubcategoria(nuevaSubcategoria).subscribe({
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

    volver(): void{
        window.history.back();
    }
}