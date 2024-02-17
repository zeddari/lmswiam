import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
import { ITypeProject } from 'app/entities/type-project/type-project.model';
import { TypeProjectService } from 'app/entities/type-project/service/type-project.service';
import { IProject } from '../project.model';
import { ProjectService } from '../service/project.service';
import { ProjectFormService } from './project-form.service';

import { ProjectUpdateComponent } from './project-update.component';

describe('Project Management Update Component', () => {
  let comp: ProjectUpdateComponent;
  let fixture: ComponentFixture<ProjectUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let projectFormService: ProjectFormService;
  let projectService: ProjectService;
  let siteService: SiteService;
  let typeProjectService: TypeProjectService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ProjectUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ProjectUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProjectUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    projectFormService = TestBed.inject(ProjectFormService);
    projectService = TestBed.inject(ProjectService);
    siteService = TestBed.inject(SiteService);
    typeProjectService = TestBed.inject(TypeProjectService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Site query and add missing value', () => {
      const project: IProject = { id: 456 };
      const site12: ISite = { id: 11384 };
      project.site12 = site12;

      const siteCollection: ISite[] = [{ id: 31911 }];
      jest.spyOn(siteService, 'query').mockReturnValue(of(new HttpResponse({ body: siteCollection })));
      const additionalSites = [site12];
      const expectedCollection: ISite[] = [...additionalSites, ...siteCollection];
      jest.spyOn(siteService, 'addSiteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ project });
      comp.ngOnInit();

      expect(siteService.query).toHaveBeenCalled();
      expect(siteService.addSiteToCollectionIfMissing).toHaveBeenCalledWith(
        siteCollection,
        ...additionalSites.map(expect.objectContaining),
      );
      expect(comp.sitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TypeProject query and add missing value', () => {
      const project: IProject = { id: 456 };
      const typeProject: ITypeProject = { id: 20683 };
      project.typeProject = typeProject;

      const typeProjectCollection: ITypeProject[] = [{ id: 28607 }];
      jest.spyOn(typeProjectService, 'query').mockReturnValue(of(new HttpResponse({ body: typeProjectCollection })));
      const additionalTypeProjects = [typeProject];
      const expectedCollection: ITypeProject[] = [...additionalTypeProjects, ...typeProjectCollection];
      jest.spyOn(typeProjectService, 'addTypeProjectToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ project });
      comp.ngOnInit();

      expect(typeProjectService.query).toHaveBeenCalled();
      expect(typeProjectService.addTypeProjectToCollectionIfMissing).toHaveBeenCalledWith(
        typeProjectCollection,
        ...additionalTypeProjects.map(expect.objectContaining),
      );
      expect(comp.typeProjectsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const project: IProject = { id: 456 };
      const site12: ISite = { id: 12930 };
      project.site12 = site12;
      const typeProject: ITypeProject = { id: 26469 };
      project.typeProject = typeProject;

      activatedRoute.data = of({ project });
      comp.ngOnInit();

      expect(comp.sitesSharedCollection).toContain(site12);
      expect(comp.typeProjectsSharedCollection).toContain(typeProject);
      expect(comp.project).toEqual(project);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProject>>();
      const project = { id: 123 };
      jest.spyOn(projectFormService, 'getProject').mockReturnValue(project);
      jest.spyOn(projectService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ project });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: project }));
      saveSubject.complete();

      // THEN
      expect(projectFormService.getProject).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(projectService.update).toHaveBeenCalledWith(expect.objectContaining(project));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProject>>();
      const project = { id: 123 };
      jest.spyOn(projectFormService, 'getProject').mockReturnValue({ id: null });
      jest.spyOn(projectService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ project: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: project }));
      saveSubject.complete();

      // THEN
      expect(projectFormService.getProject).toHaveBeenCalled();
      expect(projectService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProject>>();
      const project = { id: 123 };
      jest.spyOn(projectService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ project });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(projectService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSite', () => {
      it('Should forward to siteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(siteService, 'compareSite');
        comp.compareSite(entity, entity2);
        expect(siteService.compareSite).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTypeProject', () => {
      it('Should forward to typeProjectService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(typeProjectService, 'compareTypeProject');
        comp.compareTypeProject(entity, entity2);
        expect(typeProjectService.compareTypeProject).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
