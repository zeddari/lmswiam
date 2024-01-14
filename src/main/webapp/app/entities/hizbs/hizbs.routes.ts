import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { HizbsComponent } from './list/hizbs.component';
import { HizbsDetailComponent } from './detail/hizbs-detail.component';
import { HizbsUpdateComponent } from './update/hizbs-update.component';
import HizbsResolve from './route/hizbs-routing-resolve.service';

const hizbsRoute: Routes = [
  {
    path: '',
    component: HizbsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HizbsDetailComponent,
    resolve: {
      hizbs: HizbsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HizbsUpdateComponent,
    resolve: {
      hizbs: HizbsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HizbsUpdateComponent,
    resolve: {
      hizbs: HizbsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default hizbsRoute;
