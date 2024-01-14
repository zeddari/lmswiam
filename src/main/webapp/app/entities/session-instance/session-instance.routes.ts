import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SessionInstanceComponent } from './list/session-instance.component';
import { SessionInstanceDetailComponent } from './detail/session-instance-detail.component';
import { SessionInstanceUpdateComponent } from './update/session-instance-update.component';
import SessionInstanceResolve from './route/session-instance-routing-resolve.service';

const sessionInstanceRoute: Routes = [
  {
    path: '',
    component: SessionInstanceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SessionInstanceDetailComponent,
    resolve: {
      sessionInstance: SessionInstanceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SessionInstanceUpdateComponent,
    resolve: {
      sessionInstance: SessionInstanceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SessionInstanceUpdateComponent,
    resolve: {
      sessionInstance: SessionInstanceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default sessionInstanceRoute;
