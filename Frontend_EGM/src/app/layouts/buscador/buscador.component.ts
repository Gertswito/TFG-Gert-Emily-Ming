import { CommonModule } from '@angular/common';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Subject, debounceTime, distinctUntilChanged } from 'rxjs';

@Component({
    standalone: true,
    selector: 'buscador',
    templateUrl: './buscador.component.html',
    imports: [FormsModule, CommonModule],
    styleUrls: ['./buscador.component.css']
})
export class BuscadorComponent implements OnInit {
    texto: string = '';
    private texto$ = new Subject<string>(); 
  
    @Output() textoBuscado = new EventEmitter<string>();
  
    ngOnInit(): void {
      this.texto$
        .pipe(debounceTime(500), distinctUntilChanged()).subscribe((texto) => {
          this.textoBuscado.emit(texto);
        });
    }
  
    onInputChange(): void {
      this.texto$.next(this.texto); 
    }
  
    limpiar(): void {
      this.texto = '';
      this.textoBuscado.emit(''); 
    }
  }