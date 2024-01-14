import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISurahs } from '../surahs.model';
import { SurahsService } from '../service/surahs.service';

export const surahsResolve = (route: ActivatedRouteSnapshot): Observable<null | ISurahs> => {
  const id = route.params['id'];
  if (id) {
    return inject(SurahsService)
      .find(id)
      .pipe(
        mergeMap((surahs: HttpResponse<ISurahs>) => {
          if (surahs.body) {
            return of(surahs.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default surahsResolve;
