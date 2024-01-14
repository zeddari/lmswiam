import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypeProject } from '../type-project.model';
import { TypeProjectService } from '../service/type-project.service';

export const typeProjectResolve = (route: ActivatedRouteSnapshot): Observable<null | ITypeProject> => {
  const id = route.params['id'];
  if (id) {
    return inject(TypeProjectService)
      .find(id)
      .pipe(
        mergeMap((typeProject: HttpResponse<ITypeProject>) => {
          if (typeProject.body) {
            return of(typeProject.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default typeProjectResolve;
