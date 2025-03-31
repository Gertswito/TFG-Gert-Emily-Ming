import { Component, inject, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { ClienteService } from '../cliente.service';
import { ICliente } from '../cliente.model';
import { debounceTime } from 'rxjs';

@Component({
  standalone: true,
  templateUrl: './cliente-create.component.html',
  styleUrls: ['../../entities.css'],
  imports: [FormsModule, ReactiveFormsModule, CommonModule, RouterOutlet, RouterLink,],
})
export class ClienteCreateComponent implements OnInit {
    crearClienteFormulario!: FormGroup;

    protected router = inject(Router);
    protected clienteService = inject(ClienteService);

    ngOnInit(): void {
        this.crearClienteFormulario = new FormGroup({
            usuario: new FormControl(null, [Validators.required, Validators.maxLength(50),]),
            nombre: new FormControl(null, [Validators.maxLength(50),]),
            apellidos: new FormControl(null, [Validators.maxLength(50),]),
            email: new FormControl(null, [Validators.email, Validators.maxLength(100),]),
            telefono: new FormControl(null, [Validators.pattern(/^\d{9}$/)]), 
            tipoDoc: new FormControl(null), 
            dni: new FormControl(null, [Validators.minLength(9), Validators.maxLength(9)]),
            fechaNac: new FormControl(null),
            contrasenha: new FormControl(null, [Validators.required]),
            rol: new FormControl(null, [Validators.required]),
        });
        this.crearClienteFormulario.get('tipoDoc')?.valueChanges.pipe(debounceTime(300)).subscribe(() => this.actualizarValidacionDNI());
        this.crearClienteFormulario.get('dni')?.valueChanges.pipe(debounceTime(300)).subscribe(() => this.actualizarValidacionDNI());
    }

    comprobarForm(): void {
        if (this.crearClienteFormulario.invalid) {
          this.crearClienteFormulario.markAllAsTouched();
          return;
        }
      
        this.clienteService.crearUsuario(this.crearClienteFormulario.value).subscribe({
          next: (response) => {
            this.router.navigate(['/cliente'], { queryParams: { creado: 'true' } });
          },
          error: (error) => {
            if (error.error && error.error.error) {
              if (error.error.error === 'nombreUsuarioExiste') {
                this.crearClienteFormulario.get('usuario')?.setErrors({ nombreUsuarioExiste: true });
              }
              if (error.error.error === 'emailExiste') {
                this.crearClienteFormulario.get('email')?.setErrors({ emailExiste: true });
              }
              if (error.error.error === 'dniExiste') {
                this.crearClienteFormulario.get('dni')?.setErrors({ dniExiste: true });
              }
              if (error.error.error === 'fechaInvalida') {
                this.crearClienteFormulario.get('fechaNac')?.setErrors({ fechaInvalida: true });
              }
            }
          }
        });
    }

    volver(): void{
        window.history.back();
    }

    private actualizarValidacionDNI(): void {
        const dniControl = this.crearClienteFormulario.get('dni');
        if (!dniControl) return;
      
        const tipoDoc = this.crearClienteFormulario.get('tipoDoc')?.value;
      
        if (tipoDoc === 'dni') {
          dniControl.setValidators([
            Validators.required,
            Validators.minLength(9),
            Validators.maxLength(9),
            Validators.pattern(/^\d{8}[A-Z]$/)
          ]);
        } else if (tipoDoc === 'nie') {
          dniControl.setValidators([
            Validators.required,
            Validators.minLength(9),
            Validators.maxLength(9),
            Validators.pattern(/^[XYZ]\d{7}[A-Z]$/)
          ]);
        } else {
          dniControl.clearValidators(); 
        }
      
        dniControl.updateValueAndValidity({ emitEvent: false }); 
    }
}