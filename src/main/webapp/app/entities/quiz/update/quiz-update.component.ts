import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IGroup } from 'app/entities/group/group.model';
import { GroupService } from 'app/entities/group/service/group.service';
import { IQuestion } from 'app/entities/question/question.model';
import { QuestionService } from 'app/entities/question/service/question.service';
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
import { ITopic } from 'app/entities/topic/topic.model';
import { TopicService } from 'app/entities/topic/service/topic.service';
import { ExamType } from 'app/entities/enumerations/exam-type.model';
import { QuizService } from '../service/quiz.service';
import { IQuiz } from '../quiz.model';
import { QuizFormService, QuizFormGroup } from './quiz-form.service';

@Component({
  standalone: true,
  selector: 'jhi-quiz-update',
  templateUrl: './quiz-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class QuizUpdateComponent implements OnInit {
  isSaving = false;
  quiz: IQuiz | null = null;
  examTypeValues = Object.keys(ExamType);

  groupsSharedCollection: IGroup[] = [];
  questionsSharedCollection: IQuestion[] = [];
  sitesSharedCollection: ISite[] = [];
  topicsSharedCollection: ITopic[] = [];

  editForm: QuizFormGroup = this.quizFormService.createQuizFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected quizService: QuizService,
    protected quizFormService: QuizFormService,
    protected groupService: GroupService,
    protected questionService: QuestionService,
    protected siteService: SiteService,
    protected topicService: TopicService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareGroup = (o1: IGroup | null, o2: IGroup | null): boolean => this.groupService.compareGroup(o1, o2);

  compareQuestion = (o1: IQuestion | null, o2: IQuestion | null): boolean => this.questionService.compareQuestion(o1, o2);

  compareSite = (o1: ISite | null, o2: ISite | null): boolean => this.siteService.compareSite(o1, o2);

  compareTopic = (o1: ITopic | null, o2: ITopic | null): boolean => this.topicService.compareTopic(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quiz }) => {
      this.quiz = quiz;
      if (quiz) {
        this.updateForm(quiz);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('lmswiamApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const quiz = this.quizFormService.getQuiz(this.editForm);
    if (quiz.id !== null) {
      this.subscribeToSaveResponse(this.quizService.update(quiz));
    } else {
      this.subscribeToSaveResponse(this.quizService.create(quiz));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuiz>>): void {
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

  protected updateForm(quiz: IQuiz): void {
    this.quiz = quiz;
    this.quizFormService.resetForm(this.editForm, quiz);

    this.groupsSharedCollection = this.groupService.addGroupToCollectionIfMissing<IGroup>(
      this.groupsSharedCollection,
      ...(quiz.groups ?? []),
    );
    this.questionsSharedCollection = this.questionService.addQuestionToCollectionIfMissing<IQuestion>(
      this.questionsSharedCollection,
      ...(quiz.questions ?? []),
    );
    this.sitesSharedCollection = this.siteService.addSiteToCollectionIfMissing<ISite>(this.sitesSharedCollection, quiz.site7);
    this.topicsSharedCollection = this.topicService.addTopicToCollectionIfMissing<ITopic>(this.topicsSharedCollection, quiz.topic1);
  }

  protected loadRelationshipsOptions(): void {
    this.groupService
      .query()
      .pipe(map((res: HttpResponse<IGroup[]>) => res.body ?? []))
      .pipe(map((groups: IGroup[]) => this.groupService.addGroupToCollectionIfMissing<IGroup>(groups, ...(this.quiz?.groups ?? []))))
      .subscribe((groups: IGroup[]) => (this.groupsSharedCollection = groups));

    this.questionService
      .query()
      .pipe(map((res: HttpResponse<IQuestion[]>) => res.body ?? []))
      .pipe(
        map((questions: IQuestion[]) =>
          this.questionService.addQuestionToCollectionIfMissing<IQuestion>(questions, ...(this.quiz?.questions ?? [])),
        ),
      )
      .subscribe((questions: IQuestion[]) => (this.questionsSharedCollection = questions));

    this.siteService
      .query()
      .pipe(map((res: HttpResponse<ISite[]>) => res.body ?? []))
      .pipe(map((sites: ISite[]) => this.siteService.addSiteToCollectionIfMissing<ISite>(sites, this.quiz?.site7)))
      .subscribe((sites: ISite[]) => (this.sitesSharedCollection = sites));

    this.topicService
      .query()
      .pipe(map((res: HttpResponse<ITopic[]>) => res.body ?? []))
      .pipe(map((topics: ITopic[]) => this.topicService.addTopicToCollectionIfMissing<ITopic>(topics, this.quiz?.topic1)))
      .subscribe((topics: ITopic[]) => (this.topicsSharedCollection = topics));
  }
}
