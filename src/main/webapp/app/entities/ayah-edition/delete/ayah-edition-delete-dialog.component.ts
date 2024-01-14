import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAyahEdition } from '../ayah-edition.model';
import { AyahEditionService } from '../service/ayah-edition.service';

@Component({
  standalone: true,
  templateUrl: './ayah-edition-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AyahEditionDeleteDialogComponent {
  ayahEdition?: IAyahEdition;

  constructor(
    protected ayahEditionService: AyahEditionService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ayahEditionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
