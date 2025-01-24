import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IVenta } from './venta.model';
import { VentaService } from './venta.service';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'venta',
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './venta.component.html',
  styleUrls: ['../entities.css'],
})
export class VentaComponent implements OnInit {
  ventaList: IVenta[] = [];

  private ventaService = inject(VentaService);

  ngOnInit(): void {
    this.ventaService.getAllVentas().subscribe((res) => {
      this.ventaList = res || [];
    });
  }
}
