import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISurahs } from '../surahs.model';
import { SurahsService } from '../service/surahs.service';

@Component({
  standalone: true,
  templateUrl: './surahs-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SurahsDeleteDialogComponent {
  surahs?: ISurahs;

  constructor(
    protected surahsService: SurahsService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.surahsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
