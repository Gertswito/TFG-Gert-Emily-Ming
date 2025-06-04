import { Component, inject, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink, RouterOutlet } from '@angular/router';
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
  booleanEditarExistente = false;

  protected router = inject(Router);
  protected clienteService = inject(ClienteService);
  private route = inject(ActivatedRoute);

  ngOnInit(): void {
    window.scrollTo(0, 0);
      this.crearClienteFormulario = new FormGroup({
          id: new FormControl(null),
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

      this.route.queryParams.subscribe(params => {
        const id = params['id'];
        if (id) {
          this.booleanEditarExistente = true;
          this.clienteService.getClienteById(id).subscribe({
            next: (res) => {
              this.crearClienteFormulario.patchValue({
                id: res.id,
                usuario: res.usuario,
                nombre: res.nombre,
                apellidos: res.apellidos,
                email: res.email,
                telefono: res.telefono,
                dni: res.dni,
                contrasenha: res.contrasenha,
                rol: res.rol,
                fechaNac: this.formatearFechaParaInput(res.fechaNac)
              });
              if(res.dni){
                this.determinarTipoDoc(res.dni);
              }
            },
            error: (err) => {
              this.booleanEditarExistente = false;
            }
          });
        }
      });

      this.crearClienteFormulario.get('tipoDoc')?.valueChanges.pipe(debounceTime(300)).subscribe(() => this.actualizarValidacionDNI());
      this.crearClienteFormulario.get('dni')?.valueChanges.pipe(debounceTime(300)).subscribe(() => this.actualizarValidacionDNI());
  }

  comprobarForm(): void {
      if (this.crearClienteFormulario.invalid) {
        this.crearClienteFormulario.markAllAsTouched();
        return;
      }
      if (this.booleanEditarExistente) {
        this.clienteService.updateCliente(this.crearClienteFormulario.value).subscribe({
          next: (response) => {
            this.router.navigate(['/cliente'], { queryParams: { editado: 'true' } });
          }
        });
      } else {
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

  private determinarTipoDoc(dni: string): void {
    const dniRegex = /^\d{8}[A-Z]$/; 
    const nieRegex = /^[XYZ]\d{7}[A-Z]$/;

    if (dniRegex.test(dni)) {
        this.crearClienteFormulario.patchValue({ tipoDoc: 'dni' });
    } else if (nieRegex.test(dni)) {
        this.crearClienteFormulario.patchValue({ tipoDoc: 'nie' });
    } else {
        this.crearClienteFormulario.patchValue({ tipoDoc: null });
    }
  }

  private formatearFechaParaInput(fecha: Date | string | Array<number> | { year: number; month: number; day: number } | null): string | null {
    if (!fecha) {
      return null;
    }
    let year: number;
    let month: number;
    let day: number;
    if (Array.isArray(fecha) && fecha.length >= 3) {
      [year, month, day] = fecha as number[];
    }
    else if (
      typeof fecha === 'object' &&
      'year' in fecha &&
      'month' in fecha &&
      'day' in fecha
    ) {
      ({ year, month, day } = fecha as { year: number; month: number; day: number });
    }
    else {
      const d: Date =
        typeof fecha === 'string'
          ? new Date(fecha)     
          : (fecha as Date);  
      year = d.getFullYear();
      month = d.getMonth() + 1;
      day = d.getDate();
    }
    const mm = String(month).padStart(2, '0');
    const dd = String(day).padStart(2, '0');
    return `${year}-${mm}-${dd}`;
  }
}