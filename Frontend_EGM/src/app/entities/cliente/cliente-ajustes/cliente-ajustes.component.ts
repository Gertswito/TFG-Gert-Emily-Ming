import { Component, OnInit } from "@angular/core";
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";
import { CommonModule } from "@angular/common";
import { ClienteService } from "../../cliente/cliente.service";
import { AuthService } from "../../../auth/auth.service";
import { ICliente } from "../../cliente/cliente.model";
import { DireccionAjusteComponent } from "../../direccion/direccion-ajuste/direccion-ajuste.component";
import { PagoAjusteComponent } from "../../pago/pago-ajuste/pago-ajuste.component";

// Add the password validators function at the top of the file, before the @Component decorator
function passwordMatchValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const newPassword = control.get("newPassword")
    const confirmPassword = control.get("confirmPassword")

    if (newPassword?.value && confirmPassword?.value && newPassword.value !== confirmPassword.value) {
      return { passwordMismatch: true }
    }
    return null
  }
}

@Component({
  standalone: true,
  selector: "app-cliente-ajustes",
  imports: [FormsModule, ReactiveFormsModule, CommonModule, DireccionAjusteComponent, PagoAjusteComponent],
  templateUrl: "./cliente-ajustes.component.html",
  styleUrls: ["./cliente-ajustes.component.css"],
})
export class ClienteAjustesComponent implements OnInit {
  clienteForm: FormGroup
  cliente: ICliente | null = null
  loading = false
  success = false
  error = false
  errorMessage = ""
  usuarioActual: string | null = null

  constructor(
    private fb: FormBuilder,
    private clienteService: ClienteService,
    private authService: AuthService,
  ) {
    this.clienteForm = this.createForm()
  }

  ngOnInit(): void {
    // Obtener el usuario del token
    this.usuarioActual = this.authService.getUsuario()
    if (this.usuarioActual) {
      this.loadClienteData(this.usuarioActual)
    } else {
      this.error = true
      this.errorMessage = "No se pudo obtener el usuario del token"
    }
  }

  createForm(): FormGroup {
    return this.fb.group({
      id: [null],
      rol: [null],
      dni: ["", [Validators.required, Validators.pattern(/^([0-9]{8}[A-Za-z]|[XYZxyz][0-9]{7}[A-Za-z])$/)]],
      nombre: ["", Validators.required],
      apellidos: ["", Validators.required],
      usuario: [{ value: "", disabled: true }],
      email: ["", [Validators.required, Validators.email]],
      telefono: ["", Validators.pattern(/^[0-9]{9}$/)],
      fechaNac: [""],
      currentPassword: [""],
      passwordGroup: this.fb.group(
        {
          newPassword: ["", [Validators.minLength(6)]],
          confirmPassword: [""],
        },
        { validators: passwordMatchValidator() },
      ),
    })
  }

  loadClienteData(usuario: string): void {
    this.loading = true

    if (this.authService.isAuthenticated()) {
      this.clienteService.getCliente(usuario).subscribe({
        next: (cliente) => {
          this.cliente = cliente
          this.updateForm(cliente)
          this.loading = false
        },
        error: (error) => {
          console.error("Error al cargar los datos del cliente", error)
          this.loading = false
          this.error = true
          this.errorMessage = "Error al cargar los datos del cliente"
        },
      })
    } else {
      this.loading = false
      this.error = true
      this.errorMessage = "La sesión ha expirado"
    }
  }

  updateForm(cliente: ICliente): void {
    // Actualizar los campos del formulario
    this.clienteForm.patchValue({
      id: cliente.id,
      rol: cliente.rol,
      dni: cliente.dni,
      nombre: cliente.nombre,
      apellidos: cliente.apellidos,
      usuario: cliente.usuario, // El campo está deshabilitado pero se muestra el valor
      email: cliente.email,
      telefono: cliente.telefono,
      fechaNac: cliente.fechaNac ? this.formatDate(cliente.fechaNac) : null,
    })
  }

  onSubmit(): void {
    if (this.clienteForm.invalid) {
      this.markFormGroupTouched(this.clienteForm) // Marca los campos para mostrar errores
      return
    }

    this.loading = true
    this.success = false
    this.error = false

    const clienteData = this.prepareClienteData()

    this.clienteService.updateCliente(clienteData).subscribe({
      next: (response) => {
        this.loading = false
        this.success = true
        // Desplazar al inicio de la página para mostrar el mensaje de éxito
        window.scrollTo({ top: 0, behavior: "smooth" })
        this.clienteForm.get("passwordGroup")?.reset() // Limpiar los campos de contraseña después del cambio
      },
      error: (err) => {
        this.loading = false
        this.error = true
        this.errorMessage = err.error?.message || "Error al actualizar los datos."
        // Desplazar al inicio de la página para mostrar el mensaje de error
        window.scrollTo({ top: 0, behavior: "smooth" })
      },
    })
  }

  prepareClienteData(): ICliente {
    if (!this.cliente) {
      throw new Error("No hay datos del cliente cargados")
    }

    // Crear un nuevo objeto manteniendo las claves de ICliente
    const clienteData: ICliente = {
      ...this.cliente, // Mantiene las claves originales
      dni: this.clienteForm.value.dni,
      nombre: this.clienteForm.value.nombre,
      apellidos: this.clienteForm.value.apellidos,
      email: this.clienteForm.value.email,
      telefono: this.clienteForm.value.telefono,
      fechaNac: this.clienteForm.value.fechaNac,
      contrasenha: this.clienteForm.value.passwordGroup?.newPassword
        ? this.clienteForm.value.passwordGroup.newPassword
        : this.cliente.contrasenha,
    }

    return clienteData
  }

  // Método auxiliar para formatear fecha para mostrar
  formatDate(date: Date | null): string {
    if (!date) return ""
    const d = new Date(date)
    return d.toISOString().split("T")[0]
  }

  // Método para reiniciar el formulario con los datos originales
  resetForm(): void {
    if (this.usuarioActual) {
      this.loadClienteData(this.usuarioActual)
    }
    this.clienteForm.get("currentPassword")?.reset()
    this.clienteForm.get("passwordGroup")?.reset()
    this.success = false
    this.error = false
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
}

