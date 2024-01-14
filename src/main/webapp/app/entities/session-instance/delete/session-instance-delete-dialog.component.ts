import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISessionInstance } from '../session-instance.model';
import { SessionInstanceService } from '../service/session-instance.service';

@Component({
  standalone: true,
  templateUrl: './session-instance-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SessionInstanceDeleteDialogComponent {
  sessionInstance?: ISessionInstance;

  constructor(
    protected sessionInstanceService: SessionInstanceService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sessionInstanceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
