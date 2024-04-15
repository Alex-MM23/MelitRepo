import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'jhi-prueba',
  standalone: true,
  imports: [],
  templateUrl: './prueba.component.html',
  styleUrl: './prueba.component.scss'
})
export class PruebaComponent {
  @Input() texto?: string;
  @Output() sendText = new EventEmitter<string>();

  enviarTexto(): void {
    this.sendText.emit('Hola desde el componente hijo');
  }
}
