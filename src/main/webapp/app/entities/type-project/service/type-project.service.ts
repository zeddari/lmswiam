import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { ITypeProject, NewTypeProject } from '../type-project.model';

export type PartialUpdateTypeProject = Partial<ITypeProject> & Pick<ITypeProject, 'id'>;

export type EntityResponseType = HttpResponse<ITypeProject>;
export type EntityArrayResponseType = HttpResponse<ITypeProject[]>;

@Injectable({ providedIn: 'root' })
export class TypeProjectService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-projects');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/type-projects/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(typeProject: NewTypeProject): Observable<EntityResponseType> {
    return this.http.post<ITypeProject>(this.resourceUrl, typeProject, { observe: 'response' });
  }

  update(typeProject: ITypeProject): Observable<EntityResponseType> {
    return this.http.put<ITypeProject>(`${this.resourceUrl}/${this.getTypeProjectIdentifier(typeProject)}`, typeProject, {
      observe: 'response',
    });
  }

  partialUpdate(typeProject: PartialUpdateTypeProject): Observable<EntityResponseType> {
    return this.http.patch<ITypeProject>(`${this.resourceUrl}/${this.getTypeProjectIdentifier(typeProject)}`, typeProject, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeProject>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeProject[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITypeProject[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<ITypeProject[]>()], asapScheduler)));
  }

  getTypeProjectIdentifier(typeProject: Pick<ITypeProject, 'id'>): number {
    return typeProject.id;
  }

  compareTypeProject(o1: Pick<ITypeProject, 'id'> | null, o2: Pick<ITypeProject, 'id'> | null): boolean {
    return o1 && o2 ? this.getTypeProjectIdentifier(o1) === this.getTypeProjectIdentifier(o2) : o1 === o2;
  }

  addTypeProjectToCollectionIfMissing<Type extends Pick<ITypeProject, 'id'>>(
    typeProjectCollection: Type[],
    ...typeProjectsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const typeProjects: Type[] = typeProjectsToCheck.filter(isPresent);
    if (typeProjects.length > 0) {
      const typeProjectCollectionIdentifiers = typeProjectCollection.map(
        typeProjectItem => this.getTypeProjectIdentifier(typeProjectItem)!,
      );
      const typeProjectsToAdd = typeProjects.filter(typeProjectItem => {
        const typeProjectIdentifier = this.getTypeProjectIdentifier(typeProjectItem);
        if (typeProjectCollectionIdentifiers.includes(typeProjectIdentifier)) {
          return false;
        }
        typeProjectCollectionIdentifiers.push(typeProjectIdentifier);
        return true;
      });
      return [...typeProjectsToAdd, ...typeProjectCollection];
    }
    return typeProjectCollection;
  }
}
