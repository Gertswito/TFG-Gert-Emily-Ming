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
  loginFormulario!: FormGroup;

  protected route = inject(ActivatedRoute); 
  protected router = inject(Router);
  protected clienteService = inject(ClienteService);

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.estaRegistrado = params['registrado'];
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
        // this.router.navigate(['/']);
        console.log('Login correcto');
      },
      error: (error) => {
        if (error.error && error.error.error) {
          if (error.error.error === 'usuarioNoExiste') {
            this.loginFormulario.get('usuario')?.setErrors({ 'usuarioNoExiste': true });
          } else if (error.error.error === 'contrasenhaIncorrecta') {
            this.loginFormulario.get('contrasenha')?.setErrors({ 'contrasenhaIncorrecta': true });
          }
        }
      }
    });
  }
}