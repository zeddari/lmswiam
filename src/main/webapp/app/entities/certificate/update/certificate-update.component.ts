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
import { IGroup } from 'app/entities/group/group.model';
import { GroupService } from 'app/entities/group/service/group.service';
import { ITopic } from 'app/entities/topic/topic.model';
import { TopicService } from 'app/entities/topic/service/topic.service';
import { CertificateType } from 'app/entities/enumerations/certificate-type.model';
import { Riwayats } from 'app/entities/enumerations/riwayats.model';
import { CertificateService } from '../service/certificate.service';
import { ICertificate } from '../certificate.model';
import { CertificateFormService, CertificateFormGroup } from './certificate-form.service';

@Component({
  standalone: true,
  selector: 'jhi-certificate-update',
  templateUrl: './certificate-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CertificateUpdateComponent implements OnInit {
  isSaving = false;
  certificate: ICertificate | null = null;
  certificateTypeValues = Object.keys(CertificateType);
  riwayatsValues = Object.keys(Riwayats);

  userCustomsSharedCollection: IUserCustom[] = [];
  groupsSharedCollection: IGroup[] = [];
  topicsSharedCollection: ITopic[] = [];

  editForm: CertificateFormGroup = this.certificateFormService.createCertificateFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected certificateService: CertificateService,
    protected certificateFormService: CertificateFormService,
    protected userCustomService: UserCustomService,
    protected groupService: GroupService,
    protected topicService: TopicService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareUserCustom = (o1: IUserCustom | null, o2: IUserCustom | null): boolean => this.userCustomService.compareUserCustom(o1, o2);

  compareGroup = (o1: IGroup | null, o2: IGroup | null): boolean => this.groupService.compareGroup(o1, o2);

  compareTopic = (o1: ITopic | null, o2: ITopic | null): boolean => this.topicService.compareTopic(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ certificate }) => {
      this.certificate = certificate;
      if (certificate) {
        this.updateForm(certificate);
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
    const certificate = this.certificateFormService.getCertificate(this.editForm);
    if (certificate.id !== null) {
      this.subscribeToSaveResponse(this.certificateService.update(certificate));
    } else {
      this.subscribeToSaveResponse(this.certificateService.create(certificate));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICertificate>>): void {
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

  protected updateForm(certificate: ICertificate): void {
    this.certificate = certificate;
    this.certificateFormService.resetForm(this.editForm, certificate);

    this.userCustomsSharedCollection = this.userCustomService.addUserCustomToCollectionIfMissing<IUserCustom>(
      this.userCustomsSharedCollection,
      certificate.userCustom6,
    );
    this.groupsSharedCollection = this.groupService.addGroupToCollectionIfMissing<IGroup>(this.groupsSharedCollection, certificate.comitte);
    this.topicsSharedCollection = this.topicService.addTopicToCollectionIfMissing<ITopic>(this.topicsSharedCollection, certificate.topic4);
  }

  protected loadRelationshipsOptions(): void {
    this.userCustomService
      .query()
      .pipe(map((res: HttpResponse<IUserCustom[]>) => res.body ?? []))
      .pipe(
        map((userCustoms: IUserCustom[]) =>
          this.userCustomService.addUserCustomToCollectionIfMissing<IUserCustom>(userCustoms, this.certificate?.userCustom6),
        ),
      )
      .subscribe((userCustoms: IUserCustom[]) => (this.userCustomsSharedCollection = userCustoms));

    this.groupService
      .query()
      .pipe(map((res: HttpResponse<IGroup[]>) => res.body ?? []))
      .pipe(map((groups: IGroup[]) => this.groupService.addGroupToCollectionIfMissing<IGroup>(groups, this.certificate?.comitte)))
      .subscribe((groups: IGroup[]) => (this.groupsSharedCollection = groups));

    this.topicService
      .query()
      .pipe(map((res: HttpResponse<ITopic[]>) => res.body ?? []))
      .pipe(map((topics: ITopic[]) => this.topicService.addTopicToCollectionIfMissing<ITopic>(topics, this.certificate?.topic4)))
      .subscribe((topics: ITopic[]) => (this.topicsSharedCollection = topics));
  }
}
