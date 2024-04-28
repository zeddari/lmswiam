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
import { IStudentSponsoring, NewStudentSponsoring } from '../student-sponsoring.model';

export type PartialUpdateStudentSponsoring = Partial<IStudentSponsoring> & Pick<IStudentSponsoring, 'id'>;

type RestOf<T extends IStudentSponsoring | NewStudentSponsoring> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestStudentSponsoring = RestOf<IStudentSponsoring>;

export type NewRestStudentSponsoring = RestOf<NewStudentSponsoring>;

export type PartialUpdateRestStudentSponsoring = RestOf<PartialUpdateStudentSponsoring>;

export type EntityResponseType = HttpResponse<IStudentSponsoring>;
export type EntityArrayResponseType = HttpResponse<IStudentSponsoring[]>;

@Injectable({ providedIn: 'root' })
export class StudentSponsoringService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/student-sponsorings');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/student-sponsorings/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(studentSponsoring: NewStudentSponsoring): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentSponsoring);
    return this.http
      .post<RestStudentSponsoring>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(studentSponsoring: IStudentSponsoring): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentSponsoring);
    return this.http
      .put<RestStudentSponsoring>(`${this.resourceUrl}/${this.getStudentSponsoringIdentifier(studentSponsoring)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(studentSponsoring: PartialUpdateStudentSponsoring): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(studentSponsoring);
    return this.http
      .patch<RestStudentSponsoring>(`${this.resourceUrl}/${this.getStudentSponsoringIdentifier(studentSponsoring)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestStudentSponsoring>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestStudentSponsoring[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestStudentSponsoring[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IStudentSponsoring[]>()], asapScheduler)),
    );
  }

  getStudentSponsoringIdentifier(studentSponsoring: Pick<IStudentSponsoring, 'id'>): number {
    return studentSponsoring.id;
  }

  compareStudentSponsoring(o1: Pick<IStudentSponsoring, 'id'> | null, o2: Pick<IStudentSponsoring, 'id'> | null): boolean {
    return o1 && o2 ? this.getStudentSponsoringIdentifier(o1) === this.getStudentSponsoringIdentifier(o2) : o1 === o2;
  }

  addStudentSponsoringToCollectionIfMissing<Type extends Pick<IStudentSponsoring, 'id'>>(
    studentSponsoringCollection: Type[],
    ...studentSponsoringsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const studentSponsorings: Type[] = studentSponsoringsToCheck.filter(isPresent);
    if (studentSponsorings.length > 0) {
      const studentSponsoringCollectionIdentifiers = studentSponsoringCollection.map(
        studentSponsoringItem => this.getStudentSponsoringIdentifier(studentSponsoringItem)!,
      );
      const studentSponsoringsToAdd = studentSponsorings.filter(studentSponsoringItem => {
        const studentSponsoringIdentifier = this.getStudentSponsoringIdentifier(studentSponsoringItem);
        if (studentSponsoringCollectionIdentifiers.includes(studentSponsoringIdentifier)) {
          return false;
        }
        studentSponsoringCollectionIdentifiers.push(studentSponsoringIdentifier);
        return true;
      });
      return [...studentSponsoringsToAdd, ...studentSponsoringCollection];
    }
    return studentSponsoringCollection;
  }

  protected convertDateFromClient<T extends IStudentSponsoring | NewStudentSponsoring | PartialUpdateStudentSponsoring>(
    studentSponsoring: T,
  ): RestOf<T> {
    return {
      ...studentSponsoring,
      startDate: studentSponsoring.startDate?.format(DATE_FORMAT) ?? null,
      endDate: studentSponsoring.endDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restStudentSponsoring: RestStudentSponsoring): IStudentSponsoring {
    return {
      ...restStudentSponsoring,
      startDate: restStudentSponsoring.startDate ? dayjs(restStudentSponsoring.startDate) : undefined,
      endDate: restStudentSponsoring.endDate ? dayjs(restStudentSponsoring.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestStudentSponsoring>): HttpResponse<IStudentSponsoring> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestStudentSponsoring[]>): HttpResponse<IStudentSponsoring[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
