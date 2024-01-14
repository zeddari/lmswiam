import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IQuiz, NewQuiz } from '../quiz.model';

export type PartialUpdateQuiz = Partial<IQuiz> & Pick<IQuiz, 'id'>;

type RestOf<T extends IQuiz | NewQuiz> = Omit<T, 'deadline'> & {
  deadline?: string | null;
};

export type RestQuiz = RestOf<IQuiz>;

export type NewRestQuiz = RestOf<NewQuiz>;

export type PartialUpdateRestQuiz = RestOf<PartialUpdateQuiz>;

export type EntityResponseType = HttpResponse<IQuiz>;
export type EntityArrayResponseType = HttpResponse<IQuiz[]>;

@Injectable({ providedIn: 'root' })
export class QuizService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/quizzes');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/quizzes/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(quiz: NewQuiz): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(quiz);
    return this.http.post<RestQuiz>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(quiz: IQuiz): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(quiz);
    return this.http
      .put<RestQuiz>(`${this.resourceUrl}/${this.getQuizIdentifier(quiz)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(quiz: PartialUpdateQuiz): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(quiz);
    return this.http
      .patch<RestQuiz>(`${this.resourceUrl}/${this.getQuizIdentifier(quiz)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestQuiz>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestQuiz[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestQuiz[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IQuiz[]>()], asapScheduler)),
    );
  }

  getQuizIdentifier(quiz: Pick<IQuiz, 'id'>): number {
    return quiz.id;
  }

  compareQuiz(o1: Pick<IQuiz, 'id'> | null, o2: Pick<IQuiz, 'id'> | null): boolean {
    return o1 && o2 ? this.getQuizIdentifier(o1) === this.getQuizIdentifier(o2) : o1 === o2;
  }

  addQuizToCollectionIfMissing<Type extends Pick<IQuiz, 'id'>>(
    quizCollection: Type[],
    ...quizzesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const quizzes: Type[] = quizzesToCheck.filter(isPresent);
    if (quizzes.length > 0) {
      const quizCollectionIdentifiers = quizCollection.map(quizItem => this.getQuizIdentifier(quizItem)!);
      const quizzesToAdd = quizzes.filter(quizItem => {
        const quizIdentifier = this.getQuizIdentifier(quizItem);
        if (quizCollectionIdentifiers.includes(quizIdentifier)) {
          return false;
        }
        quizCollectionIdentifiers.push(quizIdentifier);
        return true;
      });
      return [...quizzesToAdd, ...quizCollection];
    }
    return quizCollection;
  }

  protected convertDateFromClient<T extends IQuiz | NewQuiz | PartialUpdateQuiz>(quiz: T): RestOf<T> {
    return {
      ...quiz,
      deadline: quiz.deadline?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restQuiz: RestQuiz): IQuiz {
    return {
      ...restQuiz,
      deadline: restQuiz.deadline ? dayjs(restQuiz.deadline) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestQuiz>): HttpResponse<IQuiz> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestQuiz[]>): HttpResponse<IQuiz[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
