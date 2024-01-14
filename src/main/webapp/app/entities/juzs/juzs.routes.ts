import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { JuzsComponent } from './list/juzs.component';
import { JuzsDetailComponent } from './detail/juzs-detail.component';
import { JuzsUpdateComponent } from './update/juzs-update.component';
import JuzsResolve from './route/juzs-routing-resolve.service';

const juzsRoute: Routes = [
  {
    path: '',
    component: JuzsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: JuzsDetailComponent,
    resolve: {
      juzs: JuzsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: JuzsUpdateComponent,
    resolve: {
      juzs: JuzsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: JuzsUpdateComponent,
    resolve: {
      juzs: JuzsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default juzsRoute;
