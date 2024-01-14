import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SurahsComponent } from './list/surahs.component';
import { SurahsDetailComponent } from './detail/surahs-detail.component';
import { SurahsUpdateComponent } from './update/surahs-update.component';
import SurahsResolve from './route/surahs-routing-resolve.service';

const surahsRoute: Routes = [
  {
    path: '',
    component: SurahsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SurahsDetailComponent,
    resolve: {
      surahs: SurahsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SurahsUpdateComponent,
    resolve: {
      surahs: SurahsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SurahsUpdateComponent,
    resolve: {
      surahs: SurahsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default surahsRoute;
