import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IJuzs } from '../juzs.model';
import { JuzsService } from '../service/juzs.service';

export const juzsResolve = (route: ActivatedRouteSnapshot): Observable<null | IJuzs> => {
  const id = route.params['id'];
  if (id) {
    return inject(JuzsService)
      .find(id)
      .pipe(
        mergeMap((juzs: HttpResponse<IJuzs>) => {
          if (juzs.body) {
            return of(juzs.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default juzsResolve;
