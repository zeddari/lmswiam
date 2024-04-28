import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUserCustom } from 'app/entities/user-custom/user-custom.model';
import { UserCustomService } from 'app/entities/user-custom/service/user-custom.service';
import { DepenseType } from 'app/entities/enumerations/depense-type.model';
import { DepenseTarget } from 'app/entities/enumerations/depense-target.model';
import { DepenseFrequency } from 'app/entities/enumerations/depense-frequency.model';
import { DepenseService } from '../service/depense.service';
import { IDepense } from '../depense.model';
import { DepenseFormService, DepenseFormGroup } from './depense-form.service';

@Component({
  standalone: true,
  selector: 'jhi-depense-update',
  templateUrl: './depense-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DepenseUpdateComponent implements OnInit {
  isSaving = false;
  depense: IDepense | null = null;
  depenseTypeValues = Object.keys(DepenseType);
  depenseTargetValues = Object.keys(DepenseTarget);
  depenseFrequencyValues = Object.keys(DepenseFrequency);

  userCustomsSharedCollection: IUserCustom[] = [];

  editForm: DepenseFormGroup = this.depenseFormService.createDepenseFormGroup();

  constructor(
    protected depenseService: DepenseService,
    protected depenseFormService: DepenseFormService,
    protected userCustomService: UserCustomService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareUserCustom = (o1: IUserCustom | null, o2: IUserCustom | null): boolean => this.userCustomService.compareUserCustom(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ depense }) => {
      this.depense = depense;
      if (depense) {
        this.updateForm(depense);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const depense = this.depenseFormService.getDepense(this.editForm);
    if (depense.id !== null) {
      this.subscribeToSaveResponse(this.depenseService.update(depense));
    } else {
      this.subscribeToSaveResponse(this.depenseService.create(depense));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepense>>): void {
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

  protected updateForm(depense: IDepense): void {
    this.depense = depense;
    this.depenseFormService.resetForm(this.editForm, depense);

    this.userCustomsSharedCollection = this.userCustomService.addUserCustomToCollectionIfMissing<IUserCustom>(
      this.userCustomsSharedCollection,
      depense.resource,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userCustomService
      .query()
      .pipe(map((res: HttpResponse<IUserCustom[]>) => res.body ?? []))
      .pipe(
        map((userCustoms: IUserCustom[]) =>
          this.userCustomService.addUserCustomToCollectionIfMissing<IUserCustom>(userCustoms, this.depense?.resource),
        ),
      )
      .subscribe((userCustoms: IUserCustom[]) => (this.userCustomsSharedCollection = userCustoms));
  }
}
