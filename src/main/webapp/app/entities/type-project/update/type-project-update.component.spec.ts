import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TypeProjectService } from '../service/type-project.service';
import { ITypeProject } from '../type-project.model';
import { TypeProjectFormService } from './type-project-form.service';

import { TypeProjectUpdateComponent } from './type-project-update.component';

describe('TypeProject Management Update Component', () => {
  let comp: TypeProjectUpdateComponent;
  let fixture: ComponentFixture<TypeProjectUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typeProjectFormService: TypeProjectFormService;
  let typeProjectService: TypeProjectService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TypeProjectUpdateComponent],
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
      .overrideTemplate(TypeProjectUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeProjectUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeProjectFormService = TestBed.inject(TypeProjectFormService);
    typeProjectService = TestBed.inject(TypeProjectService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const typeProject: ITypeProject = { id: 456 };

      activatedRoute.data = of({ typeProject });
      comp.ngOnInit();

      expect(comp.typeProject).toEqual(typeProject);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeProject>>();
      const typeProject = { id: 123 };
      jest.spyOn(typeProjectFormService, 'getTypeProject').mockReturnValue(typeProject);
      jest.spyOn(typeProjectService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeProject });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeProject }));
      saveSubject.complete();

      // THEN
      expect(typeProjectFormService.getTypeProject).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeProjectService.update).toHaveBeenCalledWith(expect.objectContaining(typeProject));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeProject>>();
      const typeProject = { id: 123 };
      jest.spyOn(typeProjectFormService, 'getTypeProject').mockReturnValue({ id: null });
      jest.spyOn(typeProjectService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeProject: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeProject }));
      saveSubject.complete();

      // THEN
      expect(typeProjectFormService.getTypeProject).toHaveBeenCalled();
      expect(typeProjectService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeProject>>();
      const typeProject = { id: 123 };
      jest.spyOn(typeProjectService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeProject });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typeProjectService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
