import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INationality } from '../nationality.model';
import { NationalityService } from '../service/nationality.service';

export const nationalityResolve = (route: ActivatedRouteSnapshot): Observable<null | INationality> => {
  const id = route.params['id'];
  if (id) {
    return inject(NationalityService)
      .find(id)
      .pipe(
        mergeMap((nationality: HttpResponse<INationality>) => {
          if (nationality.body) {
            return of(nationality.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default nationalityResolve;
