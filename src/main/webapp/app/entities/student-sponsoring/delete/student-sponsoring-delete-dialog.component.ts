import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IStudentSponsoring } from '../student-sponsoring.model';
import { StudentSponsoringService } from '../service/student-sponsoring.service';

@Component({
  standalone: true,
  templateUrl: './student-sponsoring-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class StudentSponsoringDeleteDialogComponent {
  studentSponsoring?: IStudentSponsoring;

  constructor(
    protected studentSponsoringService: StudentSponsoringService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.studentSponsoringService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
