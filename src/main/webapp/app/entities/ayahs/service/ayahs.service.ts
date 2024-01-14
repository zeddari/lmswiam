import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IAyahs, NewAyahs } from '../ayahs.model';

export type PartialUpdateAyahs = Partial<IAyahs> & Pick<IAyahs, 'id'>;

type RestOf<T extends IAyahs | NewAyahs> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestAyahs = RestOf<IAyahs>;

export type NewRestAyahs = RestOf<NewAyahs>;

export type PartialUpdateRestAyahs = RestOf<PartialUpdateAyahs>;

export type EntityResponseType = HttpResponse<IAyahs>;
export type EntityArrayResponseType = HttpResponse<IAyahs[]>;

@Injectable({ providedIn: 'root' })
export class AyahsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ayahs');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/ayahs/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(ayahs: NewAyahs): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ayahs);
    return this.http.post<RestAyahs>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(ayahs: IAyahs): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ayahs);
    return this.http
      .put<RestAyahs>(`${this.resourceUrl}/${this.getAyahsIdentifier(ayahs)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(ayahs: PartialUpdateAyahs): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ayahs);
    return this.http
      .patch<RestAyahs>(`${this.resourceUrl}/${this.getAyahsIdentifier(ayahs)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAyahs>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAyahs[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestAyahs[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IAyahs[]>()], asapScheduler)),
    );
  }

  getAyahsIdentifier(ayahs: Pick<IAyahs, 'id'>): number {
    return ayahs.id;
  }

  compareAyahs(o1: Pick<IAyahs, 'id'> | null, o2: Pick<IAyahs, 'id'> | null): boolean {
    return o1 && o2 ? this.getAyahsIdentifier(o1) === this.getAyahsIdentifier(o2) : o1 === o2;
  }

  addAyahsToCollectionIfMissing<Type extends Pick<IAyahs, 'id'>>(
    ayahsCollection: Type[],
    ...ayahsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ayahs: Type[] = ayahsToCheck.filter(isPresent);
    if (ayahs.length > 0) {
      const ayahsCollectionIdentifiers = ayahsCollection.map(ayahsItem => this.getAyahsIdentifier(ayahsItem)!);
      const ayahsToAdd = ayahs.filter(ayahsItem => {
        const ayahsIdentifier = this.getAyahsIdentifier(ayahsItem);
        if (ayahsCollectionIdentifiers.includes(ayahsIdentifier)) {
          return false;
        }
        ayahsCollectionIdentifiers.push(ayahsIdentifier);
        return true;
      });
      return [...ayahsToAdd, ...ayahsCollection];
    }
    return ayahsCollection;
  }

  protected convertDateFromClient<T extends IAyahs | NewAyahs | PartialUpdateAyahs>(ayahs: T): RestOf<T> {
    return {
      ...ayahs,
      createdAt: ayahs.createdAt?.toJSON() ?? null,
      updatedAt: ayahs.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAyahs: RestAyahs): IAyahs {
    return {
      ...restAyahs,
      createdAt: restAyahs.createdAt ? dayjs(restAyahs.createdAt) : undefined,
      updatedAt: restAyahs.updatedAt ? dayjs(restAyahs.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAyahs>): HttpResponse<IAyahs> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAyahs[]>): HttpResponse<IAyahs[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
