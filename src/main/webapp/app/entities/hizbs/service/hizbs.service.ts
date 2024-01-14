import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IHizbs, NewHizbs } from '../hizbs.model';

export type PartialUpdateHizbs = Partial<IHizbs> & Pick<IHizbs, 'id'>;

type RestOf<T extends IHizbs | NewHizbs> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestHizbs = RestOf<IHizbs>;

export type NewRestHizbs = RestOf<NewHizbs>;

export type PartialUpdateRestHizbs = RestOf<PartialUpdateHizbs>;

export type EntityResponseType = HttpResponse<IHizbs>;
export type EntityArrayResponseType = HttpResponse<IHizbs[]>;

@Injectable({ providedIn: 'root' })
export class HizbsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/hizbs');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/hizbs/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(hizbs: NewHizbs): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hizbs);
    return this.http.post<RestHizbs>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(hizbs: IHizbs): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hizbs);
    return this.http
      .put<RestHizbs>(`${this.resourceUrl}/${this.getHizbsIdentifier(hizbs)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(hizbs: PartialUpdateHizbs): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hizbs);
    return this.http
      .patch<RestHizbs>(`${this.resourceUrl}/${this.getHizbsIdentifier(hizbs)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestHizbs>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestHizbs[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestHizbs[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IHizbs[]>()], asapScheduler)),
    );
  }

  getHizbsIdentifier(hizbs: Pick<IHizbs, 'id'>): number {
    return hizbs.id;
  }

  compareHizbs(o1: Pick<IHizbs, 'id'> | null, o2: Pick<IHizbs, 'id'> | null): boolean {
    return o1 && o2 ? this.getHizbsIdentifier(o1) === this.getHizbsIdentifier(o2) : o1 === o2;
  }

  addHizbsToCollectionIfMissing<Type extends Pick<IHizbs, 'id'>>(
    hizbsCollection: Type[],
    ...hizbsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const hizbs: Type[] = hizbsToCheck.filter(isPresent);
    if (hizbs.length > 0) {
      const hizbsCollectionIdentifiers = hizbsCollection.map(hizbsItem => this.getHizbsIdentifier(hizbsItem)!);
      const hizbsToAdd = hizbs.filter(hizbsItem => {
        const hizbsIdentifier = this.getHizbsIdentifier(hizbsItem);
        if (hizbsCollectionIdentifiers.includes(hizbsIdentifier)) {
          return false;
        }
        hizbsCollectionIdentifiers.push(hizbsIdentifier);
        return true;
      });
      return [...hizbsToAdd, ...hizbsCollection];
    }
    return hizbsCollection;
  }

  protected convertDateFromClient<T extends IHizbs | NewHizbs | PartialUpdateHizbs>(hizbs: T): RestOf<T> {
    return {
      ...hizbs,
      createdAt: hizbs.createdAt?.toJSON() ?? null,
      updatedAt: hizbs.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restHizbs: RestHizbs): IHizbs {
    return {
      ...restHizbs,
      createdAt: restHizbs.createdAt ? dayjs(restHizbs.createdAt) : undefined,
      updatedAt: restHizbs.updatedAt ? dayjs(restHizbs.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestHizbs>): HttpResponse<IHizbs> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestHizbs[]>): HttpResponse<IHizbs[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
