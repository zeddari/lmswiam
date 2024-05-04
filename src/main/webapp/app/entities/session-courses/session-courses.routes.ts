import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SessionCoursesComponent } from './list/session-courses.component';
import { SessionCoursesDetailComponent } from './detail/session-courses-detail.component';
import { SessionCoursesUpdateComponent } from './update/session-courses-update.component';
import SessionCoursesResolve from './route/session-courses-routing-resolve.service';

const sessionCoursesRoute: Routes = [
  {
    path: '',
    component: SessionCoursesComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SessionCoursesDetailComponent,
    resolve: {
      sessionCourses: SessionCoursesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SessionCoursesUpdateComponent,
    resolve: {
      sessionCourses: SessionCoursesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SessionCoursesUpdateComponent,
    resolve: {
      sessionCourses: SessionCoursesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default sessionCoursesRoute;
