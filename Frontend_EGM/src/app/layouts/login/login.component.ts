import { Component, inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink, RouterOutlet } from '@angular/router';
import { ClienteService } from '../../entities/cliente/cliente.service';

@Component({
  standalone: true,
  selector: 'login',
  imports: [FormsModule, ReactiveFormsModule, CommonModule, RouterOutlet, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  estaRegistrado = false;
  necesitaLogin = false;
  loginFormulario!: FormGroup;

  protected route = inject(ActivatedRoute); 
  protected router = inject(Router);
  protected clienteService = inject(ClienteService);

  ngOnInit(): void {
    window.scrollTo(0, 0);
    this.route.queryParams.subscribe(params => {
      this.estaRegistrado = params['registrado'];
      this.necesitaLogin = params['necesitaLogin'];
    });
    this.loginFormulario = new FormGroup({
      usuario: new FormControl(null, [Validators.required]),
      contrasenha: new FormControl(null, [Validators.required]),
    });
  }

  comprobarLogin(): void {
    if (this.loginFormulario.invalid) {
      this.loginFormulario.markAllAsTouched();
      return;
    }

    this.clienteService.login(this.loginFormulario.value).subscribe({
      next: (response) => {
        if (response.body && response.body.token) {
          localStorage.setItem('jwtToken', response.body.token);  
          this.router.navigate(['/']);  
        }
      },
      error: (error) => {
        if (error.error && error.error.error) {
          if (error.status === 400) {
            this.loginFormulario.get('usuario')?.setErrors({ 'usuarioNoExiste': true });
          } else if (error.status === 401) {
            this.loginFormulario.get('contrasenha')?.setErrors({ 'contrasenhaIncorrecta': true });
          }
        }
      }
    });
  }

  cerrarAlerta(tipo: 'registrado' | 'carrito'): void {
    if (tipo === 'registrado') {
      this.estaRegistrado = false;
    } else if (tipo === 'carrito') {
      this.necesitaLogin = false;
    }
  }
}