import { Component, inject, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink, RouterOutlet } from '@angular/router';
import { LineasVentaService } from '../lineasVenta.service';
import { ILineasVenta } from '../lineasVenta.model';
import { VentaService } from '../../venta/venta.service';
import { IVenta } from '../../venta/venta.model';
import { IProducto } from '../../producto/producto.model';
import { ProductoService } from '../../producto/producto.service';

@Component({
  standalone: true,
  templateUrl: './lineasVenta-create.component.html',
  styleUrls: ['../../entities.css'],
  imports: [FormsModule, ReactiveFormsModule, CommonModule, RouterOutlet, RouterLink,],
})
export class LineasVentaCreateComponent implements OnInit {
    crearLineaVentaFormulario!: FormGroup;
    ventasCollection: IVenta[] = [];
    productosCollection: IProducto[] = [];
    booleanEditarExistente = false;

    protected router = inject(Router);
    protected lineasVentaService = inject(LineasVentaService);
    protected ventaService = inject(VentaService);
    protected productoService = inject(ProductoService);
    private route = inject(ActivatedRoute);

    ngOnInit(): void {
        window.scrollTo(0, 0);
        this.loadVentas();
        this.loadProductos();
        this.crearLineaVentaFormulario = new FormGroup({
            id: new FormControl(null),
            venta: new FormControl(null, [Validators.required]),
            producto: new FormControl(null, [Validators.required]),
            cantidadPedida: new FormControl(null, [Validators.required, Validators.min(0), Validators.pattern(/^\d+$/)]),
            precioUnitario: new FormControl(null, [Validators.required, Validators.min(0)]),
            precioTotal: new FormControl(null, [Validators.required, Validators.min(0)]),
        });

        this.route.queryParams.subscribe(params => {
            const id = params['id'];
            if (id) {
              this.booleanEditarExistente = true;
              this.lineasVentaService.getLineaVenta(id).subscribe({
                next: (res) => {
                  setTimeout(() => {
                    const ventaCorrespondiente = this.ventasCollection.find(c => c.id === res.venta?.id);
                    const productoCorrespondiente = this.productosCollection.find(c => c.id === res.producto?.id);
              
                    this.crearLineaVentaFormulario.patchValue({
                      id: res.id,
                      venta: ventaCorrespondiente,
                      producto: productoCorrespondiente,
                      cantidadPedida: res.cantidadPedida,
                      precioUnitario: res.precioUnitario,
                      precioTotal: res.precioTotal
                    });
                  }, 25);
                },
                error: (err) => {
                    this.booleanEditarExistente = false;
                }
              });
            }
        });

        this.crearLineaVentaFormulario.get('producto')?.valueChanges.subscribe((producto: IProducto) => {
            if (this.crearLineaVentaFormulario.get('cantidadPedida')?.value != null && producto != null) {
                const cantidad: number = this.crearLineaVentaFormulario.get('cantidadPedida')?.value;
                this.crearLineaVentaFormulario.get('precioUnitario')?.setValue(producto.precio);
                this.crearLineaVentaFormulario.get('precioTotal')?.setValue(this.crearLineaVentaFormulario.get('precioUnitario')?.value * cantidad);
            } else {
                this.crearLineaVentaFormulario.get('precioUnitario')?.setValue(null);
                this.crearLineaVentaFormulario.get('precioTotal')?.setValue(null);
            }
        });

        this.crearLineaVentaFormulario.get('cantidadPedida')?.valueChanges.subscribe((cantidad: number) => {
            if (this.crearLineaVentaFormulario.get('producto')?.value != null && cantidad != null) {
                const producto: IProducto = this.crearLineaVentaFormulario.get('producto')?.value;
                this.crearLineaVentaFormulario.get('precioUnitario')?.setValue(producto.precio);
                this.crearLineaVentaFormulario.get('precioTotal')?.setValue(this.crearLineaVentaFormulario.get('precioUnitario')?.value * cantidad);
            } else {
                this.crearLineaVentaFormulario.get('precioUnitario')?.setValue(null);
                this.crearLineaVentaFormulario.get('precioTotal')?.setValue(null);
            }
        });
    }

    loadVentas(): void {
        this.ventaService.getAllVentas().subscribe((res) => {
            this.ventasCollection = res || [];
        });
    }

    loadProductos(): void {
        this.productoService.getAllProductos().subscribe((res) => {
            this.productosCollection = res || [];
        });
    }

    comprobarForm(): void {
        if (this.crearLineaVentaFormulario.invalid) {
            this.crearLineaVentaFormulario.markAllAsTouched();
            return;
        }
        if (this.booleanEditarExistente) {
            this.lineasVentaService.editarLineaVenta(this.crearLineaVentaFormulario.value).subscribe({
                next: (response) => {
                  this.router.navigate(['/lineasVenta'], { queryParams: { creado: 'true' } });
                }
            });
        } else {
            this.lineasVentaService.crearLineaVenta(this.crearLineaVentaFormulario.value).subscribe({
                next: (response) => {
                  this.router.navigate(['/lineasVenta'], { queryParams: { creado: 'true' } });
                }
            });
        }  
    }

    volver(): void{
        window.history.back();
    }
}