import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IQuiz } from 'app/entities/quiz/quiz.model';
import { QuizService } from 'app/entities/quiz/service/quiz.service';
import { IUserCustom } from 'app/entities/user-custom/user-custom.model';
import { UserCustomService } from 'app/entities/user-custom/service/user-custom.service';
import { IQuizResult } from '../quiz-result.model';
import { QuizResultService } from '../service/quiz-result.service';
import { QuizResultFormService } from './quiz-result-form.service';

import { QuizResultUpdateComponent } from './quiz-result-update.component';

describe('QuizResult Management Update Component', () => {
  let comp: QuizResultUpdateComponent;
  let fixture: ComponentFixture<QuizResultUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let quizResultFormService: QuizResultFormService;
  let quizResultService: QuizResultService;
  let quizService: QuizService;
  let userCustomService: UserCustomService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), QuizResultUpdateComponent],
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
      .overrideTemplate(QuizResultUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuizResultUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    quizResultFormService = TestBed.inject(QuizResultFormService);
    quizResultService = TestBed.inject(QuizResultService);
    quizService = TestBed.inject(QuizService);
    userCustomService = TestBed.inject(UserCustomService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Quiz query and add missing value', () => {
      const quizResult: IQuizResult = { id: 456 };
      const quiz: IQuiz = { id: 4586 };
      quizResult.quiz = quiz;

      const quizCollection: IQuiz[] = [{ id: 27516 }];
      jest.spyOn(quizService, 'query').mockReturnValue(of(new HttpResponse({ body: quizCollection })));
      const additionalQuizzes = [quiz];
      const expectedCollection: IQuiz[] = [...additionalQuizzes, ...quizCollection];
      jest.spyOn(quizService, 'addQuizToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quizResult });
      comp.ngOnInit();

      expect(quizService.query).toHaveBeenCalled();
      expect(quizService.addQuizToCollectionIfMissing).toHaveBeenCalledWith(
        quizCollection,
        ...additionalQuizzes.map(expect.objectContaining),
      );
      expect(comp.quizzesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UserCustom query and add missing value', () => {
      const quizResult: IQuizResult = { id: 456 };
      const userCustom2: IUserCustom = { id: 26931 };
      quizResult.userCustom2 = userCustom2;

      const userCustomCollection: IUserCustom[] = [{ id: 28592 }];
      jest.spyOn(userCustomService, 'query').mockReturnValue(of(new HttpResponse({ body: userCustomCollection })));
      const additionalUserCustoms = [userCustom2];
      const expectedCollection: IUserCustom[] = [...additionalUserCustoms, ...userCustomCollection];
      jest.spyOn(userCustomService, 'addUserCustomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quizResult });
      comp.ngOnInit();

      expect(userCustomService.query).toHaveBeenCalled();
      expect(userCustomService.addUserCustomToCollectionIfMissing).toHaveBeenCalledWith(
        userCustomCollection,
        ...additionalUserCustoms.map(expect.objectContaining),
      );
      expect(comp.userCustomsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const quizResult: IQuizResult = { id: 456 };
      const quiz: IQuiz = { id: 6686 };
      quizResult.quiz = quiz;
      const userCustom2: IUserCustom = { id: 20650 };
      quizResult.userCustom2 = userCustom2;

      activatedRoute.data = of({ quizResult });
      comp.ngOnInit();

      expect(comp.quizzesSharedCollection).toContain(quiz);
      expect(comp.userCustomsSharedCollection).toContain(userCustom2);
      expect(comp.quizResult).toEqual(quizResult);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuizResult>>();
      const quizResult = { id: 123 };
      jest.spyOn(quizResultFormService, 'getQuizResult').mockReturnValue(quizResult);
      jest.spyOn(quizResultService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quizResult });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quizResult }));
      saveSubject.complete();

      // THEN
      expect(quizResultFormService.getQuizResult).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(quizResultService.update).toHaveBeenCalledWith(expect.objectContaining(quizResult));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuizResult>>();
      const quizResult = { id: 123 };
      jest.spyOn(quizResultFormService, 'getQuizResult').mockReturnValue({ id: null });
      jest.spyOn(quizResultService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quizResult: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quizResult }));
      saveSubject.complete();

      // THEN
      expect(quizResultFormService.getQuizResult).toHaveBeenCalled();
      expect(quizResultService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuizResult>>();
      const quizResult = { id: 123 };
      jest.spyOn(quizResultService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quizResult });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(quizResultService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareQuiz', () => {
      it('Should forward to quizService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(quizService, 'compareQuiz');
        comp.compareQuiz(entity, entity2);
        expect(quizService.compareQuiz).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUserCustom', () => {
      it('Should forward to userCustomService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userCustomService, 'compareUserCustom');
        comp.compareUserCustom(entity, entity2);
        expect(userCustomService.compareUserCustom).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
