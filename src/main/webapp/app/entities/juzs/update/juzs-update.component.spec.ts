import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { JuzsService } from '../service/juzs.service';
import { IJuzs } from '../juzs.model';
import { JuzsFormService } from './juzs-form.service';

import { JuzsUpdateComponent } from './juzs-update.component';

describe('Juzs Management Update Component', () => {
  let comp: JuzsUpdateComponent;
  let fixture: ComponentFixture<JuzsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let juzsFormService: JuzsFormService;
  let juzsService: JuzsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), JuzsUpdateComponent],
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
      .overrideTemplate(JuzsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(JuzsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    juzsFormService = TestBed.inject(JuzsFormService);
    juzsService = TestBed.inject(JuzsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const juzs: IJuzs = { id: 456 };

      activatedRoute.data = of({ juzs });
      comp.ngOnInit();

      expect(comp.juzs).toEqual(juzs);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IJuzs>>();
      const juzs = { id: 123 };
      jest.spyOn(juzsFormService, 'getJuzs').mockReturnValue(juzs);
      jest.spyOn(juzsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ juzs });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: juzs }));
      saveSubject.complete();

      // THEN
      expect(juzsFormService.getJuzs).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(juzsService.update).toHaveBeenCalledWith(expect.objectContaining(juzs));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IJuzs>>();
      const juzs = { id: 123 };
      jest.spyOn(juzsFormService, 'getJuzs').mockReturnValue({ id: null });
      jest.spyOn(juzsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ juzs: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: juzs }));
      saveSubject.complete();

      // THEN
      expect(juzsFormService.getJuzs).toHaveBeenCalled();
      expect(juzsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IJuzs>>();
      const juzs = { id: 123 };
      jest.spyOn(juzsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ juzs });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(juzsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
