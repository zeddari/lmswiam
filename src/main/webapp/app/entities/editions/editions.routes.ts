import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { EditionsComponent } from './list/editions.component';
import { EditionsDetailComponent } from './detail/editions-detail.component';
import { EditionsUpdateComponent } from './update/editions-update.component';
import EditionsResolve from './route/editions-routing-resolve.service';

const editionsRoute: Routes = [
  {
    path: '',
    component: EditionsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EditionsDetailComponent,
    resolve: {
      editions: EditionsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EditionsUpdateComponent,
    resolve: {
      editions: EditionsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EditionsUpdateComponent,
    resolve: {
      editions: EditionsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default editionsRoute;
