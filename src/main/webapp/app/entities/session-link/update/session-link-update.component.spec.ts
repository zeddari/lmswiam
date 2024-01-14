import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SessionLinkService } from '../service/session-link.service';
import { ISessionLink } from '../session-link.model';
import { SessionLinkFormService } from './session-link-form.service';

import { SessionLinkUpdateComponent } from './session-link-update.component';

describe('SessionLink Management Update Component', () => {
  let comp: SessionLinkUpdateComponent;
  let fixture: ComponentFixture<SessionLinkUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sessionLinkFormService: SessionLinkFormService;
  let sessionLinkService: SessionLinkService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), SessionLinkUpdateComponent],
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
      .overrideTemplate(SessionLinkUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SessionLinkUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sessionLinkFormService = TestBed.inject(SessionLinkFormService);
    sessionLinkService = TestBed.inject(SessionLinkService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const sessionLink: ISessionLink = { id: 456 };

      activatedRoute.data = of({ sessionLink });
      comp.ngOnInit();

      expect(comp.sessionLink).toEqual(sessionLink);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISessionLink>>();
      const sessionLink = { id: 123 };
      jest.spyOn(sessionLinkFormService, 'getSessionLink').mockReturnValue(sessionLink);
      jest.spyOn(sessionLinkService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sessionLink });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sessionLink }));
      saveSubject.complete();

      // THEN
      expect(sessionLinkFormService.getSessionLink).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sessionLinkService.update).toHaveBeenCalledWith(expect.objectContaining(sessionLink));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISessionLink>>();
      const sessionLink = { id: 123 };
      jest.spyOn(sessionLinkFormService, 'getSessionLink').mockReturnValue({ id: null });
      jest.spyOn(sessionLinkService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sessionLink: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sessionLink }));
      saveSubject.complete();

      // THEN
      expect(sessionLinkFormService.getSessionLink).toHaveBeenCalled();
      expect(sessionLinkService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISessionLink>>();
      const sessionLink = { id: 123 };
      jest.spyOn(sessionLinkService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sessionLink });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sessionLinkService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
