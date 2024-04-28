import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDepense } from '../depense.model';
import { DepenseService } from '../service/depense.service';

export const depenseResolve = (route: ActivatedRouteSnapshot): Observable<null | IDepense> => {
  const id = route.params['id'];
  if (id) {
    return inject(DepenseService)
      .find(id)
      .pipe(
        mergeMap((depense: HttpResponse<IDepense>) => {
          if (depense.body) {
            return of(depense.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default depenseResolve;
