import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IDepense, NewDepense } from '../depense.model';

export type PartialUpdateDepense = Partial<IDepense> & Pick<IDepense, 'id'>;

export type EntityResponseType = HttpResponse<IDepense>;
export type EntityArrayResponseType = HttpResponse<IDepense[]>;

@Injectable({ providedIn: 'root' })
export class DepenseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/depenses');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/depenses/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(depense: NewDepense): Observable<EntityResponseType> {
    return this.http.post<IDepense>(this.resourceUrl, depense, { observe: 'response' });
  }

  update(depense: IDepense): Observable<EntityResponseType> {
    return this.http.put<IDepense>(`${this.resourceUrl}/${this.getDepenseIdentifier(depense)}`, depense, { observe: 'response' });
  }

  partialUpdate(depense: PartialUpdateDepense): Observable<EntityResponseType> {
    return this.http.patch<IDepense>(`${this.resourceUrl}/${this.getDepenseIdentifier(depense)}`, depense, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDepense>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDepense[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDepense[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IDepense[]>()], asapScheduler)));
  }

  getDepenseIdentifier(depense: Pick<IDepense, 'id'>): number {
    return depense.id;
  }

  compareDepense(o1: Pick<IDepense, 'id'> | null, o2: Pick<IDepense, 'id'> | null): boolean {
    return o1 && o2 ? this.getDepenseIdentifier(o1) === this.getDepenseIdentifier(o2) : o1 === o2;
  }

  addDepenseToCollectionIfMissing<Type extends Pick<IDepense, 'id'>>(
    depenseCollection: Type[],
    ...depensesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const depenses: Type[] = depensesToCheck.filter(isPresent);
    if (depenses.length > 0) {
      const depenseCollectionIdentifiers = depenseCollection.map(depenseItem => this.getDepenseIdentifier(depenseItem)!);
      const depensesToAdd = depenses.filter(depenseItem => {
        const depenseIdentifier = this.getDepenseIdentifier(depenseItem);
        if (depenseCollectionIdentifiers.includes(depenseIdentifier)) {
          return false;
        }
        depenseCollectionIdentifiers.push(depenseIdentifier);
        return true;
      });
      return [...depensesToAdd, ...depenseCollection];
    }
    return depenseCollection;
  }
}
