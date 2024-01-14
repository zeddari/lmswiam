import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LanguageService } from '../service/language.service';
import { ILanguage } from '../language.model';
import { LanguageFormService } from './language-form.service';

import { LanguageUpdateComponent } from './language-update.component';

describe('Language Management Update Component', () => {
  let comp: LanguageUpdateComponent;
  let fixture: ComponentFixture<LanguageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let languageFormService: LanguageFormService;
  let languageService: LanguageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), LanguageUpdateComponent],
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
      .overrideTemplate(LanguageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LanguageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    languageFormService = TestBed.inject(LanguageFormService);
    languageService = TestBed.inject(LanguageService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const language: ILanguage = { id: 456 };

      activatedRoute.data = of({ language });
      comp.ngOnInit();

      expect(comp.language).toEqual(language);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILanguage>>();
      const language = { id: 123 };
      jest.spyOn(languageFormService, 'getLanguage').mockReturnValue(language);
      jest.spyOn(languageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ language });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: language }));
      saveSubject.complete();

      // THEN
      expect(languageFormService.getLanguage).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(languageService.update).toHaveBeenCalledWith(expect.objectContaining(language));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILanguage>>();
      const language = { id: 123 };
      jest.spyOn(languageFormService, 'getLanguage').mockReturnValue({ id: null });
      jest.spyOn(languageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ language: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: language }));
      saveSubject.complete();

      // THEN
      expect(languageFormService.getLanguage).toHaveBeenCalled();
      expect(languageService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILanguage>>();
      const language = { id: 123 };
      jest.spyOn(languageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ language });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(languageService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
