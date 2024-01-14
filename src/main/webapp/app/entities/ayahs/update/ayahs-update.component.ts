import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAyahs } from '../ayahs.model';
import { AyahsService } from '../service/ayahs.service';
import { AyahsFormService, AyahsFormGroup } from './ayahs-form.service';

@Component({
  standalone: true,
  selector: 'jhi-ayahs-update',
  templateUrl: './ayahs-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AyahsUpdateComponent implements OnInit {
  isSaving = false;
  ayahs: IAyahs | null = null;

  editForm: AyahsFormGroup = this.ayahsFormService.createAyahsFormGroup();

  constructor(
    protected ayahsService: AyahsService,
    protected ayahsFormService: AyahsFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ayahs }) => {
      this.ayahs = ayahs;
      if (ayahs) {
        this.updateForm(ayahs);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ayahs = this.ayahsFormService.getAyahs(this.editForm);
    if (ayahs.id !== null) {
      this.subscribeToSaveResponse(this.ayahsService.update(ayahs));
    } else {
      this.subscribeToSaveResponse(this.ayahsService.create(ayahs));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAyahs>>): void {
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

  protected updateForm(ayahs: IAyahs): void {
    this.ayahs = ayahs;
    this.ayahsFormService.resetForm(this.editForm, ayahs);
  }
}
