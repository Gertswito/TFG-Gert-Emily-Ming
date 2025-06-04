import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterOutlet } from '@angular/router';
import { IProducto } from '../../entities/producto/producto.model';
import { ProductoService } from '../../entities/producto/producto.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ProductoStockComponent } from '../../entities/producto/producto-stock/producto-stock.component';
import { STOCK_UPDATED_EVENT } from '../../config/navigation.constants';
import { filter, tap } from 'rxjs';

@Component({
  standalone: true,
  selector: 'admin-home',
  imports: [FormsModule, ReactiveFormsModule, CommonModule, RouterOutlet, RouterLink, FontAwesomeModule],
  templateUrl: './admin-home.component.html',
  styleUrls: ['./admin-home.component.css']
})
export class AdminHomeComponent implements OnInit {
  faPlus = faPlus;

  productosConStockBajo: IProducto[] = [];
  errorMessage = '';

  private productoService = inject(ProductoService);
  private modalService = inject(NgbModal);  

  ngOnInit(): void {
    window.scrollTo(0, 0);
      this.cargarProductosConStockBajo();
  }

  cargarProductosConStockBajo() {
    this.productoService.getProductosConStockBajo().subscribe(res => {
      this.productosConStockBajo = res || [];
    });
  } 

  openAddStockModal(producto: IProducto) {
    const modalRef = this.modalService.open(ProductoStockComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.producto = producto;
    modalRef.componentInstance.errorSubject.subscribe({
      next: (errorMessage: string) => {
        this.errorMessage = errorMessage;  
      }
    });
    modalRef.closed
      .pipe(
        filter(reason => reason === STOCK_UPDATED_EVENT),
        tap(() => this.cargarProductosConStockBajo()),
      )
      .subscribe();
  }
}