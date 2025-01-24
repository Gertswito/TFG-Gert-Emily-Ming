import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IDireccion } from './direccion.model';
import { DireccionService } from './direccion.service';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'direccion',
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './direccion.component.html',
  styleUrls: ['../entities.css'],
})
export class DireccionComponent implements OnInit {
  direccionList: IDireccion[] = [];

  private direccionService = inject(DireccionService);

  ngOnInit(): void {
    this.direccionService.getAllDirecciones().subscribe((res) => {
      this.direccionList = res || [];
    });
  }
}