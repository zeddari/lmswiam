import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IHizbs } from '../hizbs.model';
import { HizbsService } from '../service/hizbs.service';

@Component({
  standalone: true,
  templateUrl: './hizbs-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class HizbsDeleteDialogComponent {
  hizbs?: IHizbs;

  constructor(
    protected hizbsService: HizbsService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.hizbsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
