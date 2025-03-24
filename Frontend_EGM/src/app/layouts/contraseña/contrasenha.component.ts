import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CorreoService } from '../../services/correo.service';

@Component({
  standalone: true,
  selector: 'app-contrasenha',
  templateUrl: './contrasenha.component.html',
  imports: [ReactiveFormsModule, CommonModule, RouterModule] // Incluye ReactiveFormsModule aquí
})
export class ContrasenhaComponent implements OnInit {
  recuperarFormulario: FormGroup;

  constructor(private fb: FormBuilder, private correoService: CorreoService) {
    this.recuperarFormulario = this.fb.group({
      correo: ['', [Validators.required, Validators.email]]
    });
  }

  ngOnInit(): void {}

  enviarCorreo(): void {
    if (this.recuperarFormulario.valid) {
      const correo = this.recuperarFormulario.get('correo')?.value;
      this.correoService.enviarCorreo(correo).subscribe(
        (response) => {
          console.log('Correo enviado con éxito:', response);
          alert('Se ha enviado un correo para recuperar tu contraseña.');
        },
        (error) => {
          console.error('Error al enviar el correo:', error);
          alert('Hubo un error al enviar el correo. Por favor, inténtalo de nuevo.');
        }
      );
    }
  }
}
