import { Component, inject, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink, RouterOutlet } from '@angular/router';
import { CategoriaService } from '../categoria.service';
import { ICategoria } from '../categoria.model';

@Component({
  standalone: true,
  templateUrl: './categoria-create.component.html',
  styleUrls: ['../../entities.css'],
  imports: [FormsModule, ReactiveFormsModule, CommonModule, RouterOutlet, RouterLink,],
})
export class CategoriaCreateComponent implements OnInit {
    crearCategoriaFormulario!: FormGroup;
    booleanEditarExistente = false;

    protected router = inject(Router);
    protected categoriaService = inject(CategoriaService);
    private route = inject(ActivatedRoute);

    ngOnInit(): void {
      this.crearCategoriaFormulario = new FormGroup({
        nombre: new FormControl(null, [Validators.required, Validators.maxLength(255)])
      });
    
      this.route.queryParams.subscribe(params => {
        const id = params['id'];
        if (id) {
          this.booleanEditarExistente = true;
          this.categoriaService.getCategoria(id).subscribe((res) => {
            this.crearCategoriaFormulario = new FormGroup({
              id: new FormControl(res.id),
              nombre: new FormControl(res.nombre, [Validators.required, Validators.maxLength(255)])
            });
          });
        }
      });
    }

    comprobarForm(): void {
        if (this.crearCategoriaFormulario.invalid) {
            this.crearCategoriaFormulario.markAllAsTouched();
            return;
        }  
        if (this.booleanEditarExistente) {
          this.categoriaService.editarCategoria(this.crearCategoriaFormulario.value).subscribe({
            next: (response) => {
              this.router.navigate(['/categoria'], { queryParams: { editado: 'true' } });
            }
          });
        } else {
          this.categoriaService.crearCategoria(this.crearCategoriaFormulario.value).subscribe({
            next: (response) => {
              this.router.navigate(['/categoria'], { queryParams: { creado: 'true' } });
            },
            error: (error) => {
              if (error.error && error.error.error) {
                if (error.error.error === 'nombreExiste') {
                    this.crearCategoriaFormulario.get('nombre')?.setErrors({ 'nombreExiste': true });
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