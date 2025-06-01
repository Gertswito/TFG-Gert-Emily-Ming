import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterOutlet } from '@angular/router';

@Component({
  standalone: true,
  selector: 'error',
  imports: [FormsModule, ReactiveFormsModule, CommonModule, RouterOutlet, RouterLink],
  templateUrl: './error.component.html',
  styleUrls: ['../app.component.css']
})
export class ErrorComponent implements OnInit{
  protected router = inject(Router);

  ngOnInit(): void {
    setTimeout(() => {
      this.router.navigate(['/home']);
    }, 2000);
  }
}