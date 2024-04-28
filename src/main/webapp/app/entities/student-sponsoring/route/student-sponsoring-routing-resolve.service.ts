import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStudentSponsoring } from '../student-sponsoring.model';
import { StudentSponsoringService } from '../service/student-sponsoring.service';

export const studentSponsoringResolve = (route: ActivatedRouteSnapshot): Observable<null | IStudentSponsoring> => {
  const id = route.params['id'];
  if (id) {
    return inject(StudentSponsoringService)
      .find(id)
      .pipe(
        mergeMap((studentSponsoring: HttpResponse<IStudentSponsoring>) => {
          if (studentSponsoring.body) {
            return of(studentSponsoring.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default studentSponsoringResolve;
