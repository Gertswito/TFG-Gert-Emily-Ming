import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { faCheckCircle } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@Component({
  standalone: true,
  selector: 'compra',
  imports: [FormsModule, ReactiveFormsModule, CommonModule, RouterLink, FontAwesomeModule],
  templateUrl: './compra-exito.component.html',
  styleUrls: ['./compra-exito.component.css'],
})
export class CompraExitoComponent implements OnInit {
    faCheckCircle = faCheckCircle;
    
    protected router = inject(Router);
    
    ngOnInit(): void {
        // setTimeout(() => {
        //   this.router.navigate(['/home']);
        // }, 2000);
    }
}