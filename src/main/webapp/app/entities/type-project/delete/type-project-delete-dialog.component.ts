import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITypeProject } from '../type-project.model';
import { TypeProjectService } from '../service/type-project.service';

@Component({
  standalone: true,
  templateUrl: './type-project-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TypeProjectDeleteDialogComponent {
  typeProject?: ITypeProject;

  constructor(
    protected typeProjectService: TypeProjectService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeProjectService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
