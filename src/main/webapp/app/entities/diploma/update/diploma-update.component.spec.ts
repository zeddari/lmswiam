import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DiplomaService } from '../service/diploma.service';
import { IDiploma } from '../diploma.model';
import { DiplomaFormService } from './diploma-form.service';

import { DiplomaUpdateComponent } from './diploma-update.component';

describe('Diploma Management Update Component', () => {
  let comp: DiplomaUpdateComponent;
  let fixture: ComponentFixture<DiplomaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let diplomaFormService: DiplomaFormService;
  let diplomaService: DiplomaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), DiplomaUpdateComponent],
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
      .overrideTemplate(DiplomaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DiplomaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    diplomaFormService = TestBed.inject(DiplomaFormService);
    diplomaService = TestBed.inject(DiplomaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const diploma: IDiploma = { id: 456 };

      activatedRoute.data = of({ diploma });
      comp.ngOnInit();

      expect(comp.diploma).toEqual(diploma);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDiploma>>();
      const diploma = { id: 123 };
      jest.spyOn(diplomaFormService, 'getDiploma').mockReturnValue(diploma);
      jest.spyOn(diplomaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ diploma });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: diploma }));
      saveSubject.complete();

      // THEN
      expect(diplomaFormService.getDiploma).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(diplomaService.update).toHaveBeenCalledWith(expect.objectContaining(diploma));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDiploma>>();
      const diploma = { id: 123 };
      jest.spyOn(diplomaFormService, 'getDiploma').mockReturnValue({ id: null });
      jest.spyOn(diplomaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ diploma: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: diploma }));
      saveSubject.complete();

      // THEN
      expect(diplomaFormService.getDiploma).toHaveBeenCalled();
      expect(diplomaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDiploma>>();
      const diploma = { id: 123 };
      jest.spyOn(diplomaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ diploma });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(diplomaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
