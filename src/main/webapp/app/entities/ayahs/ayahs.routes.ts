import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AyahsComponent } from './list/ayahs.component';
import { AyahsDetailComponent } from './detail/ayahs-detail.component';
import { AyahsUpdateComponent } from './update/ayahs-update.component';
import AyahsResolve from './route/ayahs-routing-resolve.service';

const ayahsRoute: Routes = [
  {
    path: '',
    component: AyahsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AyahsDetailComponent,
    resolve: {
      ayahs: AyahsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AyahsUpdateComponent,
    resolve: {
      ayahs: AyahsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AyahsUpdateComponent,
    resolve: {
      ayahs: AyahsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ayahsRoute;
