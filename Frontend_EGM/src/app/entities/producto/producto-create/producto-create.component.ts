import { Component, inject, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink, RouterOutlet } from '@angular/router';
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
    booleanEditarExistente = false;
    ignorarCambioCategoria = false;

    protected router = inject(Router);
    protected subcategoriaService = inject(SubcategoriaService);
    protected categoriaService = inject(CategoriaService);
    protected productoService = inject(ProductoService);
    private route = inject(ActivatedRoute);

    ngOnInit(): void {
        window.scrollTo(0, 0);
        this.loadCategorias();
        this.loadSubcategorias();
        this.crearProductoFormulario = new FormGroup({
            id: new FormControl(null),
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

        this.route.queryParams.subscribe(params => {
            const id = params['id'];
            if (id) {
              this.booleanEditarExistente = true;
              this.productoService.getProducto(id).subscribe({
                next: (res) => {
                  setTimeout(() => {
                    const categoriaCorrespondiente = this.categoriasCollection.find(c => c.id === res.categoria?.id);
                    const subcategoriaCorrespondiente = this.subcategoriasCollection.find(c => c.id === res.subcategoria?.id);
                    
                    this.crearProductoFormulario.get('subcategoria')?.enable();
                    this.ignorarCambioCategoria = true;
              
                    this.crearProductoFormulario.patchValue({
                      id: res.id,
                      nombre: res.nombre,
                      marca: res.marca,
                      referencia: res.referencia,
                      categoria: categoriaCorrespondiente,
                      subcategoria: subcategoriaCorrespondiente,
                      urlImagen: res.urlImagen,
                      descripcion: res.descripcion,
                      ingredientes: res.ingredientes,
                      tipoIVA: res.tipoIVA,
                      cantidad: res.cantidad,
                      stock: res.stock,
                      precio: res.precio
                    });
                  }, 25);
                },
                error: (err) => {
                    this.booleanEditarExistente = false;
                    this.crearProductoFormulario.get('subcategoria')?.disable();
                }
              });
            }
        });

        this.crearProductoFormulario.get('categoria')?.valueChanges.subscribe((categoria) => {
            if (!this.ignorarCambioCategoria) {
                this.crearProductoFormulario.get('subcategoria')?.setValue(null);
                if (categoria) {
                    this.subcategoriaService.getSubcategoriasConIdCategoria(categoria.id).pipe(debounceTime(300)).subscribe((res) => {
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
            }
            this.ignorarCambioCategoria = false;
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
        if (this.booleanEditarExistente) {
            this.productoService.editarProducto(this.crearProductoFormulario.value).subscribe({
                next: (response) => {
                    this.router.navigate(['/producto'], { queryParams: { editado: 'true' } });
                }
            });
        } else {
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
    }

    volver(): void{
        window.history.back();
    }
}