import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IDiploma } from 'app/entities/diploma/diploma.model';
import { DiplomaService } from 'app/entities/diploma/service/diploma.service';
import { ILanguage } from 'app/entities/language/language.model';
import { LanguageService } from 'app/entities/language/service/language.service';
import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';
import { INationality } from 'app/entities/nationality/nationality.model';
import { NationalityService } from 'app/entities/nationality/service/nationality.service';
import { IJob } from 'app/entities/job/job.model';
import { JobService } from 'app/entities/job/service/job.service';
import { IDepartement } from 'app/entities/departement/departement.model';
import { DepartementService } from 'app/entities/departement/service/departement.service';
import { IUserCustom } from '../user-custom.model';
import { UserCustomService } from '../service/user-custom.service';
import { UserCustomFormService } from './user-custom-form.service';

import { UserCustomUpdateComponent } from './user-custom-update.component';

describe('UserCustom Management Update Component', () => {
  let comp: UserCustomUpdateComponent;
  let fixture: ComponentFixture<UserCustomUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userCustomFormService: UserCustomFormService;
  let userCustomService: UserCustomService;
  let userService: UserService;
  let diplomaService: DiplomaService;
  let languageService: LanguageService;
  let countryService: CountryService;
  let nationalityService: NationalityService;
  let jobService: JobService;
  let departementService: DepartementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), UserCustomUpdateComponent],
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
      .overrideTemplate(UserCustomUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserCustomUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userCustomFormService = TestBed.inject(UserCustomFormService);
    userCustomService = TestBed.inject(UserCustomService);
    userService = TestBed.inject(UserService);
    diplomaService = TestBed.inject(DiplomaService);
    languageService = TestBed.inject(LanguageService);
    countryService = TestBed.inject(CountryService);
    nationalityService = TestBed.inject(NationalityService);
    jobService = TestBed.inject(JobService);
    departementService = TestBed.inject(DepartementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const userCustom: IUserCustom = { id: 456 };
      const user: IUser = { id: 2497 };
      userCustom.user = user;

      const userCollection: IUser[] = [{ id: 17531 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userCustom });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Diploma query and add missing value', () => {
      const userCustom: IUserCustom = { id: 456 };
      const diplomas: IDiploma[] = [{ id: 30956 }];
      userCustom.diplomas = diplomas;

      const diplomaCollection: IDiploma[] = [{ id: 9737 }];
      jest.spyOn(diplomaService, 'query').mockReturnValue(of(new HttpResponse({ body: diplomaCollection })));
      const additionalDiplomas = [...diplomas];
      const expectedCollection: IDiploma[] = [...additionalDiplomas, ...diplomaCollection];
      jest.spyOn(diplomaService, 'addDiplomaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userCustom });
      comp.ngOnInit();

      expect(diplomaService.query).toHaveBeenCalled();
      expect(diplomaService.addDiplomaToCollectionIfMissing).toHaveBeenCalledWith(
        diplomaCollection,
        ...additionalDiplomas.map(expect.objectContaining),
      );
      expect(comp.diplomasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Language query and add missing value', () => {
      const userCustom: IUserCustom = { id: 456 };
      const languages: ILanguage[] = [{ id: 14311 }];
      userCustom.languages = languages;

      const languageCollection: ILanguage[] = [{ id: 32753 }];
      jest.spyOn(languageService, 'query').mockReturnValue(of(new HttpResponse({ body: languageCollection })));
      const additionalLanguages = [...languages];
      const expectedCollection: ILanguage[] = [...additionalLanguages, ...languageCollection];
      jest.spyOn(languageService, 'addLanguageToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userCustom });
      comp.ngOnInit();

      expect(languageService.query).toHaveBeenCalled();
      expect(languageService.addLanguageToCollectionIfMissing).toHaveBeenCalledWith(
        languageCollection,
        ...additionalLanguages.map(expect.objectContaining),
      );
      expect(comp.languagesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Country query and add missing value', () => {
      const userCustom: IUserCustom = { id: 456 };
      const country: ICountry = { id: 18531 };
      userCustom.country = country;

      const countryCollection: ICountry[] = [{ id: 29103 }];
      jest.spyOn(countryService, 'query').mockReturnValue(of(new HttpResponse({ body: countryCollection })));
      const additionalCountries = [country];
      const expectedCollection: ICountry[] = [...additionalCountries, ...countryCollection];
      jest.spyOn(countryService, 'addCountryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userCustom });
      comp.ngOnInit();

      expect(countryService.query).toHaveBeenCalled();
      expect(countryService.addCountryToCollectionIfMissing).toHaveBeenCalledWith(
        countryCollection,
        ...additionalCountries.map(expect.objectContaining),
      );
      expect(comp.countriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Nationality query and add missing value', () => {
      const userCustom: IUserCustom = { id: 456 };
      const nationality: INationality = { id: 32599 };
      userCustom.nationality = nationality;

      const nationalityCollection: INationality[] = [{ id: 12011 }];
      jest.spyOn(nationalityService, 'query').mockReturnValue(of(new HttpResponse({ body: nationalityCollection })));
      const additionalNationalities = [nationality];
      const expectedCollection: INationality[] = [...additionalNationalities, ...nationalityCollection];
      jest.spyOn(nationalityService, 'addNationalityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userCustom });
      comp.ngOnInit();

      expect(nationalityService.query).toHaveBeenCalled();
      expect(nationalityService.addNationalityToCollectionIfMissing).toHaveBeenCalledWith(
        nationalityCollection,
        ...additionalNationalities.map(expect.objectContaining),
      );
      expect(comp.nationalitiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Job query and add missing value', () => {
      const userCustom: IUserCustom = { id: 456 };
      const job: IJob = { id: 18550 };
      userCustom.job = job;

      const jobCollection: IJob[] = [{ id: 16055 }];
      jest.spyOn(jobService, 'query').mockReturnValue(of(new HttpResponse({ body: jobCollection })));
      const additionalJobs = [job];
      const expectedCollection: IJob[] = [...additionalJobs, ...jobCollection];
      jest.spyOn(jobService, 'addJobToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userCustom });
      comp.ngOnInit();

      expect(jobService.query).toHaveBeenCalled();
      expect(jobService.addJobToCollectionIfMissing).toHaveBeenCalledWith(jobCollection, ...additionalJobs.map(expect.objectContaining));
      expect(comp.jobsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Departement query and add missing value', () => {
      const userCustom: IUserCustom = { id: 456 };
      const departement2: IDepartement = { id: 18306 };
      userCustom.departement2 = departement2;

      const departementCollection: IDepartement[] = [{ id: 1906 }];
      jest.spyOn(departementService, 'query').mockReturnValue(of(new HttpResponse({ body: departementCollection })));
      const additionalDepartements = [departement2];
      const expectedCollection: IDepartement[] = [...additionalDepartements, ...departementCollection];
      jest.spyOn(departementService, 'addDepartementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userCustom });
      comp.ngOnInit();

      expect(departementService.query).toHaveBeenCalled();
      expect(departementService.addDepartementToCollectionIfMissing).toHaveBeenCalledWith(
        departementCollection,
        ...additionalDepartements.map(expect.objectContaining),
      );
      expect(comp.departementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const userCustom: IUserCustom = { id: 456 };
      const user: IUser = { id: 4150 };
      userCustom.user = user;
      const diplomas: IDiploma = { id: 21506 };
      userCustom.diplomas = [diplomas];
      const languages: ILanguage = { id: 306 };
      userCustom.languages = [languages];
      const country: ICountry = { id: 14834 };
      userCustom.country = country;
      const nationality: INationality = { id: 6386 };
      userCustom.nationality = nationality;
      const job: IJob = { id: 8691 };
      userCustom.job = job;
      const departement2: IDepartement = { id: 26717 };
      userCustom.departement2 = departement2;

      activatedRoute.data = of({ userCustom });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.diplomasSharedCollection).toContain(diplomas);
      expect(comp.languagesSharedCollection).toContain(languages);
      expect(comp.countriesSharedCollection).toContain(country);
      expect(comp.nationalitiesSharedCollection).toContain(nationality);
      expect(comp.jobsSharedCollection).toContain(job);
      expect(comp.departementsSharedCollection).toContain(departement2);
      expect(comp.userCustom).toEqual(userCustom);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserCustom>>();
      const userCustom = { id: 123 };
      jest.spyOn(userCustomFormService, 'getUserCustom').mockReturnValue(userCustom);
      jest.spyOn(userCustomService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userCustom });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userCustom }));
      saveSubject.complete();

      // THEN
      expect(userCustomFormService.getUserCustom).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userCustomService.update).toHaveBeenCalledWith(expect.objectContaining(userCustom));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserCustom>>();
      const userCustom = { id: 123 };
      jest.spyOn(userCustomFormService, 'getUserCustom').mockReturnValue({ id: null });
      jest.spyOn(userCustomService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userCustom: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userCustom }));
      saveSubject.complete();

      // THEN
      expect(userCustomFormService.getUserCustom).toHaveBeenCalled();
      expect(userCustomService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserCustom>>();
      const userCustom = { id: 123 };
      jest.spyOn(userCustomService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userCustom });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userCustomService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDiploma', () => {
      it('Should forward to diplomaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(diplomaService, 'compareDiploma');
        comp.compareDiploma(entity, entity2);
        expect(diplomaService.compareDiploma).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareLanguage', () => {
      it('Should forward to languageService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(languageService, 'compareLanguage');
        comp.compareLanguage(entity, entity2);
        expect(languageService.compareLanguage).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCountry', () => {
      it('Should forward to countryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(countryService, 'compareCountry');
        comp.compareCountry(entity, entity2);
        expect(countryService.compareCountry).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareNationality', () => {
      it('Should forward to nationalityService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(nationalityService, 'compareNationality');
        comp.compareNationality(entity, entity2);
        expect(nationalityService.compareNationality).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareJob', () => {
      it('Should forward to jobService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(jobService, 'compareJob');
        comp.compareJob(entity, entity2);
        expect(jobService.compareJob).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDepartement', () => {
      it('Should forward to departementService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(departementService, 'compareDepartement');
        comp.compareDepartement(entity, entity2);
        expect(departementService.compareDepartement).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
