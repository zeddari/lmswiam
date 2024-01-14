import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { ISessionInstance, NewSessionInstance } from '../session-instance.model';

export type PartialUpdateSessionInstance = Partial<ISessionInstance> & Pick<ISessionInstance, 'id'>;

type RestOf<T extends ISessionInstance | NewSessionInstance> = Omit<T, 'sessionDate' | 'startTime'> & {
  sessionDate?: string | null;
  startTime?: string | null;
};

export type RestSessionInstance = RestOf<ISessionInstance>;

export type NewRestSessionInstance = RestOf<NewSessionInstance>;

export type PartialUpdateRestSessionInstance = RestOf<PartialUpdateSessionInstance>;

export type EntityResponseType = HttpResponse<ISessionInstance>;
export type EntityArrayResponseType = HttpResponse<ISessionInstance[]>;

@Injectable({ providedIn: 'root' })
export class SessionInstanceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/session-instances');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/session-instances/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(sessionInstance: NewSessionInstance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sessionInstance);
    return this.http
      .post<RestSessionInstance>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(sessionInstance: ISessionInstance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sessionInstance);
    return this.http
      .put<RestSessionInstance>(`${this.resourceUrl}/${this.getSessionInstanceIdentifier(sessionInstance)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(sessionInstance: PartialUpdateSessionInstance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sessionInstance);
    return this.http
      .patch<RestSessionInstance>(`${this.resourceUrl}/${this.getSessionInstanceIdentifier(sessionInstance)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSessionInstance>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSessionInstance[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestSessionInstance[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<ISessionInstance[]>()], asapScheduler)),
    );
  }

  getSessionInstanceIdentifier(sessionInstance: Pick<ISessionInstance, 'id'>): number {
    return sessionInstance.id;
  }

  compareSessionInstance(o1: Pick<ISessionInstance, 'id'> | null, o2: Pick<ISessionInstance, 'id'> | null): boolean {
    return o1 && o2 ? this.getSessionInstanceIdentifier(o1) === this.getSessionInstanceIdentifier(o2) : o1 === o2;
  }

  addSessionInstanceToCollectionIfMissing<Type extends Pick<ISessionInstance, 'id'>>(
    sessionInstanceCollection: Type[],
    ...sessionInstancesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sessionInstances: Type[] = sessionInstancesToCheck.filter(isPresent);
    if (sessionInstances.length > 0) {
      const sessionInstanceCollectionIdentifiers = sessionInstanceCollection.map(
        sessionInstanceItem => this.getSessionInstanceIdentifier(sessionInstanceItem)!,
      );
      const sessionInstancesToAdd = sessionInstances.filter(sessionInstanceItem => {
        const sessionInstanceIdentifier = this.getSessionInstanceIdentifier(sessionInstanceItem);
        if (sessionInstanceCollectionIdentifiers.includes(sessionInstanceIdentifier)) {
          return false;
        }
        sessionInstanceCollectionIdentifiers.push(sessionInstanceIdentifier);
        return true;
      });
      return [...sessionInstancesToAdd, ...sessionInstanceCollection];
    }
    return sessionInstanceCollection;
  }

  protected convertDateFromClient<T extends ISessionInstance | NewSessionInstance | PartialUpdateSessionInstance>(
    sessionInstance: T,
  ): RestOf<T> {
    return {
      ...sessionInstance,
      sessionDate: sessionInstance.sessionDate?.format(DATE_FORMAT) ?? null,
      startTime: sessionInstance.startTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSessionInstance: RestSessionInstance): ISessionInstance {
    return {
      ...restSessionInstance,
      sessionDate: restSessionInstance.sessionDate ? dayjs(restSessionInstance.sessionDate) : undefined,
      startTime: restSessionInstance.startTime ? dayjs(restSessionInstance.startTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSessionInstance>): HttpResponse<ISessionInstance> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSessionInstance[]>): HttpResponse<ISessionInstance[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
