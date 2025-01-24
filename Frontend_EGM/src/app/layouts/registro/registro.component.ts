import { Component, inject, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { ClienteService } from '../../entities/cliente/cliente.service';


@Component({
  standalone: true,
  selector: 'registro',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    RouterOutlet,
    RouterLink,
  ],
  templateUrl: './registro.component.html',
  styleUrls: ['../login/login.component.css'],
})
export class RegistroComponent implements OnInit {
  registroFormulario!: FormGroup;

  protected router = inject(Router);
  protected clienteService = inject(ClienteService);

  ngOnInit(): void {
    this.registroFormulario = new FormGroup({
      usuario: new FormControl(null, [Validators.required]),
      nombre: new FormControl(null, [Validators.required, Validators.maxLength(50),]),
      apellidos: new FormControl(null, [Validators.required, Validators.maxLength(50),]),
      email: new FormControl(null, [Validators.required, Validators.email, Validators.maxLength(100),]),
      telefono: new FormControl(null, [Validators.required, Validators.pattern(/^\d{9}$/)]),
      dni: new FormControl(null, [Validators.required, Validators.minLength(9), Validators.maxLength(9)]),
      fechaNac: new FormControl(null, [Validators.required]),
      contrasenha: new FormControl(null, [Validators.required]),
      repiteContrasenha: new FormControl(null, [Validators.required]),
    });
  }

  comprobarRegistro(): void {
    if (this.registroFormulario.invalid) {
      this.registroFormulario.markAllAsTouched();
      return;
    }
  
    const contrasenha1 = this.registroFormulario.get('contrasenha')?.value;
    const contrasenha2 = this.registroFormulario.get('repiteContrasenha')?.value;
    if (contrasenha1 !== contrasenha2) {
      this.registroFormulario.get('repiteContrasenha')?.setErrors({ 'noCoincide': true });
      return;
    }
  
    this.clienteService.crearUsuario(this.registroFormulario.value).subscribe({
      next: (response) => {
        this.router.navigate(['/login'], { queryParams: { registrado: 'true' } });
      },
      error: (error) => {
        if (error.error && error.error.error) {
          if (error.error.error === 'nombreUsuarioExiste') {
            this.registroFormulario.get('usuario')?.setErrors({ nombreUsuarioExiste: true });
          }
          if (error.error.error === 'emailExiste') {
            this.registroFormulario.get('email')?.setErrors({ emailExiste: true });
          }
          if (error.error.error === 'dniExiste') {
            this.registroFormulario.get('dni')?.setErrors({ dniExiste: true });
          }
          if (error.error.error === 'fechaInvalida') {
            this.registroFormulario.get('fechaNac')?.setErrors({ fechaInvalida: true });
          }
        }
      }
    });
  }

  irHaciaAtras(): void {
    window.history.back();
  }
}
