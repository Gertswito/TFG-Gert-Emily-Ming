import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatIconModule } from "@angular/material/icon";
import { RouterLink, RouterOutlet } from "@angular/router";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { NgbDropdownModule } from "@ng-bootstrap/ng-bootstrap";

@Component({
  standalone: true,
  selector: 'footer',
  imports: [FormsModule, ReactiveFormsModule, CommonModule, MatIconModule, FontAwesomeModule, NgbDropdownModule, RouterLink, RouterOutlet],
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent {}