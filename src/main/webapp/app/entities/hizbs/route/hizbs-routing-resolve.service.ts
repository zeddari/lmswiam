import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHizbs } from '../hizbs.model';
import { HizbsService } from '../service/hizbs.service';

export const hizbsResolve = (route: ActivatedRouteSnapshot): Observable<null | IHizbs> => {
  const id = route.params['id'];
  if (id) {
    return inject(HizbsService)
      .find(id)
      .pipe(
        mergeMap((hizbs: HttpResponse<IHizbs>) => {
          if (hizbs.body) {
            return of(hizbs.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default hizbsResolve;
