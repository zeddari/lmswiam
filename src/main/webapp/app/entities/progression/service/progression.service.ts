import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IProgression, NewProgression } from '../progression.model';

export type PartialUpdateProgression = Partial<IProgression> & Pick<IProgression, 'id'>;

export type EntityResponseType = HttpResponse<IProgression>;
export type EntityArrayResponseType = HttpResponse<IProgression[]>;

@Injectable({ providedIn: 'root' })
export class ProgressionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/progressions');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/progressions/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(progression: NewProgression): Observable<EntityResponseType> {
    return this.http.post<IProgression>(this.resourceUrl, progression, { observe: 'response' });
  }

  update(progression: IProgression): Observable<EntityResponseType> {
    return this.http.put<IProgression>(`${this.resourceUrl}/${this.getProgressionIdentifier(progression)}`, progression, {
      observe: 'response',
    });
  }

  partialUpdate(progression: PartialUpdateProgression): Observable<EntityResponseType> {
    return this.http.patch<IProgression>(`${this.resourceUrl}/${this.getProgressionIdentifier(progression)}`, progression, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProgression>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProgression[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProgression[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IProgression[]>()], asapScheduler)));
  }

  getProgressionIdentifier(progression: Pick<IProgression, 'id'>): number {
    return progression.id;
  }

  compareProgression(o1: Pick<IProgression, 'id'> | null, o2: Pick<IProgression, 'id'> | null): boolean {
    return o1 && o2 ? this.getProgressionIdentifier(o1) === this.getProgressionIdentifier(o2) : o1 === o2;
  }

  addProgressionToCollectionIfMissing<Type extends Pick<IProgression, 'id'>>(
    progressionCollection: Type[],
    ...progressionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const progressions: Type[] = progressionsToCheck.filter(isPresent);
    if (progressions.length > 0) {
      const progressionCollectionIdentifiers = progressionCollection.map(
        progressionItem => this.getProgressionIdentifier(progressionItem)!,
      );
      const progressionsToAdd = progressions.filter(progressionItem => {
        const progressionIdentifier = this.getProgressionIdentifier(progressionItem);
        if (progressionCollectionIdentifiers.includes(progressionIdentifier)) {
          return false;
        }
        progressionCollectionIdentifiers.push(progressionIdentifier);
        return true;
      });
      return [...progressionsToAdd, ...progressionCollection];
    }
    return progressionCollection;
  }
}
