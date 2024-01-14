import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITypeProject } from '../type-project.model';
import { TypeProjectService } from '../service/type-project.service';
import { TypeProjectFormService, TypeProjectFormGroup } from './type-project-form.service';

@Component({
  standalone: true,
  selector: 'jhi-type-project-update',
  templateUrl: './type-project-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TypeProjectUpdateComponent implements OnInit {
  isSaving = false;
  typeProject: ITypeProject | null = null;

  editForm: TypeProjectFormGroup = this.typeProjectFormService.createTypeProjectFormGroup();

  constructor(
    protected typeProjectService: TypeProjectService,
    protected typeProjectFormService: TypeProjectFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeProject }) => {
      this.typeProject = typeProject;
      if (typeProject) {
        this.updateForm(typeProject);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeProject = this.typeProjectFormService.getTypeProject(this.editForm);
    if (typeProject.id !== null) {
      this.subscribeToSaveResponse(this.typeProjectService.update(typeProject));
    } else {
      this.subscribeToSaveResponse(this.typeProjectService.create(typeProject));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeProject>>): void {
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

  protected updateForm(typeProject: ITypeProject): void {
    this.typeProject = typeProject;
    this.typeProjectFormService.resetForm(this.editForm, typeProject);
  }
}
