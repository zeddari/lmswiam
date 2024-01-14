import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { HizbsService } from '../service/hizbs.service';
import { IHizbs } from '../hizbs.model';
import { HizbsFormService } from './hizbs-form.service';

import { HizbsUpdateComponent } from './hizbs-update.component';

describe('Hizbs Management Update Component', () => {
  let comp: HizbsUpdateComponent;
  let fixture: ComponentFixture<HizbsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let hizbsFormService: HizbsFormService;
  let hizbsService: HizbsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), HizbsUpdateComponent],
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
      .overrideTemplate(HizbsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HizbsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    hizbsFormService = TestBed.inject(HizbsFormService);
    hizbsService = TestBed.inject(HizbsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const hizbs: IHizbs = { id: 456 };

      activatedRoute.data = of({ hizbs });
      comp.ngOnInit();

      expect(comp.hizbs).toEqual(hizbs);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHizbs>>();
      const hizbs = { id: 123 };
      jest.spyOn(hizbsFormService, 'getHizbs').mockReturnValue(hizbs);
      jest.spyOn(hizbsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hizbs });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hizbs }));
      saveSubject.complete();

      // THEN
      expect(hizbsFormService.getHizbs).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(hizbsService.update).toHaveBeenCalledWith(expect.objectContaining(hizbs));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHizbs>>();
      const hizbs = { id: 123 };
      jest.spyOn(hizbsFormService, 'getHizbs').mockReturnValue({ id: null });
      jest.spyOn(hizbsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hizbs: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hizbs }));
      saveSubject.complete();

      // THEN
      expect(hizbsFormService.getHizbs).toHaveBeenCalled();
      expect(hizbsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHizbs>>();
      const hizbs = { id: 123 };
      jest.spyOn(hizbsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hizbs });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(hizbsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
