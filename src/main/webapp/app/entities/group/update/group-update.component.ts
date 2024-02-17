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
import { IUserCustom } from 'app/entities/user-custom/user-custom.model';
import { UserCustomService } from 'app/entities/user-custom/service/user-custom.service';
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
import { GroupType } from 'app/entities/enumerations/group-type.model';
import { GroupService } from '../service/group.service';
import { IGroup } from '../group.model';
import { GroupFormService, GroupFormGroup } from './group-form.service';

@Component({
  standalone: true,
  selector: 'jhi-group-update',
  templateUrl: './group-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class GroupUpdateComponent implements OnInit {
  isSaving = false;
  group: IGroup | null = null;
  groupTypeValues = Object.keys(GroupType);

  groupsSharedCollection: IGroup[] = [];
  userCustomsSharedCollection: IUserCustom[] = [];
  sitesSharedCollection: ISite[] = [];

  editForm: GroupFormGroup = this.groupFormService.createGroupFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected groupService: GroupService,
    protected groupFormService: GroupFormService,
    protected userCustomService: UserCustomService,
    protected siteService: SiteService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareGroup = (o1: IGroup | null, o2: IGroup | null): boolean => this.groupService.compareGroup(o1, o2);

  compareUserCustom = (o1: IUserCustom | null, o2: IUserCustom | null): boolean => this.userCustomService.compareUserCustom(o1, o2);

  compareSite = (o1: ISite | null, o2: ISite | null): boolean => this.siteService.compareSite(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ group }) => {
      this.group = group;
      if (group) {
        this.updateForm(group);
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
    const group = this.groupFormService.getGroup(this.editForm);
    if (group.id !== null) {
      this.subscribeToSaveResponse(this.groupService.update(group));
    } else {
      this.subscribeToSaveResponse(this.groupService.create(group));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGroup>>): void {
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

  protected updateForm(group: IGroup): void {
    this.group = group;
    this.groupFormService.resetForm(this.editForm, group);

    this.groupsSharedCollection = this.groupService.addGroupToCollectionIfMissing<IGroup>(this.groupsSharedCollection, group.group1);
    this.userCustomsSharedCollection = this.userCustomService.addUserCustomToCollectionIfMissing<IUserCustom>(
      this.userCustomsSharedCollection,
      ...(group.elements ?? []),
    );
    this.sitesSharedCollection = this.siteService.addSiteToCollectionIfMissing<ISite>(this.sitesSharedCollection, group.site11);
  }

  protected loadRelationshipsOptions(): void {
    this.groupService
      .query()
      .pipe(map((res: HttpResponse<IGroup[]>) => res.body ?? []))
      .pipe(map((groups: IGroup[]) => this.groupService.addGroupToCollectionIfMissing<IGroup>(groups, this.group?.group1)))
      .subscribe((groups: IGroup[]) => (this.groupsSharedCollection = groups));

    this.userCustomService
      .query()
      .pipe(map((res: HttpResponse<IUserCustom[]>) => res.body ?? []))
      .pipe(
        map((userCustoms: IUserCustom[]) =>
          this.userCustomService.addUserCustomToCollectionIfMissing<IUserCustom>(userCustoms, ...(this.group?.elements ?? [])),
        ),
      )
      .subscribe((userCustoms: IUserCustom[]) => (this.userCustomsSharedCollection = userCustoms));

    this.siteService
      .query()
      .pipe(map((res: HttpResponse<ISite[]>) => res.body ?? []))
      .pipe(map((sites: ISite[]) => this.siteService.addSiteToCollectionIfMissing<ISite>(sites, this.group?.site11)))
      .subscribe((sites: ISite[]) => (this.sitesSharedCollection = sites));
  }
}
