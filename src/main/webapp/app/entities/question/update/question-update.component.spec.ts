import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
import { QuestionService } from '../service/question.service';
import { IQuestion } from '../question.model';
import { QuestionFormService } from './question-form.service';

import { QuestionUpdateComponent } from './question-update.component';

describe('Question Management Update Component', () => {
  let comp: QuestionUpdateComponent;
  let fixture: ComponentFixture<QuestionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let questionFormService: QuestionFormService;
  let questionService: QuestionService;
  let siteService: SiteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), QuestionUpdateComponent],
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
      .overrideTemplate(QuestionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuestionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    questionFormService = TestBed.inject(QuestionFormService);
    questionService = TestBed.inject(QuestionService);
    siteService = TestBed.inject(SiteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Site query and add missing value', () => {
      const question: IQuestion = { id: 456 };
      const site5: ISite = { id: 14635 };
      question.site5 = site5;

      const siteCollection: ISite[] = [{ id: 15454 }];
      jest.spyOn(siteService, 'query').mockReturnValue(of(new HttpResponse({ body: siteCollection })));
      const additionalSites = [site5];
      const expectedCollection: ISite[] = [...additionalSites, ...siteCollection];
      jest.spyOn(siteService, 'addSiteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ question });
      comp.ngOnInit();

      expect(siteService.query).toHaveBeenCalled();
      expect(siteService.addSiteToCollectionIfMissing).toHaveBeenCalledWith(
        siteCollection,
        ...additionalSites.map(expect.objectContaining),
      );
      expect(comp.sitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const question: IQuestion = { id: 456 };
      const site5: ISite = { id: 11425 };
      question.site5 = site5;

      activatedRoute.data = of({ question });
      comp.ngOnInit();

      expect(comp.sitesSharedCollection).toContain(site5);
      expect(comp.question).toEqual(question);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuestion>>();
      const question = { id: 123 };
      jest.spyOn(questionFormService, 'getQuestion').mockReturnValue(question);
      jest.spyOn(questionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ question });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: question }));
      saveSubject.complete();

      // THEN
      expect(questionFormService.getQuestion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(questionService.update).toHaveBeenCalledWith(expect.objectContaining(question));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuestion>>();
      const question = { id: 123 };
      jest.spyOn(questionFormService, 'getQuestion').mockReturnValue({ id: null });
      jest.spyOn(questionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ question: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: question }));
      saveSubject.complete();

      // THEN
      expect(questionFormService.getQuestion).toHaveBeenCalled();
      expect(questionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuestion>>();
      const question = { id: 123 };
      jest.spyOn(questionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ question });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(questionService.update).toHaveBeenCalled();
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
  });
});
