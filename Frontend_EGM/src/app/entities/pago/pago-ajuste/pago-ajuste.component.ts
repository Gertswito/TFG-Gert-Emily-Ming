import { Component, Input, OnInit } from "@angular/core";
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators, FormArray } from "@angular/forms";
import { CommonModule } from "@angular/common";
import { ClienteService } from "../../cliente/cliente.service";
import { PagoService } from "../../pago/pago.service";
import { IPago } from "../../pago/pago.model";
import { ICliente } from "../../cliente/cliente.model";
import { IVenta } from "../../venta/venta.model";
import { RouterLink } from "@angular/router";

@Component({
  standalone: true,
  selector: "app-pago-ajuste",
  imports: [FormsModule, ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: "./pago-ajuste.component.html",
  styleUrls: ["./pago-ajuste.component.css"],
})
export class PagoAjusteComponent implements OnInit {
  @Input() usuario: string | null = null
  @Input() esUnaCompra: boolean = false

  pagosForm: FormGroup
  loading = false
  success = false
  error = false
  selectedPago = false
  errorMessage = ""
  pagosOriginales: IPago[] = []
  user: ICliente | null = null
  venta: IVenta | null = null
  pagoSeleccionado: IPago | null = null
  indexPagoSeleccionado: number | null = null

  constructor(
    private fb: FormBuilder,
    private clienteService: ClienteService,
    private pagoService: PagoService,
  ) {
    this.pagosForm = this.createForm()
  }

  ngOnInit(): void {
    if (this.usuario) {
      this.loadPagos(this.usuario)
      this.clienteService.getCliente(this.usuario).subscribe((res) => {
        this.user = res || null
      });
    } else {
      this.error = true
      this.errorMessage = "No se pudo obtener el usuario"
    }
  }

  createForm(): FormGroup {
    return this.fb.group({
      pagos: this.fb.array([]),
    })
  }

  get pagosFormArray(): FormArray {
    return this.pagosForm.get("pagos") as FormArray
  }

  createPagoFormGroup(): FormGroup {
    return this.fb.group({
      id: [null],
      numeroTarjeta: [null, [Validators.required]],
      fechaCaducidad: [null, Validators.required],
      cvv: [null, [Validators.required, Validators.pattern("[0-9]{3}")]],
      cliente: [null],
    })
  }

  addPago(): void {
    this.pagosFormArray.push(this.createPagoFormGroup())
  }

  removePago(index: number): void {
    const pago = this.pagosFormArray.at(index).value
    if (pago.id) {
      // Si el pago ya existe en la base de datos, lo eliminamos
      this.loading = true
      this.pagoService.disablePago(pago.id).subscribe({
        next: () => {
          this.pagosFormArray.removeAt(index)
          this.loading = false
          this.success = true
          setTimeout(() => (this.success = false), 3000)
        },
        error: (err) => {
          this.loading = false
          this.error = true
          this.errorMessage = err.error?.message || "Error al eliminar el método de pago."
          setTimeout(() => (this.error = false), 3000)
        },
      })
    } else {
      // Si es un pago nuevo que no existe en la base de datos, simplemente lo quitamos del formulario
      this.pagosFormArray.removeAt(index)
    }
  }

  loadPagos(usuario: string): void {
    this.loading = true;
    this.pagoService.getPagosByCliente(usuario).subscribe({
      next: (pagos) => {
        // Guardar los pagos originales para comparar después
        this.pagosOriginales = [...pagos];
        // Limpiar arrays de formularios existentes
        while (this.pagosFormArray.length) {
          this.pagosFormArray.removeAt(0);
        }
  
        // Filtrar pagos activos
        const pagosActivos = pagos.filter((pago) => pago.activo);  // Filtra solo los pagos activos
  
        // Añadir pagos activos
        if (pagosActivos && pagosActivos.length > 0) {
          pagosActivos.forEach((pago) => {
            const pagoForm = this.createPagoFormGroup();
            pagoForm.patchValue({
              ...pago,
              fechaCaducidad: pago.fechaCaducidad ? this.formatDateToMonthInput(pago.fechaCaducidad) : null,
            });
            this.pagosFormArray.push(pagoForm);
            // Formatear el número de tarjeta para visualización
            if (pago.numeroTarjeta) {
              try {
                const formattedCardNumber = this.formatCardNumberForDisplay(pago.numeroTarjeta);
                pagoForm.get("numeroTarjeta")?.setValue(formattedCardNumber);
              } catch (error) {
                console.error("Error al formatear número de tarjeta:", error);
                // Si hay error, dejamos el número como está
              }
            }
          });
        }
        this.loading = false;
      },
      error: (error) => {
        console.error("Error al cargar los métodos de pago", error);
        this.loading = false;
        this.error = true;
        this.errorMessage = "Error al cargar los métodos de pago";
      },
    });
  }

  onSubmit(): void {
    if (this.pagosForm.invalid) {
      this.markFormGroupTouched(this.pagosForm)
      return
    }

    this.loading = true
    this.success = false
    this.error = false

    // Obtener los pagos del formulario y preparar los datos para el backend
    const pagosFormulario = this.pagosForm.value.pagos.map((pago: any) => {
      return {
        ...pago,
        numeroTarjeta: pago.numeroTarjeta ? String(pago.numeroTarjeta).replace(/\s/g, "") : null,
        fechaCaducidad: pago.fechaCaducidad ? this.formatMonthInputToDate(pago.fechaCaducidad) : null,
      }
    })

    // Procesar cada pago de forma secuencial para evitar problemas de concurrencia
    this.procesarPagos(pagosFormulario, 0)
  }

  procesarPagos(pagos: IPago[], index: number): void {
    // Si hemos procesado todos los pagos, terminamos
    if (index >= pagos.length) {
      this.loading = false
      this.success = true
      // Recargar los pagos para tener los IDs actualizados
      if (this.usuario) {
        this.loadPagos(this.usuario)
      }
      return
    }

    const pago = pagos[index]

    // Función para procesar el siguiente pago
    const procesarSiguiente = () => {
      this.procesarPagos(pagos, index + 1)
    }

    if (pago.id) {
      // Si tiene ID, es una actualización
      this.pagoService.updatePago(pago.id, pago).subscribe({
        next: () => procesarSiguiente(),
        error: (err) => {
          this.loading = false
          this.error = true
          this.errorMessage = err.error?.message || "Error al actualizar el método de pago."
        },
      })
    } else {
      // Si no tiene ID, es un nuevo pago
      // Asegurarse de que tenga la referencia al cliente
      if (this.usuario) {
        this.pagoService.createPago(this.usuario, pago).subscribe({
          next: () => procesarSiguiente(),
          error: (err) => {
            this.loading = false
            this.error = true
            this.errorMessage = err.error?.message || "Error al crear el método de pago."
          },
        })
      } else {
        procesarSiguiente()
      }
    }
  }

  resetForm(): void {
    if (this.usuario) {
      this.loadPagos(this.usuario)
    }
    this.success = false
    this.error = false
  }

  // Método para convertir una fecha del backend (LocalDate) a formato YYYY-MM para input type="month"
  formatDateToMonthInput(date: any): string {
    if (!date) return ""

    // Si la fecha viene como string (formato ISO), convertirla a Date
    const d = typeof date === "string" ? new Date(date) : new Date(date)

    // Formato YYYY-MM para input type="month"
    return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, "0")}`
  }

  // Método para convertir el valor del input type="month" (YYYY-MM) a una fecha completa para el backend (LocalDate)
  formatMonthInputToDate(monthInput: string): string {
    if (!monthInput) return ""

    // Extraer año y mes del formato YYYY-MM
    const [year, month] = monthInput.split("-")

    // Crear una fecha con el último día del mes (para asegurar que la tarjeta es válida todo el mes)
    const lastDay = new Date(Number.parseInt(year), Number.parseInt(month), 0).getDate()

    // Formato YYYY-MM-DD para LocalDate en el backend
    return `${year}-${month}-${String(lastDay).padStart(2, "0")}`
  }

  // Método para formatear el número de tarjeta mientras el usuario escribe
  formatCardNumber(event: any, index: number): void {
    const input = event.target
    let value = input.value.replace(/\s/g, "") // Eliminar espacios existentes
    value = value.replace(/[^0-9]/g, "") // Permitir solo números

    // Formatear con espacios cada 4 dígitos
    let formattedValue = ""
    for (let i = 0; i < value.length; i++) {
      if (i > 0 && i % 4 === 0) {
        formattedValue += " "
      }
      formattedValue += value[i]
    }

    // Actualizar el valor en el input
    input.value = formattedValue

    // Actualizar el valor en el formulario (sin espacios para validación)
    const control = this.pagosFormArray.at(index).get("numeroTarjeta")
    control?.setValue(formattedValue)

    // Validar el número de tarjeta (16 dígitos sin espacios)
    const digitsOnly = value.replace(/\s/g, "")
    if (digitsOnly.length === 16) {
      control?.setErrors(null)
    } else {
      control?.setErrors({ pattern: true })
    }
    control?.markAsTouched();
  }

  // Método para formatear el número de tarjeta para visualización
  formatCardNumberForDisplay(cardNumber: any): string {
    if (!cardNumber) return ""

    // Asegurarse de que cardNumber sea una cadena de texto
    const cardNumberStr = String(cardNumber)

    // Eliminar espacios existentes
    const digitsOnly = cardNumberStr.replace(/\s/g, "")

    // Formatear con espacios cada 4 dígitos
    let formattedValue = ""
    for (let i = 0; i < digitsOnly.length; i++) {
      if (i > 0 && i % 4 === 0) {
        formattedValue += " "
      }
      formattedValue += digitsOnly[i]
    }

    return formattedValue
  }

  // Add a helper method to mark all form controls as touched
  markFormGroupTouched(formGroup: FormGroup) {
    Object.values(formGroup.controls).forEach((control) => {
      control.markAsTouched()

      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control)
      }
    })
  }

  choosePago(index: number): void {
    const pagoFormGroup = this.pagosFormArray.at(index);
    if (!pagoFormGroup) {
      this.error = true;
      this.errorMessage = "No se encontró el pago seleccionada.";
      return;
    }
  
    const pago = pagoFormGroup.value as IPago;
  
    if (this.user) {
      const carritoKey = `carrito_${this.user.id}`;

      this.venta = JSON.parse(localStorage.getItem(carritoKey) ?? 'null');
      this.venta!.pago = pago;
      localStorage.setItem(carritoKey, JSON.stringify(this.venta));

      this.pagoSeleccionado = pago;
      this.indexPagoSeleccionado = index;
      this.selectedPago = true;
    }
  }

  removeSelectedPago(): void {
    if (this.user) {
      const carritoKey = `carrito_${this.user.id}`;

      this.venta = JSON.parse(localStorage.getItem(carritoKey) ?? 'null');
      this.venta!.pago = null;
      localStorage.setItem(carritoKey, JSON.stringify(this.venta));

      this.pagoSeleccionado = null;
      this.indexPagoSeleccionado = null;
      this.selectedPago = false;
    }
  }
}

