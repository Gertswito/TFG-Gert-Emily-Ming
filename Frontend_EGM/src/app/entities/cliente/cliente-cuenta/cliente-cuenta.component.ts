import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../auth/auth.service';
import { ICliente } from '../cliente.model';
import { ClienteService } from '../cliente.service';

@Component({
  standalone: true,
  selector: 'cliente',
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './cliente-cuenta.component.html',
  styleUrls: ['../../entities.css'],
})
export class ClienteCuentaComponent implements OnInit {
  isLoggedIn: boolean = false;
  cliente: ICliente | null = null;
  usuario: string | null = null;
  usuarioEncontrado = false;

  protected authService = inject(AuthService);
  protected clienteSevice = inject(ClienteService);

  ngOnInit(): void {
    this.authService.loggedIn$.subscribe({
      next: (status) => {
        this.isLoggedIn = status;
        if (status) {
          this.usuario = this.authService.getUsuario();
        } else {
          this.usuario = null;
        }

        if (this.usuario !== null) {
          this.clienteSevice.getCliente(this.usuario).subscribe({
            next: (res) => {
              this.cliente = res;
              this.usuarioEncontrado = true;
            },
            error: () => {
              this.usuarioEncontrado = false;
            },
          });
        }
      },
      error: (err) => {
        this.usuarioEncontrado = false;
      },
    });
  }
}
