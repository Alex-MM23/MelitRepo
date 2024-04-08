import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICoche } from '../coche.model';
import { CocheService } from '../service/coche.service';

@Component({
  standalone: true,
  templateUrl: './coche-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CocheDeleteDialogComponent {
  coche?: ICoche;

  protected cocheService = inject(CocheService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cocheService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
