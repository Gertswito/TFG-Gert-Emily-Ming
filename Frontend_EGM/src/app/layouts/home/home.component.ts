import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { AuthService } from '../../auth/auth.service';
import { ICategoria } from '../../entities/categoria/categoria.model';
import { CategoriaService } from '../../entities/categoria/categoria.service';
import { BuscadorComponent } from '../buscador/buscador.component';

@Component({
  standalone: true,
  selector: 'home',
  imports: [FormsModule, ReactiveFormsModule, CommonModule, RouterOutlet, RouterLink, BuscadorComponent],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  isLoggedIn: boolean = false;
  usuario: string | null = '';
  rol: string | null = '';
  categoriaList: ICategoria[] = [];
  currentSlide = 0;

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

  onBuscar(texto: string): void {
    if (texto) {
      this.categoriaService.getCategoriaConFiltro(texto).subscribe((data) => {
        this.categoriaList = data;
      });
    } else {
      this.categoriaService.getAllCategorias().subscribe((data) => {
        this.categoriaList = data;
      });
    }
  }

  prevSlide() {
    const slide = document.getElementById('carouselSlide') as HTMLElement;
    const totalSlides = slide.children.length;
    this.currentSlide = (this.currentSlide - 1 + totalSlides) % totalSlides;
    slide.style.transform = `translateX(-${this.currentSlide * 100}%)`;
  }

  nextSlide() {
    const slide = document.getElementById('carouselSlide') as HTMLElement;
    const totalSlides = slide.children.length;
    this.currentSlide = (this.currentSlide + 1) % totalSlides;
    slide.style.transform = `translateX(-${this.currentSlide * 100}%)`;
  }
}