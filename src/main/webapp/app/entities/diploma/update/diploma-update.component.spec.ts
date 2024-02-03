import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
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
  let siteService: SiteService;

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
    siteService = TestBed.inject(SiteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Site query and add missing value', () => {
      const diploma: IDiploma = { id: 456 };
      const site20: ISite = { id: 2071 };
      diploma.site20 = site20;

      const siteCollection: ISite[] = [{ id: 3445 }];
      jest.spyOn(siteService, 'query').mockReturnValue(of(new HttpResponse({ body: siteCollection })));
      const additionalSites = [site20];
      const expectedCollection: ISite[] = [...additionalSites, ...siteCollection];
      jest.spyOn(siteService, 'addSiteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ diploma });
      comp.ngOnInit();

      expect(siteService.query).toHaveBeenCalled();
      expect(siteService.addSiteToCollectionIfMissing).toHaveBeenCalledWith(
        siteCollection,
        ...additionalSites.map(expect.objectContaining),
      );
      expect(comp.sitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const diploma: IDiploma = { id: 456 };
      const site20: ISite = { id: 22179 };
      diploma.site20 = site20;

      activatedRoute.data = of({ diploma });
      comp.ngOnInit();

      expect(comp.sitesSharedCollection).toContain(site20);
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

  describe('Compare relationships', () => {
    describe('compareSite', () => {
      it('Should forward to siteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(siteService, 'compareSite');
        comp.compareSite(entity, entity2);
        expect(siteService.compareSite).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
