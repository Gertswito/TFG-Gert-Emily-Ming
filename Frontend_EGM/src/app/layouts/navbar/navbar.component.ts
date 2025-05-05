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
import { ClienteService } from '../../entities/cliente/cliente.service';
import { ICliente } from '../../entities/cliente/cliente.model';
import { CarritoService } from '../carrito/carrito.service';

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
  usuario: ICliente | null = null;
  carritoCount = 0;

  protected authService = inject(AuthService);
  protected categoriaService = inject(CategoriaService);
  protected clienteService = inject(ClienteService);
  private carritoService = inject(CarritoService);
  protected router = inject(Router);

  ngOnInit(): void {
    this.authService.loggedIn$.subscribe((status) => {
      this.isLoggedIn = status;

      if (status) {
        this.rol = this.authService.getRol();
        const nombreUsuario = this.authService.getUsuario();
        if (nombreUsuario != null) {
          this.clienteService.getCliente(nombreUsuario).subscribe((res) => {
            this.usuario = res || null;
            if (this.usuario) {
              this.carritoService.actualizarCarritoCount(this.usuario.id!);
              this.suscribirseAlCarrito();
            }
          });
        }
      } else {
        this.rol = null;
        this.usuario = null;
        this.carritoCount = 0;
        this.carritoService.actualizarCarritoCount(0);
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

  actualizarCarritoCount(): void {
    const carritoKey = `carrito_${this.usuario?.id}_lineas`;
    const lineas = JSON.parse(localStorage.getItem(carritoKey) || '[]');
    this.carritoCount = Array.isArray(lineas) ? lineas.length : 0;
  }

  suscribirseAlCarrito(): void {
    this.carritoService.carritoCount$.subscribe(count => {
      this.carritoCount = count;
    });
  }
}
