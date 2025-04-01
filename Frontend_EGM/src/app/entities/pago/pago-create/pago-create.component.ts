import { Component, inject, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { PagoService } from '../pago.service';
import { ClienteService } from '../../cliente/cliente.service';
import { ICliente } from '../../cliente/cliente.model';
import { IPago } from '../pago.model';

@Component({
  standalone: true,
  templateUrl: './pago-create.component.html',
  styleUrls: ['../../entities.css'],
  imports: [FormsModule, ReactiveFormsModule, CommonModule, RouterOutlet, RouterLink,],
})
export class PagoCreateComponent implements OnInit {
    crearPagoFormulario!: FormGroup;
    clientesCollection: ICliente[] = [];

    protected router = inject(Router);
    protected pagoService = inject(PagoService);
    protected clienteService = inject(ClienteService);

    ngOnInit(): void {
        this.loadClientes();
        this.crearPagoFormulario = new FormGroup({
            numeroTarjeta: new FormControl(null, [Validators.required]),
            fechaCaducidad: new FormControl(null, [Validators.required]),
            cvv: new FormControl(null, [Validators.required, Validators.pattern("[0-9]{3}")]),
            activo: new FormControl(true, [Validators.required]),
            cliente: new FormControl(null, [Validators.required]),
        });
    }

    loadClientes() {
        this.clienteService.getAllClientes().subscribe((res) => {
            this.clientesCollection = res || [];
        });
    }

    formatCardNumber(event: any): void {
        const input = event.target;
        let value = input.value.replace(/\s/g, "").replace(/[^0-9]/g, "");
        let formattedValue = "";
        for (let i = 0; i < value.length; i++) {
        if (i > 0 && i % 4 === 0) {
            formattedValue += " ";
        }
        formattedValue += value[i];
        }

        input.value = formattedValue;
        const control = this.crearPagoFormulario.get("numeroTarjeta");
        control?.setValue(formattedValue);
    
        const digitsOnly = value.replace(/\s/g, "");
        if (digitsOnly.length === 16) {
        control?.setErrors(null);
        } else {
        control?.setErrors({ pattern: true });
        }
        control?.markAsTouched();
    }
    
    formatCardNumberForDisplay(cardNumber: any): string {
        if (!cardNumber) return "";
        const cardNumberStr = String(cardNumber);
        const digitsOnly = cardNumberStr.replace(/\s/g, "");
    
        let formattedValue = "";
        for (let i = 0; i < digitsOnly.length; i++) {
        if (i > 0 && i % 4 === 0) {
            formattedValue += " ";
        }
        formattedValue += digitsOnly[i];
        }
        return formattedValue;
    }

    getLastDayOfMonth(monthString: string): Date {
        const [year, month] = monthString.split("-").map(Number);
        return new Date(year, month, 0); 
    }

    comprobarForm(): void {
        if (this.crearPagoFormulario.invalid) {
            this.crearPagoFormulario.markAllAsTouched();
            return;
        } 
        const monthYear = this.crearPagoFormulario.get('fechaCaducidad')?.value;
        const lastDayOfMonth = this.getLastDayOfMonth(monthYear);
        
        const clienteSeleccionado = this.crearPagoFormulario.get('cliente')?.value as ICliente;
        const pago: IPago = {
            ...this.crearPagoFormulario.value,
            fechaCaducidad: lastDayOfMonth
        } as IPago;
        
        if (pago.numeroTarjeta) {
            pago.numeroTarjeta = pago.numeroTarjeta.replace(/\s/g, "");
        } 
        if (pago.numeroTarjeta) {
            pago.numeroTarjeta = pago.numeroTarjeta.replace(/\s/g, "");
        }
        if (clienteSeleccionado.usuario) {
            this.pagoService.createPago(clienteSeleccionado.usuario, pago).subscribe({
                next: (response) => {
                  this.router.navigate(['/pago'], { queryParams: { creado: 'true' } });
                }
            });
        }
    }

    volver(): void{
        window.history.back();
    }
}