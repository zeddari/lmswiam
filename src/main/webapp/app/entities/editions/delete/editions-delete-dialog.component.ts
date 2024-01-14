import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEditions } from '../editions.model';
import { EditionsService } from '../service/editions.service';

@Component({
  standalone: true,
  templateUrl: './editions-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EditionsDeleteDialogComponent {
  editions?: IEditions;

  constructor(
    protected editionsService: EditionsService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.editionsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
