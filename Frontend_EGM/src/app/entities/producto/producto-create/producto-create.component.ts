import { Component, inject, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { ProductoService } from '../producto.service';
import { ISubcategoria } from '../../subcategoria/subcategoria.model';
import { ICategoria } from '../../categoria/categoria.model';
import { CategoriaService } from '../../categoria/categoria.service';
import { SubcategoriaService } from '../../subcategoria/subcategoria.service';
import { debounceTime } from 'rxjs';

@Component({
  standalone: true,
  templateUrl: './producto-create.component.html',
  styleUrls: ['../../entities.css'],
  imports: [FormsModule, ReactiveFormsModule, CommonModule, RouterOutlet, RouterLink,],
})
export class ProductoCreateComponent implements OnInit {
    crearProductoFormulario!: FormGroup;
    categoriasCollection: ICategoria[] = [];
    subcategoriasCollection: ISubcategoria[] = [];

    protected router = inject(Router);
    protected subcategoriaService = inject(SubcategoriaService);
    protected categoriaService = inject(CategoriaService);
    protected productoService = inject(ProductoService);

    ngOnInit(): void {
        this.loadCategorias();
        this.loadSubcategorias();
        this.crearProductoFormulario = new FormGroup({
            nombre: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
            marca: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
            referencia: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
            categoria: new FormControl(null, [Validators.required]),
            subcategoria: new FormControl({ value: null, disabled: true }, [Validators.required]),
            urlImagen: new FormControl(null),
            descripcion: new FormControl(null, [Validators.maxLength(3000)]),
            ingredientes: new FormControl(null, [Validators.maxLength(3000)]),
            tipoIVA: new FormControl(null, [Validators.min(0), Validators.max(100)]),
            cantidad: new FormControl(null, [Validators.min(0), Validators.pattern(/^\d+$/)]),
            stock: new FormControl(null, [Validators.min(0), Validators.pattern(/^\d+$/)]),
            precio: new FormControl(null, [Validators.required, Validators.min(0)]),
        });

        this.crearProductoFormulario.get('categoria')?.valueChanges.subscribe((categoria) => {
            if (categoria) {
                this.subcategoriaService.getSubcategoriasConIdCategoria(categoria.id).pipe(debounceTime(300)).subscribe((res) => {
                    console.log(categoria.id);
                    this.subcategoriasCollection = res || [];
                    if (this.subcategoriasCollection.length > 0) {
                        this.crearProductoFormulario.get('subcategoria')?.enable();
                    } else {
                        this.crearProductoFormulario.get('subcategoria')?.disable();
                    }
                });
            } else {
                this.loadSubcategorias();
                this.crearProductoFormulario.get('subcategoria')?.disable();
            }
        });
    }

    loadCategorias() {
        this.categoriaService.getAllCategorias().subscribe((res) => {
            this.categoriasCollection = res || [];
        });
    }

    loadSubcategorias() {
        this.subcategoriaService.getAllSubcategorias().subscribe((res) => {
            this.subcategoriasCollection = res || [];
        });
    }

    comprobarForm(): void {
        if (this.crearProductoFormulario.invalid) {
            this.crearProductoFormulario.markAllAsTouched();
            return;
        }  
        this.productoService.crearProducto(this.crearProductoFormulario?.value).subscribe({
            next: (response) => {
              this.router.navigate(['/producto'], { queryParams: { creado: 'true' } });
            },
            error: (error) => {
              if (error.error && error.error.error) {
                if (error.error.error === 'referenciaExiste') {
                    this.crearProductoFormulario.get('referencia')?.setErrors({ 'referenciaExiste': true });
                }
              }
            }
        });
    }

    volver(): void{
        window.history.back();
    }
}