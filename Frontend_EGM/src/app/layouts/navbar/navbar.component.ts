import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faHome, faThList, faUser, faShoppingCart, faSignOut, faCog } from '@fortawesome/free-solid-svg-icons'; 
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { AuthService } from '../../auth/auth.service';
import { ICategoria } from '../../entities/categoria/categoria.model';
import { CategoriaService } from '../../entities/categoria/categoria.service';

@Component({
  standalone: true,
  selector: 'navbar',
  imports: [FormsModule, ReactiveFormsModule, CommonModule, MatIconModule, FontAwesomeModule, NgbDropdownModule, RouterLink, RouterOutlet],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavBarComponent implements OnInit {
  faHome = faHome;
  faThList = faThList;
  faUser = faUser;
  faSignOut = faSignOut;
  faCog = faCog;
  faShoppingCart = faShoppingCart;
  isLoggedIn: boolean = false;
  categorias: ICategoria[] = [];
  rol: string | null = '';

  protected authService = inject(AuthService);
  protected categoriaService = inject(CategoriaService);
  protected router = inject(Router);

  ngOnInit(): void {
    this.authService.loggedIn$.subscribe((status) => {
      this.isLoggedIn = status;

      if (status) {
        this.rol = this.authService.getRol();
      } else {
        this.rol = null;
      }
    });

    this.categoriaService.getAllCategorias().subscribe((res) => {
      this.categorias = res || [];
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/']);  
  }
}
