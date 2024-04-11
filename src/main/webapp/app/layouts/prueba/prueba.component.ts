import { Component, Input } from '@angular/core';

@Component({
  selector: 'jhi-prueba',
  standalone: true,
  imports: [],
  templateUrl: './prueba.component.html',
  styleUrl: './prueba.component.scss'
})
export class PruebaComponent {
  @Input() texto?: string;
}
