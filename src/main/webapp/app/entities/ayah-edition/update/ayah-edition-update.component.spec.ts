import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AyahEditionService } from '../service/ayah-edition.service';
import { IAyahEdition } from '../ayah-edition.model';
import { AyahEditionFormService } from './ayah-edition-form.service';

import { AyahEditionUpdateComponent } from './ayah-edition-update.component';

describe('AyahEdition Management Update Component', () => {
  let comp: AyahEditionUpdateComponent;
  let fixture: ComponentFixture<AyahEditionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ayahEditionFormService: AyahEditionFormService;
  let ayahEditionService: AyahEditionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), AyahEditionUpdateComponent],
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
      .overrideTemplate(AyahEditionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AyahEditionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ayahEditionFormService = TestBed.inject(AyahEditionFormService);
    ayahEditionService = TestBed.inject(AyahEditionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const ayahEdition: IAyahEdition = { id: 456 };

      activatedRoute.data = of({ ayahEdition });
      comp.ngOnInit();

      expect(comp.ayahEdition).toEqual(ayahEdition);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAyahEdition>>();
      const ayahEdition = { id: 123 };
      jest.spyOn(ayahEditionFormService, 'getAyahEdition').mockReturnValue(ayahEdition);
      jest.spyOn(ayahEditionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ayahEdition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ayahEdition }));
      saveSubject.complete();

      // THEN
      expect(ayahEditionFormService.getAyahEdition).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ayahEditionService.update).toHaveBeenCalledWith(expect.objectContaining(ayahEdition));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAyahEdition>>();
      const ayahEdition = { id: 123 };
      jest.spyOn(ayahEditionFormService, 'getAyahEdition').mockReturnValue({ id: null });
      jest.spyOn(ayahEditionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ayahEdition: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ayahEdition }));
      saveSubject.complete();

      // THEN
      expect(ayahEditionFormService.getAyahEdition).toHaveBeenCalled();
      expect(ayahEditionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAyahEdition>>();
      const ayahEdition = { id: 123 };
      jest.spyOn(ayahEditionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ayahEdition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ayahEditionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
