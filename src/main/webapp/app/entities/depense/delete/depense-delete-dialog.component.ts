import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDepense } from '../depense.model';
import { DepenseService } from '../service/depense.service';

@Component({
  standalone: true,
  templateUrl: './depense-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DepenseDeleteDialogComponent {
  depense?: IDepense;

  constructor(
    protected depenseService: DepenseService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.depenseService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
