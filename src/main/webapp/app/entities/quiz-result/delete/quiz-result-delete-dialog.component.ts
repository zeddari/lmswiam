import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IQuizResult } from '../quiz-result.model';
import { QuizResultService } from '../service/quiz-result.service';

@Component({
  standalone: true,
  templateUrl: './quiz-result-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class QuizResultDeleteDialogComponent {
  quizResult?: IQuizResult;

  constructor(
    protected quizResultService: QuizResultService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.quizResultService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
