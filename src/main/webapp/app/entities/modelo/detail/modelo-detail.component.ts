import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IModelo } from '../modelo.model';

@Component({
  standalone: true,
  selector: 'jhi-modelo-detail',
  templateUrl: './modelo-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ModeloDetailComponent {
  @Input() modelo: IModelo | null = null;

  previousState(): void {
    window.history.back();
  }
}
