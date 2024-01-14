import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AyahsService } from '../service/ayahs.service';
import { IAyahs } from '../ayahs.model';
import { AyahsFormService } from './ayahs-form.service';

import { AyahsUpdateComponent } from './ayahs-update.component';

describe('Ayahs Management Update Component', () => {
  let comp: AyahsUpdateComponent;
  let fixture: ComponentFixture<AyahsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ayahsFormService: AyahsFormService;
  let ayahsService: AyahsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), AyahsUpdateComponent],
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
      .overrideTemplate(AyahsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AyahsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ayahsFormService = TestBed.inject(AyahsFormService);
    ayahsService = TestBed.inject(AyahsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const ayahs: IAyahs = { id: 456 };

      activatedRoute.data = of({ ayahs });
      comp.ngOnInit();

      expect(comp.ayahs).toEqual(ayahs);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAyahs>>();
      const ayahs = { id: 123 };
      jest.spyOn(ayahsFormService, 'getAyahs').mockReturnValue(ayahs);
      jest.spyOn(ayahsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ayahs });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ayahs }));
      saveSubject.complete();

      // THEN
      expect(ayahsFormService.getAyahs).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ayahsService.update).toHaveBeenCalledWith(expect.objectContaining(ayahs));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAyahs>>();
      const ayahs = { id: 123 };
      jest.spyOn(ayahsFormService, 'getAyahs').mockReturnValue({ id: null });
      jest.spyOn(ayahsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ayahs: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ayahs }));
      saveSubject.complete();

      // THEN
      expect(ayahsFormService.getAyahs).toHaveBeenCalled();
      expect(ayahsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAyahs>>();
      const ayahs = { id: 123 };
      jest.spyOn(ayahsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ayahs });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ayahsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
