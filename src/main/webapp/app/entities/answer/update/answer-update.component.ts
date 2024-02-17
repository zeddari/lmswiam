import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
import { IQuestion } from 'app/entities/question/question.model';
import { QuestionService } from 'app/entities/question/service/question.service';
import { IUserCustom } from 'app/entities/user-custom/user-custom.model';
import { UserCustomService } from 'app/entities/user-custom/service/user-custom.service';
import { AnswerService } from '../service/answer.service';
import { IAnswer } from '../answer.model';
import { AnswerFormService, AnswerFormGroup } from './answer-form.service';

@Component({
  standalone: true,
  selector: 'jhi-answer-update',
  templateUrl: './answer-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AnswerUpdateComponent implements OnInit {
  isSaving = false;
  answer: IAnswer | null = null;

  sitesSharedCollection: ISite[] = [];
  questionsSharedCollection: IQuestion[] = [];
  userCustomsSharedCollection: IUserCustom[] = [];

  editForm: AnswerFormGroup = this.answerFormService.createAnswerFormGroup();

  constructor(
    protected answerService: AnswerService,
    protected answerFormService: AnswerFormService,
    protected siteService: SiteService,
    protected questionService: QuestionService,
    protected userCustomService: UserCustomService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareSite = (o1: ISite | null, o2: ISite | null): boolean => this.siteService.compareSite(o1, o2);

  compareQuestion = (o1: IQuestion | null, o2: IQuestion | null): boolean => this.questionService.compareQuestion(o1, o2);

  compareUserCustom = (o1: IUserCustom | null, o2: IUserCustom | null): boolean => this.userCustomService.compareUserCustom(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ answer }) => {
      this.answer = answer;
      if (answer) {
        this.updateForm(answer);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const answer = this.answerFormService.getAnswer(this.editForm);
    if (answer.id !== null) {
      this.subscribeToSaveResponse(this.answerService.update(answer));
    } else {
      this.subscribeToSaveResponse(this.answerService.create(answer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnswer>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(answer: IAnswer): void {
    this.answer = answer;
    this.answerFormService.resetForm(this.editForm, answer);

    this.sitesSharedCollection = this.siteService.addSiteToCollectionIfMissing<ISite>(this.sitesSharedCollection, answer.site6);
    this.questionsSharedCollection = this.questionService.addQuestionToCollectionIfMissing<IQuestion>(
      this.questionsSharedCollection,
      answer.question,
    );
    this.userCustomsSharedCollection = this.userCustomService.addUserCustomToCollectionIfMissing<IUserCustom>(
      this.userCustomsSharedCollection,
      answer.userCustom1,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.siteService
      .query()
      .pipe(map((res: HttpResponse<ISite[]>) => res.body ?? []))
      .pipe(map((sites: ISite[]) => this.siteService.addSiteToCollectionIfMissing<ISite>(sites, this.answer?.site6)))
      .subscribe((sites: ISite[]) => (this.sitesSharedCollection = sites));

    this.questionService
      .query()
      .pipe(map((res: HttpResponse<IQuestion[]>) => res.body ?? []))
      .pipe(
        map((questions: IQuestion[]) => this.questionService.addQuestionToCollectionIfMissing<IQuestion>(questions, this.answer?.question)),
      )
      .subscribe((questions: IQuestion[]) => (this.questionsSharedCollection = questions));

    this.userCustomService
      .query()
      .pipe(map((res: HttpResponse<IUserCustom[]>) => res.body ?? []))
      .pipe(
        map((userCustoms: IUserCustom[]) =>
          this.userCustomService.addUserCustomToCollectionIfMissing<IUserCustom>(userCustoms, this.answer?.userCustom1),
        ),
      )
      .subscribe((userCustoms: IUserCustom[]) => (this.userCustomsSharedCollection = userCustoms));
  }
}
