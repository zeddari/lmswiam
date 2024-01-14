import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IJuzs } from '../juzs.model';
import { JuzsService } from '../service/juzs.service';

@Component({
  standalone: true,
  templateUrl: './juzs-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class JuzsDeleteDialogComponent {
  juzs?: IJuzs;

  constructor(
    protected juzsService: JuzsService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.juzsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
