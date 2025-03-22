import { Component, Input, OnInit } from "@angular/core";
import {
  FormsModule,
  ReactiveFormsModule,
  FormBuilder, // Importación normal
  FormGroup,
  Validators,
  FormArray, // Importación normal
} from "@angular/forms";
import { CommonModule } from "@angular/common";
import { ClienteService } from "../../cliente/cliente.service";
import { DireccionService } from "../../direccion/direccion.service";
import { IDireccion } from "../../direccion/direccion.model";

@Component({
  standalone: true,
  selector: "app-direccion-ajuste",
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: "./direccion-ajuste.component.html",
  styleUrls: ["./direccion-ajuste.component.css"],
})
export class DireccionAjusteComponent implements OnInit {
  @Input() usuario: string | null = null

  direccionesForm: FormGroup
  loading = false
  success = false
  error = false
  errorMessage = ""
  direccionesOriginales: IDireccion[] = []

  constructor(
    private fb: FormBuilder,
    private clienteService: ClienteService,
    private direccionService: DireccionService,
  ) {
    this.direccionesForm = this.createForm()
  }

  ngOnInit(): void {
    if (this.usuario) {
      this.loadDirecciones(this.usuario)
    } else {
      this.error = true
      this.errorMessage = "No se pudo obtener el usuario"
    }
  }

  createForm(): FormGroup {
    return this.fb.group({
      direcciones: this.fb.array([]),
    })
  }

  get direccionesFormArray(): FormArray {
    return this.direccionesForm.get("direcciones") as FormArray
  }

  createDireccionFormGroup(): FormGroup {
    return this.fb.group({
      id: [null],
      direccion: [null, Validators.required],
      codigoPostal: [null, [Validators.required, Validators.pattern("[0-9]{5}")]],
      localidad: [null, Validators.required],
      comunidadAutonoma: [null, Validators.required],
      cliente: [null],
    })
  }

  addDireccion(): void {
    this.direccionesFormArray.push(this.createDireccionFormGroup())
  }

  removeDireccion(index: number): void {
    const direccion = this.direccionesFormArray.at(index).value
    if (direccion.id) {
      // Si la dirección ya existe en la base de datos, la eliminamos
      this.loading = true
      this.direccionService.deleteDireccion(direccion.id).subscribe({
        next: () => {
          this.direccionesFormArray.removeAt(index)
          this.loading = false
          this.success = true
          setTimeout(() => (this.success = false), 3000)
        },
        error: (err) => {
          this.loading = false
          this.error = true
          this.errorMessage = err.error?.message || "Error al eliminar la dirección."
          setTimeout(() => (this.error = false), 3000)
        },
      })
    } else {
      // Si es una dirección nueva que no existe en la base de datos, simplemente la quitamos del formulario
      this.direccionesFormArray.removeAt(index)
    }
  }

  loadDirecciones(usuario: string): void {
    this.loading = true

    this.direccionService.getDireccionesPorCliente(usuario).subscribe({
      next: (direcciones) => {
        // Guardar las direcciones originales para comparar después
        this.direccionesOriginales = [...direcciones]

        // Limpiar arrays de formularios existentes
        while (this.direccionesFormArray.length) {
          this.direccionesFormArray.removeAt(0)
        }

        // Añadir direcciones
        if (direcciones && direcciones.length > 0) {
          direcciones.forEach((direccion) => {
            const direccionForm = this.createDireccionFormGroup()
            direccionForm.patchValue(direccion)
            this.direccionesFormArray.push(direccionForm)
          })
        }
        this.loading = false
      },
      error: (error) => {
        console.error("Error al cargar las direcciones", error)
        this.loading = false
        this.error = true
        this.errorMessage = "Error al cargar las direcciones"
      },
    })
  }

  onSubmit(): void {
    if (this.direccionesForm.invalid) {
      this.markFormGroupTouched(this.direccionesForm)
      return
    }

    this.loading = true
    this.success = false
    this.error = false

    // Obtener las direcciones del formulario
    const direccionesFormulario = [...this.direccionesForm.value.direcciones]

    // Procesar cada dirección de forma secuencial para evitar problemas de concurrencia
    this.procesarDirecciones(direccionesFormulario, 0)
  }

  procesarDirecciones(direcciones: IDireccion[], index: number): void {
    // Si hemos procesado todas las direcciones, terminamos
    if (index >= direcciones.length) {
      this.loading = false
      this.success = true
      // Recargar las direcciones para tener los IDs actualizados
      if (this.usuario) {
        this.loadDirecciones(this.usuario)
      }
      return
    }

    const direccion = direcciones[index]

    // Función para procesar la siguiente dirección
    const procesarSiguiente = () => {
      this.procesarDirecciones(direcciones, index + 1)
    }

    if (direccion.id) {
      // Si tiene ID, es una actualización
      this.direccionService.updateDireccion(direccion.id, direccion).subscribe({
        next: () => procesarSiguiente(),
        error: (err) => {
          this.loading = false
          this.error = true
          this.errorMessage = err.error?.message || "Error al actualizar la dirección."
        },
      })
    } else {
      // Si no tiene ID, es una nueva dirección
      // Asegurarse de que tenga la referencia al cliente
      if (this.usuario) {
        direccion.cliente = { usuario: this.usuario } as any // Simplificado, ajustar según tu modelo
        this.direccionService.createDireccion( this.usuario,direccion).subscribe({
          next: () => procesarSiguiente(),
          error: (err) => {
            this.loading = false
            this.error = true
            this.errorMessage = err.error?.message || "Error al crear la dirección."
          },
        })
      } else {
        procesarSiguiente()
      }
    }
  }

  resetForm(): void {
    if (this.usuario) {
      this.loadDirecciones(this.usuario)
    }
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

