import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { DepenseComponent } from './list/depense.component';
import { DepenseDetailComponent } from './detail/depense-detail.component';
import { DepenseUpdateComponent } from './update/depense-update.component';
import DepenseResolve from './route/depense-routing-resolve.service';

const depenseRoute: Routes = [
  {
    path: '',
    component: DepenseComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DepenseDetailComponent,
    resolve: {
      depense: DepenseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DepenseUpdateComponent,
    resolve: {
      depense: DepenseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DepenseUpdateComponent,
    resolve: {
      depense: DepenseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default depenseRoute;
