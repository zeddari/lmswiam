import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
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
import { Role } from 'app/entities/enumerations/role.model';
import { AccountStatus } from 'app/entities/enumerations/account-status.model';
import { Sex } from 'app/entities/enumerations/sex.model';
import { UserCustomService } from '../service/user-custom.service';
import { IUserCustom } from '../user-custom.model';
import { UserCustomFormService, UserCustomFormGroup } from './user-custom-form.service';

@Component({
  standalone: true,
  selector: 'jhi-user-custom-update',
  templateUrl: './user-custom-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UserCustomUpdateComponent implements OnInit {
  isSaving = false;
  userCustom: IUserCustom | null = null;
  roleValues = Object.keys(Role);
  accountStatusValues = Object.keys(AccountStatus);
  sexValues = Object.keys(Sex);

  usersSharedCollection: IUser[] = [];
  diplomasSharedCollection: IDiploma[] = [];
  languagesSharedCollection: ILanguage[] = [];
  countriesSharedCollection: ICountry[] = [];
  nationalitiesSharedCollection: INationality[] = [];
  jobsSharedCollection: IJob[] = [];
  departementsSharedCollection: IDepartement[] = [];

  editForm: UserCustomFormGroup = this.userCustomFormService.createUserCustomFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected userCustomService: UserCustomService,
    protected userCustomFormService: UserCustomFormService,
    protected userService: UserService,
    protected diplomaService: DiplomaService,
    protected languageService: LanguageService,
    protected countryService: CountryService,
    protected nationalityService: NationalityService,
    protected jobService: JobService,
    protected departementService: DepartementService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareDiploma = (o1: IDiploma | null, o2: IDiploma | null): boolean => this.diplomaService.compareDiploma(o1, o2);

  compareLanguage = (o1: ILanguage | null, o2: ILanguage | null): boolean => this.languageService.compareLanguage(o1, o2);

  compareCountry = (o1: ICountry | null, o2: ICountry | null): boolean => this.countryService.compareCountry(o1, o2);

  compareNationality = (o1: INationality | null, o2: INationality | null): boolean => this.nationalityService.compareNationality(o1, o2);

  compareJob = (o1: IJob | null, o2: IJob | null): boolean => this.jobService.compareJob(o1, o2);

  compareDepartement = (o1: IDepartement | null, o2: IDepartement | null): boolean => this.departementService.compareDepartement(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userCustom }) => {
      this.userCustom = userCustom;
      if (userCustom) {
        this.updateForm(userCustom);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('lmswiamApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userCustom = this.userCustomFormService.getUserCustom(this.editForm);
    if (userCustom.id !== null) {
      this.subscribeToSaveResponse(this.userCustomService.update(userCustom));
    } else {
      this.subscribeToSaveResponse(this.userCustomService.create(userCustom));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserCustom>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(userCustom: IUserCustom): void {
    this.userCustom = userCustom;
    this.userCustomFormService.resetForm(this.editForm, userCustom);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, userCustom.user);
    this.diplomasSharedCollection = this.diplomaService.addDiplomaToCollectionIfMissing<IDiploma>(
      this.diplomasSharedCollection,
      ...(userCustom.diplomas ?? []),
    );
    this.languagesSharedCollection = this.languageService.addLanguageToCollectionIfMissing<ILanguage>(
      this.languagesSharedCollection,
      ...(userCustom.languages ?? []),
    );
    this.countriesSharedCollection = this.countryService.addCountryToCollectionIfMissing<ICountry>(
      this.countriesSharedCollection,
      userCustom.country,
    );
    this.nationalitiesSharedCollection = this.nationalityService.addNationalityToCollectionIfMissing<INationality>(
      this.nationalitiesSharedCollection,
      userCustom.nationality,
    );
    this.jobsSharedCollection = this.jobService.addJobToCollectionIfMissing<IJob>(this.jobsSharedCollection, userCustom.job);
    this.departementsSharedCollection = this.departementService.addDepartementToCollectionIfMissing<IDepartement>(
      this.departementsSharedCollection,
      userCustom.departement2,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.userCustom?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.diplomaService
      .query()
      .pipe(map((res: HttpResponse<IDiploma[]>) => res.body ?? []))
      .pipe(
        map((diplomas: IDiploma[]) =>
          this.diplomaService.addDiplomaToCollectionIfMissing<IDiploma>(diplomas, ...(this.userCustom?.diplomas ?? [])),
        ),
      )
      .subscribe((diplomas: IDiploma[]) => (this.diplomasSharedCollection = diplomas));

    this.languageService
      .query()
      .pipe(map((res: HttpResponse<ILanguage[]>) => res.body ?? []))
      .pipe(
        map((languages: ILanguage[]) =>
          this.languageService.addLanguageToCollectionIfMissing<ILanguage>(languages, ...(this.userCustom?.languages ?? [])),
        ),
      )
      .subscribe((languages: ILanguage[]) => (this.languagesSharedCollection = languages));

    this.countryService
      .query()
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(
        map((countries: ICountry[]) => this.countryService.addCountryToCollectionIfMissing<ICountry>(countries, this.userCustom?.country)),
      )
      .subscribe((countries: ICountry[]) => (this.countriesSharedCollection = countries));

    this.nationalityService
      .query()
      .pipe(map((res: HttpResponse<INationality[]>) => res.body ?? []))
      .pipe(
        map((nationalities: INationality[]) =>
          this.nationalityService.addNationalityToCollectionIfMissing<INationality>(nationalities, this.userCustom?.nationality),
        ),
      )
      .subscribe((nationalities: INationality[]) => (this.nationalitiesSharedCollection = nationalities));

    this.jobService
      .query()
      .pipe(map((res: HttpResponse<IJob[]>) => res.body ?? []))
      .pipe(map((jobs: IJob[]) => this.jobService.addJobToCollectionIfMissing<IJob>(jobs, this.userCustom?.job)))
      .subscribe((jobs: IJob[]) => (this.jobsSharedCollection = jobs));

    this.departementService
      .query()
      .pipe(map((res: HttpResponse<IDepartement[]>) => res.body ?? []))
      .pipe(
        map((departements: IDepartement[]) =>
          this.departementService.addDepartementToCollectionIfMissing<IDepartement>(departements, this.userCustom?.departement2),
        ),
      )
      .subscribe((departements: IDepartement[]) => (this.departementsSharedCollection = departements));
  }
}
