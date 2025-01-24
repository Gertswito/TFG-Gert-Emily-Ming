import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ICliente } from './cliente.model';
import { ClienteService } from './cliente.service';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'cliente',
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './cliente.component.html',
  styleUrls: ['../entities.css'],
})
export class ClienteComponent implements OnInit {
  clienteList: ICliente[] = [];

  private clienteService = inject(ClienteService);

  ngOnInit(): void {
    this.clienteService.getAllClientes().subscribe((res) => {
      this.clienteList = res || [];
    });
  }
}