import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISessionCourses } from '../session-courses.model';
import { SessionCoursesService } from '../service/session-courses.service';

@Component({
  standalone: true,
  templateUrl: './session-courses-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SessionCoursesDeleteDialogComponent {
  sessionCourses?: ISessionCourses;

  constructor(
    protected sessionCoursesService: SessionCoursesService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sessionCoursesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
