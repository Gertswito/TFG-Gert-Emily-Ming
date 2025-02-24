import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'cliente',
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './cliente-ajustes.component.html',
  styleUrls: ['../../entities.css'],
})
export class ClienteAjustesComponent {

}