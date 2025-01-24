import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faHome, faThList, faUser, faShoppingCart } from '@fortawesome/free-solid-svg-icons'; 
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';

@Component({
  standalone: true,
  selector: 'navbar',
  imports: [FormsModule, ReactiveFormsModule, CommonModule, MatIconModule, FontAwesomeModule, NgbDropdownModule, RouterLink, RouterOutlet],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavBarComponent {
  faHome = faHome;
  faThList = faThList;
  faUser = faUser;
  faShoppingCart = faShoppingCart;
}
