import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IPago } from './pago.model';
import { PagoService } from './pago.service';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'pago',
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './pago.component.html',
  styleUrls: ['../entities.css'],
})
export class PagoComponent implements OnInit {
  pagoList: IPago[] = [];

  private pagoService = inject(PagoService);

  ngOnInit(): void {
    this.pagoService.getAllPagos().subscribe((res) => {
      this.pagoList = res || [];
    });
  }
}