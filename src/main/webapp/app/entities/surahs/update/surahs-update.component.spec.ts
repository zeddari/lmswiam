import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SurahsService } from '../service/surahs.service';
import { ISurahs } from '../surahs.model';
import { SurahsFormService } from './surahs-form.service';

import { SurahsUpdateComponent } from './surahs-update.component';

describe('Surahs Management Update Component', () => {
  let comp: SurahsUpdateComponent;
  let fixture: ComponentFixture<SurahsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let surahsFormService: SurahsFormService;
  let surahsService: SurahsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), SurahsUpdateComponent],
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
      .overrideTemplate(SurahsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SurahsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    surahsFormService = TestBed.inject(SurahsFormService);
    surahsService = TestBed.inject(SurahsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const surahs: ISurahs = { id: 456 };

      activatedRoute.data = of({ surahs });
      comp.ngOnInit();

      expect(comp.surahs).toEqual(surahs);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISurahs>>();
      const surahs = { id: 123 };
      jest.spyOn(surahsFormService, 'getSurahs').mockReturnValue(surahs);
      jest.spyOn(surahsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ surahs });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: surahs }));
      saveSubject.complete();

      // THEN
      expect(surahsFormService.getSurahs).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(surahsService.update).toHaveBeenCalledWith(expect.objectContaining(surahs));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISurahs>>();
      const surahs = { id: 123 };
      jest.spyOn(surahsFormService, 'getSurahs').mockReturnValue({ id: null });
      jest.spyOn(surahsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ surahs: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: surahs }));
      saveSubject.complete();

      // THEN
      expect(surahsFormService.getSurahs).toHaveBeenCalled();
      expect(surahsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISurahs>>();
      const surahs = { id: 123 };
      jest.spyOn(surahsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ surahs });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(surahsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
