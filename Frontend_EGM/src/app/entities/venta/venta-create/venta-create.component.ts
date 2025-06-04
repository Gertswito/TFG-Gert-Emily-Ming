import { Component, inject, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink, RouterOutlet } from '@angular/router';
import { VentaService } from '../venta.service';
import { IVenta } from '../venta.model';
import { ClienteService } from '../../cliente/cliente.service';
import { PagoService } from '../../pago/pago.service';
import { DireccionService } from '../../direccion/direccion.service';
import { ICliente } from '../../cliente/cliente.model';
import { IPago } from '../../pago/pago.model';
import { IDireccion } from '../../direccion/direccion.model';
import { debounceTime } from 'rxjs';

@Component({
  standalone: true,
  templateUrl: './venta-create.component.html',
  styleUrls: ['../../entities.css'],
  imports: [FormsModule, ReactiveFormsModule, CommonModule, RouterOutlet, RouterLink,],
})
export class VentaCreateComponent implements OnInit {
    crearVentaFormulario!: FormGroup;
    clientesCollection: ICliente[] = [];
    pagosCollection: IPago[] = [];
    direccionesCollection: IDireccion[] = [];
    booleanEditarExistente = false;
    ignorarCambioDireccionYPago = false;

    protected router = inject(Router);
    protected ventaService = inject(VentaService);
    protected clienteService = inject(ClienteService);
    protected pagoService = inject(PagoService);
    protected direccionService = inject(DireccionService);
    private route = inject(ActivatedRoute);

    ngOnInit(): void {
        window.scrollTo(0, 0);
        this.loadClientes();
        this.loadPagos();
        this.loadDirecciones();
        this.crearVentaFormulario = new FormGroup({
            id: new FormControl(null),
            cliente: new FormControl(null, [Validators.required]),
            pago: new FormControl({ value: null, disabled: true }, [Validators.required]),
            direccion: new FormControl({ value: null, disabled: true }, [Validators.required]),
            fechaHora: new FormControl(null, [Validators.required]),
            precioFinal: new FormControl(null, [Validators.required, Validators.min(0)]),
        });

        this.route.queryParams.subscribe(params => {
            const id = params['id'];
            if (id) {
            this.booleanEditarExistente = true;
            this.ventaService.getVenta(id).subscribe((res) => {
                setTimeout(() => {
                const clienteCorrespondiente = this.clientesCollection.find(c => c.id === res.cliente?.id);
                const pagoCorrespondiente = this.pagosCollection.find(c => c.id === res.pago?.id);
                const direccionCorrespondiente = this.direccionesCollection.find(c => c.id === res.direccion?.id);
                this.crearVentaFormulario.get('direccion')?.enable();
                this.crearVentaFormulario.get('pago')?.enable();
                this.ignorarCambioDireccionYPago = true;

                this.crearVentaFormulario.patchValue({
                    id: res.id,
                    cliente: clienteCorrespondiente,
                    pago: pagoCorrespondiente,
                    direccion: direccionCorrespondiente,
                    fechaHora: res.fechaHora,
                    precioFinal: res.precioFinal
                });
                }, 25);
            });
            }
        });

        this.crearVentaFormulario.get('cliente')?.valueChanges.subscribe((cliente: ICliente) => {
            if (!this.ignorarCambioDireccionYPago) {
                if (cliente && cliente.usuario) {
                    this.direccionService.getDireccionesPorCliente(cliente.usuario).pipe(debounceTime(300)).subscribe((res) => {
                        this.direccionesCollection = res || [];
                        if (this.direccionesCollection.length > 0) {
                            this.crearVentaFormulario.get('direccion')?.enable();
                        } else {
                            this.crearVentaFormulario.get('direccion')?.disable();
                            this.crearVentaFormulario.get('cliente')?.setErrors({ 'clienteSinCosas': true });
                        }
                    });
                    this.pagoService.getPagosByCliente(cliente.usuario).pipe(debounceTime(300)).subscribe((res) => {
                        this.pagosCollection = res || [];
                        if (this.pagosCollection.length > 0) {
                            this.crearVentaFormulario.get('pago')?.enable();
                        } else {
                            this.crearVentaFormulario.get('pago')?.disable();
                            this.crearVentaFormulario.get('cliente')?.setErrors({ 'clienteSinCosas': true });
                        }
                    });
                } else {
                    this.loadDirecciones();
                    this.loadPagos();
                    this.crearVentaFormulario.get('pago')?.disable();
                    this.crearVentaFormulario.get('direccion')?.disable();
                }
            }
            this.ignorarCambioDireccionYPago = false;
        });
    }

    loadClientes(): void {
        this.clienteService.getAllClientes().subscribe((res) => {
            this.clientesCollection = res || [];
        });
    }

    loadPagos(): void {
        this.pagoService.getAllPagos().subscribe((res) => {
            this.pagosCollection = res || [];
        });
    }

    loadDirecciones(): void {
        this.direccionService.getAllDirecciones().subscribe((res) => {
            this.direccionesCollection = res || [];
        });
    }


    comprobarForm(): void {
        if (this.crearVentaFormulario.invalid) {
            this.crearVentaFormulario.markAllAsTouched();
            return;
        }  
        let fechaHora = this.crearVentaFormulario.get('fechaHora')?.value;
        if (fechaHora && (fechaHora.match(/:/g) || []).length === 1) {
          fechaHora = `${fechaHora}:00`;
        }
        this.crearVentaFormulario.get('fechaHora')?.setValue(fechaHora);
        if (this.booleanEditarExistente) {
            this.ventaService.editarVenta(this.crearVentaFormulario.value).subscribe({
                next: (response) => {
                    this.router.navigate(['/venta'], { queryParams: { editado: 'true' } });
                }
            });
        } else {
            this.ventaService.crearVenta(this.crearVentaFormulario.value).subscribe({
                next: (response) => {
                  this.router.navigate(['/venta'], { queryParams: { creado: 'true' } });
                },
                error: (error) => {
                  if (error.error && error.error.error) {
                    if (error.error.error === 'fechaInvalida') {
                        this.crearVentaFormulario.get('fechaHora')?.setErrors({ 'fechaInvalida': true });
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