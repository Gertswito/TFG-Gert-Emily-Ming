import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ILineasVenta } from './lineasVenta.model';
import { LineasVentaService } from './lineasVenta.service';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'lineas-venta',
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './lineasVenta.component.html',
  styleUrls: ['../entities.css'],
})
export class LineasVentaComponent implements OnInit {
  lineasVentaList: ILineasVenta[] = [];
  editarLineaVenta(lineaVenta: any) {
    // Lógica para editar la línea de venta
    console.log('Editar Línea de Venta:', lineaVenta);
  }

  eliminarLineaVenta(lineaVenta: any) {
    // Lógica para eliminar la línea de venta
    console.log('Eliminar Línea de Venta:', lineaVenta);
  }

  private lineasVentaService = inject(LineasVentaService);

  ngOnInit(): void {
    this.lineasVentaService.getAllLineasVentas().subscribe((res) => {
      this.lineasVentaList = res || [];
    });
  }
}
