import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQuizResult } from '../quiz-result.model';
import { QuizResultService } from '../service/quiz-result.service';

export const quizResultResolve = (route: ActivatedRouteSnapshot): Observable<null | IQuizResult> => {
  const id = route.params['id'];
  if (id) {
    return inject(QuizResultService)
      .find(id)
      .pipe(
        mergeMap((quizResult: HttpResponse<IQuizResult>) => {
          if (quizResult.body) {
            return of(quizResult.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default quizResultResolve;
