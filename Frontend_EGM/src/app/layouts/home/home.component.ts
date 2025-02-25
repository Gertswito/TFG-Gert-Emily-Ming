import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { AuthService } from '../../auth/auth.service';
import { ICategoria } from '../../entities/categoria/categoria.model';
import { CategoriaService } from '../../entities/categoria/categoria.service';

@Component({
  standalone: true,
  selector: 'home',
  imports: [FormsModule, ReactiveFormsModule, CommonModule, RouterOutlet, RouterLink],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  isLoggedIn: boolean = false;
  usuario: string | null = '';
  rol: string | null = '';
  categoriaList: ICategoria[] = [];

  protected authService = inject(AuthService);
  protected router = inject(Router);
  private categoriaService = inject(CategoriaService);

  ngOnInit(): void {
    this.categoriaService.getAllCategorias().subscribe((res) => {
      this.categoriaList = res || [];
    });

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
        if (this.rol == 'ADMIN') {
          this.router.navigate(['/admin-home']);
          return;  
        }
      } else {
        this.usuario = null;
        this.rol = null;
      }
    });
  }
}