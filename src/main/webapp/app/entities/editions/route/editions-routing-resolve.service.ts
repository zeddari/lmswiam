import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEditions } from '../editions.model';
import { EditionsService } from '../service/editions.service';

export const editionsResolve = (route: ActivatedRouteSnapshot): Observable<null | IEditions> => {
  const id = route.params['id'];
  if (id) {
    return inject(EditionsService)
      .find(id)
      .pipe(
        mergeMap((editions: HttpResponse<IEditions>) => {
          if (editions.body) {
            return of(editions.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default editionsResolve;
