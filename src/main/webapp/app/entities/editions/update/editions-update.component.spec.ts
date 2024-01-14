import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EditionsService } from '../service/editions.service';
import { IEditions } from '../editions.model';
import { EditionsFormService } from './editions-form.service';

import { EditionsUpdateComponent } from './editions-update.component';

describe('Editions Management Update Component', () => {
  let comp: EditionsUpdateComponent;
  let fixture: ComponentFixture<EditionsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let editionsFormService: EditionsFormService;
  let editionsService: EditionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), EditionsUpdateComponent],
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
      .overrideTemplate(EditionsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EditionsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    editionsFormService = TestBed.inject(EditionsFormService);
    editionsService = TestBed.inject(EditionsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const editions: IEditions = { id: 456 };

      activatedRoute.data = of({ editions });
      comp.ngOnInit();

      expect(comp.editions).toEqual(editions);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEditions>>();
      const editions = { id: 123 };
      jest.spyOn(editionsFormService, 'getEditions').mockReturnValue(editions);
      jest.spyOn(editionsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ editions });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: editions }));
      saveSubject.complete();

      // THEN
      expect(editionsFormService.getEditions).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(editionsService.update).toHaveBeenCalledWith(expect.objectContaining(editions));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEditions>>();
      const editions = { id: 123 };
      jest.spyOn(editionsFormService, 'getEditions').mockReturnValue({ id: null });
      jest.spyOn(editionsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ editions: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: editions }));
      saveSubject.complete();

      // THEN
      expect(editionsFormService.getEditions).toHaveBeenCalled();
      expect(editionsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEditions>>();
      const editions = { id: 123 };
      jest.spyOn(editionsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ editions });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(editionsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
