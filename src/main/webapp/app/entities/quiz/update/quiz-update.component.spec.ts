import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IGroup } from 'app/entities/group/group.model';
import { GroupService } from 'app/entities/group/service/group.service';
import { IQuestion } from 'app/entities/question/question.model';
import { QuestionService } from 'app/entities/question/service/question.service';
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
import { ITopic } from 'app/entities/topic/topic.model';
import { TopicService } from 'app/entities/topic/service/topic.service';
import { IQuiz } from '../quiz.model';
import { QuizService } from '../service/quiz.service';
import { QuizFormService } from './quiz-form.service';

import { QuizUpdateComponent } from './quiz-update.component';

describe('Quiz Management Update Component', () => {
  let comp: QuizUpdateComponent;
  let fixture: ComponentFixture<QuizUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let quizFormService: QuizFormService;
  let quizService: QuizService;
  let groupService: GroupService;
  let questionService: QuestionService;
  let siteService: SiteService;
  let topicService: TopicService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), QuizUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(QuizUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuizUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    quizFormService = TestBed.inject(QuizFormService);
    quizService = TestBed.inject(QuizService);
    groupService = TestBed.inject(GroupService);
    questionService = TestBed.inject(QuestionService);
    siteService = TestBed.inject(SiteService);
    topicService = TestBed.inject(TopicService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Group query and add missing value', () => {
      const quiz: IQuiz = { id: 456 };
      const groups: IGroup[] = [{ id: 17191 }];
      quiz.groups = groups;

      const groupCollection: IGroup[] = [{ id: 17285 }];
      jest.spyOn(groupService, 'query').mockReturnValue(of(new HttpResponse({ body: groupCollection })));
      const additionalGroups = [...groups];
      const expectedCollection: IGroup[] = [...additionalGroups, ...groupCollection];
      jest.spyOn(groupService, 'addGroupToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quiz });
      comp.ngOnInit();

      expect(groupService.query).toHaveBeenCalled();
      expect(groupService.addGroupToCollectionIfMissing).toHaveBeenCalledWith(
        groupCollection,
        ...additionalGroups.map(expect.objectContaining),
      );
      expect(comp.groupsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Question query and add missing value', () => {
      const quiz: IQuiz = { id: 456 };
      const questions: IQuestion[] = [{ id: 414 }];
      quiz.questions = questions;

      const questionCollection: IQuestion[] = [{ id: 20392 }];
      jest.spyOn(questionService, 'query').mockReturnValue(of(new HttpResponse({ body: questionCollection })));
      const additionalQuestions = [...questions];
      const expectedCollection: IQuestion[] = [...additionalQuestions, ...questionCollection];
      jest.spyOn(questionService, 'addQuestionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quiz });
      comp.ngOnInit();

      expect(questionService.query).toHaveBeenCalled();
      expect(questionService.addQuestionToCollectionIfMissing).toHaveBeenCalledWith(
        questionCollection,
        ...additionalQuestions.map(expect.objectContaining),
      );
      expect(comp.questionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Site query and add missing value', () => {
      const quiz: IQuiz = { id: 456 };
      const site7: ISite = { id: 17229 };
      quiz.site7 = site7;

      const siteCollection: ISite[] = [{ id: 1161 }];
      jest.spyOn(siteService, 'query').mockReturnValue(of(new HttpResponse({ body: siteCollection })));
      const additionalSites = [site7];
      const expectedCollection: ISite[] = [...additionalSites, ...siteCollection];
      jest.spyOn(siteService, 'addSiteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quiz });
      comp.ngOnInit();

      expect(siteService.query).toHaveBeenCalled();
      expect(siteService.addSiteToCollectionIfMissing).toHaveBeenCalledWith(
        siteCollection,
        ...additionalSites.map(expect.objectContaining),
      );
      expect(comp.sitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Topic query and add missing value', () => {
      const quiz: IQuiz = { id: 456 };
      const topic1: ITopic = { id: 31219 };
      quiz.topic1 = topic1;

      const topicCollection: ITopic[] = [{ id: 2718 }];
      jest.spyOn(topicService, 'query').mockReturnValue(of(new HttpResponse({ body: topicCollection })));
      const additionalTopics = [topic1];
      const expectedCollection: ITopic[] = [...additionalTopics, ...topicCollection];
      jest.spyOn(topicService, 'addTopicToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quiz });
      comp.ngOnInit();

      expect(topicService.query).toHaveBeenCalled();
      expect(topicService.addTopicToCollectionIfMissing).toHaveBeenCalledWith(
        topicCollection,
        ...additionalTopics.map(expect.objectContaining),
      );
      expect(comp.topicsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const quiz: IQuiz = { id: 456 };
      const groups: IGroup = { id: 26905 };
      quiz.groups = [groups];
      const questions: IQuestion = { id: 11472 };
      quiz.questions = [questions];
      const site7: ISite = { id: 29833 };
      quiz.site7 = site7;
      const topic1: ITopic = { id: 27914 };
      quiz.topic1 = topic1;

      activatedRoute.data = of({ quiz });
      comp.ngOnInit();

      expect(comp.groupsSharedCollection).toContain(groups);
      expect(comp.questionsSharedCollection).toContain(questions);
      expect(comp.sitesSharedCollection).toContain(site7);
      expect(comp.topicsSharedCollection).toContain(topic1);
      expect(comp.quiz).toEqual(quiz);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuiz>>();
      const quiz = { id: 123 };
      jest.spyOn(quizFormService, 'getQuiz').mockReturnValue(quiz);
      jest.spyOn(quizService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quiz });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quiz }));
      saveSubject.complete();

      // THEN
      expect(quizFormService.getQuiz).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(quizService.update).toHaveBeenCalledWith(expect.objectContaining(quiz));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuiz>>();
      const quiz = { id: 123 };
      jest.spyOn(quizFormService, 'getQuiz').mockReturnValue({ id: null });
      jest.spyOn(quizService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quiz: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quiz }));
      saveSubject.complete();

      // THEN
      expect(quizFormService.getQuiz).toHaveBeenCalled();
      expect(quizService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuiz>>();
      const quiz = { id: 123 };
      jest.spyOn(quizService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quiz });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(quizService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareGroup', () => {
      it('Should forward to groupService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(groupService, 'compareGroup');
        comp.compareGroup(entity, entity2);
        expect(groupService.compareGroup).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareQuestion', () => {
      it('Should forward to questionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(questionService, 'compareQuestion');
        comp.compareQuestion(entity, entity2);
        expect(questionService.compareQuestion).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSite', () => {
      it('Should forward to siteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(siteService, 'compareSite');
        comp.compareSite(entity, entity2);
        expect(siteService.compareSite).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTopic', () => {
      it('Should forward to topicService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(topicService, 'compareTopic');
        comp.compareTopic(entity, entity2);
        expect(topicService.compareTopic).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
