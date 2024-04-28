import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
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
  let siteService: SiteService;
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
    siteService = TestBed.inject(SiteService);
    quizService = TestBed.inject(QuizService);
    userCustomService = TestBed.inject(UserCustomService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Site query and add missing value', () => {
      const quizResult: IQuizResult = { id: 456 };
      const site8: ISite = { id: 19470 };
      quizResult.site8 = site8;

      const siteCollection: ISite[] = [{ id: 16241 }];
      jest.spyOn(siteService, 'query').mockReturnValue(of(new HttpResponse({ body: siteCollection })));
      const additionalSites = [site8];
      const expectedCollection: ISite[] = [...additionalSites, ...siteCollection];
      jest.spyOn(siteService, 'addSiteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quizResult });
      comp.ngOnInit();

      expect(siteService.query).toHaveBeenCalled();
      expect(siteService.addSiteToCollectionIfMissing).toHaveBeenCalledWith(
        siteCollection,
        ...additionalSites.map(expect.objectContaining),
      );
      expect(comp.sitesSharedCollection).toEqual(expectedCollection);
    });

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
      const userCustom2: IUserCustom = { id: 9761 };
      quizResult.userCustom2 = userCustom2;

      const userCustomCollection: IUserCustom[] = [{ id: 20607 }];
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
      const site8: ISite = { id: 22497 };
      quizResult.site8 = site8;
      const quiz: IQuiz = { id: 6686 };
      quizResult.quiz = quiz;
      const userCustom2: IUserCustom = { id: 18675 };
      quizResult.userCustom2 = userCustom2;

      activatedRoute.data = of({ quizResult });
      comp.ngOnInit();

      expect(comp.sitesSharedCollection).toContain(site8);
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
    describe('compareSite', () => {
      it('Should forward to siteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(siteService, 'compareSite');
        comp.compareSite(entity, entity2);
        expect(siteService.compareSite).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
