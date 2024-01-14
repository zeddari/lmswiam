import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IJuzs } from '../juzs.model';
import { JuzsService } from '../service/juzs.service';
import { JuzsFormService, JuzsFormGroup } from './juzs-form.service';

@Component({
  standalone: true,
  selector: 'jhi-juzs-update',
  templateUrl: './juzs-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class JuzsUpdateComponent implements OnInit {
  isSaving = false;
  juzs: IJuzs | null = null;

  editForm: JuzsFormGroup = this.juzsFormService.createJuzsFormGroup();

  constructor(
    protected juzsService: JuzsService,
    protected juzsFormService: JuzsFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ juzs }) => {
      this.juzs = juzs;
      if (juzs) {
        this.updateForm(juzs);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const juzs = this.juzsFormService.getJuzs(this.editForm);
    if (juzs.id !== null) {
      this.subscribeToSaveResponse(this.juzsService.update(juzs));
    } else {
      this.subscribeToSaveResponse(this.juzsService.create(juzs));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJuzs>>): void {
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

  protected updateForm(juzs: IJuzs): void {
    this.juzs = juzs;
    this.juzsFormService.resetForm(this.editForm, juzs);
  }
}
