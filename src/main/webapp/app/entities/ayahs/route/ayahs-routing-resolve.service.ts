import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAyahs } from '../ayahs.model';
import { AyahsService } from '../service/ayahs.service';

export const ayahsResolve = (route: ActivatedRouteSnapshot): Observable<null | IAyahs> => {
  const id = route.params['id'];
  if (id) {
    return inject(AyahsService)
      .find(id)
      .pipe(
        mergeMap((ayahs: HttpResponse<IAyahs>) => {
          if (ayahs.body) {
            return of(ayahs.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default ayahsResolve;
