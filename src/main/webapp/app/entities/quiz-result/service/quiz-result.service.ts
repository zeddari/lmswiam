import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IQuizResult, NewQuizResult } from '../quiz-result.model';

export type PartialUpdateQuizResult = Partial<IQuizResult> & Pick<IQuizResult, 'id'>;

type RestOf<T extends IQuizResult | NewQuizResult> = Omit<T, 'submittedAT'> & {
  submittedAT?: string | null;
};

export type RestQuizResult = RestOf<IQuizResult>;

export type NewRestQuizResult = RestOf<NewQuizResult>;

export type PartialUpdateRestQuizResult = RestOf<PartialUpdateQuizResult>;

export type EntityResponseType = HttpResponse<IQuizResult>;
export type EntityArrayResponseType = HttpResponse<IQuizResult[]>;

@Injectable({ providedIn: 'root' })
export class QuizResultService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/quiz-results');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/quiz-results/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(quizResult: NewQuizResult): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(quizResult);
    return this.http
      .post<RestQuizResult>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(quizResult: IQuizResult): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(quizResult);
    return this.http
      .put<RestQuizResult>(`${this.resourceUrl}/${this.getQuizResultIdentifier(quizResult)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(quizResult: PartialUpdateQuizResult): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(quizResult);
    return this.http
      .patch<RestQuizResult>(`${this.resourceUrl}/${this.getQuizResultIdentifier(quizResult)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestQuizResult>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestQuizResult[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestQuizResult[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IQuizResult[]>()], asapScheduler)),
    );
  }

  getQuizResultIdentifier(quizResult: Pick<IQuizResult, 'id'>): number {
    return quizResult.id;
  }

  compareQuizResult(o1: Pick<IQuizResult, 'id'> | null, o2: Pick<IQuizResult, 'id'> | null): boolean {
    return o1 && o2 ? this.getQuizResultIdentifier(o1) === this.getQuizResultIdentifier(o2) : o1 === o2;
  }

  addQuizResultToCollectionIfMissing<Type extends Pick<IQuizResult, 'id'>>(
    quizResultCollection: Type[],
    ...quizResultsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const quizResults: Type[] = quizResultsToCheck.filter(isPresent);
    if (quizResults.length > 0) {
      const quizResultCollectionIdentifiers = quizResultCollection.map(quizResultItem => this.getQuizResultIdentifier(quizResultItem)!);
      const quizResultsToAdd = quizResults.filter(quizResultItem => {
        const quizResultIdentifier = this.getQuizResultIdentifier(quizResultItem);
        if (quizResultCollectionIdentifiers.includes(quizResultIdentifier)) {
          return false;
        }
        quizResultCollectionIdentifiers.push(quizResultIdentifier);
        return true;
      });
      return [...quizResultsToAdd, ...quizResultCollection];
    }
    return quizResultCollection;
  }

  protected convertDateFromClient<T extends IQuizResult | NewQuizResult | PartialUpdateQuizResult>(quizResult: T): RestOf<T> {
    return {
      ...quizResult,
      submittedAT: quizResult.submittedAT?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restQuizResult: RestQuizResult): IQuizResult {
    return {
      ...restQuizResult,
      submittedAT: restQuizResult.submittedAT ? dayjs(restQuizResult.submittedAT) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestQuizResult>): HttpResponse<IQuizResult> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestQuizResult[]>): HttpResponse<IQuizResult[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
