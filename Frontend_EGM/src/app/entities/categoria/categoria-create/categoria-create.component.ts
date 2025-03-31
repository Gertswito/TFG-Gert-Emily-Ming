import { Component, inject, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
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

    protected router = inject(Router);
    protected categoriaService = inject(CategoriaService);

    ngOnInit(): void {
        this.crearCategoriaFormulario = new FormGroup({
          nombre: new FormControl(null, [Validators.required, Validators.maxLength(255)])
        });
    }

    comprobarForm(): void {
        if (this.crearCategoriaFormulario.invalid) {
            this.crearCategoriaFormulario.markAllAsTouched();
            return;
        }  
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

    volver(): void{
        window.history.back();
    }
}