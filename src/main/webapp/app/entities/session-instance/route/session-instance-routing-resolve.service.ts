import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISessionInstance } from '../session-instance.model';
import { SessionInstanceService } from '../service/session-instance.service';

export const sessionInstanceResolve = (route: ActivatedRouteSnapshot): Observable<null | ISessionInstance> => {
  const id = route.params['id'];
  if (id) {
    return inject(SessionInstanceService)
      .find(id)
      .pipe(
        mergeMap((sessionInstance: HttpResponse<ISessionInstance>) => {
          if (sessionInstance.body) {
            return of(sessionInstance.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default sessionInstanceResolve;
