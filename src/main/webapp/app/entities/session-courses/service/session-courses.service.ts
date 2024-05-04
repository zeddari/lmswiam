import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { ISessionCourses, NewSessionCourses } from '../session-courses.model';

export type PartialUpdateSessionCourses = Partial<ISessionCourses> & Pick<ISessionCourses, 'id'>;

export type EntityResponseType = HttpResponse<ISessionCourses>;
export type EntityArrayResponseType = HttpResponse<ISessionCourses[]>;

@Injectable({ providedIn: 'root' })
export class SessionCoursesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/session-courses');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/session-courses/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(sessionCourses: NewSessionCourses): Observable<EntityResponseType> {
    return this.http.post<ISessionCourses>(this.resourceUrl, sessionCourses, { observe: 'response' });
  }

  update(sessionCourses: ISessionCourses): Observable<EntityResponseType> {
    return this.http.put<ISessionCourses>(`${this.resourceUrl}/${this.getSessionCoursesIdentifier(sessionCourses)}`, sessionCourses, {
      observe: 'response',
    });
  }

  partialUpdate(sessionCourses: PartialUpdateSessionCourses): Observable<EntityResponseType> {
    return this.http.patch<ISessionCourses>(`${this.resourceUrl}/${this.getSessionCoursesIdentifier(sessionCourses)}`, sessionCourses, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISessionCourses>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISessionCourses[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISessionCourses[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<ISessionCourses[]>()], asapScheduler)));
  }

  getSessionCoursesIdentifier(sessionCourses: Pick<ISessionCourses, 'id'>): number {
    return sessionCourses.id;
  }

  compareSessionCourses(o1: Pick<ISessionCourses, 'id'> | null, o2: Pick<ISessionCourses, 'id'> | null): boolean {
    return o1 && o2 ? this.getSessionCoursesIdentifier(o1) === this.getSessionCoursesIdentifier(o2) : o1 === o2;
  }

  addSessionCoursesToCollectionIfMissing<Type extends Pick<ISessionCourses, 'id'>>(
    sessionCoursesCollection: Type[],
    ...sessionCoursesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sessionCourses: Type[] = sessionCoursesToCheck.filter(isPresent);
    if (sessionCourses.length > 0) {
      const sessionCoursesCollectionIdentifiers = sessionCoursesCollection.map(
        sessionCoursesItem => this.getSessionCoursesIdentifier(sessionCoursesItem)!,
      );
      const sessionCoursesToAdd = sessionCourses.filter(sessionCoursesItem => {
        const sessionCoursesIdentifier = this.getSessionCoursesIdentifier(sessionCoursesItem);
        if (sessionCoursesCollectionIdentifiers.includes(sessionCoursesIdentifier)) {
          return false;
        }
        sessionCoursesCollectionIdentifiers.push(sessionCoursesIdentifier);
        return true;
      });
      return [...sessionCoursesToAdd, ...sessionCoursesCollection];
    }
    return sessionCoursesCollection;
  }
}
