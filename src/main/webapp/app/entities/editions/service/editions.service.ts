import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IEditions, NewEditions } from '../editions.model';

export type PartialUpdateEditions = Partial<IEditions> & Pick<IEditions, 'id'>;

type RestOf<T extends IEditions | NewEditions> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestEditions = RestOf<IEditions>;

export type NewRestEditions = RestOf<NewEditions>;

export type PartialUpdateRestEditions = RestOf<PartialUpdateEditions>;

export type EntityResponseType = HttpResponse<IEditions>;
export type EntityArrayResponseType = HttpResponse<IEditions[]>;

@Injectable({ providedIn: 'root' })
export class EditionsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/editions');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/editions/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(editions: NewEditions): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(editions);
    return this.http
      .post<RestEditions>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(editions: IEditions): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(editions);
    return this.http
      .put<RestEditions>(`${this.resourceUrl}/${this.getEditionsIdentifier(editions)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(editions: PartialUpdateEditions): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(editions);
    return this.http
      .patch<RestEditions>(`${this.resourceUrl}/${this.getEditionsIdentifier(editions)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEditions>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEditions[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestEditions[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IEditions[]>()], asapScheduler)),
    );
  }

  getEditionsIdentifier(editions: Pick<IEditions, 'id'>): number {
    return editions.id;
  }

  compareEditions(o1: Pick<IEditions, 'id'> | null, o2: Pick<IEditions, 'id'> | null): boolean {
    return o1 && o2 ? this.getEditionsIdentifier(o1) === this.getEditionsIdentifier(o2) : o1 === o2;
  }

  addEditionsToCollectionIfMissing<Type extends Pick<IEditions, 'id'>>(
    editionsCollection: Type[],
    ...editionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const editions: Type[] = editionsToCheck.filter(isPresent);
    if (editions.length > 0) {
      const editionsCollectionIdentifiers = editionsCollection.map(editionsItem => this.getEditionsIdentifier(editionsItem)!);
      const editionsToAdd = editions.filter(editionsItem => {
        const editionsIdentifier = this.getEditionsIdentifier(editionsItem);
        if (editionsCollectionIdentifiers.includes(editionsIdentifier)) {
          return false;
        }
        editionsCollectionIdentifiers.push(editionsIdentifier);
        return true;
      });
      return [...editionsToAdd, ...editionsCollection];
    }
    return editionsCollection;
  }

  protected convertDateFromClient<T extends IEditions | NewEditions | PartialUpdateEditions>(editions: T): RestOf<T> {
    return {
      ...editions,
      createdAt: editions.createdAt?.toJSON() ?? null,
      updatedAt: editions.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restEditions: RestEditions): IEditions {
    return {
      ...restEditions,
      createdAt: restEditions.createdAt ? dayjs(restEditions.createdAt) : undefined,
      updatedAt: restEditions.updatedAt ? dayjs(restEditions.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEditions>): HttpResponse<IEditions> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEditions[]>): HttpResponse<IEditions[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
