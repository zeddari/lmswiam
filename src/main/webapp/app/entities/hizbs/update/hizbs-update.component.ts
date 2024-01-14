import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IHizbs } from '../hizbs.model';
import { HizbsService } from '../service/hizbs.service';
import { HizbsFormService, HizbsFormGroup } from './hizbs-form.service';

@Component({
  standalone: true,
  selector: 'jhi-hizbs-update',
  templateUrl: './hizbs-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class HizbsUpdateComponent implements OnInit {
  isSaving = false;
  hizbs: IHizbs | null = null;

  editForm: HizbsFormGroup = this.hizbsFormService.createHizbsFormGroup();

  constructor(
    protected hizbsService: HizbsService,
    protected hizbsFormService: HizbsFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hizbs }) => {
      this.hizbs = hizbs;
      if (hizbs) {
        this.updateForm(hizbs);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const hizbs = this.hizbsFormService.getHizbs(this.editForm);
    if (hizbs.id !== null) {
      this.subscribeToSaveResponse(this.hizbsService.update(hizbs));
    } else {
      this.subscribeToSaveResponse(this.hizbsService.create(hizbs));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHizbs>>): void {
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

  protected updateForm(hizbs: IHizbs): void {
    this.hizbs = hizbs;
    this.hizbsFormService.resetForm(this.editForm, hizbs);
  }
}
