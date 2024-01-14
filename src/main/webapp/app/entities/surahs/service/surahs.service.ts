import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { ISurahs, NewSurahs } from '../surahs.model';

export type PartialUpdateSurahs = Partial<ISurahs> & Pick<ISurahs, 'id'>;

type RestOf<T extends ISurahs | NewSurahs> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestSurahs = RestOf<ISurahs>;

export type NewRestSurahs = RestOf<NewSurahs>;

export type PartialUpdateRestSurahs = RestOf<PartialUpdateSurahs>;

export type EntityResponseType = HttpResponse<ISurahs>;
export type EntityArrayResponseType = HttpResponse<ISurahs[]>;

@Injectable({ providedIn: 'root' })
export class SurahsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/surahs');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/surahs/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(surahs: NewSurahs): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(surahs);
    return this.http
      .post<RestSurahs>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(surahs: ISurahs): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(surahs);
    return this.http
      .put<RestSurahs>(`${this.resourceUrl}/${this.getSurahsIdentifier(surahs)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(surahs: PartialUpdateSurahs): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(surahs);
    return this.http
      .patch<RestSurahs>(`${this.resourceUrl}/${this.getSurahsIdentifier(surahs)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSurahs>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSurahs[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestSurahs[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<ISurahs[]>()], asapScheduler)),
    );
  }

  getSurahsIdentifier(surahs: Pick<ISurahs, 'id'>): number {
    return surahs.id;
  }

  compareSurahs(o1: Pick<ISurahs, 'id'> | null, o2: Pick<ISurahs, 'id'> | null): boolean {
    return o1 && o2 ? this.getSurahsIdentifier(o1) === this.getSurahsIdentifier(o2) : o1 === o2;
  }

  addSurahsToCollectionIfMissing<Type extends Pick<ISurahs, 'id'>>(
    surahsCollection: Type[],
    ...surahsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const surahs: Type[] = surahsToCheck.filter(isPresent);
    if (surahs.length > 0) {
      const surahsCollectionIdentifiers = surahsCollection.map(surahsItem => this.getSurahsIdentifier(surahsItem)!);
      const surahsToAdd = surahs.filter(surahsItem => {
        const surahsIdentifier = this.getSurahsIdentifier(surahsItem);
        if (surahsCollectionIdentifiers.includes(surahsIdentifier)) {
          return false;
        }
        surahsCollectionIdentifiers.push(surahsIdentifier);
        return true;
      });
      return [...surahsToAdd, ...surahsCollection];
    }
    return surahsCollection;
  }

  protected convertDateFromClient<T extends ISurahs | NewSurahs | PartialUpdateSurahs>(surahs: T): RestOf<T> {
    return {
      ...surahs,
      createdAt: surahs.createdAt?.toJSON() ?? null,
      updatedAt: surahs.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSurahs: RestSurahs): ISurahs {
    return {
      ...restSurahs,
      createdAt: restSurahs.createdAt ? dayjs(restSurahs.createdAt) : undefined,
      updatedAt: restSurahs.updatedAt ? dayjs(restSurahs.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSurahs>): HttpResponse<ISurahs> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSurahs[]>): HttpResponse<ISurahs[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
