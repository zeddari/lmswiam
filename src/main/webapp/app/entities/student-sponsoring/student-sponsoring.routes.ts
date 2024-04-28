import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { StudentSponsoringComponent } from './list/student-sponsoring.component';
import { StudentSponsoringDetailComponent } from './detail/student-sponsoring-detail.component';
import { StudentSponsoringUpdateComponent } from './update/student-sponsoring-update.component';
import StudentSponsoringResolve from './route/student-sponsoring-routing-resolve.service';

const studentSponsoringRoute: Routes = [
  {
    path: '',
    component: StudentSponsoringComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StudentSponsoringDetailComponent,
    resolve: {
      studentSponsoring: StudentSponsoringResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StudentSponsoringUpdateComponent,
    resolve: {
      studentSponsoring: StudentSponsoringResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StudentSponsoringUpdateComponent,
    resolve: {
      studentSponsoring: StudentSponsoringResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default studentSponsoringRoute;
