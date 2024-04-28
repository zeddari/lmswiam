import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IUserCustom } from 'app/entities/user-custom/user-custom.model';
import { UserCustomService } from 'app/entities/user-custom/service/user-custom.service';
import { DepenseService } from '../service/depense.service';
import { IDepense } from '../depense.model';
import { DepenseFormService } from './depense-form.service';

import { DepenseUpdateComponent } from './depense-update.component';

describe('Depense Management Update Component', () => {
  let comp: DepenseUpdateComponent;
  let fixture: ComponentFixture<DepenseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let depenseFormService: DepenseFormService;
  let depenseService: DepenseService;
  let userCustomService: UserCustomService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), DepenseUpdateComponent],
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
      .overrideTemplate(DepenseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DepenseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    depenseFormService = TestBed.inject(DepenseFormService);
    depenseService = TestBed.inject(DepenseService);
    userCustomService = TestBed.inject(UserCustomService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call UserCustom query and add missing value', () => {
      const depense: IDepense = { id: 456 };
      const resource: IUserCustom = { id: 20725 };
      depense.resource = resource;

      const userCustomCollection: IUserCustom[] = [{ id: 19005 }];
      jest.spyOn(userCustomService, 'query').mockReturnValue(of(new HttpResponse({ body: userCustomCollection })));
      const additionalUserCustoms = [resource];
      const expectedCollection: IUserCustom[] = [...additionalUserCustoms, ...userCustomCollection];
      jest.spyOn(userCustomService, 'addUserCustomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ depense });
      comp.ngOnInit();

      expect(userCustomService.query).toHaveBeenCalled();
      expect(userCustomService.addUserCustomToCollectionIfMissing).toHaveBeenCalledWith(
        userCustomCollection,
        ...additionalUserCustoms.map(expect.objectContaining),
      );
      expect(comp.userCustomsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const depense: IDepense = { id: 456 };
      const resource: IUserCustom = { id: 18253 };
      depense.resource = resource;

      activatedRoute.data = of({ depense });
      comp.ngOnInit();

      expect(comp.userCustomsSharedCollection).toContain(resource);
      expect(comp.depense).toEqual(depense);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepense>>();
      const depense = { id: 123 };
      jest.spyOn(depenseFormService, 'getDepense').mockReturnValue(depense);
      jest.spyOn(depenseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ depense });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: depense }));
      saveSubject.complete();

      // THEN
      expect(depenseFormService.getDepense).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(depenseService.update).toHaveBeenCalledWith(expect.objectContaining(depense));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepense>>();
      const depense = { id: 123 };
      jest.spyOn(depenseFormService, 'getDepense').mockReturnValue({ id: null });
      jest.spyOn(depenseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ depense: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: depense }));
      saveSubject.complete();

      // THEN
      expect(depenseFormService.getDepense).toHaveBeenCalled();
      expect(depenseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepense>>();
      const depense = { id: 123 };
      jest.spyOn(depenseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ depense });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(depenseService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
