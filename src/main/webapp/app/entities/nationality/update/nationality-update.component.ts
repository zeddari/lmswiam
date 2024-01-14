import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { INationality } from '../nationality.model';
import { NationalityService } from '../service/nationality.service';
import { NationalityFormService, NationalityFormGroup } from './nationality-form.service';

@Component({
  standalone: true,
  selector: 'jhi-nationality-update',
  templateUrl: './nationality-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class NationalityUpdateComponent implements OnInit {
  isSaving = false;
  nationality: INationality | null = null;

  editForm: NationalityFormGroup = this.nationalityFormService.createNationalityFormGroup();

  constructor(
    protected nationalityService: NationalityService,
    protected nationalityFormService: NationalityFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nationality }) => {
      this.nationality = nationality;
      if (nationality) {
        this.updateForm(nationality);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const nationality = this.nationalityFormService.getNationality(this.editForm);
    if (nationality.id !== null) {
      this.subscribeToSaveResponse(this.nationalityService.update(nationality));
    } else {
      this.subscribeToSaveResponse(this.nationalityService.create(nationality));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INationality>>): void {
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

  protected updateForm(nationality: INationality): void {
    this.nationality = nationality;
    this.nationalityFormService.resetForm(this.editForm, nationality);
  }
}
