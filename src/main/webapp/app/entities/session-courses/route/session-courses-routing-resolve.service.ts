import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISessionCourses } from '../session-courses.model';
import { SessionCoursesService } from '../service/session-courses.service';

export const sessionCoursesResolve = (route: ActivatedRouteSnapshot): Observable<null | ISessionCourses> => {
  const id = route.params['id'];
  if (id) {
    return inject(SessionCoursesService)
      .find(id)
      .pipe(
        mergeMap((sessionCourses: HttpResponse<ISessionCourses>) => {
          if (sessionCourses.body) {
            return of(sessionCourses.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default sessionCoursesResolve;
