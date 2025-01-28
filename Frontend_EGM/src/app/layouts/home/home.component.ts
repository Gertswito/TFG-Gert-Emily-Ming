import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterOutlet } from '@angular/router';
import { AuthService } from '../../auth/auth.service';
import { ICliente } from '../../entities/cliente/cliente.model';

@Component({
  standalone: true,
  selector: 'home',
  imports: [FormsModule, ReactiveFormsModule, CommonModule, RouterOutlet, RouterLink],
  templateUrl: './home.component.html',
})
export class HomeComponent implements OnInit {
  isLoggedIn: boolean = false;
  usuario: string | null = '';
  rol: string | null = '';

  protected authService = inject(AuthService);

  ngOnInit(): void {
    if (this.authService.isAuthenticated()) {
      this.authService.setLoggedIn(true); 
    } else {
      this.authService.setLoggedIn(false); 
    }

    this.authService.loggedIn$.subscribe((status) => {
      this.isLoggedIn = status;

      if (status) {
        this.usuario = this.authService.getUsuario(); 
        this.rol = this.authService.getRol();
      } else {
        this.usuario = null;
        this.rol = null;
      }
    });
  }
}