import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IJuzs, NewJuzs } from '../juzs.model';

export type PartialUpdateJuzs = Partial<IJuzs> & Pick<IJuzs, 'id'>;

type RestOf<T extends IJuzs | NewJuzs> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestJuzs = RestOf<IJuzs>;

export type NewRestJuzs = RestOf<NewJuzs>;

export type PartialUpdateRestJuzs = RestOf<PartialUpdateJuzs>;

export type EntityResponseType = HttpResponse<IJuzs>;
export type EntityArrayResponseType = HttpResponse<IJuzs[]>;

@Injectable({ providedIn: 'root' })
export class JuzsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/juzs');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/juzs/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(juzs: NewJuzs): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(juzs);
    return this.http.post<RestJuzs>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(juzs: IJuzs): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(juzs);
    return this.http
      .put<RestJuzs>(`${this.resourceUrl}/${this.getJuzsIdentifier(juzs)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(juzs: PartialUpdateJuzs): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(juzs);
    return this.http
      .patch<RestJuzs>(`${this.resourceUrl}/${this.getJuzsIdentifier(juzs)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestJuzs>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestJuzs[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestJuzs[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IJuzs[]>()], asapScheduler)),
    );
  }

  getJuzsIdentifier(juzs: Pick<IJuzs, 'id'>): number {
    return juzs.id;
  }

  compareJuzs(o1: Pick<IJuzs, 'id'> | null, o2: Pick<IJuzs, 'id'> | null): boolean {
    return o1 && o2 ? this.getJuzsIdentifier(o1) === this.getJuzsIdentifier(o2) : o1 === o2;
  }

  addJuzsToCollectionIfMissing<Type extends Pick<IJuzs, 'id'>>(
    juzsCollection: Type[],
    ...juzsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const juzs: Type[] = juzsToCheck.filter(isPresent);
    if (juzs.length > 0) {
      const juzsCollectionIdentifiers = juzsCollection.map(juzsItem => this.getJuzsIdentifier(juzsItem)!);
      const juzsToAdd = juzs.filter(juzsItem => {
        const juzsIdentifier = this.getJuzsIdentifier(juzsItem);
        if (juzsCollectionIdentifiers.includes(juzsIdentifier)) {
          return false;
        }
        juzsCollectionIdentifiers.push(juzsIdentifier);
        return true;
      });
      return [...juzsToAdd, ...juzsCollection];
    }
    return juzsCollection;
  }

  protected convertDateFromClient<T extends IJuzs | NewJuzs | PartialUpdateJuzs>(juzs: T): RestOf<T> {
    return {
      ...juzs,
      createdAt: juzs.createdAt?.toJSON() ?? null,
      updatedAt: juzs.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restJuzs: RestJuzs): IJuzs {
    return {
      ...restJuzs,
      createdAt: restJuzs.createdAt ? dayjs(restJuzs.createdAt) : undefined,
      updatedAt: restJuzs.updatedAt ? dayjs(restJuzs.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestJuzs>): HttpResponse<IJuzs> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestJuzs[]>): HttpResponse<IJuzs[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
