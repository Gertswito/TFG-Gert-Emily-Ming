import { Component, inject, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink, RouterOutlet } from '@angular/router';
import { DireccionService } from '../direccion.service';
import { ClienteService } from '../../cliente/cliente.service';
import { ICliente } from '../../cliente/cliente.model';
import { IDireccion } from '../direccion.model';

@Component({
  standalone: true,
  templateUrl: './direccion-create.component.html',
  styleUrls: ['../../entities.css'],
  imports: [FormsModule, ReactiveFormsModule, CommonModule, RouterOutlet, RouterLink,],
})
export class DireccionCreateComponent implements OnInit {
    crearDireccionFormulario!: FormGroup;
    clientesCollection: ICliente[] = [];
    booleanEditarExistente = false;

    protected router = inject(Router);
    protected direccionService = inject(DireccionService);
    protected clienteService = inject(ClienteService);
    private route = inject(ActivatedRoute);

    ngOnInit(): void {
      window.scrollTo(0, 0);
        this.loadClientes();
        this.crearDireccionFormulario = new FormGroup({
            id: new FormControl(null),
            direccion: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
            localidad: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
            comunidadAutonoma: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
            codigoPostal: new FormControl(null, [Validators.required, Validators.pattern("[0-9]{5}")]),
            activo: new FormControl(true, [Validators.required]),
            cliente: new FormControl(null, [Validators.required]),
        });

        this.route.queryParams.subscribe(params => {
            const id = params['id'];
            if (id) {
              this.booleanEditarExistente = true;
              this.direccionService.getDireccion(id).subscribe({
                next: (res) => {
                  setTimeout(() => {
                    const clienteCorrespondiente = this.clientesCollection.find(c => c.id === res.cliente?.id);
                    
                    this.crearDireccionFormulario.patchValue({
                      id: res.id,
                      direccion: res.direccion,
                      localidad: res.localidad,
                      comunidadAutonoma: res.comunidadAutonoma,
                      codigoPostal: res.codigoPostal,
                      activo: res.activo,
                      cliente: clienteCorrespondiente,
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

    loadClientes() {
        this.clienteService.getAllClientes().subscribe((res) => {
            this.clientesCollection = res || [];
        });
    }

    comprobarForm(): void {
        if (this.crearDireccionFormulario.invalid) {
            this.crearDireccionFormulario.markAllAsTouched();
            return;
        }  
        const clienteSeleccionado = this.crearDireccionFormulario.get('cliente')?.value as ICliente;
        const direccion: IDireccion = this.crearDireccionFormulario.value as IDireccion;
        if (clienteSeleccionado.usuario) {
            if (this.booleanEditarExistente) {
                if (direccion.id) {
                    this.direccionService.updateDireccion(direccion.id, direccion).subscribe({
                        next: (response) => {
                          this.router.navigate(['/direccion'], { queryParams: { editado: 'true' } });
                        }
                    });
                }
            } else {
                this.direccionService.createDireccion(clienteSeleccionado.usuario, direccion).subscribe({
                    next: (response) => {
                      this.router.navigate(['/direccion'], { queryParams: { creado: 'true' } });
                    }
                });
            }
        }
    }

    volver(): void{
        window.history.back();
    }
}