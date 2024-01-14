import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { INationality } from '../nationality.model';
import { NationalityService } from '../service/nationality.service';

@Component({
  standalone: true,
  templateUrl: './nationality-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class NationalityDeleteDialogComponent {
  nationality?: INationality;

  constructor(
    protected nationalityService: NationalityService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.nationalityService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
