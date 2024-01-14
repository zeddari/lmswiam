import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IAyahEdition, NewAyahEdition } from '../ayah-edition.model';

export type PartialUpdateAyahEdition = Partial<IAyahEdition> & Pick<IAyahEdition, 'id'>;

type RestOf<T extends IAyahEdition | NewAyahEdition> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestAyahEdition = RestOf<IAyahEdition>;

export type NewRestAyahEdition = RestOf<NewAyahEdition>;

export type PartialUpdateRestAyahEdition = RestOf<PartialUpdateAyahEdition>;

export type EntityResponseType = HttpResponse<IAyahEdition>;
export type EntityArrayResponseType = HttpResponse<IAyahEdition[]>;

@Injectable({ providedIn: 'root' })
export class AyahEditionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ayah-editions');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/ayah-editions/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(ayahEdition: NewAyahEdition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ayahEdition);
    return this.http
      .post<RestAyahEdition>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(ayahEdition: IAyahEdition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ayahEdition);
    return this.http
      .put<RestAyahEdition>(`${this.resourceUrl}/${this.getAyahEditionIdentifier(ayahEdition)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(ayahEdition: PartialUpdateAyahEdition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ayahEdition);
    return this.http
      .patch<RestAyahEdition>(`${this.resourceUrl}/${this.getAyahEditionIdentifier(ayahEdition)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAyahEdition>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAyahEdition[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestAyahEdition[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IAyahEdition[]>()], asapScheduler)),
    );
  }

  getAyahEditionIdentifier(ayahEdition: Pick<IAyahEdition, 'id'>): number {
    return ayahEdition.id;
  }

  compareAyahEdition(o1: Pick<IAyahEdition, 'id'> | null, o2: Pick<IAyahEdition, 'id'> | null): boolean {
    return o1 && o2 ? this.getAyahEditionIdentifier(o1) === this.getAyahEditionIdentifier(o2) : o1 === o2;
  }

  addAyahEditionToCollectionIfMissing<Type extends Pick<IAyahEdition, 'id'>>(
    ayahEditionCollection: Type[],
    ...ayahEditionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ayahEditions: Type[] = ayahEditionsToCheck.filter(isPresent);
    if (ayahEditions.length > 0) {
      const ayahEditionCollectionIdentifiers = ayahEditionCollection.map(
        ayahEditionItem => this.getAyahEditionIdentifier(ayahEditionItem)!,
      );
      const ayahEditionsToAdd = ayahEditions.filter(ayahEditionItem => {
        const ayahEditionIdentifier = this.getAyahEditionIdentifier(ayahEditionItem);
        if (ayahEditionCollectionIdentifiers.includes(ayahEditionIdentifier)) {
          return false;
        }
        ayahEditionCollectionIdentifiers.push(ayahEditionIdentifier);
        return true;
      });
      return [...ayahEditionsToAdd, ...ayahEditionCollection];
    }
    return ayahEditionCollection;
  }

  protected convertDateFromClient<T extends IAyahEdition | NewAyahEdition | PartialUpdateAyahEdition>(ayahEdition: T): RestOf<T> {
    return {
      ...ayahEdition,
      createdAt: ayahEdition.createdAt?.toJSON() ?? null,
      updatedAt: ayahEdition.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAyahEdition: RestAyahEdition): IAyahEdition {
    return {
      ...restAyahEdition,
      createdAt: restAyahEdition.createdAt ? dayjs(restAyahEdition.createdAt) : undefined,
      updatedAt: restAyahEdition.updatedAt ? dayjs(restAyahEdition.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAyahEdition>): HttpResponse<IAyahEdition> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAyahEdition[]>): HttpResponse<IAyahEdition[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
