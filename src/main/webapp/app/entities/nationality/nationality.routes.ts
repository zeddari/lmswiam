import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { NationalityComponent } from './list/nationality.component';
import { NationalityDetailComponent } from './detail/nationality-detail.component';
import { NationalityUpdateComponent } from './update/nationality-update.component';
import NationalityResolve from './route/nationality-routing-resolve.service';

const nationalityRoute: Routes = [
  {
    path: '',
    component: NationalityComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NationalityDetailComponent,
    resolve: {
      nationality: NationalityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NationalityUpdateComponent,
    resolve: {
      nationality: NationalityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NationalityUpdateComponent,
    resolve: {
      nationality: NationalityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default nationalityRoute;
