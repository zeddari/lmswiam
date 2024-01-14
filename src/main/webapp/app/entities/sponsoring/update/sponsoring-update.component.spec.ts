import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IUserCustom } from 'app/entities/user-custom/user-custom.model';
import { UserCustomService } from 'app/entities/user-custom/service/user-custom.service';
import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';
import { ICurrency } from 'app/entities/currency/currency.model';
import { CurrencyService } from 'app/entities/currency/service/currency.service';
import { ISponsoring } from '../sponsoring.model';
import { SponsoringService } from '../service/sponsoring.service';
import { SponsoringFormService } from './sponsoring-form.service';

import { SponsoringUpdateComponent } from './sponsoring-update.component';

describe('Sponsoring Management Update Component', () => {
  let comp: SponsoringUpdateComponent;
  let fixture: ComponentFixture<SponsoringUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sponsoringFormService: SponsoringFormService;
  let sponsoringService: SponsoringService;
  let userCustomService: UserCustomService;
  let projectService: ProjectService;
  let currencyService: CurrencyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), SponsoringUpdateComponent],
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
      .overrideTemplate(SponsoringUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SponsoringUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sponsoringFormService = TestBed.inject(SponsoringFormService);
    sponsoringService = TestBed.inject(SponsoringService);
    userCustomService = TestBed.inject(UserCustomService);
    projectService = TestBed.inject(ProjectService);
    currencyService = TestBed.inject(CurrencyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call UserCustom query and add missing value', () => {
      const sponsoring: ISponsoring = { id: 456 };
      const sponsor: IUserCustom = { id: 17635 };
      sponsoring.sponsor = sponsor;

      const userCustomCollection: IUserCustom[] = [{ id: 18923 }];
      jest.spyOn(userCustomService, 'query').mockReturnValue(of(new HttpResponse({ body: userCustomCollection })));
      const additionalUserCustoms = [sponsor];
      const expectedCollection: IUserCustom[] = [...additionalUserCustoms, ...userCustomCollection];
      jest.spyOn(userCustomService, 'addUserCustomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sponsoring });
      comp.ngOnInit();

      expect(userCustomService.query).toHaveBeenCalled();
      expect(userCustomService.addUserCustomToCollectionIfMissing).toHaveBeenCalledWith(
        userCustomCollection,
        ...additionalUserCustoms.map(expect.objectContaining),
      );
      expect(comp.userCustomsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Project query and add missing value', () => {
      const sponsoring: ISponsoring = { id: 456 };
      const project: IProject = { id: 23755 };
      sponsoring.project = project;

      const projectCollection: IProject[] = [{ id: 27430 }];
      jest.spyOn(projectService, 'query').mockReturnValue(of(new HttpResponse({ body: projectCollection })));
      const additionalProjects = [project];
      const expectedCollection: IProject[] = [...additionalProjects, ...projectCollection];
      jest.spyOn(projectService, 'addProjectToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sponsoring });
      comp.ngOnInit();

      expect(projectService.query).toHaveBeenCalled();
      expect(projectService.addProjectToCollectionIfMissing).toHaveBeenCalledWith(
        projectCollection,
        ...additionalProjects.map(expect.objectContaining),
      );
      expect(comp.projectsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Currency query and add missing value', () => {
      const sponsoring: ISponsoring = { id: 456 };
      const currency: ICurrency = { id: 25706 };
      sponsoring.currency = currency;

      const currencyCollection: ICurrency[] = [{ id: 9440 }];
      jest.spyOn(currencyService, 'query').mockReturnValue(of(new HttpResponse({ body: currencyCollection })));
      const additionalCurrencies = [currency];
      const expectedCollection: ICurrency[] = [...additionalCurrencies, ...currencyCollection];
      jest.spyOn(currencyService, 'addCurrencyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sponsoring });
      comp.ngOnInit();

      expect(currencyService.query).toHaveBeenCalled();
      expect(currencyService.addCurrencyToCollectionIfMissing).toHaveBeenCalledWith(
        currencyCollection,
        ...additionalCurrencies.map(expect.objectContaining),
      );
      expect(comp.currenciesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sponsoring: ISponsoring = { id: 456 };
      const sponsor: IUserCustom = { id: 23430 };
      sponsoring.sponsor = sponsor;
      const project: IProject = { id: 16878 };
      sponsoring.project = project;
      const currency: ICurrency = { id: 24991 };
      sponsoring.currency = currency;

      activatedRoute.data = of({ sponsoring });
      comp.ngOnInit();

      expect(comp.userCustomsSharedCollection).toContain(sponsor);
      expect(comp.projectsSharedCollection).toContain(project);
      expect(comp.currenciesSharedCollection).toContain(currency);
      expect(comp.sponsoring).toEqual(sponsoring);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISponsoring>>();
      const sponsoring = { id: 123 };
      jest.spyOn(sponsoringFormService, 'getSponsoring').mockReturnValue(sponsoring);
      jest.spyOn(sponsoringService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sponsoring });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sponsoring }));
      saveSubject.complete();

      // THEN
      expect(sponsoringFormService.getSponsoring).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sponsoringService.update).toHaveBeenCalledWith(expect.objectContaining(sponsoring));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISponsoring>>();
      const sponsoring = { id: 123 };
      jest.spyOn(sponsoringFormService, 'getSponsoring').mockReturnValue({ id: null });
      jest.spyOn(sponsoringService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sponsoring: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sponsoring }));
      saveSubject.complete();

      // THEN
      expect(sponsoringFormService.getSponsoring).toHaveBeenCalled();
      expect(sponsoringService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISponsoring>>();
      const sponsoring = { id: 123 };
      jest.spyOn(sponsoringService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sponsoring });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sponsoringService.update).toHaveBeenCalled();
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

    describe('compareProject', () => {
      it('Should forward to projectService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(projectService, 'compareProject');
        comp.compareProject(entity, entity2);
        expect(projectService.compareProject).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCurrency', () => {
      it('Should forward to currencyService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(currencyService, 'compareCurrency');
        comp.compareCurrency(entity, entity2);
        expect(currencyService.compareCurrency).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
