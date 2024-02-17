import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
import { IQuiz } from 'app/entities/quiz/quiz.model';
import { QuizService } from 'app/entities/quiz/service/quiz.service';
import { IUserCustom } from 'app/entities/user-custom/user-custom.model';
import { UserCustomService } from 'app/entities/user-custom/service/user-custom.service';
import { QuizResultService } from '../service/quiz-result.service';
import { IQuizResult } from '../quiz-result.model';
import { QuizResultFormService, QuizResultFormGroup } from './quiz-result-form.service';

@Component({
  standalone: true,
  selector: 'jhi-quiz-result-update',
  templateUrl: './quiz-result-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class QuizResultUpdateComponent implements OnInit {
  isSaving = false;
  quizResult: IQuizResult | null = null;

  sitesSharedCollection: ISite[] = [];
  quizzesSharedCollection: IQuiz[] = [];
  userCustomsSharedCollection: IUserCustom[] = [];

  editForm: QuizResultFormGroup = this.quizResultFormService.createQuizResultFormGroup();

  constructor(
    protected quizResultService: QuizResultService,
    protected quizResultFormService: QuizResultFormService,
    protected siteService: SiteService,
    protected quizService: QuizService,
    protected userCustomService: UserCustomService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareSite = (o1: ISite | null, o2: ISite | null): boolean => this.siteService.compareSite(o1, o2);

  compareQuiz = (o1: IQuiz | null, o2: IQuiz | null): boolean => this.quizService.compareQuiz(o1, o2);

  compareUserCustom = (o1: IUserCustom | null, o2: IUserCustom | null): boolean => this.userCustomService.compareUserCustom(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quizResult }) => {
      this.quizResult = quizResult;
      if (quizResult) {
        this.updateForm(quizResult);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const quizResult = this.quizResultFormService.getQuizResult(this.editForm);
    if (quizResult.id !== null) {
      this.subscribeToSaveResponse(this.quizResultService.update(quizResult));
    } else {
      this.subscribeToSaveResponse(this.quizResultService.create(quizResult));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuizResult>>): void {
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

  protected updateForm(quizResult: IQuizResult): void {
    this.quizResult = quizResult;
    this.quizResultFormService.resetForm(this.editForm, quizResult);

    this.sitesSharedCollection = this.siteService.addSiteToCollectionIfMissing<ISite>(this.sitesSharedCollection, quizResult.site8);
    this.quizzesSharedCollection = this.quizService.addQuizToCollectionIfMissing<IQuiz>(this.quizzesSharedCollection, quizResult.quiz);
    this.userCustomsSharedCollection = this.userCustomService.addUserCustomToCollectionIfMissing<IUserCustom>(
      this.userCustomsSharedCollection,
      quizResult.userCustom2,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.siteService
      .query()
      .pipe(map((res: HttpResponse<ISite[]>) => res.body ?? []))
      .pipe(map((sites: ISite[]) => this.siteService.addSiteToCollectionIfMissing<ISite>(sites, this.quizResult?.site8)))
      .subscribe((sites: ISite[]) => (this.sitesSharedCollection = sites));

    this.quizService
      .query()
      .pipe(map((res: HttpResponse<IQuiz[]>) => res.body ?? []))
      .pipe(map((quizzes: IQuiz[]) => this.quizService.addQuizToCollectionIfMissing<IQuiz>(quizzes, this.quizResult?.quiz)))
      .subscribe((quizzes: IQuiz[]) => (this.quizzesSharedCollection = quizzes));

    this.userCustomService
      .query()
      .pipe(map((res: HttpResponse<IUserCustom[]>) => res.body ?? []))
      .pipe(
        map((userCustoms: IUserCustom[]) =>
          this.userCustomService.addUserCustomToCollectionIfMissing<IUserCustom>(userCustoms, this.quizResult?.userCustom2),
        ),
      )
      .subscribe((userCustoms: IUserCustom[]) => (this.userCustomsSharedCollection = userCustoms));
  }
}
