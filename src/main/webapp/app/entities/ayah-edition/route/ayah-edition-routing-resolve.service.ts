import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAyahEdition } from '../ayah-edition.model';
import { AyahEditionService } from '../service/ayah-edition.service';

export const ayahEditionResolve = (route: ActivatedRouteSnapshot): Observable<null | IAyahEdition> => {
  const id = route.params['id'];
  if (id) {
    return inject(AyahEditionService)
      .find(id)
      .pipe(
        mergeMap((ayahEdition: HttpResponse<IAyahEdition>) => {
          if (ayahEdition.body) {
            return of(ayahEdition.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default ayahEditionResolve;
