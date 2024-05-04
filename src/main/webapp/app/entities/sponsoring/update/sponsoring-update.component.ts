import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
import { IUserCustom } from 'app/entities/user-custom/user-custom.model';
import { UserCustomService } from 'app/entities/user-custom/service/user-custom.service';
import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';
import { ICurrency } from 'app/entities/currency/currency.model';
import { CurrencyService } from 'app/entities/currency/service/currency.service';
import { SponsoringRef } from 'app/entities/enumerations/sponsoring-ref.model';
import { SponsoringService } from '../service/sponsoring.service';
import { ISponsoring } from '../sponsoring.model';
import { SponsoringFormService, SponsoringFormGroup } from './sponsoring-form.service';

@Component({
  standalone: true,
  selector: 'jhi-sponsoring-update',
  templateUrl: './sponsoring-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SponsoringUpdateComponent implements OnInit {
  isSaving = false;
  sponsoring: ISponsoring | null = null;
  sponsoringRefValues = Object.keys(SponsoringRef);

  sitesSharedCollection: ISite[] = [];
  userCustomsSharedCollection: IUserCustom[] = [];
  projectsSharedCollection: IProject[] = [];
  currenciesSharedCollection: ICurrency[] = [];

  editForm: SponsoringFormGroup = this.sponsoringFormService.createSponsoringFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected sponsoringService: SponsoringService,
    protected sponsoringFormService: SponsoringFormService,
    protected siteService: SiteService,
    protected userCustomService: UserCustomService,
    protected projectService: ProjectService,
    protected currencyService: CurrencyService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareSite = (o1: ISite | null, o2: ISite | null): boolean => this.siteService.compareSite(o1, o2);

  compareUserCustom = (o1: IUserCustom | null, o2: IUserCustom | null): boolean => this.userCustomService.compareUserCustom(o1, o2);

  compareProject = (o1: IProject | null, o2: IProject | null): boolean => this.projectService.compareProject(o1, o2);

  compareCurrency = (o1: ICurrency | null, o2: ICurrency | null): boolean => this.currencyService.compareCurrency(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sponsoring }) => {
      this.sponsoring = sponsoring;
      if (sponsoring) {
        this.updateForm(sponsoring);
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

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sponsoring = this.sponsoringFormService.getSponsoring(this.editForm);
    if (sponsoring.id !== null) {
      this.subscribeToSaveResponse(this.sponsoringService.update(sponsoring));
    } else {
      this.subscribeToSaveResponse(this.sponsoringService.create(sponsoring));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISponsoring>>): void {
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

  protected updateForm(sponsoring: ISponsoring): void {
    this.sponsoring = sponsoring;
    this.sponsoringFormService.resetForm(this.editForm, sponsoring);

    this.sitesSharedCollection = this.siteService.addSiteToCollectionIfMissing<ISite>(this.sitesSharedCollection, sponsoring.site10);
    this.userCustomsSharedCollection = this.userCustomService.addUserCustomToCollectionIfMissing<IUserCustom>(
      this.userCustomsSharedCollection,
      sponsoring.sponsor,
    );
    this.projectsSharedCollection = this.projectService.addProjectToCollectionIfMissing<IProject>(
      this.projectsSharedCollection,
      sponsoring.project,
    );
    this.currenciesSharedCollection = this.currencyService.addCurrencyToCollectionIfMissing<ICurrency>(
      this.currenciesSharedCollection,
      sponsoring.currency,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.siteService
      .query()
      .pipe(map((res: HttpResponse<ISite[]>) => res.body ?? []))
      .pipe(map((sites: ISite[]) => this.siteService.addSiteToCollectionIfMissing<ISite>(sites, this.sponsoring?.site10)))
      .subscribe((sites: ISite[]) => (this.sitesSharedCollection = sites));

    this.userCustomService
      .query()
      .pipe(map((res: HttpResponse<IUserCustom[]>) => res.body ?? []))
      .pipe(
        map((userCustoms: IUserCustom[]) =>
          this.userCustomService.addUserCustomToCollectionIfMissing<IUserCustom>(userCustoms, this.sponsoring?.sponsor),
        ),
      )
      .subscribe((userCustoms: IUserCustom[]) => (this.userCustomsSharedCollection = userCustoms));

    this.projectService
      .query()
      .pipe(map((res: HttpResponse<IProject[]>) => res.body ?? []))
      .pipe(
        map((projects: IProject[]) => this.projectService.addProjectToCollectionIfMissing<IProject>(projects, this.sponsoring?.project)),
      )
      .subscribe((projects: IProject[]) => (this.projectsSharedCollection = projects));

    this.currencyService
      .query()
      .pipe(map((res: HttpResponse<ICurrency[]>) => res.body ?? []))
      .pipe(
        map((currencies: ICurrency[]) =>
          this.currencyService.addCurrencyToCollectionIfMissing<ICurrency>(currencies, this.sponsoring?.currency),
        ),
      )
      .subscribe((currencies: ICurrency[]) => (this.currenciesSharedCollection = currencies));
  }
}
