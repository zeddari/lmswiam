import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAyahs } from '../ayahs.model';
import { AyahsService } from '../service/ayahs.service';

@Component({
  standalone: true,
  templateUrl: './ayahs-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AyahsDeleteDialogComponent {
  ayahs?: IAyahs;

  constructor(
    protected ayahsService: AyahsService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ayahsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
